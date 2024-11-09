import java.util.Objects;

import static ShallowOrDeepCopy.ShallowOrDeepCopy.verifyAndCopy;
import static java.lang.Integer.compare;

/// # HuffmanNode
///
/// Represents a node in the Huffman encoding tree.
///
/// This class is used to store characters and their frequencies. A `HuffmanNode` can be a leaf node containing a character and its frequency, or an internal node with child nodes to build the Huffman tree.
///
/// ## Fields
///
/// - `character`: The character stored in this node (only for leaf nodes).
/// - `frequency`: The frequency of the character or the sum of frequencies of the child nodes (for internal nodes).
/// - `leftNode`: The left child node in the Huffman tree.
/// - `rightNode`: The right child node in the Huffman tree.
///
/// ## Example
///
/// ```java
/// HuffmanNode leafNode = new HuffmanNode('a', 5);
/// HuffmanNode internalNode = new HuffmanNode(10, leafNode, leafNode.clone());
///```
public class HuffmanNode implements Comparable<HuffmanNode>, Cloneable {

    /// The character stored in this node (only for leaf nodes).
    public char character;

    /// The frequency of the character or the sum of frequencies of the child nodes (for internal nodes).
    public int frequency;

    /// The left child node in the Huffman tree.
    public HuffmanNode leftNode;

    /// The right child node in the Huffman tree.
    public HuffmanNode rightNode;

    /// ## HuffmanNode(char character, int frequency)
    ///
    /// Constructs a new leaf `HuffmanNode` with the specified character and frequency.
    ///
    /// ### Parameters
    ///
    /// - `character`: The character to be stored in this node.
    /// - `frequency`: The frequency of the character.
    ///
    /// ### Example
    ///
    /// ```java
    /// HuffmanNode leaf = new HuffmanNode('a', 5);
    ///```
    ///
    /// @param character The character stored in this node.
    /// @param frequency The frequency of the character.
    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.leftNode = null;
        this.rightNode = null;
    }

    /// ## HuffmanNode(int frequency, HuffmanNode leftNode, HuffmanNode rightNode)
    ///
    /// Constructs a new internal `HuffmanNode` with the specified frequency and child nodes.
    ///
    /// ### Parameters
    ///
    /// - `frequency`: The sum of the frequencies of the child nodes.
    /// - `leftNode`: The left child node.
    /// - `rightNode`: The right child node.
    ///
    /// ### Example
    ///
    /// ```java
    /// HuffmanNode internal = new HuffmanNode(10, leftNode, rightNode);
    ///```
    ///
    /// @param frequency The combined frequency of the child nodes.
    /// @param leftNode  The left child node.
    /// @param rightNode The right child node.
    public HuffmanNode(int frequency, HuffmanNode leftNode, HuffmanNode rightNode) {
        this.character = '\0'; // Indicator of an internal node
        this.frequency = frequency;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    /// ## HuffmanNode(HuffmanNode node)
    ///
    /// Copy constructor to create a deep copy of the given `HuffmanNode`.
    ///
    /// ### Parameters
    ///
    /// - `node`: The node to be copied.
    ///
    /// ### Example
    ///
    /// ```java
    /// HuffmanNode original = new HuffmanNode('a', 5);
    /// HuffmanNode copy = new HuffmanNode(original);
    ///```
    ///
    /// @param node The node to be copied.
    public HuffmanNode(HuffmanNode node) {
        this.character = node.character;
        this.frequency = node.frequency;
        this.leftNode = (HuffmanNode) verifyAndCopy(node.leftNode);
        this.rightNode = (HuffmanNode) verifyAndCopy(node.rightNode);
    }

    /// ## clone()
    ///
    /// Creates a deep copy of this `HuffmanNode`.
    ///
    /// ### Returns
    ///
    /// A new instance of `HuffmanNode` with the same values as this node.
    ///
    /// ### Example
    ///
    /// ```java
    /// HuffmanNode clone = original.clone();
    ///```
    ///
    /// @return A deep copy of this node.
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

    /// ## equals(Object o)
    ///
    /// Checks if this node is equal to another object.
    ///
    /// ### Parameters
    ///
    /// - `o`: The object to compare with.
    ///
    /// ### Returns
    ///
    /// `true` if both objects are equal; `false` otherwise.
    ///
    /// ### Example
    ///
    /// ```java
    /// boolean isEqual = node1.equals(node2);
    ///```
    ///
    /// @param o The object to compare with.
    /// @return `true` if both objects are equal; `false` otherwise.
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        HuffmanNode that = (HuffmanNode) o;

        return character == that.character &&
               frequency == that.frequency &&
               Objects.equals(leftNode, that.leftNode) &&
               Objects.equals(rightNode, that.rightNode);
    }

    /// ## hashCode()
    ///
    /// Computes the hash code for this node.
    ///
    /// ### Returns
    ///
    /// The hash code value.
    ///
    /// ### Example
    ///
    /// ```java
    /// int hash = node.hashCode();
    ///```
    ///
    /// @return The hash code value.
    @Override
    public final int hashCode() {
        final int prime = 31;
        int hash = 1;

        hash = prime * hash + character;
        hash = prime * hash + frequency;
        hash = prime * hash + (leftNode == null ? 0 : leftNode.hashCode());
        hash = prime * hash + (rightNode == null ? 0 : rightNode.hashCode());

        if (hash < 0) hash = -hash;

        return hash;
    }

    /// ## compareTo(HuffmanNode o)
    ///
    /// Compares this node with another node based on frequency.
    ///
    /// ### Parameters
    ///
    /// - `o`: The node to compare with.
    ///
    /// ### Returns
    ///
    /// A negative integer, zero, or a positive integer as this node is less than, equal to, or greater than the specified node.
    ///
    /// ### Example
    ///
    /// ```java
    /// int comparison = node1.compareTo(node2);
    ///```
    ///
    /// @param o The node to compare with.
    /// @return A negative integer, zero, or a positive integer as this node is less than, equal to, or greater than the specified node.
    @Override
    public final int compareTo(HuffmanNode o) {
        return compare(this.frequency, o.frequency);
    }

    /// ## toString()
    ///
    /// Returns a string representation of this node.
    ///
    /// ### Returns
    ///
    /// A string representing this node.
    ///
    /// ### Example
    ///
    /// ```java
    /// String representation = node.toString();
    ///```
    ///
    /// @return The string representation of this node.
    @Override
    public final String toString() {
        if (isLeaf()) {
            return "HuffmanNode{" +
                   "character=" + character +
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

    /// ## isLeaf()
    ///
    /// Checks if this node is a leaf node (has no children).
    ///
    /// ### Returns
    ///
    /// `true` if the node is a leaf; `false` otherwise.
    ///
    /// ### Example
    ///
    /// ```java
    /// boolean leaf = node.isLeaf();
    ///```
    ///
    /// @return `true` if the node is a leaf; `false` otherwise.
    public final boolean isLeaf() {
        return leftNode == null && rightNode == null;
    }
}
