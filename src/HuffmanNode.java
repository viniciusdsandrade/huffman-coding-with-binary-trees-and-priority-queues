import Tree.Node;

import static java.lang.Integer.compare;

public class HuffmanNode implements Comparable<HuffmanNode> {
    public char character; // Character represented (only for leaf nodes)
    public int frequency;  // Character frequency or sum of frequencies (for internal nodes)
    public HuffmanNode leftNode;  // Left child
    public HuffmanNode rightNode; // Right child

    // Constructor for leaf nodes
    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.leftNode = null;
        this.rightNode = null;
    }

    // Constructor for internal nodes
    public HuffmanNode(int frequency, HuffmanNode leftNode, HuffmanNode rightNode) {
        this.character = '\0'; // Indicator of internal node
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
