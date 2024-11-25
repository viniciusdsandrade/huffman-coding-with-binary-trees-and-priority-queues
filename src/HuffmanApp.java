import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

import static java.io.File.separator;
import static java.lang.System.in;
import static java.nio.file.Files.readAllBytes;

/// # HuffmanApp
///
/// A classe `HuffmanApp` fornece funcionalidades para compactar e descompactar arquivos e diretórios utilizando o algoritmo de **Codificação de Huffman**.
/// Ela permite a manipulação recursiva de estruturas de diretórios e oferece uma interface de linha de comando para interagir com o usuário.
///
/// ## Funcionalidades
/// - **Compactação:** Compacta arquivos ou diretórios, preservando a estrutura de diretórios original.
/// - **Descompactação:** Descompacta arquivos `.huff` em um diretório especificado, também preservando a estrutura.
/// - **Comparação:** Compara opcionalmente os arquivos descompactados com os originais para garantir a integridade dos dados.
///
/// ## Estrutura Interna
/// - **resultBasePath:** Variável estática que armazena o caminho base para os resultados da compactação/descompactação, dependendo do ambiente selecionado.
///
/// ## Uso
/// Execute o programa e siga as instruções no menu para escolher entre compactar, descompactar ou sair.
/// O programa solicitará a seleção do ambiente (Desktop ou Laptop) e procederá de acordo com a opção escolhida.
///
/// ### Exemplos de Uso
/// ```java
/// // Executando o programa
/// java HuffmanApp
///
/// // Selecionando o ambiente 'Desktop Vini'
/// === Seleção de Ambiente ===
/// 1. Desktop Vini
/// 2. Laptop Vini
/// Selecione o ambiente (1-2): 1
/// Ambiente 'desktop_vini' selecionado.
///
/// // Menu de Operações
/// === Menu de Operações ===
/// 1. Compactar arquivo ou diretório
/// 2. Descompactar diretório
/// 3. Sair
/// Selecione uma opção (1-3): 1
/// Por favor, insira o caminho completo do arquivo ou diretório a ser compactado: C:\Users\Vini\Documents\file.txt
/// Caminho fornecido: C:\Users\Vini\Documents\file.txt
/// Diretório de saída criado em: C:\Users\Pichau\Desktop\huffman-coding-with-binary-trees-and-priority-queues\result
///
/// Processando arquivo para compressão: C:\Users\Vini\Documents\file.txt
/// Arquivo compactado com sucesso em: C:\Users\Pichau\Desktop\huffman-coding-with-binary-trees-and-priority-queues\result\file.txt.huff
/// Tamanho do arquivo compactado: 1234 bytes
///```
public class HuffmanApp {

    /// Variável para armazenar o caminho base dependendo do ambiente selecionado
    private static String resultBasePath;

    /// ## main
    ///
    /// metodo principal que inicia a aplicação. Ele configura o ambiente de trabalho, exibe o menu de operações e processa as ações escolhidas pelo usuário.
    ///
    /// ### Parâmetros
    /// - 'ignoredArgs': Array de strings para receber argumentos da linha de comando (não utilizados).
    ///
    /// ### Fluxo de Operações
    /// 1. Inicializa um `Scanner` para receber entradas do usuário.
    /// 2. Solicita ao usuário a seleção do ambiente de trabalho (Desktop ou Laptop).
    /// 3. Entra em um loop principal que exibe o menu de operações até que o usuário escolha sair.
    /// 4. Processa a opção escolhida pelo usuário (compactar, descompactar ou sair).
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste metodo. Exceções são tratadas nos metodos chamados.
    ///
    /// ### Exemplo
    /// ```java
    /// public static void main(String[] args){
    ///     HuffmanApp.main(new String[]{});
    ///}
    ///```
    public static void main(String[] ignoredArgs) {
        // Inicializa o Scanner para receber entradas do usuário a partir da entrada padrão
        Scanner scanner = new Scanner(in);
        // Variável de controle para manter o loop do menu ativo
        boolean continuar = true;

        // Menu para selecionar o ambiente
        while (true) {
            System.out.print("""
                    \s
                     === Seleção de Ambiente ===
                     1. Desktop Vini
                     2. Laptop Vini
                     Selecione o ambiente (1-2):
                    """);

            String ambienteOpcao = scanner.nextLine().trim();

            if (ambienteOpcao.equals("1")) {
                configurarAmbienteDesktopVini();
                break;
            } else if (ambienteOpcao.equals("2")) {
                configurarAmbienteLaptopVini();
                break;
            } else {
                System.err.println("Opção inválida. Por favor, tente novamente.");
            }
        }

        // Loop principal que continua executando enquanto 'continuar' for verdadeiro
        while (continuar) {
            // Exibição do menu de operações utilizando Text Blocks para melhor legibilidade
            System.out.print("""
                    \s
                     === Menu de Operações ===
                     1. Compactar arquivo ou diretório
                     2. Descompactar diretório
                     3. Sair
                     Selecione uma opção (1-3):\s
                    """);

            // Recebe a opção escolhida pelo usuário e remove espaços em branco nas extremidades
            String opcao = scanner.nextLine().trim();

            // Executa a ação correspondente à opção selecionada
            switch (opcao) {
                case "1" -> compactar(scanner); // Opção 1: Compactar
                case "2" -> descompactar(scanner); // Opção 2: Descompactar
                case "3" -> { // Opção 3: Sair
                    continuar = false; // Atualiza a variável de controle para encerrar o loop
                    System.out.println("Encerrando o programa."); // Informa ao usuário que o programa está sendo encerrado
                }
                default -> System.err.println("Opção inválida. Por favor, tente novamente."); // Opção inválida
            }
        }

        // Fecha o Scanner para liberar recursos
        scanner.close();
    }

    /// ## configurarAmbienteDesktopVini
    ///
    /// Configura os caminhos base para o ambiente 'desktop_vini'.
    ///
    /// ### Fluxo de Operações
    /// 1. Define `resultBasePath` para o ambiente desktop.
    /// 2. Informa ao usuário que o ambiente 'desktop_vini' foi selecionado.
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste metodo.
    ///
    /// ### Exemplo
    /// ```java
    /// configurarAmbienteDesktopVini();
    ///```
    ///
    /// @see #configurarAmbienteLaptopVini()
    private static void configurarAmbienteDesktopVini() {
        resultBasePath = "C:\\Users\\Pichau\\Desktop\\huffman-coding-with-binary-trees-and-priority-queues\\result";
        System.out.println("Ambiente 'desktop_vini' selecionado.");
    }

    /// ## configurarAmbienteLaptopVini
    ///
    /// Configura os caminhos base para o ambiente 'laptop_vini'.
    ///
    /// ### Fluxo de Operações
    /// 1. Define `resultBasePath` para o ambiente laptop.
    /// 2. Informa ao usuário que o ambiente 'laptop_vini' foi selecionado.
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste metodo.
    ///
    /// ### Exemplo
    /// ```java
    /// configurarAmbienteLaptopVini();
    ///```
    ///
    /// @see #configurarAmbienteDesktopVini()
    private static void configurarAmbienteLaptopVini() {
        resultBasePath = "C:\\Users\\Vinícius Andrade\\Desktop\\submission-cotuca-4-terms\\huffman-coding-with-binary-trees-and-priority-queues\\result";
        System.out.println("Ambiente 'laptop_vini' selecionado.");
    }

    /// ## compactar
    ///
    /// Este metodo lida com o processo de compactação de arquivos ou diretórios. Ele solicita ao usuário o caminho do arquivo/diretório a ser compactado, verifica a existência do caminho, define o diretório de saída, inicializa a classe `HuffmanCoding` e processa a compactação de forma recursiva.
    ///
    /// ### Parâmetros
    /// - **scanner:** Instância de `Scanner` para receber entradas do usuário.
    ///
    /// ### Fluxo de Operações
    /// 1. Solicita ao usuário o caminho completo do arquivo ou diretório a ser compactado.
    /// 2. Verifica se o caminho fornecido existe.
    /// 3. Define o diretório de saída para os arquivos compactados com base no ambiente selecionado.
    /// 4. Inicializa a classe `HuffmanCoding` responsável pela compactação.
    /// 5. Determina o `basePath` para preservar a estrutura de diretórios.
    /// 6. Define um diretório específico dentro do diretório de saída para armazenar os arquivos compactados.
    /// 7. Processa o arquivo ou diretório com base no tipo de entrada (arquivo único ou diretório).
    ///
    /// ### Exceções
    /// - **IOException:** Captura erros de entrada/saída durante o processo de compactação.
    /// - **Exception:** Captura quaisquer outras exceções inesperadas.
    ///
    /// ### Exemplo
    /// ```java
    /// compactar(scanner);
    ///```
    ///
    /// @param scanner Instância de `Scanner` para receber entradas do usuário.
    private static void compactar(Scanner scanner) {
        try {
            // Solicita ao usuário o caminho completo do arquivo ou diretório a ser compactado
            System.out.print("Por favor, insira o caminho completo do arquivo ou diretório a ser compactado: ");
            String inputPath = scanner.nextLine().trim(); // Recebe e limpa a entrada
            File inputFile = new File(inputPath); // Cria um objeto File com o caminho fornecido
            System.out.println("Caminho fornecido: " + inputFile.getAbsolutePath()); // Exibe o caminho absoluto

            // Verifica se o arquivo ou diretório existe
            if (!inputFile.exists()) {
                System.err.println("O caminho fornecido não existe."); // Informa o erro
                return; // Encerra o metodo
            }

            // Define o diretório de saída para os arquivos compactados com base no ambiente selecionado
            String outputDirPath = resultBasePath;
            File outputDir = new File(outputDirPath); // Cria um objeto File para o diretório de saída
            if (!outputDir.exists()) { // Verifica se o diretório de saída existe
                if (outputDir.mkdirs()) { // Tenta criar o diretório de saída
                    System.out.println("Diretório de saída criado em: " + outputDirPath); // Informa a criação bem-sucedida
                } else {
                    System.err.println("Não foi possível criar o diretório de saída."); // Informa a falha na criação
                    return; // Encerra o metodo
                }
            }

            // Inicializa a classe HuffmanCoding responsável pela compactação
            HuffmanCoding huffman = new HuffmanCoding();

            // Define o basePath dinamicamente com base no tipo de entrada
            Path basePath;
            String subDirName = "";
            if (inputFile.isDirectory()) { // Se a entrada for um diretório
                basePath = inputFile.toPath(); // Define o basePath como o caminho do diretório
                subDirName = inputFile.getName(); // Obtém o nome do diretório para criar uma subpasta em 'result'
            } else { // Se a entrada for um arquivo único
                basePath = inputFile.getParentFile().toPath(); // Define o basePath como o diretório pai do arquivo
            }

            // Define o diretório específico dentro de 'result' para armazenar os arquivos compactados
            String specificOutputDirPath = outputDirPath;
            if (inputFile.isDirectory()) { // Se a entrada for um diretório
                specificOutputDirPath = outputDirPath + separator + subDirName; // Define o caminho específico
                File specificOutputDir = new File(specificOutputDirPath); // Cria um objeto File para o diretório específico
                if (!specificOutputDir.exists()) { // Verifica se o diretório específico existe
                    if (specificOutputDir.mkdirs()) { // Tenta criar o diretório específico
                        System.out.println("Subdiretório para compactação criado em: " + specificOutputDirPath); // Informa a criação
                    } else {
                        System.err.println("Não foi possível criar o subdiretório para compactação."); // Informa a falha
                        return; // Encerra o metodo
                    }
                }
            }

            // Processa o arquivo ou diretório com base no tipo de entrada
            if (inputFile.isFile()) { // Se a entrada for um arquivo único
                // Compacta um único arquivo
                processCompressFile(inputFile, specificOutputDirPath, huffman, basePath);
            } else if (inputFile.isDirectory()) { // Se a entrada for um diretório
                // Compacta todos os arquivos no diretório de forma recursiva
                processCompressDirectory(inputFile, specificOutputDirPath, huffman, basePath);
            }

        } catch (IOException e) { // Captura exceções de E/S
            System.err.println("Erro de E/S durante a compactação: " + e.getMessage()); // Informa o erro
        } catch (Exception e) { // Captura quaisquer outras exceções
            System.err.println("Erro inesperado durante a compactação: " + e.getMessage()); // Informa o erro
        }
    }

    /// ## descompactar
    ///
    /// Este metodo lida com o processo de descompactação de arquivos `.huff` em um diretório especificado. Ele solicita ao usuário o caminho do diretório contendo os arquivos `.huff`, verifica a validade do caminho, define o diretório de saída, inicializa a classe `HuffmanCoding`, processa a descompactação de forma recursiva e, opcionalmente, compara os arquivos descompactados com os originais.
    ///
    /// ### Parâmetros
    /// - **scanner:** Instância de `Scanner` para receber entradas do usuário.
    ///
    /// ### Fluxo de Operações
    /// 1. Solicita ao usuário o caminho completo do diretório contendo arquivos `.huff` a serem descompactados.
    /// 2. Verifica se o caminho fornecido existe e é um diretório válido.
    /// 3. Define o diretório de saída para os arquivos descompactados com base no ambiente selecionado.
    /// 4. Inicializa a classe `HuffmanCoding` responsável pela descompactação.
    /// 5. Define um subdiretório dentro do diretório de saída para manter a organização dos arquivos descompactados.
    /// 6. Processa todos os arquivos `.huff` no diretório de entrada de forma recursiva.
    /// 7. Opcionalmente, solicita ao usuário para comparar os arquivos descompactados com os originais.
    ///
    /// ### Exceções
    /// - **IOException:** Captura erros de entrada/saída durante o processo de descompactação.
    /// - **Exception:** Captura quaisquer outras exceções inesperadas.
    ///
    /// ### Exemplo
    /// ```java
    /// descompactar(scanner);
    ///```
    ///
    /// @param scanner Instância de `Scanner` para receber entradas do usuário.
    private static void descompactar(Scanner scanner) {
        try {
            // Solicita ao usuário o caminho completo do diretório contendo arquivos .huff a serem descompactados
            System.out.print("Por favor, insira o caminho completo do diretório contendo arquivos .huff a serem descompactados: ");
            String inputPath = scanner.nextLine().trim(); // Recebe e limpa a entrada
            File inputDir = new File(inputPath); // Cria um objeto File com o caminho fornecido
            System.out.println("Caminho fornecido: " + inputDir.getAbsolutePath()); // Exibe o caminho absoluto

            // Verifica se o diretório existe e é válido
            if (!inputDir.exists() || !inputDir.isDirectory()) {
                System.err.println("O caminho fornecido não existe ou não é um diretório válido."); // Informa o erro
                return; // Encerra o metodo
            }

            // Define o diretório de saída para os arquivos descompactados com base no ambiente selecionado
            String outputDirPath = resultBasePath;
            File outputDir = new File(outputDirPath); // Cria um objeto File para o diretório de saída
            if (!outputDir.exists()) { // Verifica se o diretório de saída existe
                if (outputDir.mkdirs()) { // Tenta criar o diretório de saída
                    System.out.println("Diretório de saída criado em: " + outputDirPath); // Informa a criação bem-sucedida
                } else {
                    System.err.println("Não foi possível criar o diretório de saída."); // Informa a falha na criação
                    return; // Encerra o metodo
                }
            }

            // Inicializa a classe HuffmanCoding responsável pela descompactação
            HuffmanCoding huffman = new HuffmanCoding();

            // Define um subdiretório dentro de 'result' para manter a organização dos arquivos descompactados
            String subDirName = inputDir.getName() + "_decompressed"; // Nome do subdiretório
            String specificOutputDirPath = outputDirPath + separator + subDirName; // Caminho completo do subdiretório
            File specificOutputDir = new File(specificOutputDirPath); // Cria um objeto File para o subdiretório
            if (!specificOutputDir.exists()) { // Verifica se o subdiretório existe
                if (specificOutputDir.mkdirs()) { // Tenta criar o subdiretório
                    System.out.println("Subdiretório para descompressão criado em: " + specificOutputDirPath); // Informa a criação
                } else {
                    System.err.println("Não foi possível criar o subdiretório para descompressão."); // Informa a falha na criação
                    return; // Encerra o metodo
                }
            }

            // Processa todos os arquivos .huff no diretório de entrada de forma recursiva
            processDecompressDirectory(inputDir, specificOutputDirPath, huffman, inputDir.toPath());

            // Solicita ao usuário se deseja comparar os arquivos descompactados com os originais
            System.out.print("Deseja comparar os arquivos descompactados com os originais? (s/n): ");
            String comparar = scanner.nextLine().trim().toLowerCase(); // Recebe e limpa a entrada
            if (comparar.equals("s")) { // Se o usuário optar por comparar
                // Solicita ao usuário o caminho completo da pasta original que foi compactada
                System.out.print("Por favor, insira o caminho completo da pasta original que foi compactada: ");
                String originalPath = scanner.nextLine().trim(); // Recebe e limpa a entrada
                File originalDir = new File(originalPath); // Cria um objeto File com o caminho fornecido

                // Verifica se o diretório original existe e é válido
                if (!originalDir.exists() || !originalDir.isDirectory()) {
                    System.err.println("O caminho fornecido para a pasta original não existe ou não é um diretório válido."); // Informa o erro
                } else {
                    // Compara os arquivos descompactados com os originais
                    boolean todasIguais = compareDescompressedWithOriginal(originalDir, specificOutputDirPath);
                    if (todasIguais) { // Se todas as comparações forem bem-sucedidas
                        System.out.println("\nTodos os arquivos foram descompactados corretamente! Tudo deu certo."); // Informa o sucesso total
                    } else { // Se houver falhas na comparação
                        System.err.println("\nAlguns arquivos diferem dos originais. Verifique as mensagens acima."); // Informa sobre as discrepâncias
                    }
                }
            }

        } catch (IOException e) { // Captura exceções de E/S
            System.err.println("Erro de E/S durante a descompactação: " + e.getMessage()); // Informa o erro
        } catch (Exception e) { // Captura quaisquer outras exceções
            System.err.println("Erro inesperado durante a descompactação: " + e.getMessage()); // Informa o erro
        }
    }

    /// ## processCompressFile
    ///
    /// Este metodo processa a compactação de um único arquivo. Ele determina o caminho relativo para preservar a estrutura de diretórios, cria o diretório de saída se necessário, compacta o arquivo utilizando a classe `HuffmanCoding` e exibe informações sobre o processo.
    ///
    /// ### Parâmetros
    /// - **file:** Objeto `File` representando o arquivo a ser compactado.
    /// - **outputDirPath:** Caminho do diretório de saída específico onde o arquivo compactado será armazenado.
    /// - **huffman:** Instância da classe `HuffmanCoding` responsável pela compactação.
    /// - **basePath:** Objeto `Path` representando o caminho base para preservar a estrutura de diretórios.
    ///
    /// ### Exceções
    /// - **IOException:** Lança exceção se ocorrer um erro durante a leitura ou escrita do arquivo.
    ///
    /// ### Exemplo
    /// ```java
    /// processCompressFile(file, outputDirPath, huffman, basePath);
    ///```
    ///
    /// @param file          Objeto `File` representando o arquivo a ser compactado.
    /// @param outputDirPath Caminho do diretório de saída específico.
    /// @param huffman       Instância da classe `HuffmanCoding`.
    /// @param basePath      Objeto `Path` representando o caminho base.
    private static void processCompressFile(File file, String outputDirPath, HuffmanCoding huffman, Path basePath) throws IOException {
        // Determina o caminho relativo em relação ao basePath para preservar a estrutura de diretórios
        Path filePath = file.toPath(); // Obtém o caminho do arquivo
        Path relativePath = basePath.relativize(filePath.getParent()); // Calcula o caminho relativo
        String fileOutputDirPath = outputDirPath + separator + relativePath; // Define o caminho completo do diretório de saída

        // Cria o diretório de saída para o arquivo, se ainda não existir
        File fileOutputDir = new File(fileOutputDirPath); // Cria um objeto File para o diretório de saída
        if (!fileOutputDir.exists()) { // Verifica se o diretório existe
            if (fileOutputDir.mkdirs()) { // Tenta criar o diretório
                System.out.println("Diretório para arquivo criado em: " + fileOutputDirPath); // Informa a criação bem-sucedida
            } else {
                System.err.println("Não foi possível criar o diretório para arquivo: " + fileOutputDirPath); // Informa a falha na criação
                return; // Encerra o metodo
            }
        }

        String fileName = file.getName(); // Obtém o nome do arquivo
        String compressedFilePath = fileOutputDirPath + separator + fileName + ".huff"; // Define o caminho do arquivo compactado

        // Informa ao usuário que o processo de compactação está iniciando
        System.out.println("\nProcessando arquivo para compressão: " + file.getAbsolutePath());

        // Executa a compactação do arquivo utilizando a classe HuffmanCoding
        huffman.compress(file.getAbsolutePath(), compressedFilePath);
        // Informa ao usuário que a compactação foi concluída com sucesso
        System.out.println("Arquivo compactado com sucesso em: " + compressedFilePath);

        // Cria um objeto File para o arquivo compactado e exibe seu tamanho
        File compressedFile = new File(compressedFilePath);
        System.out.println("Tamanho do arquivo compactado: " + compressedFile.length() + " bytes");
    }

    /// ## processCompressDirectory
    ///
    /// Este metodo processa a compactação de todos os arquivos em um diretório de forma recursiva.
    /// Para cada arquivo encontrado, ele chama o metodo `processCompressFile`.
    /// Para cada subdiretório, ele cria o diretório correspondente no local de saída e chama a si recursivamente.
    ///
    /// ### Parâmetros
    /// - **dir:** Objeto `File` representando o diretório a ser processado.
    /// - **specificOutputDirPath:** Caminho do diretório de saída específico onde os arquivos compactados serão armazenados.
    /// - **huffman:** Instância da classe `HuffmanCoding` responsável pela compactação.
    /// - **basePath:** Objeto `Path` representando o caminho base para preservar a estrutura de diretórios.
    ///
    /// ### Exceções
    /// - **IOException:** Lança exceção se ocorrer um erro durante a leitura ou escrita dos arquivos.
    ///
    /// ### Exemplo
    /// ```java
    /// processCompressDirectory(dir, specificOutputDirPath, huffman, basePath);
    ///```
    ///
    /// @param dir                   Objeto `File` representando o diretório a ser processado.
    /// @param specificOutputDirPath Caminho do diretório de saída específico.
    /// @param huffman               Instância da classe `HuffmanCoding`.
    /// @param basePath              Objeto `Path` representando o caminho base.
    private static void processCompressDirectory(File dir, String specificOutputDirPath, HuffmanCoding huffman, Path basePath) throws IOException {
        // Lista todos os arquivos e subdiretórios no diretório atual
        File[] files = dir.listFiles();
        if (files == null) { // Verifica se a listagem foi bem-sucedida
            System.err.println("Não foi possível listar os arquivos no diretório: " + dir.getAbsolutePath());
            return; // Encerra o metodo
        }

        // Itera sobre cada arquivo ou subdiretório encontrado
        for (File file : files) {
            if (file.isFile()) { // Se for um arquivo
                processCompressFile(file, specificOutputDirPath, huffman, basePath); // Processa a compactação do arquivo
            } else if (file.isDirectory()) { // Se for um subdiretório
                // Determina o caminho relativo para o subdiretório
                Path relativePath = basePath.relativize(file.toPath()); // Calcula o caminho relativo
                String subDirOutputPath = specificOutputDirPath + separator + relativePath; // Define o caminho de saída do subdiretório
                File subDirOutput = new File(subDirOutputPath); // Cria um objeto File para o subdiretório de saída

                if (!subDirOutput.exists()) { // Verifica se o subdiretório de saída existe
                    if (subDirOutput.mkdirs()) { // Tenta criar o subdiretório
                        System.out.println("Subdiretório criado em: " + subDirOutputPath); // Informa a criação bem-sucedida
                    } else {
                        System.err.println("Não foi possível criar o subdiretório: " + subDirOutputPath); // Informa a falha na criação
                        continue; // Pula este subdiretório e continua com os próximos
                    }
                }

                // Chama recursivamente este metodo para processar o subdiretório
                processCompressDirectory(file, specificOutputDirPath, huffman, basePath);
            }
        }
    }

    /// ## processDecompressFile
    ///
    /// Este metodo processa a descompactação de um único arquivo `.huff`. Ele determina o caminho relativo para preservar a estrutura de diretórios, cria o diretório de saída se necessário, descompacta o arquivo utilizando a classe `HuffmanCoding` e exibe informações sobre o processo.
    ///
    /// ### Parâmetros
    /// - **file:** Objeto `File` representando o arquivo `.huff` a ser descompactado.
    /// - **specificOutputDirPath:** Caminho do diretório de saída específico onde o arquivo descompactado será armazenado.
    /// - **huffman:** Instância da classe `HuffmanCoding` responsável pela descompactação.
    /// - **basePath:** Objeto `Path` representando o caminho base para preservar a estrutura de diretórios.
    ///
    /// ### Exceções
    /// - **IOException:** Lança exceção se ocorrer um erro durante a leitura ou escrita do arquivo.
    ///
    /// ### Exemplo
    /// ```java
    /// processDecompressFile(file, specificOutputDirPath, huffman, basePath);
    ///```
    ///
    /// @param file                  Objeto `File` representando o arquivo `.huff` a ser descompactado.
    /// @param specificOutputDirPath Caminho do diretório de saída específico.
    /// @param huffman               Instância da classe `HuffmanCoding`.
    /// @param basePath              Objeto `Path` representando o caminho base.
    private static void processDecompressFile(File file, String specificOutputDirPath, HuffmanCoding huffman, Path basePath) throws IOException {
        // Determina o caminho relativo em relação ao basePath para preservar a estrutura de diretórios
        Path filePath = file.toPath(); // Obtém o caminho do arquivo
        Path relativePath = basePath.relativize(filePath.getParent()); // Calcula o caminho relativo
        String fileOutputDirPath = specificOutputDirPath + separator + relativePath; // Define o caminho completo do diretório de saída

        // Cria o diretório de saída para o arquivo descompactado, se ainda não existir
        File fileOutputDir = new File(fileOutputDirPath); // Cria um objeto File para o diretório de saída
        if (!fileOutputDir.exists()) { // Verifica se o diretório existe
            if (fileOutputDir.mkdirs()) { // Tenta criar o diretório
                System.out.println("Diretório para arquivo descompactado criado em: " + fileOutputDirPath); // Informa a criação bem-sucedida
            } else {
                System.err.println("Não foi possível criar o diretório para arquivo descompactado: " + fileOutputDirPath); // Informa a falha na criação
                return; // Encerra o metodo
            }
        }

        String fileName = file.getName(); // Obtém o nome do arquivo
        String decompressedFileName = fileName.substring(0, fileName.length() - 5); // Remove a extensão ".huff" para obter o nome original
        String decompressedFilePath = fileOutputDirPath + separator + decompressedFileName; // Define o caminho do arquivo descompactado

        // Informa ao usuário que o processo de descompactação está iniciando
        System.out.println("\nProcessando arquivo para descompactação: " + file.getAbsolutePath());

        // Executa a descompactação do arquivo utilizando a classe `HuffmanCoding`
        huffman.decompress(file.getAbsolutePath(), decompressedFilePath);
        // Informa ao usuário que a descompactação foi concluída com sucesso
        System.out.println("Arquivo descompactado com sucesso em: " + decompressedFilePath);

        // Cria um objeto File para o arquivo descompactado e exibe seu tamanho
        File decompressedFile = new File(decompressedFilePath);
        System.out.println("Tamanho do arquivo descompactado: " + decompressedFile.length() + " bytes");
    }

    /// ## processDecompressDirectory
    ///
    /// Este metodo processa a descompactação de todos os arquivos `.huff` em um diretório de forma recursiva.
    /// Para cada arquivo `.huff` encontrado, ele chama o metodo `processDecompressFile`.
    /// Para cada subdiretório, ele cria o diretório correspondente no local de saída e chama a si recursivamente.
    ///
    /// ### Parâmetros
    /// - **dir:** Objeto `File` representando o diretório a ser processado.
    /// - **specificOutputDirPath:** Caminho do diretório de saída específico onde os arquivos descompactados serão armazenados.
    /// - **huffman:** Instância da classe `HuffmanCoding` responsável pela descompactação.
    /// - **basePath:** Objeto `Path` representando o caminho base para preservar a estrutura de diretórios.
    ///
    /// ### Exceções
    /// - **IOException:** Lança exceção se ocorrer um erro durante a leitura ou escrita dos arquivos.
    ///
    /// ### Exemplo
    /// ```java
    /// processDecompressDirectory(dir, specificOutputDirPath, huffman, basePath);
    ///```
    ///
    /// @param dir                   Objeto `File` representando o diretório a ser processado.
    /// @param specificOutputDirPath Caminho do diretório de saída específico.
    /// @param huffman               Instância da classe `HuffmanCoding`.
    /// @param basePath              Objeto `Path` representando o caminho base.
    private static void processDecompressDirectory(File dir, String specificOutputDirPath, HuffmanCoding huffman, Path basePath) throws IOException {
        // Lista todos os arquivos e subdiretórios no diretório atual
        File[] files = dir.listFiles();
        if (files == null) { // Verifica se a listagem foi bem-sucedida
            System.err.println("Não foi possível listar os arquivos no diretório: " + dir.getAbsolutePath());
            return; // Encerra o metodo
        }

        // Itera sobre cada arquivo ou subdiretório encontrado
        for (File file : files) {
            if (file.isFile()) { // Se for um arquivo
                if (file.getName().toLowerCase().endsWith(".huff")) { // Verifica se o arquivo tem a extensão ".huff"
                    processDecompressFile(file, specificOutputDirPath, huffman, basePath); // Processa a descompactação do arquivo
                }
            } else if (file.isDirectory()) { // Se for um subdiretório
                // Determina o caminho relativo para o subdiretório
                Path relativePath = basePath.relativize(file.toPath()); // Calcula o caminho relativo
                String subDirOutputPath = specificOutputDirPath + separator + relativePath; // Define o caminho de saída do subdiretório
                File subDirOutput = new File(subDirOutputPath); // Cria um objeto File para o subdiretório de saída

                if (!subDirOutput.exists()) { // Verifica se o subdiretório de saída existe
                    if (subDirOutput.mkdirs()) { // Tenta criar o subdiretório
                        System.out.println("Subdiretório para descompressão criado em: " + subDirOutputPath); // Informa a criação
                    } else {
                        System.err.println("Não foi possível criar o subdiretório para descompressão: " + subDirOutputPath); // Informa a falha na criação
                        continue; // Pula este subdiretório e continua com os próximos
                    }
                }

                // Chama recursivamente este metodo para processar o subdiretório
                processDecompressDirectory(file, specificOutputDirPath, huffman, basePath);
            }
        }
    }

    /// ## compareFiles
    ///
    /// Este metodo compara dois arquivos byte a byte para verificar se são exatamente iguais. É utilizado para garantir que a descompactação preservou a integridade dos arquivos originais.
    ///
    /// ### Parâmetros
    /// - **file1:** Objeto `File` representando o primeiro arquivo a ser comparado.
    /// - **file2:** Objeto `File` representando o segundo arquivo a ser comparado.
    ///
    /// ### Retorno
    /// - **true:** Se os arquivos forem exatamente iguais.
    /// - **false:** Se houver qualquer diferença entre os arquivos.
    ///
    /// ### Exceções
    /// - **IOException:** Lança exceção se ocorrer um erro durante a leitura dos arquivos.
    ///
    /// ### Exemplo
    /// ```java
    /// boolean iguais = compareFiles(file1, file2);
    ///```
    ///
    /// @param file1 Primeiro arquivo a ser comparado.
    /// @param file2 Segundo arquivo a ser comparado.
    /// @return `true` se os arquivos forem iguais; `false` caso contrário.
    private static boolean compareFiles(File file1, File file2) throws IOException {
        // Lê todos os bytes do primeiro arquivo
        byte[] f1 = readAllBytes(file1.toPath());
        // Lê todos os bytes do segundo arquivo
        byte[] f2 = readAllBytes(file2.toPath());

        // Compara os arrays de bytes para verificar se são iguais
        return Arrays.equals(f1, f2);
    }

    /// ## compareDescompressedWithOriginal
    ///
    /// Este metodo compara os arquivos descompactados com os originais dentro dos diretórios correspondentes. Ele verifica se todos os arquivos e subdiretórios foram descompactados corretamente.
    ///
    /// ### Parâmetros
    /// - **originalDir:** Objeto `File` representando o diretório original compactado.
    /// - **decompressedDirPath:** Caminho do diretório onde os arquivos foram descompactados.
    ///
    /// ### Retorno
    /// - **true:** Se todos os arquivos e subdiretórios corresponderem corretamente.
    /// - **false:** Se houver qualquer discrepância.
    ///
    /// ### Exceções
    /// - **IOException:** Lança exceção se ocorrer um erro durante a leitura dos arquivos.
    ///
    /// ### Exemplo
    /// ```java
    /// boolean iguais = compareDescompressedWithOriginal(originalDir, decompressedDirPath);
    ///```
    ///
    /// @param originalDir         Objeto `File` representando o diretório original compactado.
    /// @param decompressedDirPath Caminho do diretório onde os arquivos foram descompactados.
    /// @return `true` se todos os arquivos corresponderem; `false` caso contrário.
    private static boolean compareDescompressedWithOriginal(File originalDir, String decompressedDirPath) throws IOException {
        File decompressedDir = new File(decompressedDirPath); // Cria um objeto File para o diretório descompactado

        // Verifica se o diretório descompactado existe e é válido
        if (!decompressedDir.exists() || !decompressedDir.isDirectory()) {
            System.err.println("O diretório descompactado não existe ou não é um diretório válido: " + decompressedDirPath);
            return false; // Retorna falso indicando falha na comparação
        }

        // Chama o metodo recursivo para comparar os diretórios
        return compareDirectories(originalDir, decompressedDir);
    }

    /// ## compareDirectories
    ///
    /// Este metodo compara dois diretórios de forma recursiva para verificar se todos os arquivos e subdiretórios correspondentes são iguais.
    ///
    /// ### Parâmetros
    /// - **originalDir:** Objeto `File` representando o diretório original.
    /// - **decompressedDir:** Objeto `File` representando o diretório descompactado.
    ///
    /// ### Retorno
    /// - **true:** Se todos os arquivos e subdiretórios corresponderem corretamente.
    /// - **false:** Se houver qualquer discrepância.
    ///
    /// ### Exceções
    /// - **IOException:** Lança exceção se ocorrer um erro durante a leitura dos arquivos.
    ///
    /// ### Exemplo
    /// ```java
    /// boolean iguais = compareDirectories(originalDir, decompressedDir);
    ///```
    ///
    /// @param originalDir     Objeto `File` representando o diretório original.
    /// @param decompressedDir Objeto `File` representando o diretório descompactado.
    /// @return `true` se todos os arquivos corresponderem; `false` caso contrário.
    private static boolean compareDirectories(File originalDir, File decompressedDir) throws IOException {
        // Lista todos os arquivos e subdiretórios no diretório original
        File[] originalFiles = originalDir.listFiles();
        if (originalFiles == null) { // Verifica se a listagem foi bem-sucedida
            System.err.println("Não foi possível listar os arquivos no diretório original: " + originalDir.getAbsolutePath());
            return false; // Encerra o metodo indicando falha
        }

        // Variável para rastrear se todas as comparações foram bem-sucedidas
        boolean todasIguais = true;

        // Itera sobre cada arquivo ou subdiretório no diretório original
        for (File originalFile : originalFiles) {
            // Determina o arquivo ou subdiretório correspondente no diretório descompactado
            File correspondingDecompressedFile = new File(decompressedDir, originalFile.getName());

            if (originalFile.isFile()) { // Se for um arquivo
                // Verifica se o arquivo descompactado existe e é um arquivo
                if (!correspondingDecompressedFile.exists() || !correspondingDecompressedFile.isFile()) {
                    System.err.println("Arquivo descompactado não encontrado para: " + originalFile.getAbsolutePath());
                    todasIguais = false; // Atualiza o rastreamento de sucesso
                    continue; // Continua com o próximo arquivo
                }

                // Compara os arquivos
                boolean iguais = compareFiles(originalFile, correspondingDecompressedFile);
                if (iguais) { // Se os arquivos forem iguais
                    System.out.println("Sucesso: " + originalFile.getName() + " foi descompactado corretamente."); // Informa o sucesso
                } else { // Se os arquivos diferirem
                    System.err.println("Falha: " + originalFile.getName() + " difere do arquivo original."); // Informa a falha
                    todasIguais = false; // Atualiza o rastreamento de sucesso
                }
            } else if (originalFile.isDirectory()) { // Se for um subdiretório
                // Verifica se o subdiretório descompactado existe e é um diretório
                if (!correspondingDecompressedFile.exists() || !correspondingDecompressedFile.isDirectory()) {
                    System.err.println("Subdiretório descompactado não encontrado para: " + originalFile.getAbsolutePath());
                    todasIguais = false; // Atualiza o rastreamento de sucesso
                    continue; // Continua com o próximo subdiretório
                }

                // Chama recursivamente este metodo para comparar o subdiretório
                boolean subDirIguais = compareDirectories(originalFile, correspondingDecompressedFile);

                if (!subDirIguais) todasIguais = false; // Atualiza o rastreamento se houver discrepâncias
            }
        }

        return todasIguais; // Retorna o resultado final da comparação
    }
}
