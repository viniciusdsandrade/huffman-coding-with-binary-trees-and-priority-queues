import java.io.*;

import data_structures.PriorityQueue.PriorityQueue;
import data_structures.HashMap.HashMapLinkedListUnordered;
import data_structures.LinkedList.Unordered.LinkedListUnordered;

import static java.lang.Integer.toBinaryString;
import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.write;

/// # HuffmanCoding
///
/// A classe 'HuffmanCoding' fornece funcionalidades para **compactar** e **descompactar** arquivos utilizando o algoritmo de **Codificação de Huffman**. Ela utiliza estruturas de dados personalizadas, como 'PriorityQueue', 'HashMapLinkedListUnordered' e 'LinkedListUnordered', para gerenciar frequências e construir a árvore de Huffman.
///
/// ## Funcionalidades
/// - **Compactação ('compress'):** Compacta um arquivo de entrada, gerando um arquivo '.huff' contendo os dados compactados e as informações de frequência necessárias para a descompactação.
/// - **Descompactação ('decompress'):** Descompacta um arquivo '.huff', restaurando os dados originais.
/// - **Comparação de Arquivos ('compareFiles', 'compareDescompressedWithOriginal', 'compareDirectories'):** Garante que a descompactação preservou a integridade dos dados originais.
///
/// ## Uso
/// Crie uma instância de 'HuffmanCoding' e utilize os métodos 'compress' e 'decompress' fornecendo os caminhos dos arquivos de entrada e saída.
///
/// ### Exemplo
/// '''java
/// HuffmanCoding huffman = new HuffmanCoding();
/// huffman.compress("caminho/do/arquivo/original.txt", "caminho/do/arquivo/compactado.huff");
/// huffman.decompress("caminho/do/arquivo/compactado.huff", "caminho/do/arquivo/restaurado.txt");
///'''

public class HuffmanCoding {
    /// ### Campos
    ///
    /// - `huffmanCodes`: Mapa que associa cada byte ao seu respectivo código de Huffman.
    /// - `huffmanTreeRoot`: Nó raiz da árvore de Huffman.
    /// - `frequencies`: Mapa que armazena a frequência de cada byte no arquivo.
    private final HashMapLinkedListUnordered<Byte, String> huffmanCodes;
    private HuffmanNode huffmanTreeRoot;
    private final HashMapLinkedListUnordered<Byte, Integer> frequencies;

    /// ## HuffmanCoding
    ///
    /// Construtor padrão que inicializa os mapas de códigos de Huffman e frequências.
    ///
    /// ### Fluxo de Operações
    /// 1. Inicializa 'huffmanCodes' como uma nova instância de 'HashMapLinkedListUnordered'.
    /// 2. Inicializa 'frequencies' como uma nova instância de 'HashMapLinkedListUnordered'.
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste construtor.
    public HuffmanCoding() {
        huffmanCodes = new HashMapLinkedListUnordered<>();
        frequencies = new HashMapLinkedListUnordered<>();
    }

    /// ## compress
    ///
    /// Compacta um arquivo utilizando o algoritmo de **Codificação de Huffman**.
    ///
    /// ### Parâmetros
    /// - 'inputFilePath': caminho completo do arquivo original a ser compactado.
    /// - 'outputFilePath': caminho completo onde o arquivo compactado será armazenado.
    ///
    /// ### Fluxo de Operações
    /// 1. Lê todos os bytes do arquivo de entrada.
    /// 2. Verifica se os dados foram lidos corretamente e se o arquivo não está vazio.
    /// 3. Calcula as frequências de cada byte presente no arquivo.
    /// 4. Constrói a árvore de Huffman com base nas frequências calculadas.
    /// 5. Gera os códigos de Huffman a partir da árvore construída.
    /// 6. Codifica os dados do arquivo utilizando os códigos de Huffman.
    /// 7. Converte a string de bits codificada para um array de bytes, tratando o padding necessário.
    /// 8. Escreve as informações de frequência e os dados codificados no arquivo de saída.
    ///
    /// ### Exceções
    /// - 'IOException': lança exceção se ocorrer um erro durante a leitura ou escrita do arquivo.
    /// - 'IllegalArgumentException': lança exceção se o arquivo de entrada estiver vazio.
    public void compress(String inputFilePath, String outputFilePath) throws IOException {
        // Ler todos os bytes do arquivo de entrada
        byte[] data = readFile(inputFilePath);

        // Verificar se os dados foram lidos corretamente
        System.out.println("Tamanho dos dados lidos: " + data.length + " bytes");

        // Verificar se o arquivo de entrada não está vazio
        if (data.length == 0) throw new IllegalArgumentException("O arquivo de entrada está vazio.");

        // Calcular as frequências dos bytes
        calculateFrequencies(data);

        // Construir a árvore de Huffman com base nas frequências
        buildHuffmanTree();

        // Gerar os códigos de Huffman a partir da árvore construída
        generateCodes(huffmanTreeRoot, "");

        // Codificar os dados do arquivo utilizando os códigos de Huffman
        StringBuilder encodedData = encodeData(data);
        int numberOfBits = encodedData.length();

        // Converter a string de bits codificada para um array de bytes, tratando o padding
        byte[] encodedBytes = new byte[(numberOfBits + 7) / 8];
        for (int i = 0; i < numberOfBits; i++) {
            if (encodedData.charAt(i) == '1') encodedBytes[i / 8] |= (byte) (1 << (7 - (i % 8)));
        }

        // Escrever o arquivo compactado com as informações de frequência e os dados codificados
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFilePath))) {
            // Escrever o número de entradas de frequência
            dos.writeInt(frequencies.size());

            // Iterar sobre as chaves e escrever cada byte seguido de sua frequência
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

        // Informar ao usuário que a compactação foi concluída com sucesso
        System.out.println("Arquivo compactado com sucesso!");
    }

    /// ## decompress
    ///
    /// Descompacta um arquivo '.huff' utilizando o algoritmo de **Codificação de Huffman**.
    ///
    /// ### Parâmetros
    /// - 'inputFilePath': caminho completo do arquivo compactado a ser descompactado.
    /// - 'outputFilePath': caminho completo onde o arquivo descompactado será armazenado.
    ///
    /// ### Fluxo de Operações
    /// 1. Abre o arquivo compactado para leitura.
    /// 2. Lê o número de entradas de frequência e popula o mapa 'frequencies'.
    /// 3. Reconstrói a árvore de Huffman com base nas frequências lidas.
    /// 4. Gera os códigos de Huffman a partir da árvore reconstruída.
    /// 5. Lê o número de bits codificados e os dados codificados.
    /// 6. Converte os bytes codificados de volta para uma string de bits, ajustando o padding.
    /// 7. Decodifica os dados codificados utilizando a árvore de Huffman.
    /// 8. Escreve os dados descompactados no arquivo de saída.
    ///
    /// ### Exceções
    /// - 'IOException': lança exceção se ocorrer um erro durante a leitura ou escrita do arquivo.
    /// - 'IllegalStateException': lança exceção se o mapa de frequências estiver vazio após a leitura.
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

            // Verificar se o mapa de frequências está vazio após a leitura
            if (frequencies.isEmpty())
                throw new IllegalStateException("Mapa de frequências está vazio após a leitura. Verifique o arquivo compactado.");

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

        // Informar ao usuário que a descompactação foi concluída com sucesso
        System.out.println("Arquivo descompactado com sucesso!");
    }

    /// ## readFile
    ///
    /// Lê todos os bytes de um arquivo especificado pelo caminho.
    ///
    /// ### Parâmetros
    /// - 'filePath': caminho completo do arquivo a ser lido.
    ///
    /// ### Retorno
    /// - 'byte[]': Array de bytes contendo os dados do arquivo.
    ///
    /// ### Exceções
    /// - 'IOException': lança exceção se ocorrer um erro durante a leitura do arquivo.
    private byte[] readFile(String filePath) throws IOException {
        return readAllBytes(new File(filePath).toPath());
    }

    /// ## writeFile
    ///
    /// Escreve um array de bytes em um arquivo especificado pelo caminho.
    ///
    /// ### Parâmetros
    /// - 'data': Array de bytes a ser escrito no arquivo.
    /// - 'filePath': caminho completo onde o arquivo será escrito.
    ///
    /// ### Exceções
    /// - 'IOException': lança exceção se ocorrer um erro durante a escrita do arquivo.
    private void writeFile(byte[] data, String filePath) throws IOException {
        write(new File(filePath).toPath(), data);
    }

    /// ## calculateFrequencies
    ///
    /// Calcula a frequência de cada byte presente nos dados fornecidos.
    ///
    /// ### Parâmetros
    /// - 'data': Array de bytes representando os dados do arquivo a ser compactado.
    ///
    /// ### Fluxo de Operações
    /// 1. Itera sobre cada byte nos dados.
    /// 2. Atualiza o mapa 'frequencies' incrementando a contagem para cada byte.
    /// 3. Exibe as frequências calculadas para depuração.
    ///
    /// ### Observações
    /// - Exibe as frequências dos bytes no console para fins de depuração.
    private void calculateFrequencies(byte[] data) {
        // Iterar sobre cada byte nos dados e atualizar as frequências
        for (byte b : data) {
            Integer currentFreq = frequencies.get(b);
            frequencies.put(b, currentFreq == null ? 1 : currentFreq + 1);
        }

        // Debug: Imprimir frequências
        System.out.println("Frequências dos bytes:");
        LinkedListUnordered<Byte> keys = frequencies.keySet();
        LinkedListUnordered<Byte>.Node currentKey = keys.getPrimeiro();

        // Verificar se há frequências calculadas
        if (currentKey == null) System.err.println("Nenhuma frequência calculada. O mapa de frequências está vazio.");

        // Iterar sobre as chaves e exibir as frequências
        while (currentKey != null) {
            Byte key = currentKey.elemento;
            Integer freq = frequencies.get(key);
            System.out.println((key & 0xFF) + ": " + freq); // Exibir byte como inteiro sem sinal
            currentKey = currentKey.proximo;
        }
    }

    /// ## buildHuffmanTree
    ///
    /// Constrói a árvore de Huffman com base nas frequências calculadas.
    ///
    /// ### Fluxo de Operações
    /// 1. Inicializa uma fila de prioridade personalizada ('PriorityQueue') para armazenar os nós de Huffman.
    /// 2. Insere todos os 'nós' folha na fila de prioridade com suas respectivas frequências.
    /// 3. Verifica se a fila de prioridade está vazia e lança uma exceção se estiver.
    /// 4. Enquanto houver mais de um nó na fila:
    ///    - Remove os dois nós com as menores frequências.
    ///    - Cria um nó pai cuja frequência é a soma das frequências dos nós removidos.
    ///    - Insere o nó pai de volta na fila de prioridade.
    /// 5. O nó restante na fila é a raiz da árvore de Huffman.
    ///
    /// ### Exceções
    /// - 'IllegalStateException': lança exceção se o mapa de frequências estiver vazio ou se a árvore de Huffman não for construída corretamente.
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
        if (priorityQueue.isEmpty())
            throw new IllegalStateException("Mapa de frequências está vazio. Verifique se o arquivo não está vazio.");

        // Construir a árvore de Huffman
        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.dequeue(); // Nó com menor frequência
            HuffmanNode right = priorityQueue.dequeue(); // Próximo nó com menor frequência
            HuffmanNode parent = new HuffmanNode(left.frequency + right.frequency, left, right); // Criar nó pai
            priorityQueue.enqueue(parent, parent.frequency); // Inserir nó pai de volta na fila
        }

        // A raiz da árvore de Huffman
        huffmanTreeRoot = priorityQueue.dequeue();

        // Verificação adicional para garantir que a árvore foi construída corretamente
        if (huffmanTreeRoot == null)
            throw new IllegalStateException("A árvore de Huffman não foi construída corretamente.");
    }

    /// ## generateCodes
    ///
    /// Gera os códigos de Huffman para cada byte na árvore de Huffman.
    ///
    /// ### Parâmetros
    /// - 'node': nó atual da árvore de Huffman.
    /// - 'code': Código de Huffman acumulado até o nó atual.
    ///
    /// ### Fluxo de Operações
    /// 1. Verifica se o nó atual não é nulo.
    /// 2. Se o nó for folha, associa o byte ao código acumulado no mapa 'huffmanCodes'. Se o código estiver vazio (caso de apenas um nó na árvore), associa o código '"0"'.
    /// 3. Se o nó não for folha, chama recursivamente 'generateCodes' para o nó filho esquerdo adicionando '"0"' ao código e para o nó filho direito adicionando '"1"'.
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste metodo.
    private void generateCodes(HuffmanNode node, String code) {
        if (node != null) {
            if (node.isLeaf()) {
                // Atribui o código ao byte no mapa, usando "0" se o código estiver vazio
                huffmanCodes.put(node.byteValue, !code.isEmpty() ? code : "0");
            } else {
                // Percorre o filho esquerdo adicionando "0" ao código
                generateCodes(node.leftNode, code + "0");
                // Percorre o filho direito adicionando "1" ao código
                generateCodes(node.rightNode, code + "1");
            }
        }
    }

    /// ## encodeData
    ///
    /// Codifica os dados do arquivo original utilizando os códigos de Huffman gerados.
    ///
    /// ### Parâmetros
    /// - 'data': Array de bytes representando os dados do arquivo original.
    ///
    /// ### Retorno
    /// - 'StringBuilder': objeto 'StringBuilder' contendo a string de bits codificada.
    ///
    /// ### Fluxo de Operações
    /// 1. Itera sobre cada byte nos dados.
    /// 2. Obtém o código de Huffman correspondente ao byte.
    /// 3. Adiciona o código à 'StringBuilder'.
    /// 4. Lança uma exceção se um byte não tiver um código de Huffman associado.
    ///
    /// ### Exceções
    /// - 'IllegalArgumentException': lança exceção se um byte não tiver um código de Huffman associado.
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

    /// ## decodeData
    ///
    /// Decodifica uma string de bits codificada de volta para os dados originais utilizando a árvore de Huffman.
    ///
    /// ### Parâmetros
    /// - 'encodedData': string de bits representando os dados codificados.
    ///
    /// ### Retorno
    /// - 'byte[]': Array de bytes contendo os dados descompactados.
    ///
    /// ### Fluxo de Operações
    /// 1. Inicializa um 'ByteArrayOutputStream' para armazenar os bytes decodificados.
    /// 2. Itera sobre cada bit na string codificada:
    ///    - Move para o nó filho esquerdo se o bit for ''0'' ou para o nó filho direito se for ''1''.
    ///    - Se chegar a um nó folha, escreve o byte correspondente no 'ByteArrayOutputStream' e reinicia a busca a partir da raiz.
    /// 3. Trata o caso especial onde a árvore de Huffman possui apenas um nó folha (todos os bytes são iguais).
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste metodo.
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

            for (int i = 0; i < repetitions; i++) baos.write(singleByte);
        }
        return baos.toByteArray();
    }

    /// ## bytesToBinaryString
    ///
    /// Converte um array de bytes para uma string de bits, garantindo que cada byte seja representado por 8 bits.
    ///
    /// ### Parâmetros
    /// - `bytes`: Array de bytes a ser convertido.
    ///
    /// ### Retorno
    /// - `String`: String de bits representando os bytes.
    ///
    /// ### Fluxo de Operações
    /// 1. Inicializa um `StringBuilder`.
    /// 2. Itera sobre cada byte no array:
    ///    - Converte o byte para uma string binária de 8 bits, preenchendo com zeros à esquerda se necessário.
    ///    - Adiciona a string binária ao `StringBuilder`.
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste método.
    private String bytesToBinaryString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(format("%8s", toBinaryString(b & 0xFF)).replace(' ', '0'));
        return sb.toString();
    }
}
