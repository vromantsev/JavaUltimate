package com.bobocode.hashtable;

import java.util.Objects;

/**
 * A simple implementation of the Hash Table that allows storing a generic key-value pair. The table itself is based
 * on the array of {@link Node} objects.
 * <p>
 * An initial array capacity is 16.
 * <p>
 * Every time a number of elements is equal to the array size that tables gets resized
 * (it gets replaced with a new array that it twice bigger than before). E.g. resize operation will replace array
 * of size 16 with a new array of size 32. PLEASE NOTE that all elements should be reinserted to the new table to make
 * sure that they are still accessible  from the outside by the same key.
 *
 * @param <K> key type parameter
 * @param <V> value type parameter
 */
public class HashTable<K, V> {

    private static final int DEFAULT_CAPACITY = 16;

    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public HashTable() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    /**
     * Puts a new element to the table by its key. If there is an existing element by such key then it gets replaced
     * with a new one, and the old value is returned from the method. If there is no such key then it gets added and
     * null value is returned.
     *
     * @param key   element key
     * @param value element value
     * @return old value or null
     */
    public V put(K key, V value) {
        Objects.requireNonNull(key, "Null keys are not allowed!");
        Objects.requireNonNull(value, "Null values are not allowed!");
        checkInternalCapacity();
        var bucket = getBucket(key, this.table.length);
        V oldValue = null;
        var old = findNode(key);
        if (old != null) {
            if (!old.value.equals(value)) {
                var newNode = new Node<>(key, value);
                newNode.next = old;
                this.table[bucket] = newNode;
            }
            oldValue = old.value;
        } else {
            this.table[bucket] = new Node<>(key, value);
            this.size++;
        }
        return oldValue;
    }

    /**
     * Checks whether it's enough capacity to insert new elements. If not then a table will be resized using formula:
     * newCapacity = oldCapacity * 2
     */
    private void checkInternalCapacity() {
        var oldCapacity = this.table.length;
        var oldSize = this.size;
        if ((oldCapacity - oldSize) < 1) {
            var newCapacity = oldCapacity * 2;
            @SuppressWarnings("unchecked") final Node<K, V>[] newTable = new Node[newCapacity];
            var oldTable = this.table;
            for (final Node<K, V> current : oldTable) {
                if (current != null) {
                    var bucket = getBucket(current.key, newTable.length);
                    newTable[bucket] = current;
                }
            }
            this.table = newTable;
        }
    }

    /**
     * Prints a content of the underlying table (array) according to the following format:
     * 0: key1:value1 -> key2:value2
     * 1:
     * 2: key3:value3
     * ...
     */
    public void printTable() {
        var currentTable = this.table;
        for (int i = 0; i < currentTable.length; i++) {
            final Node<K, V> node = currentTable[i];
            if (node != null) {
                System.out.print(i + ": " + node.key + ":" + node.value);
                if (node.next != null) {
                    var tmp = node.next;
                    while (tmp != null) {
                        System.out.print(" -> " + tmp.key + ":" + tmp.value);
                        tmp = tmp.next;
                    }
                }
                System.out.println();
            } else {
                System.out.println(i + ": ");
            }
        }
    }

    /**
     * Calculates a bucket on top of key and table capacity.
     *
     * @param key      key
     * @param capacity table capacity
     * @return position which is basically an array index where an element will be inserted
     */
    private int getBucket(final K key, final int capacity) {
        return Math.abs(key.hashCode() % capacity);
    }

    /**
     * Searches for a node in a current table by a given key.
     *
     * @param key key
     * @return instance of {@link Node} or null if a node with a given key does not exist
     */
    private Node<K, V> findNode(final K key) {
        var currentTable = this.table;
        Node<K, V> target = null;
        if (size > 0) {
            for (final Node<K, V> node : currentTable) {
                if (node != null) {
                    final K currentKey = node.key;
                    if (currentKey.equals(key)) {
                        target = node;
                        break;
                    }
                }
            }
        }
        return target;
    }
}