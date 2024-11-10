import java.util.Objects;

import static data_structures.ShallowOrDeepCopy.ShallowOrDeepCopy.verifyAndCopy;
import static java.lang.Integer.compare;

/// # HuffmanNode
///
/// A classe 'HuffmanNode' representa um nó na árvore de **Codificação de Huffman**. Cada nó pode ser um nó folha, representando um byte específico, ou um nó interno, representando a soma das frequências dos seus nós filhos.
///
/// ## Funcionalidades
/// - **Construtores:** Permite a criação de nós folha e internos, bem como a clonagem de nós existentes.
/// - **Comparação:** Implementa a interface 'Comparable' para ordenar os nós com base na frequência.
/// - **Clonagem:** Implementa a interface 'Cloneable' para permitir a clonagem profunda dos nós.
/// - **Igualdade e Hashing:** Sobrescreve os métodos 'equals' e 'hashCode' para garantir a correta comparação e utilização em estruturas de dados baseadas em hashing.
/// - **Representação em String:** Sobrescreve o metodo 'toString' para fornecer uma representação textual dos nós.
/// - **Verificação de Folha:** Metodo 'isLeaf' para verificar se o nó é uma folha.
///
/// ## Uso
/// Utilize os construtores para criar 'nós' folha ou internos e as funcionalidades de clonagem, comparação e verificação conforme necessário.
///
/// ### Exemplo
/// ```java
/// // Criando um nó folha
/// HuffmanNode leafNode = new HuffmanNode((byte) 'A', 5);
///
/// // Criando nós internos
/// HuffmanNode leftChild = new HuffmanNode((byte) 'A', 5);
/// HuffmanNode rightChild = new HuffmanNode((byte) 'B', 3);
/// HuffmanNode internalNode = new HuffmanNode(8, leftChild, rightChild);
///
/// // Clonando um nó
/// HuffmanNode clonedNode = (HuffmanNode) internalNode.clone();
///
/// // Verificando se um nó é folha
/// boolean isLeaf = leafNode.isLeaf(); // Retorna true
/// boolean isLeafInternal = internalNode.isLeaf(); // Retorna false
///```
public class HuffmanNode implements Comparable<HuffmanNode>, Cloneable {

    /// ### Campos
    ///
    /// - 'byteValue': O byte representado pelo nó. Utilizado apenas para nós folha.
    /// - 'frequency': A frequência do byte ou a soma das frequências dos nós filhos.
    /// - 'leftNode': referência para o nó filho à esquerda.
    /// - 'rightNode': referência para o nó filho à direita.
    public byte byteValue;
    public int frequency;
    public HuffmanNode leftNode;
    public HuffmanNode rightNode;

    /// ## HuffmanNode (Construtor para Nós Folha)
    ///
    /// Cria um nó folha com um byte específico e sua frequência.
    ///
    /// ### Parâmetros
    /// - 'byteValue': byte representado pelo nó.
    /// - 'frequency': frequência do byte.
    ///
    /// ### Fluxo de Operações
    /// 1. Inicializa 'byteValue' com o valor fornecido.
    /// 2. Inicializa 'frequency' com a frequência fornecida.
    /// 3. Define 'leftNode' e 'rightNode' como 'null', indicando que é um nó folha.
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste construtor.
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
    /// - 'frequency': soma das frequências dos nós filhos.
    /// - 'leftNode': nó filho à esquerda.
    /// - 'rightNode': nó filho à direita.
    ///
    /// ### Fluxo de Operações
    /// 1. Inicializa 'byteValue' com '0', pois não representa um byte específico.
    /// 2. Inicializa 'frequency' com a soma das frequências dos nós filhos.
    /// 3. Define 'leftNode' e 'rightNode' com as referências fornecidas.
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste construtor.
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
    /// - 'node': nó existente a ser clonado.
    ///
    /// ### Fluxo de Operações
    /// 1. Copia 'byteValue' e 'frequency' do nó fornecido.
    /// 2. Utiliza o metodo 'verifyAndCopy' para clonar recursivamente os nós filhos.
    ///
    /// ### Exceções
    /// - Pode lançar exceções dependendo da implementação de 'verifyAndCopy'.
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
    /// - 'HuffmanNode': cópia clonada do nó atual.
    ///
    /// ### Fluxo de Operações
    /// 1. Tenta criar uma nova instância de 'HuffmanNode' utilizando o construtor de cópia.
    /// 2. Retorna a cópia clonada.
    ///
    /// ### Exceções
    /// - Ignora quaisquer exceções lançadas durante o processo de clonagem.
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public HuffmanNode clone() {
        HuffmanNode clone = null;
        try {
            clone = new HuffmanNode(this);
        } catch (Exception ignored) {
            // Exceções são ignoradas conforme anotação @SuppressWarnings
        }
        return clone;
    }

    /// ## equals
    ///
    /// Verifica se este nó é igual a outro objeto.
    ///
    /// ### Parâmetros
    /// - 'o': objeto a ser comparado com este nó.
    ///
    /// ### Retorno
    /// - 'true': se o objeto for uma instância de 'HuffmanNode' com os mesmos valores de 'byteValue', 'frequency', 'leftNode' e 'rightNode'.
    /// - 'false': caso contrário.
    ///
    /// ### Fluxo de Operações
    /// 1. Verifica se o objeto fornecido é a própria instância ('this').
    /// 2. Verifica se o objeto é 'null'.
    /// 3. Verifica se as classes dos objetos são diferentes.
    /// 4. Compara os valores de 'byteValue', 'frequency', 'leftNode' e 'rightNode'.
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste metodo.
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true; // Verifica referência
        if (o == null) return false; // Verifica null
        if (this.getClass() != o.getClass()) return false; // Verifica classe

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
    /// - 'int': Hash code calculado.
    ///
    /// ### Fluxo de Operações
    /// 1. Utiliza um número primo ('31') para calcular o hash.
    /// 2. Combina os valores de 'byteValue', 'frequency', 'leftNode' e 'rightNode' para gerar o hash.
    /// 3. Garante que o hash seja positivo.
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste metodo.
    @Override
    public final int hashCode() {
        final int prime = 31;
        int hash = 1;

        hash = prime * hash + byteValue;
        hash = prime * hash + frequency;
        hash = prime * hash + (leftNode == null ? 0 : leftNode.hashCode());
        hash = prime * hash + (rightNode == null ? 0 : rightNode.hashCode());

        if (hash < 0) hash = -hash; // Garante que o hash seja positivo

        return hash;
    }

    /// ## compareTo
    ///
    /// Compara este nó com outro nó baseado na frequência.
    ///
    /// ### Parâmetros
    /// - 'o': nó a ser comparado com este nó.
    ///
    /// ### Retorno
    /// - 'int': valor negativo se este nó for menor, zero se forem iguais, e positivo se este nó for maior.
    ///
    /// ### Fluxo de Operações
    /// 1. Compara as frequências dos dois nós utilizando o metodo 'compare' da classe 'Integer'.
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste metodo.
    @Override
    public final int compareTo(HuffmanNode o) {
        return compare(this.frequency, o.frequency);
    }

    /// ## toString
    ///
    /// Retorna uma representação em string deste nó.
    ///
    /// ### Retorno
    /// - 'String': representação textual do nó.
    ///
    /// ### Fluxo de Operações
    /// 1. Verifica se o nó é uma folha.
    /// 2. Retorna uma string formatada contendo 'byteValue' e 'frequency' para nós folha.
    /// 3. Retorna uma string formatada contendo 'frequency', 'leftNode' e 'rightNode' para nós internos.
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste metodo.
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
    /// - 'true': se o nó for folha.
    /// - 'false': caso contrário.
    ///
    /// ### Fluxo de Operações
    /// 1. Verifica se 'leftNode' e 'rightNode' são 'null'.
    ///
    /// ### Exceções
    /// Nenhuma exceção lançada diretamente neste metodo.
    public final boolean isLeaf() {
        return leftNode == null && rightNode == null;
    }
}
