import java.io.*;

import LinkedList.Unordered.LinkedListUnordered;
import PriorityQueue.PriorityQueue;
import HashMap.HashMapLinkedListUnordered;
import Tree.BinaryTree;
import Tree.Node;

public class HuffmanCoding {
    private HashMapLinkedListUnordered<Character, String> huffmanCodes; // Mapeamento de caracteres para códigos
    private BinaryTree<HuffmanNode> huffmanTree; // Árvore de Huffman
    private HashMapLinkedListUnordered<Character, Integer> frequencies; // Frequências dos caracteres

    public HuffmanCoding() {
        huffmanCodes = new HashMapLinkedListUnordered<>();
        frequencies = new HashMapLinkedListUnordered<>();
        huffmanTree = new BinaryTree<>();
    }

    // Método principal para compactar um arquivo
    public void compress(String inputFilePath, String outputFilePath) throws IOException {
        String text = readFile(inputFilePath);
        calculateFrequencies(text);
        buildHuffmanTree();
        generateCodes(huffmanTree.getRoot().getValor(), ""); // Passar HuffmanNode

        String encodedText = encodeText(text);
        writeCompressedFile(encodedText, outputFilePath);
    }

    // Método principal para descompactar um arquivo
    public void decompress(String inputFilePath, String outputFilePath) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(inputFilePath));

        // Leitura das frequências para reconstruir a árvore
        int size = dis.readInt(); // Número de caracteres únicos
        frequencies = new HashMapLinkedListUnordered<>();
        for (int i = 0; i < size; i++) {
            char character = dis.readChar();
            int frequency = dis.readInt();
            frequencies.put(character, frequency);
        }

        buildHuffmanTree();
        generateCodes(huffmanTree.getRoot().getValor(), ""); // Passar HuffmanNode

        // Leitura do número total de bits e do texto codificado
        int numberOfBits = dis.readInt(); // Número total de bits
        byte[] encodedBytes = new byte[(numberOfBits + 7) / 8]; // Calcula o número de bytes necessários
        dis.readFully(encodedBytes);
        dis.close();

        String encodedText = bytesToBinaryString(encodedBytes);
        if (encodedText.length() > numberOfBits) {
            encodedText = encodedText.substring(0, numberOfBits); // Trunca os bits de preenchimento
        }
        String decodedText = decodeText(encodedText);

        writeFile(decodedText, outputFilePath);
    }


    // Metodo para ler o conteúdo de um arquivo de texto
    private String readFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        int ch;
        while ((ch = br.read()) != -1) {
            sb.append((char) ch);
        }
        br.close();
        return sb.toString();
    }

    // Metodo para escrever um texto em um arquivo
    private void writeFile(String text, String filePath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
        bw.write(text);
        bw.close();
    }

    // Cálculo das frequências dos caracteres
    private void calculateFrequencies(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Integer freq = frequencies.get(c);
            if (freq == null) {
                frequencies.put(c, 1);
            } else {
                frequencies.put(c, freq + 1);
            }
        }
    }

    // Construção da árvore de Huffman
    private void buildHuffmanTree() {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<HuffmanNode>();

        // Criação dos 'nós' folha e inserção na fila de prioridade
        LinkedListUnordered<Character> keys = frequencies.keySet();
        LinkedListUnordered<Character>.Node currentKey = keys.getPrimeiro();
        while (currentKey != null) {
            Character c = currentKey.elemento;
            if (c != null) {
                int freq = frequencies.get(c);
                HuffmanNode huffmanNode = new HuffmanNode(c, freq);
                priorityQueue.enqueue(huffmanNode, freq);
            }
            currentKey = currentKey.proximo;
        }

        // Construção da árvore
        while (priorityQueue.size() > 1) {
            HuffmanNode leftHuffmanNode = priorityQueue.dequeue();
            HuffmanNode rightHuffmanNode = priorityQueue.dequeue();

            int combinedFreq = leftHuffmanNode.frequency + rightHuffmanNode.frequency;
            HuffmanNode parentHuffmanNode = new HuffmanNode(combinedFreq, leftHuffmanNode, rightHuffmanNode);
            priorityQueue.enqueue(parentHuffmanNode, combinedFreq);
        }

        // A raiz da árvore é o único nó restante na fila
        if (!priorityQueue.isEmpty()) {
            HuffmanNode rootHuffmanNode = priorityQueue.dequeue();
            huffmanTree = new BinaryTree<>(new Node<>(rootHuffmanNode));
        }
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
            sb.append(code);
        }
        return sb.toString();
    }

    // Método para escrever o arquivo compactado
    private void writeCompressedFile(String encodedText, String filePath) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePath));

        // Escrita das frequências para descompactação
        LinkedListUnordered<Character> keys = frequencies.keySet();
        int size = 0;
        LinkedListUnordered<Character>.Node currentKey = keys.getPrimeiro();
        while (currentKey != null) {
            if (currentKey.elemento != null) size++;
            currentKey = currentKey.proximo;
        }
        dos.writeInt(size); // Number of unique characters
        currentKey = keys.getPrimeiro();
        while (currentKey != null) {
            Character c = currentKey.elemento;
            if (c != null) {
                dos.writeChar(c);
                dos.writeInt(frequencies.get(c));
            }
            currentKey = currentKey.proximo;
        }

        // Conversão do texto codificado em bytes
        byte[] encodedBytes = binaryStringToBytes(encodedText);

        // Escrita do número total de bits e dos bytes do texto codificado
        dos.writeInt(encodedText.length()); // Número total de bits
        dos.write(encodedBytes);
        dos.close();
    }

    // Decodificação do texto codificado
    private String decodeText(String encodedText) {
        StringBuilder sb = new StringBuilder();
        HuffmanNode root = huffmanTree.getRoot().getValor();
        HuffmanNode current = root;
        int index = 0;
        while (index < encodedText.length()) {
            char bit = encodedText.charAt(index);
            if (bit == '0') {
                current = current.leftNode;
            } else {
                current = current.rightNode;
            }

            // If a leaf node is reached, append the character and reset to root
            if (current.leftNode == null && current.rightNode == null) {
                sb.append(current.character);
                current = root;
            }
            index++;
        }
        return sb.toString();
    }


    // Conversão de String binária para array de bytes
    private byte[] binaryStringToBytes(String binaryString) {
        int byteLength = (binaryString.length() + 7) / 8;
        byte[] data = new byte[byteLength];
        int index = 0;
        for (int i = 0; i < binaryString.length(); i += 8) {
            String byteString = binaryString.substring(i, Math.min(i + 8, binaryString.length()));
            data[index++] = (byte) Integer.parseInt(byteString, 2);
        }
        return data;
    }

    // Conversão de array de bytes para String binária
    private String bytesToBinaryString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String byteString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            sb.append(byteString);
        }
        return sb.toString();
    }
}
