import java.util.Objects;

import static data_structures.ShallowOrDeepCopy.ShallowOrDeepCopy.verifyAndCopy;
import static java.lang.Integer.compare;

/// # HuffmanNode
///
/// A classe `HuffmanNode` representa um nó na árvore de **Codificação de Huffman**, uma estrutura de dados fundamental utilizada em algoritmos de compressão de dados.
/// Cada nó pode ser um nó folha, que representa um byte específico e sua frequência de ocorrência, ou um nó interno, que representa a soma das frequências dos seus nós filhos.
///
/// ## Funcionalidades
/// - **Construtores:** Permite a criação de nós folha e internos, bem como a clonagem de nós existentes para suportar operações de cópia profunda.
/// - **Comparação:** Implementa a interface `Comparable` para ordenar os nós com base na frequência, essencial para a construção eficiente da árvore de Huffman.
/// - **Clonagem:** Implementa a interface `Cloneable` para permitir a clonagem profunda dos nós, garantindo a integridade da árvore durante operações de duplicação.
/// - **Igualdade e Hashing:** Sobrescreve os metodos `equals` e `hashCode` para garantir a correta comparação e utilização em estruturas de dados baseadas em hashing, como `HashMap` e `HashSet`.
/// - **Representação em String:** Sobrescreve o metodo `toString` para fornecer uma representação textual clara e informativa dos nós, facilitando o debug e a visualização da árvore.
/// - **Verificação de Folha:** Metodo `isLeaf` para verificar se o nó é uma folha, permitindo decisões lógicas baseadas na estrutura da árvore.
///
/// ## Estrutura Interna
/// - **byteValue:** O byte representado pelo nó. Utilizado apenas para nós folha.
/// - **frequency:** A frequência do byte ou a soma das frequências dos nós filhos.
/// - **leftNode:** Referência para o nó filho à esquerda.
/// - **rightNode:** Referência para o nó filho à direita.
///
/// ## Uso
/// Utilize os construtores para criar nós folha ou internos e aproveite as funcionalidades de clonagem, comparação e verificação conforme necessário.
/// A classe é projetada para ser utilizada em estruturas de dados como filas de prioridade, que são essenciais na construção da árvore de Huffman.
///
/// ### Exemplos de Uso
/// ```java
/// // Criando um nó folha para o byte 'A' com frequência 5
/// HuffmanNode leafNodeA = new HuffmanNode((byte) 'A', 5);
///
/// // Criando um nó folha para o byte 'B' com frequência 3
/// HuffmanNode leafNodeB = new HuffmanNode((byte) 'B', 3);
///
/// // Criando um nó interno com frequência 8, combinando os nós folha de 'A' e 'B'
/// HuffmanNode internalNode = new HuffmanNode(8, leafNodeA, leafNodeB);
///
/// // Clonando um nó interno
/// HuffmanNode clonedInternalNode = (HuffmanNode) internalNode.clone();
///
/// // Verificando se um nó é folha
/// boolean isLeafA = leafNodeA.isLeaf(); // Retorna true
/// boolean isLeafInternal = internalNode.isLeaf(); // Retorna false
///
/// // Comparando dois nós com base na frequência
/// int comparisonResult = leafNodeA.compareTo(leafNodeB); // Retorna positivo, pois 5 > 3
///```
///
/// ## Considerações de Implementação
/// - **Imutabilidade:** Embora a classe permita modificações nos nós após sua criação, recomenda-se tratá-la como imutável para evitar inconsistências na estrutura da árvore.
/// - **Performance:** A implementação de `hashCode` e `equals` assegura eficiência em operações de hashing, mas deve-se considerar o custo de cálculo em árvores muito grandes.
/// - **Clonagem Profunda:** A implementação de clonagem profunda assegura que cópias dos nós não compartilhem referências com os originais, prevenindo efeitos colaterais indesejados.
///
/// ## Limitações
/// - A classe não é thread-safe. Se múltiplas threads acessarem e modificarem nós simultaneamente, deve-se implementar mecanismos de sincronização externos.
/// - A classe assume que as frequências são valores não negativos. Frequências negativas podem levar a comportamentos inesperados.
///
/// ## Referências
/// - [Algoritmo de Huffman](https://pt.wikipedia.org/wiki/Codifica%C3%A7%C3%A3o_de_Huffman)
/// - [Java Documentation](https://docs.oracle.com/en/java/javase/17/docs/api/index.html)
public class HuffmanNode implements Comparable<HuffmanNode>, Cloneable {

    /// ### Campos
    ///
    /// - **byteValue:** O byte representado pelo nó. Utilizado apenas para nós folha.
    /// - **frequency:** A frequência do byte ou a soma das frequências dos nós filhos.
    /// - **leftNode:** Referência para o nó filho à esquerda.
    /// - **rightNode:** Referência para o nó filho à direita.
    public byte byteValue;
    public int frequency;
    public HuffmanNode leftNode;
    public HuffmanNode rightNode;

    /// ## HuffmanNode (Construtor para Nós Folha)
    ///
    /// Cria um nó folha com um byte específico e sua frequência.
    ///
    /// ### Parâmetros
    /// - **byteValue:** Byte representado pelo nó.
    /// - **frequency:** Frequência do byte.
    ///
    /// ### Fluxo de Operações
    /// 1. Inicializa `byteValue` com o valor fornecido.
    /// 2. Inicializa `frequency` com a frequência fornecida.
    /// 3. Define `leftNode` e `rightNode` como `null`, indicando que é um nó folha.
    ///
    /// ### Exceções
    /// Nenhuma exceção é lançada diretamente neste construtor. É responsabilidade do chamador garantir que os parâmetros sejam válidos.
    ///
    /// ### Exemplo
    /// ```java
    /// HuffmanNode leaf = new HuffmanNode((byte) 'C', 10);
    ///```
    ///
    /// @param byteValue O byte que este nó representa.
    /// @param frequency A frequência de ocorrência do byte.
    public HuffmanNode(byte byteValue, int frequency) {
        this.byteValue = byteValue;
        this.frequency = frequency;
        this.leftNode = null;
        this.rightNode = null;
    }

    /// ## HuffmanNode (Construtor para Nós Internos)
    ///
    /// Cria um nó interno com a soma das frequências dos nós filhos.
    ///
    /// ### Parâmetros
    /// - **frequency:** Soma das frequências dos nós filhos.
    /// - **leftNode:** Nó filho à esquerda.
    /// - **rightNode:** Nó filho à direita.
    ///
    /// ### Fluxo de Operações
    /// 1. Inicializa `byteValue` com `0`, pois não representa um byte específico.
    /// 2. Inicializa `frequency` com a soma das frequências dos nós filhos.
    /// 3. Define `leftNode` e `rightNode` com as referências fornecidas.
    ///
    /// ### Exceções
    /// Nenhuma exceção é lançada diretamente neste construtor. É responsabilidade do chamador garantir que os nós filhos não sejam `null`.
    ///
    /// ### Exemplo
    /// ```java
    /// HuffmanNode internal = new HuffmanNode(15, leafNodeA, leafNodeB);
    ///```
    ///
    /// @param frequency A soma das frequências dos nós filhos.
    /// @param leftNode  O nó filho à esquerda.
    /// @param rightNode O nó filho à direita.
    public HuffmanNode(int frequency, HuffmanNode leftNode, HuffmanNode rightNode) {
        this.byteValue = 0; // Valor arbitrário para nós internos
        this.frequency = frequency;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    /// ## HuffmanNode (Construtor de Cópia)
    ///
    /// Cria uma cópia profunda de um nó existente.
    ///
    /// ### Parâmetros
    /// - **node:** Nó existente a ser clonado.
    ///
    /// ### Fluxo de Operações
    /// 1. Copia `byteValue` e `frequency` do nó fornecido.
    /// 2. Utiliza o metodo `verifyAndCopy` para clonar recursivamente os nós filhos.
    ///
    /// ### Exceções
    /// - Pode lançar exceções dependendo da implementação de `verifyAndCopy`. Recomenda-se tratar ou propagar exceções apropriadas.
    ///
    /// ### Exemplo
    /// ```java
    /// HuffmanNode original = new HuffmanNode((byte) 'D', 7);
    /// HuffmanNode clone = new HuffmanNode(original);
    ///```
    ///
    /// @param node O nó existente a ser clonado.
    public HuffmanNode(HuffmanNode node) {
        this.byteValue = node.byteValue;
        this.frequency = node.frequency;
        this.leftNode = (HuffmanNode) verifyAndCopy(node.leftNode);
        this.rightNode = (HuffmanNode) verifyAndCopy(node.rightNode);
    }

    /// ## clone
    ///
    /// Cria uma cópia profunda do nó atual.
    ///
    /// ### Retorno
    /// - **HuffmanNode:** Cópia clonada do nó atual.
    ///
    /// ### Fluxo de Operações
    /// 1. Tenta criar uma nova instância de `HuffmanNode` utilizando o construtor de cópia.
    /// 2. Retorna a cópia clonada.
    ///
    /// ### Exceções
    /// - Ignora quaisquer exceções lançadas durante o processo de clonagem. Em caso de falha, retorna `null`.
    ///
    /// ### Exemplo
    /// ```java
    /// HuffmanNode original = new HuffmanNode((byte) 'E', 12);
    /// HuffmanNode cloned = original.clone();
    ///```
    ///
    /// @return Uma cópia clonada do nó atual ou `null` se a clonagem falhar.
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public HuffmanNode clone() {
        HuffmanNode clone = null;
        try {
            clone = new HuffmanNode(this);
        } catch (Exception ignored) {
            // Clonagem falhou; retorna null
        }
        return clone;
    }

    /// ## equals
    ///
    /// Verifica se este nó é igual a outro objeto.
    ///
    /// ### Parâmetros
    /// - **o:** Objeto a ser comparado com este nó.
    ///
    /// ### Retorno
    /// - **boolean:** `true` se o objeto for uma instância de `HuffmanNode` com os mesmos valores de `byteValue`, `frequency`, `leftNode` e `rightNode`. `false` caso contrário.
    ///
    /// ### Fluxo de Operações
    /// 1. Verifica se o objeto fornecido é a própria instância (`this`).
    /// 2. Verifica se o objeto é `null`.
    /// 3. Verifica se as classes dos objetos são diferentes.
    /// 4. Compara os valores de `byteValue`, `frequency`, `leftNode` e `rightNode` utilizando `Objects.equals`.
    ///
    /// ### Exceções
    /// Nenhuma exceção é lançada diretamente neste metodo.
    ///
    /// ### Exemplo
    /// ```java
    /// HuffmanNode node1 = new HuffmanNode((byte) 'F', 4);
    /// HuffmanNode node2 = new HuffmanNode((byte) 'F', 4);
    /// boolean isEqual = node1.equals(node2); // Retorna true
    ///```
    ///
    /// @param o O objeto a ser comparado.
    /// @return `true` se os objetos forem iguais; `false` caso contrário.
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HuffmanNode that = (HuffmanNode) o;

        return byteValue == that.byteValue &&
               frequency == that.frequency &&
               Objects.equals(leftNode, that.leftNode) &&
               Objects.equals(rightNode, that.rightNode);
    }

    /// ## hashCode
    ///
    /// Calcula o hash code deste nó.
    ///
    /// ### Retorno
    /// - **int:** Hash code calculado.
    ///
    /// ### Fluxo de Operações
    /// 1. Utiliza um número primo (`31`) para calcular o hash.
    /// 2. Combina os valores de `byteValue`, `frequency`, `leftNode` e `rightNode` para gerar o hash.
    /// 3. Garante que o hash seja positivo.
    ///
    /// ### Exceções
    /// Nenhuma exceção é lançada diretamente neste metodo.
    ///
    /// ### Exemplo
    /// ```java
    /// HuffmanNode node = new HuffmanNode((byte) 'G', 9);
    /// int hash = node.hashCode();
    ///```
    ///
    /// @return O hash code calculado para este nó.
    @Override
    public final int hashCode() {
        final int prime = 31;
        int hash = 1;

        hash *= prime + byteValue;
        hash *= prime + frequency;
        hash *= prime + (leftNode == null ? 0 : leftNode.hashCode());
        hash *= prime + (rightNode == null ? 0 : rightNode.hashCode());

        if (hash < 0) hash = -hash; // Garante que o hash seja positivo

        return hash;
    }

    /// ## compareTo
    ///
    /// Compara este nó com outro nó baseado na frequência.
    ///
    /// ### Parâmetros
    /// - **o:** Nó a ser comparado com este nó.
    ///
    /// ### Retorno
    /// - **int:** Valor negativo se este nó for menor, zero se forem iguais, e positivo se este nó for maior.
    ///
    /// ### Fluxo de Operações
    /// 1. Compara as frequências dos dois nós utilizando o metodo `compare` da classe `Integer`.
    ///
    /// ### Exceções
    /// Nenhuma exceção é lançada diretamente neste metodo.
    ///
    /// ### Exemplo
    /// ```java
    /// HuffmanNode node1 = new HuffmanNode((byte) 'H', 2);
    /// HuffmanNode node2 = new HuffmanNode((byte) 'I', 3);
    /// int result = node1.compareTo(node2); // Retorna um valor negativo
    ///```
    ///
    /// @param o O nó a ser comparado.
    /// @return Um inteiro indicando a ordem relativa dos nós.
    @Override
    public final int compareTo(HuffmanNode o) {
        return compare(this.frequency, o.frequency);
    }

    /// ## toString
    ///
    /// Retorna uma representação em string deste nó.
    ///
    /// ### Retorno
    /// - **String:** Representação textual do nó.
    ///
    /// ### Fluxo de Operações
    /// 1. Verifica se o nó é uma folha utilizando o metodo `isLeaf`.
    /// 2. Retorna uma string formatada contendo `byteValue` e `frequency` para nós folha.
    /// 3. Retorna uma string formatada contendo `frequency`, `leftNode` e `rightNode` para nós internos.
    ///
    /// ### Exceções
    /// Nenhuma exceção é lançada diretamente neste metodo.
    ///
    /// ### Exemplo
    /// ```java
    /// HuffmanNode leaf = new HuffmanNode((byte) 'J', 6);
    /// System.out.println(leaf.toString()); // Saída: HuffmanNode{byteValue=74, frequency=6}
    ///
    /// HuffmanNode internal = new HuffmanNode(9, leaf, null);
    /// System.out.println(internal.toString()); // Saída: HuffmanNode{frequency=9, leftNode=HuffmanNode{byteValue=74, frequency=6}, rightNode=null}
    ///```
    ///
    /// @return Uma string representando o nó.
    @Override
    public final String toString() {
        if (isLeaf()) {
            return "HuffmanNode{" +
                   "byteValue=" + byteValue +
                   ", frequency=" + frequency +
                   '}';
        } else {
            return "HuffmanNode{" +
                   "frequency=" + frequency +
                   ", leftNode=" + leftNode +
                   ", rightNode=" + rightNode +
                   '}';
        }
    }

    /// ## isLeaf
    ///
    /// Verifica se o nó atual é um nó folha (não possui filhos).
    ///
    /// ### Retorno
    /// - **boolean:** `true` se o nó for folha; `false` caso contrário.
    ///
    /// ### Fluxo de Operações
    /// 1. Verifica se `leftNode` e `rightNode` são `null`.
    ///
    /// ### Exceções
    /// Nenhuma exceção é lançada diretamente neste metodo.
    ///
    /// ### Exemplo
    /// ```java
    /// HuffmanNode leaf = new HuffmanNode((byte) 'K', 7);
    /// boolean isLeaf = leaf.isLeaf(); // Retorna true
    ///
    /// HuffmanNode internal = new HuffmanNode(10, leaf, null);
    /// boolean isInternalLeaf = internal.isLeaf(); // Retorna false
    ///```
    ///
    /// @return `true` se o nó for uma folha; `false` caso contrário.
    public final boolean isLeaf() {
        return leftNode == null && rightNode == null;
    }
}
