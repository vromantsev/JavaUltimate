package com.bobocode.reversedLL;

import java.util.Objects;
import java.util.Stack;

public class ReverseLinkedList {

    public static void main(String[] args) {
        var head = createLinkedList(4, 3, 9, 1);
        printReversedRecursively(head);
        System.out.println();
        printReversedUsingStack(head);
    }

    /**
     * Creates a list of linked {@link Node} objects based on the given array of elements and returns a head of the list.
     *
     * @param elements an array of elements that should be added to the list
     * @param <T>      elements type
     * @return head of the list
     */
    @SafeVarargs
    public static <T> Node<T> createLinkedList(T... elements) {
        Objects.requireNonNull(elements, "Initial arguments must be provided!");
        final Node<T> head = new Node<>(elements[0]);
        var headCopy = head;
        for (int i = 1; i < elements.length; i++) {
            headCopy.next = new Node<>(elements[i]);
            headCopy = headCopy.next;
        }
        return head;
    }

    /**
     * Prints a list in a reserved order using a recursion technique. Please note that it should not change the list,
     * just print its elements.
     * <p>
     * Imagine you have a list of elements 4,3,9,1 and the current head is 4. Then the outcome should be the following:
     * 1 -> 9 -> 3 -> 4
     *
     * @param head the first node of the list
     * @param <T>  elements type
     */
    public static <T> void printReversedRecursively(Node<T> head) {
        if (head.next != null) {
            printReversedRecursively(head.next);
            System.out.print(" -> " + head.value);
        } else {
            System.out.print(head.value);
        }
    }

    /**
     * Prints a list in a reserved order using a {@link java.util.Stack} instance. Please note that it should not change
     * the list, just print its elements.
     * <p>
     * Imagine you have a list of elements 4,3,9,1 and the current head is 4. Then the outcome should be the following:
     * 1 -> 9 -> 3 -> 4
     *
     * @param head the first node of the list
     * @param <T>  elements type
     */
    public static <T> void printReversedUsingStack(Node<T> head) {
        Objects.requireNonNull(head);
        var stack = new Stack<T>();
        var headCopy = head;
        while (headCopy != null) {
            stack.push(headCopy.value);
            headCopy = headCopy.next;
        }
        System.out.print(stack.pop());
        while (!stack.isEmpty()) {
            System.out.print(" -> " + stack.pop());
        }
    }

    public static class Node<T> {
        T value;
        Node<T> next;

        public Node(T value) {
            this.value = value;
        }
    }
}
