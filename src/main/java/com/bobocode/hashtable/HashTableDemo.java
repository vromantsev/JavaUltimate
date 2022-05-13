package com.bobocode.hashtable;

public class HashTableDemo {
    public static void main(String[] args) {
        HashTable<String, Integer> hashTable = new HashTable<>();
        for (int i = 0; i < 20; i++) {
            hashTable.put(String.valueOf(i), i);
            if (i % 4 == 0) {
                hashTable.put(String.valueOf(i), i + 100);
            }
            if (i % 4 == 0) {
                hashTable.put(String.valueOf(i), i + 1000);
            }
        }
        hashTable.printTable();
    }
}
