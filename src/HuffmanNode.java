import java.util.Objects;

import static data_structures.ShallowOrDeepCopy.ShallowOrDeepCopy.verifyAndCopy;
import static java.lang.Integer.compare;

public class HuffmanNode implements Comparable<HuffmanNode>, Cloneable {

    public byte byteValue;
    public int frequency;
    public HuffmanNode leftNode;
    public HuffmanNode rightNode;

    // Construtor para nós folha
    public HuffmanNode(byte byteValue, int frequency) {
        this.byteValue = byteValue;
        this.frequency = frequency;
        this.leftNode = null;
        this.rightNode = null;
    }

    // Construtor para nós internos
    public HuffmanNode(int frequency, HuffmanNode leftNode, HuffmanNode rightNode) {
        this.byteValue = 0; // Valor arbitrário para nós internos
        this.frequency = frequency;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public HuffmanNode(HuffmanNode node) {
        this.byteValue = node.byteValue;
        this.frequency = node.frequency;
        this.leftNode = (HuffmanNode) verifyAndCopy(node.leftNode);
        this.rightNode = (HuffmanNode) verifyAndCopy(node.rightNode);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public HuffmanNode clone() {
        HuffmanNode clone = null;
        try {
            clone = new HuffmanNode(this);
        } catch (Exception ignored) {
        }
        return clone;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;

        HuffmanNode that = (HuffmanNode) o;

        return byteValue == that.byteValue &&
               frequency == that.frequency &&
               Objects.equals(leftNode, that.leftNode) &&
               Objects.equals(rightNode, that.rightNode);
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int hash = 1;

        hash *= prime + byteValue;
        hash *= prime + frequency;
        hash *= prime + (leftNode == null ? 0 : leftNode.hashCode());
        hash *= prime + (rightNode == null ? 0 : rightNode.hashCode());

        if (hash < 0) hash = -hash;

        return hash;
    }

    @Override
    public final int compareTo(HuffmanNode o) {
        return compare(this.frequency, o.frequency);
    }

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

    public final boolean isLeaf() {
        return leftNode == null && rightNode == null;
    }
}
