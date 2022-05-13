package com.bobocode.hashtable;

/**
 * A generic class Node that supports two type params: one for the key and one for the value.
 *
 * @param <K> key type parameter
 * @param <V> value type parameter
 */
public class Node<K, V> {
    K key;
    V value;
    Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}