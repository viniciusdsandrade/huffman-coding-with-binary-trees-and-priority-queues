package data_structures.LinkedList.Tests.Unordered.DoubleLinkedListUnordered;

import data_structures.LinkedList.Unordered.DoubleLinkedListUnordered;

public class TestEqualsAndHashCode {
    public static void main(String[] args) {
        DoubleLinkedListUnordered<Integer> list1 = new DoubleLinkedListUnordered<>();
        DoubleLinkedListUnordered<Integer> list2 = new DoubleLinkedListUnordered<>();
        DoubleLinkedListUnordered<Integer> list3 = new DoubleLinkedListUnordered<>();
        list1.addLast(1);
        list1.addLast(2);
        list1.addLast(3);
        list1.addLast(4);
        list1.addLast(5);

        list2.addFirst(1);
        list2.addFirst(2);
        list2.addFirst(3);
        list2.addFirst(4);
        list2.addFirst(5);

        System.out.println("List1: " + list1);
        System.out.println("List2: " + list2);

        System.out.println("List1.hashCode(): " + list1.hashCode());
        System.out.println("List2.hashCode(): " + list2.hashCode());

        System.out.println("List1.equals(List2): " + list1.equals(list2));


        list3.addLast(1);
        list3.addLast(2);
        list3.addLast(3);
        list3.addLast(4);
        list3.addLast(5);

        System.out.println("List3: " + list3);
        System.out.println("List3.hashCode(): " + list3.hashCode());
    }
}
