import java.io.*;
import java.util.*;

import static java.lang.Integer.toBinaryString;
import static java.nio.charset.StandardCharsets.UTF_8;

public class HuffmanCoding {
    private final HashMap<Character, String> huffmanCodes;
    private HuffmanNode huffmanTreeRoot;
    private HashMap<Character, Integer> frequencies;

    public HuffmanCoding() {
        huffmanCodes = new HashMap<>();
        frequencies = new HashMap<>();
    }

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
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            dos.writeChar(entry.getKey());
            dos.writeInt(entry.getValue());
        }
        dos.writeInt(numberOfBits); // Escreve o número de bits significativos
        dos.write(encodedBytes);    // Escreve os bytes codificados
        dos.close();

        System.out.println("Arquivo compactado com sucesso!");
    }

    public void decompress(String inputFilePath, String outputFilePath) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(inputFilePath));

        // Leitura das frequências para reconstruir a árvore
        int size = dis.readInt(); // Número de caracteres únicos
        frequencies = new HashMap<>();
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


    // Metodo para ler o conteúdo de um arquivo de texto
    private String readFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), UTF_8));
        int ch;

        while ((ch = br.read()) != -1) sb.append((char) ch);

        br.close();
        return sb.toString();
    }

    // Metodo para escrever um texto em um arquivo
    private void writeFile(String text, String filePath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), UTF_8));
        bw.write(text);
        bw.close();
    }

    // Cálculo das frequências dos caracteres
    private void calculateFrequencies(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
        }
        // Debug: Imprimir frequências
        System.out.println("Frequências dos caracteres:");
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            System.out.println("'" + entry.getKey() + "': " + entry.getValue());
        }
    }

    // Construção da árvore de Huffman
    private void buildHuffmanTree() {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();

        // Criação dos 'nós' folha e inserção na fila de prioridade
        Set<Character> keys = frequencies.keySet();
        for (Character c : keys) {
            if (c != null) {
                int freq = frequencies.get(c);
                HuffmanNode huffmanNode = new HuffmanNode(c, freq);
                priorityQueue.offer(huffmanNode);
            }
        }

        // Construção da árvore
        while (priorityQueue.size() > 1) {
            HuffmanNode leftHuffmanNode = priorityQueue.poll();
            HuffmanNode rightHuffmanNode = priorityQueue.poll();

            int combinedFreq = leftHuffmanNode.frequency + (rightHuffmanNode != null ? rightHuffmanNode.frequency : 0);
            HuffmanNode parentHuffmanNode = new HuffmanNode(combinedFreq, leftHuffmanNode, rightHuffmanNode);
            priorityQueue.offer(parentHuffmanNode);
        }

        // A raiz da árvore é o único nó restante na fila
        if (!priorityQueue.isEmpty()) huffmanTreeRoot = priorityQueue.poll();
    }

    // Geração dos códigos de Huffman a partir da árvore
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

    // Codificação do texto usando os códigos de Huffman
    private String encodeText(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String code = huffmanCodes.get(c);
            if (code != null) {
                sb.append(code);
            } else {
                throw new IllegalArgumentException("Caracter não encontrado nos códigos de Huffman: " + c);
            }
        }
        // Debug: Imprimir texto codificado
        System.out.println("Texto Codificado: " + sb);
        return sb.toString();
    }

    // Decodificação do texto codificado
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
            if ((current != null ? current.leftNode : null) == null && Objects.requireNonNull(current).rightNode == null) {
                sb.append(current.character);
                current = root;

                // Verifica se alcançou o número esperado de caracteres
                if (sb.length() == expectedLength) {
                    break;
                }
            }
        }

        return sb.toString();
    }

    // Conversão de array de bytes para String binária
    private String bytesToBinaryString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String byteString = String.format("%8s", toBinaryString(b & 0xFF)).replace(' ', '0');
            sb.append(byteString);
        }
        // Debug: Imprimir string de bits
        //System.out.println("String de Bits Decodificada: " + sb);
        return sb.toString();
    }
}
