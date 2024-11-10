import static java.io.File.separator;
import static java.lang.System.in;
import static java.nio.file.Files.readAllBytes;

public static void main(String[] ignoredArgs) {
    Scanner scanner = new Scanner(in);
    boolean continuar = true;

    while (continuar) {
        System.out.println("\n=== Menu de Operações ===");
        System.out.println("1. Compactar arquivo ou diretório");
        System.out.println("2. Descompactar diretório");
        System.out.println("3. Sair");
        System.out.print("Selecione uma opção (1-3): ");

        String opcao = scanner.nextLine().trim();

        switch (opcao) {
            case "1" -> compactar(scanner);
            case "2" -> descompactar(scanner);
            case "3" -> {
                continuar = false;
                System.out.println("Encerrando o programa.");
            }
            default -> System.err.println("Opção inválida. Por favor, tente novamente.");
        }
    }

    scanner.close();
}

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
        String subDirName = "";
        if (inputFile.isDirectory()) {
            basePath = inputFile.toPath();
            subDirName = inputFile.getName(); // Nome da pasta original para criar subpasta em 'result'
        } else {
            basePath = inputFile.getParentFile().toPath();
        }

        // Define o diretório específico dentro de 'result' para armazenar os arquivos compactados
        String specificOutputDirPath = outputDirPath;
        if (inputFile.isDirectory()) {
            specificOutputDirPath = outputDirPath + separator + subDirName;
            File specificOutputDir = new File(specificOutputDirPath);
            if (!specificOutputDir.exists()) {
                if (specificOutputDir.mkdirs()) {
                    System.out.println("Subdiretório para compactação criado em: " + specificOutputDirPath);
                } else {
                    System.err.println("Não foi possível criar o subdiretório para compactação.");
                    return;
                }
            }
        }

        // Processa o arquivo ou diretório com base no tipo de entrada
        if (inputFile.isFile()) {
            // Compacta um único arquivo
            processCompressFile(inputFile, specificOutputDirPath, huffman, basePath);
        } else if (inputFile.isDirectory()) {
            // Compacta todos os arquivos no diretório de forma recursiva
            processCompressDirectory(inputFile, specificOutputDirPath, huffman, basePath);
        }

    } catch (IOException e) {
        System.err.println("Erro de E/S durante a compactação: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Erro inesperado durante a compactação: " + e.getMessage());
    }
}

private static void descompactar(Scanner scanner) {
    try {
        System.out.print("Por favor, insira o caminho completo do diretório contendo arquivos .huff a serem descompactados: ");
        String inputPath = scanner.nextLine().trim();
        File inputDir = new File(inputPath);
        System.out.println("Caminho fornecido: " + inputDir.getAbsolutePath());

        if (!inputDir.exists() || !inputDir.isDirectory()) {
            System.err.println("O caminho fornecido não existe ou não é um diretório válido.");
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

        // Definir subdiretório dentro de 'result' para manter a organização
        String subDirName = inputDir.getName() + "_decompressed";
        String specificOutputDirPath = outputDirPath + separator + subDirName;
        File specificOutputDir = new File(specificOutputDirPath);
        if (!specificOutputDir.exists()) {
            if (specificOutputDir.mkdirs()) {
                System.out.println("Subdiretório para descompressão criado em: " + specificOutputDirPath);
            } else {
                System.err.println("Não foi possível criar o subdiretório para descompressão.");
                return;
            }
        }

        // Processa todos os arquivos .huff no diretório de entrada de forma recursiva
        processDecompressDirectory(inputDir, specificOutputDirPath, huffman, inputDir.toPath());

        // Opcional: Comparar os arquivos descompactados com os originais
        System.out.print("Deseja comparar os arquivos descompactados com os originais? (s/n): ");
        String comparar = scanner.nextLine().trim().toLowerCase();
        if (comparar.equals("s")) {
            System.out.print("Por favor, insira o caminho completo da pasta original que foi compactada: ");
            String originalPath = scanner.nextLine().trim();
            File originalDir = new File(originalPath);

            if (!originalDir.exists() || !originalDir.isDirectory()) {
                System.err.println("O caminho fornecido para a pasta original não existe ou não é um diretório válido.");
            } else {
                boolean todasIguais = compareDescompressedWithOriginal(originalDir, specificOutputDirPath);
                if (todasIguais) {
                    System.out.println("\nTodos os arquivos foram descompactados corretamente! Tudo deu certo.");
                } else {
                    System.err.println("\nAlguns arquivos diferem dos originais. Verifique as mensagens acima.");
                }
            }
        }

    } catch (IOException e) {
        System.err.println("Erro de E/S durante a descompactação: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Erro inesperado durante a descompactação: " + e.getMessage());
    }
}

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

private static void processCompressDirectory(File dir, String specificOutputDirPath, HuffmanCoding huffman, Path basePath) throws IOException {
    File[] files = dir.listFiles();
    if (files == null) {
        System.err.println("Não foi possível listar os arquivos no diretório: " + dir.getAbsolutePath());
        return;
    }

    for (File file : files) {
        if (file.isFile()) {
            processCompressFile(file, specificOutputDirPath, huffman, basePath);
        } else if (file.isDirectory()) {
            // Determinar o caminho relativo para subdiretórios
            Path relativePath = basePath.relativize(file.toPath());
            String subDirOutputPath = specificOutputDirPath + separator + relativePath;
            File subDirOutput = new File(subDirOutputPath);

            if (!subDirOutput.exists()) {
                if (subDirOutput.mkdirs()) {
                    System.out.println("Subdiretório criado em: " + subDirOutputPath);
                } else {
                    System.err.println("Não foi possível criar o subdiretório: " + subDirOutputPath);
                    continue; // Pular este subdiretório e continuar
                }
            }

            // Recursivamente compactar o subdiretório
            processCompressDirectory(file, specificOutputDirPath, huffman, basePath);
        }
    }
}

private static void processDecompressFile(File file, String specificOutputDirPath, HuffmanCoding huffman, Path basePath) throws IOException {
    // Determinar o caminho relativo para preservar a estrutura
    Path filePath = file.toPath();
    Path relativePath = basePath.relativize(filePath.getParent());
    String fileOutputDirPath = specificOutputDirPath + separator + relativePath;

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

    String fileName = file.getName();
    String decompressedFileName = fileName.substring(0, fileName.length() - 5); // Remove ".huff"
    String decompressedFilePath = fileOutputDirPath + separator + decompressedFileName;

    System.out.println("\nProcessando arquivo para descompactação: " + file.getAbsolutePath());

    // Descompactar o arquivo
    huffman.decompress(file.getAbsolutePath(), decompressedFilePath);
    System.out.println("Arquivo descompactado com sucesso em: " + decompressedFilePath);

    // Exibir o tamanho da descompactação
    File decompressedFile = new File(decompressedFilePath);
    System.out.println("Tamanho do arquivo descompactado: " + decompressedFile.length() + " bytes");
}

private static void processDecompressDirectory(File dir, String specificOutputDirPath, HuffmanCoding huffman, Path basePath) throws IOException {
    File[] files = dir.listFiles();
    if (files == null) {
        System.err.println("Não foi possível listar os arquivos no diretório: " + dir.getAbsolutePath());
        return;
    }

    for (File file : files) {
        if (file.isFile()) {
            if (file.getName().toLowerCase().endsWith(".huff")) {
                processDecompressFile(file, specificOutputDirPath, huffman, basePath);
            }
        } else if (file.isDirectory()) {
            // Determinar o caminho relativo para subdiretórios
            Path relativePath = basePath.relativize(file.toPath());
            String subDirOutputPath = specificOutputDirPath + separator + relativePath;
            File subDirOutput = new File(subDirOutputPath);

            if (!subDirOutput.exists()) {
                if (subDirOutput.mkdirs()) {
                    System.out.println("Subdiretório para descompressão criado em: " + subDirOutputPath);
                } else {
                    System.err.println("Não foi possível criar o subdiretório para descompressão: " + subDirOutputPath);
                    continue; // Pular este subdiretório e continuar
                }
            }

            // Recursivamente descompactar o subdiretório
            processDecompressDirectory(file, specificOutputDirPath, huffman, basePath);
        }
    }
}

private static boolean compareFiles(File file1, File file2) throws IOException {
    byte[] f1 = readAllBytes(file1.toPath());
    byte[] f2 = readAllBytes(file2.toPath());

    return Arrays.equals(f1, f2);
}

private static boolean compareDescompressedWithOriginal(File originalDir, String decompressedDirPath) throws IOException {
    File decompressedDir = new File(decompressedDirPath);

    if (!decompressedDir.exists() || !decompressedDir.isDirectory()) {
        System.err.println("O diretório descompactado não existe ou não é um diretório válido: " + decompressedDirPath);
        return false;
    }

    return compareDirectories(originalDir, decompressedDir);
}

private static boolean compareDirectories(File originalDir, File decompressedDir) throws IOException {
    File[] originalFiles = originalDir.listFiles();
    if (originalFiles == null) {
        System.err.println("Não foi possível listar os arquivos no diretório original: " + originalDir.getAbsolutePath());
        return false;
    }

    boolean todasIguais = true;

    for (File originalFile : originalFiles) {
        File correspondingDecompressedFile = new File(decompressedDir, originalFile.getName());

        if (originalFile.isFile()) {
            if (!correspondingDecompressedFile.exists() || !correspondingDecompressedFile.isFile()) {
                System.err.println("Arquivo descompactado não encontrado para: " + originalFile.getAbsolutePath());
                todasIguais = false;
                continue;
            }

            boolean iguais = compareFiles(originalFile, correspondingDecompressedFile);
            if (iguais) {
                System.out.println("Sucesso: " + originalFile.getName() + " foi descompactado corretamente.");
            } else {
                System.err.println("Falha: " + originalFile.getName() + " difere do arquivo original.");
                todasIguais = false;
            }
        } else if (originalFile.isDirectory()) {
            if (!correspondingDecompressedFile.exists() || !correspondingDecompressedFile.isDirectory()) {
                System.err.println("Subdiretório descompactado não encontrado para: " + originalFile.getAbsolutePath());
                todasIguais = false;
                continue;
            }

            // Recursivamente comparar subdiretórios
            boolean subDirIguais = compareDirectories(originalFile, correspondingDecompressedFile);

            if (!subDirIguais) todasIguais = false;
        }
    }
    return todasIguais;
}



