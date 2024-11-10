import java.io.*;

import data_structures.PriorityQueue.PriorityQueue;
import data_structures.HashMap.HashMapLinkedListUnordered;
import data_structures.LinkedList.Unordered.LinkedListUnordered;

import static java.lang.Integer.toBinaryString;
import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.write;

public class HuffmanCoding {
    private final HashMapLinkedListUnordered<Byte, String> huffmanCodes;
    private HuffmanNode huffmanTreeRoot;
    private final HashMapLinkedListUnordered<Byte, Integer> frequencies;

    public HuffmanCoding() {
        huffmanCodes = new HashMapLinkedListUnordered<>();
        frequencies = new HashMapLinkedListUnordered<>();
    }

    public void compress(String inputFilePath, String outputFilePath) throws IOException {
        byte[] data = readFile(inputFilePath);

        // Verificar se os dados foram lidos corretamente
        System.out.println("Tamanho dos dados lidos: " + data.length + " bytes");

        if (data.length == 0) throw new IllegalArgumentException("O arquivo de entrada está vazio.");

        calculateFrequencies(data);
        buildHuffmanTree();
        generateCodes(huffmanTreeRoot, "");

        // Codificar os dados
        StringBuilder encodedData = encodeData(data);
        int numberOfBits = encodedData.length();

        // Converter para bytes, tratando o padding
        byte[] encodedBytes = new byte[(numberOfBits + 7) / 8];
        for (int i = 0; i < numberOfBits; i++) {
            if (encodedData.charAt(i) == '1')
                encodedBytes[i / 8] |= (byte) (1 << (7 - (i % 8)));
        }

        // Escrever o arquivo compactado
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFilePath))) {
            // Escrever o número de entradas de frequência
            dos.writeInt(frequencies.size());

            // Iterar sobre as chaves usando keySet() e obter os valores com get(key)
            LinkedListUnordered<Byte> keys = frequencies.keySet();
            LinkedListUnordered<Byte>.Node currentKey = keys.getPrimeiro();
            while (currentKey != null) {
                Byte key = currentKey.elemento;
                Integer freq = frequencies.get(key);
                dos.writeByte(key);
                dos.writeInt(freq);
                currentKey = currentKey.proximo;
            }

            // Escrever o número de bits codificados e os dados codificados
            dos.writeInt(numberOfBits);
            dos.write(encodedBytes);
        }

        System.out.println("Arquivo compactado com sucesso!");
    }

    public void decompress(String inputFilePath, String outputFilePath) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(inputFilePath))) {
            // Ler o número de entradas de frequência
            int size = dis.readInt();
            frequencies.clear();

            // Ler cada par byte-frequência e armazenar no HashMap
            for (int i = 0; i < size; i++) {
                byte b = dis.readByte();
                int freq = dis.readInt();
                frequencies.put(b, freq);
            }

            if (frequencies.isEmpty()) throw new IllegalStateException("Mapa de frequências está vazio após a leitura. Verifique o arquivo compactado.");

            // Reconstruir a árvore de Huffman e gerar os códigos
            buildHuffmanTree();
            generateCodes(huffmanTreeRoot, "");

            // Ler o número de bits e os dados codificados
            int numberOfBits = dis.readInt();
            byte[] encodedBytes = new byte[(numberOfBits + 7) / 8];
            dis.readFully(encodedBytes);

            // Converter os bytes codificados para uma string de bits
            String encodedData = bytesToBinaryString(encodedBytes);
            if (encodedData.length() > numberOfBits) encodedData = encodedData.substring(0, numberOfBits);

            // Decodificar os dados para obter os bytes originais
            byte[] decodedData = decodeData(encodedData);
            writeFile(decodedData, outputFilePath);
        }

        System.out.println("Arquivo descompactado com sucesso!");
    }

    private byte[] readFile(String filePath) throws IOException {
        return readAllBytes(new File(filePath).toPath());
    }

    private void writeFile(byte[] data, String filePath) throws IOException {
        write(new File(filePath).toPath(), data);
    }

    private void calculateFrequencies(byte[] data) {
        for (byte b : data) {
            Integer currentFreq = frequencies.get(b);
            frequencies.put(b, currentFreq == null ? 1 : currentFreq + 1);
        }

        // Debug: Imprimir frequências
        System.out.println("Frequências dos bytes:");
        LinkedListUnordered<Byte> keys = frequencies.keySet();
        LinkedListUnordered<Byte>.Node currentKey = keys.getPrimeiro();

        if (currentKey == null) System.err.println("Nenhuma frequência calculada. O mapa de frequências está vazio.");

        while (currentKey != null) {
            Byte key = currentKey.elemento;
            Integer freq = frequencies.get(key);
            System.out.println((key & 0xFF) + ": " + freq); // Exibir byte como inteiro sem sinal
            currentKey = currentKey.proximo;
        }
    }

    private void buildHuffmanTree() {
        // Inicializar a fila de prioridade personalizada
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();

        // Inserir todos os 'nós' folha na fila de prioridade com sua frequência como prioridade
        LinkedListUnordered<Byte> keys = frequencies.keySet();
        LinkedListUnordered<Byte>.Node currentKey = keys.getPrimeiro();
        while (currentKey != null) {
            Byte key = currentKey.elemento;
            Integer freq = frequencies.get(key);
            priorityQueue.enqueue(new HuffmanNode(key, freq), freq);
            currentKey = currentKey.proximo;
        }

        // Verificação: Se não houver frequências, lançar uma exceção mais informativa
        if (priorityQueue.isEmpty()) throw new IllegalStateException("Mapa de frequências está vazio. Verifique se o arquivo não está vazio.");

        // Construir a árvore de Huffman
        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.dequeue(); // Nó com menor frequência
            HuffmanNode right = priorityQueue.dequeue(); // Próximo nó com menor frequência
            HuffmanNode parent = new HuffmanNode(left.frequency + right.frequency, left, right);
            priorityQueue.enqueue(parent, parent.frequency);
        }

        // A raiz da árvore de Huffman
        huffmanTreeRoot = priorityQueue.dequeue();

        // Verificação adicional
        if (huffmanTreeRoot == null) throw new IllegalStateException("A árvore de Huffman não foi construída corretamente.");
    }

    private void generateCodes(HuffmanNode node, String code) {
        if (node != null) {
            if (node.isLeaf()) {
                huffmanCodes.put(node.byteValue, !code.isEmpty() ? code : "0"); // Código "0" para único byte
            } else {
                generateCodes(node.leftNode, code + "0");
                generateCodes(node.rightNode, code + "1");
            }
        }
    }

    private StringBuilder encodeData(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            String code = huffmanCodes.get(b);
            if (code != null) {
                sb.append(code);
            } else {
                throw new IllegalArgumentException("Byte não encontrado nos códigos de Huffman: " + b);
            }
        }
        return sb;
    }

    private byte[] decodeData(String encodedData) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HuffmanNode current = huffmanTreeRoot;

        for (int i = 0; i < encodedData.length(); i++) {
            char bit = encodedData.charAt(i);
            current = (bit == '0') ? current.leftNode : current.rightNode;

            if (current.isLeaf()) {
                baos.write(current.byteValue);
                current = huffmanTreeRoot;
            }
        }

        // Caso a árvore tenha apenas um nó folha
        if (huffmanTreeRoot.isLeaf()) {
            int repetitions = encodedData.length(); // Cada bit representa o mesmo byte
            byte singleByte = huffmanTreeRoot.byteValue;
            baos.reset();
            for (int i = 0; i < repetitions; i++) {
                baos.write(singleByte);
            }
        }
        return baos.toByteArray();
    }

    private String bytesToBinaryString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(format("%8s", toBinaryString(b & 0xFF)).replace(' ', '0'));
        return sb.toString();
    }
}
