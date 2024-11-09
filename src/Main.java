import static java.nio.charset.StandardCharsets.UTF_8;

public static void main(String[] ignoredArgs) {
    try {
        // Passo 1: Criar uma instância da classe HuffmanCoding
        HuffmanCoding huffman = new HuffmanCoding();

        // Passo 2: Definir um texto de exemplo para ser compactado
        String originalText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                              "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure \n" +
                              "dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non \n" +
                              "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n" +
                              "\n" +
                              "Curabitur pretium tincidunt lacus. Nulla gravida orci a odio. Nullam varius, turpis et commodo pharetra, est eros bibendum \n" +
                              "elit, nec luctus magna felis sollicitudin mauris. Integer in mauris eu nibh euismod gravida. Duis ac tellus et risus vulputate \n" +
                              "vehicula. Donec lobortis risus a elit. Etiam tempor. Ut ullamcorper, ligula eu tempor congue, eros est euismod turpis, id \n" +
                              "tincidunt sapien risus a quam. Maecenas fermentum consequat mi. Donec fermentum. Pellentesque malesuada nulla a mi. Duis sapien \n" +
                              "sem, aliquet nec, commodo eget, consequat quis, neque. Aliquam faucibus, elit ut dictum aliquet, felis nisl adipiscing sapien, \n" +
                              "sed malesuada diam lacus eget erat. Cras mollis scelerisque nunc. Nullam arcu. Aliquam consequat. Curabitur augue lorem, \n" +
                              "dignissim eget, malesuada ut, scelerisque sed, sapien.\n";

        // Passo 3: Escrever esse texto em um arquivo temporário
        String originalFilePath = "original_text.txt";
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(originalFilePath), UTF_8));
        writer.write(originalText);
        writer.close();

        // Passo 4: Utilizar o metodo compress para compactar o arquivo
        String compressedFilePath = "compressed_text.huff";
        huffman.compress(originalFilePath, compressedFilePath);
        System.out.println("Arquivo compactado com sucesso!");

        // Passo 5: Utilizar o método decompress para descompactar o arquivo compactado
        String decompressedFilePath = "decompressed_text.txt";
        huffman.decompress(compressedFilePath, decompressedFilePath);
        System.out.println("Arquivo descompactado com sucesso!");

        // Passo 6: Ler o conteúdo do arquivo descompactado
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(decompressedFilePath), UTF_8));
        StringBuilder sb = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        reader.close();
        String decompressedText = sb.toString();

        // Passo 7: Comparar o texto original com o texto descompactado
        if (originalText.equals(decompressedText)) {
            System.out.println("Teste bem-sucedido: O texto original e o texto descompactado são iguais.");
        } else {
            System.out.println("Teste falhou: O texto original e o texto descompactado são diferentes.");
        }

        // Exibir os textos para verificação
        System.out.println("\nTexto Original:");
        System.out.println(originalText);

        System.out.println("\nTexto Descompactado:");
        System.out.println(decompressedText);

        // Exibir o tamanho dos arquivos para verificar a compressão
        File originalFile = new File(originalFilePath);
        File compressedFile = new File(compressedFilePath);
        File decompressedFile = new File(decompressedFilePath);

        System.out.println("\nTamanho do arquivo original: " + originalFile.length() + " bytes");
        System.out.println("Tamanho do arquivo compactado: " + compressedFile.length() + " bytes");
        System.out.println("Tamanho do arquivo descompactado: " + decompressedFile.length() + " bytes");

        // Remover os arquivos temporários após o teste (opcional)
        // originalFile.delete();
        // compressedFile.delete();
        // decompressedFile.delete();

    } catch (FileNotFoundException e) {
        System.err.println("Arquivo não encontrado: " + e.getMessage());
    } catch (IOException e) {
        System.err.println("Erro de E/S: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Erro inesperado: " + e.getMessage());
    }
}
