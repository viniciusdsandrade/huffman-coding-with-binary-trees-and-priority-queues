import static java.io.File.separator;

public static void main(String[] ignoredArgs) {
    Scanner scanner = new Scanner(System.in);
    boolean continuar = true;

    while (continuar) {
        System.out.println("\n=== Menu de Operações ===");
        System.out.println("1. Compactar arquivo ou diretório");
        System.out.println("2. Descompactar arquivo");
        System.out.println("3. Sair");
        System.out.print("Selecione uma opção (1-3): ");

        String opcao = scanner.nextLine().trim();

        switch (opcao) {
            case "1":
                compactar(scanner);
                break;
            case "2":
                descompactar(scanner);
                break;
            case "3":
                continuar = false;
                System.out.println("Encerrando o programa.");
                break;
            default:
                System.out.println("Opção inválida. Por favor, tente novamente.");
        }
    }

    scanner.close();
}

/**
 * Método para compactar arquivos ou diretórios.
 *
 * @param scanner Scanner para receber entradas do usuário.
 */
private static void compactar(Scanner scanner) {
    try {
        System.out.print("Por favor, insira o caminho completo do arquivo ou diretório a ser compactado: ");
        String inputPath = scanner.nextLine().trim();
        File inputFile = new File(inputPath);
        System.out.println("Caminho fornecido: " + inputFile.getAbsolutePath());

        if (!inputFile.exists()) {
            System.err.println("O caminho fornecido não existe.");
            return;
        }

        // Define o diretório de saída
        String outputDirPath = "C:\\Users\\vinic\\OneDrive\\Área de Trabalho\\huffman-coding-with-binary-trees-and-priority-queues\\result";
        File outputDir = new File(outputDirPath);
        if (!outputDir.exists()) {
            if (outputDir.mkdirs()) {
                System.out.println("Diretório de saída criado em: " + outputDirPath);
            } else {
                System.err.println("Não foi possível criar o diretório de saída.");
                return;
            }
        }

        // Inicializa a classe HuffmanCoding
        HuffmanCoding huffman = new HuffmanCoding();

        // Define o basePath dinamicamente
        Path basePath;
        if (inputFile.isDirectory()) {
            basePath = inputFile.toPath();
        } else {
            basePath = inputFile.getParentFile().toPath();
        }

        // Processa o arquivo ou diretório com base no tipo de entrada
        if (inputFile.isFile()) {
            // Compacta um único arquivo
            processCompressFile(inputFile, outputDirPath, huffman, basePath);
        } else if (inputFile.isDirectory()) {
            // Compacta todos os arquivos no diretório de forma recursiva
            processCompressDirectory(inputFile, outputDirPath, huffman, basePath);
        }

    } catch (IOException e) {
        System.err.println("Erro de E/S durante a compactação: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Erro inesperado durante a compactação: " + e.getMessage());
    }
}

/**
 * Método para descompactar um arquivo.
 *
 * @param scanner Scanner para receber entradas do usuário.
 */
private static void descompactar(Scanner scanner) {
    try {
        System.out.print("Por favor, insira o caminho completo do arquivo .huff a ser descompactado: ");
        String inputPath = scanner.nextLine().trim();
        File inputFile = new File(inputPath);
        System.out.println("Caminho fornecido: " + inputFile.getAbsolutePath());

        if (!inputFile.exists() || !inputFile.isFile()) {
            System.err.println("O caminho fornecido não existe ou não é um arquivo válido.");
            return;
        }

        // Verificar se o arquivo tem a extensão .huff
        String fileName = inputFile.getName();
        if (!fileName.endsWith(".huff")) {
            System.err.println("O arquivo não tem a extensão .huff e não pode ser descompactado.");
            return;
        }

        // Define o diretório de saída
        String outputDirPath = "C:\\Users\\vinic\\OneDrive\\Área de Trabalho\\huffman-coding-with-binary-trees-and-priority-queues\\result";
        File outputDir = new File(outputDirPath);
        if (!outputDir.exists()) {
            if (outputDir.mkdirs()) {
                System.out.println("Diretório de saída criado em: " + outputDirPath);
            } else {
                System.err.println("Não foi possível criar o diretório de saída.");
                return;
            }
        }

        // Inicializa a classe HuffmanCoding
        HuffmanCoding huffman = new HuffmanCoding();

        // Define o basePath dinamicamente
        Path basePath = inputFile.getParentFile().toPath();

        // Determinar o caminho relativo para preservar a estrutura
        Path filePath = inputFile.toPath();
        Path relativePath = basePath.relativize(filePath.getParent());
        String fileOutputDirPath = outputDirPath + separator + relativePath.toString();

        // Cria o diretório de saída para o arquivo descompactado, se não existir
        File fileOutputDir = new File(fileOutputDirPath);
        if (!fileOutputDir.exists()) {
            if (fileOutputDir.mkdirs()) {
                System.out.println("Diretório para arquivo descompactado criado em: " + fileOutputDirPath);
            } else {
                System.err.println("Não foi possível criar o diretório para arquivo descompactado: " + fileOutputDirPath);
                return;
            }
        }

        // Remover a extensão .huff para obter o nome original
        String decompressedFileName = fileName.substring(0, fileName.length() - 5); // Remove ".huff"
        String decompressedFilePath = fileOutputDirPath + separator + decompressedFileName;

        System.out.println("\nProcessando arquivo para descompactação: " + inputFile.getAbsolutePath());

        // Descompactar o arquivo
        huffman.decompress(inputFile.getAbsolutePath(), decompressedFilePath);
        System.out.println("Arquivo descompactado com sucesso em: " + decompressedFilePath);

        // Exibir o tamanho da descompactação
        File decompressedFile = new File(decompressedFilePath);
        System.out.println("Tamanho do arquivo descompactado: " + decompressedFile.length() + " bytes");

        // Opcional: Comparar o arquivo descompactado com o original
        // **Nota:** Este passo requer que você tenha o arquivo original disponível
        System.out.print("Deseja comparar o arquivo descompactado com o original? (s/n): ");
        String comparar = scanner.nextLine().trim().toLowerCase();
        if (comparar.equals("s")) {
            System.out.print("Por favor, insira o caminho completo do arquivo original: ");
            String originalPath = scanner.nextLine().trim();
            File originalFile = new File(originalPath);

            if (!originalFile.exists() || !originalFile.isFile()) {
                System.err.println("O caminho fornecido para o arquivo original não existe ou não é um arquivo válido.");
            } else {
                boolean iguais = compareFiles(originalFile, decompressedFile);
                if (iguais) {
                    System.out.println("Sucesso: O arquivo descompactado é idêntico ao original.");
                } else {
                    System.err.println("Falha: O arquivo descompactado difere do original.");
                }
            }
        }

    } catch (IOException e) {
        System.err.println("Erro de E/S durante a descompactação: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Erro inesperado durante a descompactação: " + e.getMessage());
    }
}

/**
 * Processa a compactação de um único arquivo.
 *
 * @param file          Arquivo a ser compactado.
 * @param outputDirPath Caminho do diretório de saída.
 * @param huffman       Instância da classe HuffmanCoding.
 * @param basePath      Caminho base para preservação da estrutura de diretórios.
 * @throws IOException Se ocorrer um erro de E/S.
 */
private static void processCompressFile(File file, String outputDirPath, HuffmanCoding huffman, Path basePath) throws IOException {
    // Determinar o caminho relativo para preservar a estrutura
    Path filePath = file.toPath();
    Path relativePath = basePath.relativize(filePath.getParent());
    String fileOutputDirPath = outputDirPath + separator + relativePath;

    // Cria o diretório de saída para o arquivo, se não existir
    File fileOutputDir = new File(fileOutputDirPath);
    if (!fileOutputDir.exists()) {
        if (fileOutputDir.mkdirs()) {
            System.out.println("Diretório para arquivo criado em: " + fileOutputDirPath);
        } else {
            System.err.println("Não foi possível criar o diretório para arquivo: " + fileOutputDirPath);
            return;
        }
    }

    String fileName = file.getName();
    String compressedFilePath = fileOutputDirPath + separator + fileName + ".huff";

    System.out.println("\nProcessando arquivo para compressão: " + file.getAbsolutePath());

    // Compactar o arquivo
    huffman.compress(file.getAbsolutePath(), compressedFilePath);
    System.out.println("Arquivo compactado com sucesso em: " + compressedFilePath);

    // Exibir o tamanho da compactação
    File compressedFile = new File(compressedFilePath);
    System.out.println("Tamanho do arquivo compactado: " + compressedFile.length() + " bytes");
}

/**
 * Processa a compactação de todos os arquivos em um diretório de forma recursiva.
 *
 * @param dir           Diretório a ser processado.
 * @param outputDirPath Caminho do diretório de saída.
 * @param huffman       Instância da classe HuffmanCoding.
 * @param basePath      Caminho base para preservação da estrutura de diretórios.
 * @throws IOException Se ocorrer um erro de E/S.
 */
private static void processCompressDirectory(File dir, String outputDirPath, HuffmanCoding huffman, Path basePath) throws IOException {
    File[] files = dir.listFiles();
    if (files != null) {
        for (File file : files) {
            if (file.isFile()) {
                processCompressFile(file, outputDirPath, huffman, basePath);
            } else if (file.isDirectory()) {
                processCompressDirectory(file, outputDirPath, huffman, basePath);
            }
        }
    }
}

/**
 * Compara dois arquivos byte a byte para verificar se são iguais.
 *
 * @param file1 Primeiro arquivo.
 * @param file2 Segundo arquivo.
 * @return true se os arquivos forem iguais, false caso contrário.
 * @throws IOException Se ocorrer um erro durante a leitura dos arquivos.
 */
private static boolean compareFiles(File file1, File file2) throws IOException {
    byte[] f1 = Files.readAllBytes(file1.toPath());
    byte[] f2 = Files.readAllBytes(file2.toPath());

    return Arrays.equals(f1, f2);
}

