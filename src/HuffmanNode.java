import static java.lang.Integer.compare;

public class HuffmanNode implements Comparable<HuffmanNode> {
    public char character;
    public int frequency;
    public HuffmanNode leftNode;
    public HuffmanNode rightNode;

    // Construtor para nós folha
    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.leftNode = null;
        this.rightNode = null;
    }

    // Construtor para nós internos
    public HuffmanNode(int frequency, HuffmanNode leftNode, HuffmanNode rightNode) {
        this.character = '\0'; // Indicador de nó interno
        this.frequency = frequency;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    // Method for comparison in PriorityQueue
    @Override
    public int compareTo(HuffmanNode o) {
        return compare(this.frequency, o.frequency);
    }
}
