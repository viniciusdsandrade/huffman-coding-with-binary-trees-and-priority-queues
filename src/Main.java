public static void main(String[] ignoredArgs) {
    try {
        // Passo 1: Criar uma instância da classe HuffmanCoding
        HuffmanCoding huffman = new HuffmanCoding();

        // Passo 2: Definir um texto de exemplo para ser compactado
        String originalText = "Este é um exemplo de texto para testar a compressão e descompressão usando Huffman Coding.";

        // Passo 3: Escrever esse texto em um arquivo temporário
        String originalFilePath = "original_text.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(originalFilePath));
        writer.write(originalText);
        writer.close();

        // Passo 4: Utilizar o metodo compress para compactar o arquivo
        String compressedFilePath = "compressed_text.huff";
        huffman.compress(originalFilePath, compressedFilePath);
        System.out.println("Arquivo compactado com sucesso!");

        // Passo 5: Utilizar o metodo decompress para descompactar o arquivo compactado
        String decompressedFilePath = "decompressed_text.txt";
        huffman.decompress(compressedFilePath, decompressedFilePath);
        System.out.println("Arquivo descompactado com sucesso!");

        // Passo 6: Ler o conteúdo do arquivo descompactado
        BufferedReader reader = new BufferedReader(new FileReader(decompressedFilePath));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        String decompressedText = sb.toString();

        // Passo 7: Comparar o texto original com o texto descompactado
        if (originalText.equals(decompressedText)) {
            System.out.println("Teste bem-sucedido: O texto original e o texto descompactado são iguais.");
        } else {
            System.out.println("Teste falhou: O texto original e o texto descompactado são diferentes.");
        }

        // Opcional: Exibir os textos para verificação
        System.out.println("\nTexto Original:");
        System.out.println(originalText);

        System.out.println("\nTexto Descompactado:");
        System.out.println(decompressedText);

        // Opcional: Exibir o tamanho dos arquivos para verificar a compressão
        File originalFile = new File(originalFilePath);
        File compressedFile = new File(compressedFilePath);
        File decompressedFile = new File(decompressedFilePath);

        System.out.println("\nTamanho do arquivo original: " + originalFile.length() + " bytes");
        System.out.println("Tamanho do arquivo compactado: " + compressedFile.length() + " bytes");
        System.out.println("Tamanho do arquivo descompactado: " + decompressedFile.length() + " bytes");

        // Opcional: Remover os arquivos temporários após o teste
        originalFile.delete();
        compressedFile.delete();
        decompressedFile.delete();

    } catch (FileNotFoundException e) {
        System.err.println("Arquivo não encontrado: " + e.getMessage());
    } catch (IOException e) {
        System.err.println("Erro de E/S: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Erro inesperado: " + e.getMessage());
    }
}
