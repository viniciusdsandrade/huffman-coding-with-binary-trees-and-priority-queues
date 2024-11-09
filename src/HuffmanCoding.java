import java.io.*;

import PriorityQueue.PriorityQueue;
import HashMap.HashMapLinkedListUnordered;
import LinkedList.Unordered.LinkedListUnordered;

import static java.lang.Integer.toBinaryString;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/// # HuffmanCoding
///
/// A class that provides methods to compress and decompress text files using Huffman Coding.
///
/// ## Overview
///
/// Huffman Coding is a lossless data compression algorithm that assigns variable-length codes to characters based on their frequencies. Characters with higher frequencies are assigned shorter codes, resulting in efficient compression.
///
/// ## Fields
///
/// - `huffmanCodes`: A map storing the Huffman codes for each character.
/// - `huffmanTreeRoot`: The root node of the Huffman tree.
/// - `frequencies`: A map storing the frequency of each character.
///
/// ## Example
///
/// ```java
/// HuffmanCoding hc = new HuffmanCoding();
/// hc.compress("input.txt", "compressed.bin");
/// hc.decompress("compressed.bin", "output.txt");
/// ```
public class HuffmanCoding {
    private final HashMapLinkedListUnordered<Character, String> huffmanCodes;
    private HuffmanNode huffmanTreeRoot;
    private HashMapLinkedListUnordered<Character, Integer> frequencies;

    /// ## HuffmanCoding()
    ///
    /// Constructs a new `HuffmanCoding` instance with empty frequency and code maps.
    public HuffmanCoding() {
        huffmanCodes = new HashMapLinkedListUnordered<>();
        frequencies = new HashMapLinkedListUnordered<>();
    }

    /// ## compress
    ///
    /// Compresses the input file and writes the compressed data to the output file.
    ///
    /// ### Parameters
    ///
    /// - `inputFilePath`: The path to the input text file to be compressed.
    /// - `outputFilePath`: The path where the compressed file will be written.
    ///
    /// ### Throws
    ///
    /// - `IOException`: If an I/O error occurs during reading or writing files.
    ///
    /// ### Example
    ///
    /// ```java
    /// hc.compress("input.txt", "compressed.bin");
    /// ```
    ///
    /// @param inputFilePath the path to the input file
    /// @param outputFilePath the path to the output file
    /// @throws IOException if an I/O error occurs
    public void compress(String inputFilePath, String outputFilePath) throws IOException {
        String text = readFile(inputFilePath);
        calculateFrequencies(text);
        buildHuffmanTree();
        generateCodes(huffmanTreeRoot, "");

        // Codificar o texto
        String encodedText = encodeText(text);
        int numberOfBits = encodedText.length();

        // Converta o texto codificado para bytes, tratando o padding
        byte[] encodedBytes = new byte[(numberOfBits + 7) / 8];
        for (int i = 0; i < numberOfBits; i++) {
            if (encodedText.charAt(i) == '1') {
                encodedBytes[i / 8] |= (byte) (1 << (7 - (i % 8)));
            }
        }

        // Escreva o arquivo compactado
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFilePath));
        dos.writeInt(frequencies.size());
        LinkedListUnordered<Character> keys = frequencies.keySet();
        LinkedListUnordered<Character>.Node currentKey = keys.getPrimeiro();

        while (currentKey != null) {
            char character = currentKey.elemento;
            int frequency = frequencies.get(character);
            dos.writeChar(character);
            dos.writeInt(frequency);
            currentKey = currentKey.proximo;
        }

        dos.writeInt(numberOfBits); // Escreve o número de bits significativos
        dos.write(encodedBytes);    // Escreve os bytes codificados
        dos.close();

        System.out.println("Arquivo compactado com sucesso!");
    }

    /// ## decompress
    ///
    /// Decompresses the input file and writes the decompressed data to the output file.
    ///
    /// ### Parameters
    ///
    /// - `inputFilePath`: The path to the compressed input file.
    /// - `outputFilePath`: The path where the decompressed file will be written.
    ///
    /// ### Throws
    ///
    /// - `IOException`: If an I/O error occurs during reading or writing files.
    ///
    /// ### Example
    ///
    /// ```java
    /// hc.decompress("compressed.bin", "output.txt");
    /// ```
    ///
    /// @param inputFilePath the path to the compressed input file
    /// @param outputFilePath the path to the decompressed output file
    /// @throws IOException if an I/O error occurs
    public void decompress(String inputFilePath, String outputFilePath) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(inputFilePath));

        // Leitura das frequências para reconstruir a árvore
        int size = dis.readInt(); // Número de caracteres únicos
        frequencies = new HashMapLinkedListUnordered<>();
        int totalCharacters = 0; // Variável para contar o total de caracteres

        for (int i = 0; i < size; i++) {
            char character = dis.readChar();
            int frequency = dis.readInt();
            frequencies.put(character, frequency);
            totalCharacters += frequency; // Soma as frequências para obter o total de caracteres
        }

        buildHuffmanTree();
        generateCodes(huffmanTreeRoot, "");

        // Leitura do número total de bits e do texto codificado
        int numberOfBits = dis.readInt(); // Número total de bits
        byte[] encodedBytes = new byte[(numberOfBits + 7) / 8]; // Calcula o número de bytes necessários
        dis.readFully(encodedBytes);
        dis.close();

        String encodedText = bytesToBinaryString(encodedBytes);
        if (encodedText.length() > numberOfBits)
            encodedText = encodedText.substring(0, numberOfBits); // Trunca os bits de preenchimento

        // Passa o total de caracteres esperados para a função decodeText
        String decodedText = decodeText(encodedText, totalCharacters);

        writeFile(decodedText, outputFilePath);
    }

    /// ## readFile
    ///
    /// Reads the content of a text file and returns it as a `String`.
    ///
    /// ### Parameters
    ///
    /// - `filePath`: The path to the file to be read.
    ///
    /// ### Throws
    ///
    /// - `IOException`: If an I/O error occurs during reading the file.
    ///
    /// ### Example
    ///
    /// ```java
    /// String content = hc.readFile("input.txt");
    /// ```
    ///
    /// @param filePath the path to the file
    /// @return the content of the file as a String
    /// @throws IOException if an I/O error occurs
    private String readFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), UTF_8));
        int ch;

        while ((ch = br.read()) != -1) sb.append((char) ch);

        br.close();
        return sb.toString();
    }

    /// ## writeFile
    ///
    /// Writes the given text to a file at the specified path.
    ///
    /// ### Parameters
    ///
    /// - `text`: The text to be written to the file.
    /// - `filePath`: The path where the file will be written.
    ///
    /// ### Throws
    ///
    /// - `IOException`: If an I/O error occurs during writing the file.
    ///
    /// ### Example
    ///
    /// ```java
    /// hc.writeFile("Hello, World!", "output.txt");
    /// ```
    ///
    /// @param text the text to write to the file
    /// @param filePath the path to the file
    /// @throws IOException if an I/O error occurs
    private void writeFile(String text, String filePath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), UTF_8));
        bw.write(text);
        bw.close();
    }

    /// ## calculateFrequencies
    ///
    /// Calculates the frequency of each character in the given text.
    ///
    /// ### Parameters
    ///
    /// - `text`: The text for which character frequencies are to be calculated.
    ///
    /// ### Example
    ///
    /// ```java
    /// hc.calculateFrequencies("example");
    /// ```
    ///
    /// @param text the text to analyze
    private void calculateFrequencies(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Integer currentFreq = frequencies.get(c);
            frequencies.put(c, currentFreq == null ? 1 : currentFreq + 1);
        }

        // Debug: Imprimir frequências
        System.out.println("Frequências dos caracteres:");
        LinkedListUnordered<Character> keys = frequencies.keySet();
        LinkedListUnordered<Character>.Node currentKey = keys.getPrimeiro();

        while (currentKey != null) {
            char character = currentKey.elemento;
            int freq = frequencies.get(character);
            System.out.println("'" + character + "': " + freq);
            currentKey = currentKey.proximo;
        }
    }

    /// ## buildHuffmanTree
    ///
    /// Builds the Huffman tree based on the calculated character frequencies.
    ///
    /// ### Example
    ///
    /// ```java
    /// hc.buildHuffmanTree();
    /// ```
    ///
    private void buildHuffmanTree() {
        // Inicializa a fila de prioridade personalizada
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<HuffmanNode>();

        // Criação dos 'nós' folha e inserção na fila de prioridade com a frequência como prioridade
        LinkedListUnordered<Character> keys = frequencies.keySet();
        LinkedListUnordered<Character>.Node currentKey = keys.getPrimeiro();

        while (currentKey != null) {
            char c = currentKey.elemento;
            int freq = frequencies.get(c);
            HuffmanNode huffmanNode = new HuffmanNode(c, freq);
            priorityQueue.enqueue(huffmanNode, freq); // Insere com a frequência como prioridade
            currentKey = currentKey.proximo;
        }

        // Construção da árvore de Huffman
        while (priorityQueue.size() > 1) {
            // Remove os dois nós com as menores frequências
            HuffmanNode leftHuffmanNode = priorityQueue.dequeue();
            HuffmanNode rightHuffmanNode = priorityQueue.dequeue();

            // Combina as frequências dos dois nós removidos
            int combinedFreq = leftHuffmanNode.frequency + rightHuffmanNode.frequency;
            HuffmanNode parentHuffmanNode = new HuffmanNode(combinedFreq, leftHuffmanNode, rightHuffmanNode);

            // Insere o nó pai de volta na fila de prioridade com a frequência combinada
            priorityQueue.enqueue(parentHuffmanNode, combinedFreq);
        }

        // A raiz da árvore é o único nó restante na fila
        if (priorityQueue.size() == 1) huffmanTreeRoot = priorityQueue.dequeue();
    }

    /// ## generateCodes
    ///
    /// Generates Huffman codes by traversing the Huffman tree.
    ///
    /// ### Parameters
    ///
    /// - `node`: The current node in the Huffman tree.
    /// - `code`: The current Huffman code being generated.
    ///
    /// ### Example
    ///
    /// ```java
    /// hc.generateCodes(huffmanTreeRoot, "");
    /// ```
    ///
    /// @param node the current Huffman node
    /// @param code the current Huffman code
    private void generateCodes(HuffmanNode node, String code) {
        if (node != null) {
            // Nó folha
            if (node.leftNode == null && node.rightNode == null) {
                huffmanCodes.put(node.character, code);
            } else {
                // Nós internos
                generateCodes(node.leftNode, code + "0");
                generateCodes(node.rightNode, code + "1");
            }
        }
    }

    /// ## encodeText
    ///
    /// Encodes the given text into its Huffman binary representation.
    ///
    /// ### Parameters
    ///
    /// - `text`: The text to be encoded.
    ///
    /// ### Returns
    ///
    /// A `String` representing the binary encoded text.
    ///
    /// ### Throws
    ///
    /// - `IllegalArgumentException`: If a character in the text does not have a corresponding Huffman code.
    ///
    /// ### Example
    ///
    /// ```java
    /// String encoded = hc.encodeText("example");
    /// ```
    ///
    /// @param text the text to encode
    /// @return the encoded binary string
    /// @throws IllegalArgumentException if a character is not found in Huffman codes
    private String encodeText(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String code = huffmanCodes.get(c);
            if (code != null) sb.append(code);
            else throw new IllegalArgumentException("Caracter não encontrado nos códigos de Huffman: " + c);
        }
        // Debug: Imprimir texto codificado
        System.out.println("Texto Codificado: " + sb);
        return sb.toString();
    }

    /// ## decodeText
    ///
    /// Decodes the given binary string back into the original text using the Huffman tree.
    ///
    /// ### Parameters
    ///
    /// - `encodedText`: The binary string to be decoded.
    /// - `expectedLength`: The expected number of characters in the decoded text.
    ///
    /// ### Returns
    ///
    /// The decoded original text as a `String`.
    ///
    /// ### Throws
    ///
    /// - `IllegalArgumentException`: If an invalid bit is encountered in the encoded text.
    ///
    /// ### Example
    ///
    /// ```java
    /// String decoded = hc.decodeText(encoded, 7);
    /// ```
    ///
    /// @param encodedText the binary string to decode
    /// @param expectedLength the expected number of characters
    /// @return the decoded text
    /// @throws IllegalArgumentException if an invalid bit is encountered
    private String decodeText(String encodedText, int expectedLength) {
        StringBuilder sb = new StringBuilder();
        HuffmanNode root = huffmanTreeRoot;
        HuffmanNode current = root;

        for (int i = 0; i < encodedText.length(); i++) {
            char bit = encodedText.charAt(i);
            current = switch (bit) {
                case '0' -> current.leftNode;
                case '1' -> current.rightNode;
                default -> throw new IllegalArgumentException("Bit inválido no texto codificado: " + bit);
            };

            // Se um nó folha for alcançado
            if (requireNonNull(current).leftNode == null && current.rightNode == null) {
                sb.append(current.character);
                current = root;

                // Verifica se alcançou o número esperado de caracteres
                if (sb.length() == expectedLength) break;
            }
        }
        return sb.toString();
    }

    /// ## bytesToBinaryString
    ///
    /// Converts an array of bytes into a binary `String`.
    ///
    /// ### Parameters
    ///
    /// - `bytes`: The array of bytes to be converted.
    ///
    /// ### Returns
    ///
    /// A `String` representing the binary form of the input bytes.
    ///
    /// ### Example
    ///
    /// ```java
    /// String binary = hc.bytesToBinaryString(new byte[]{(byte)0b10101010});
    /// ```
    ///
    /// @param bytes the byte array to convert
    /// @return the binary string representation
    private String bytesToBinaryString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String byteString = String.format("%8s", toBinaryString(b & 0xFF)).replace(' ', '0');
            sb.append(byteString);
        }
        return sb.toString();
    }
}
