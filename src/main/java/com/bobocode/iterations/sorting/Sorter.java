package com.bobocode.iterations.sorting;

import com.bobocode.iterations.utils.SortUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public final class Sorter {

    private Sorter() {
    }

    /**
     * Implementation of Insertion Sort algorithm.
     *
     * @param source collection to sort
     * @param <T>    parameter type
     * @return sorted collection
     */
    public static <T extends Comparable<? super T>> List<T> insertionSort(final List<T> source) {
        Objects.requireNonNull(source, "Source collection must be provided!");
        for (int i = 1; i < source.size(); i++) {
            var current = source.get(i);
            var j = i - 1;
            while (j >= 0 && current.compareTo(source.get(j)) <= 0) {
                source.set(j + 1, source.get(j));
                j--;
            }
            source.set(j + 1, current);
        }
        return source;
    }

    /**
     * Implementation of Bubble Sort algorithm.
     *
     * @param source collection to sort
     * @param <T>    parameter type
     * @return sorted collection
     */
    public static <T extends Comparable<? super T>> List<T> bubbleSort(final List<T> source) {
        Objects.requireNonNull(source, "Source collection must be provided!");
        for (int i = 0; i < source.size(); i++) {
            for (int j = 0; j < source.size() - i - 1; j++) {
                var current = source.get(j);
                var next = source.get(j + 1);
                if (current.compareTo(next) >= 0) {
                    source.set(j, next);
                    source.set(j + 1, current);
                }
            }
        }
        return source;
    }

    /**
     * Implementation of Merge Sort algorithm.
     *
     * @param source collection to sort
     * @param <T>    parameter type
     * @return sorted collection
     */
    public static <T extends Comparable<? super T>> List<T> recursiveMergeSort(final List<T> source) {
        Objects.requireNonNull(source, "Source collection must be provided!");
        return doSort(source);
    }

    /**
     * Copying the elements of a given collection and divides it recursively on left and right parts.
     *
     * @param source collection to copy and divide
     * @param <T>    parameter type
     * @return sorted collection
     */
    private static <T extends Comparable<? super T>> List<T> doSort(final List<T> source) {
        if (source.size() <= 1) {
            return source;
        }
        var start = 0;
        var size = source.size();
        var middle = size / 2;
        var leftCopy = copy(source, start, middle);
        var rightCopy = copy(source, middle, size);
        return merge(source, doSort(leftCopy), doSort(rightCopy));
    }

    /**
     * Makes a copy of the given range of elements and stores them into a new collection. Requires additional memory
     * allocation.
     *
     * @param source source collection
     * @param start  index
     * @param end    index
     * @param <T>    parameter type
     * @return partially copied collection
     */
    private static <T extends Comparable<? super T>> List<T> copy(final List<T> source, final int start, final int end) {
        var result = new ArrayList<T>();
        for (int i = start; i < end; i++) {
            result.add(source.get(i));
        }
        return result;
    }

    /**
     * Sorting out the given parts of left and right collection and store the elements in a source collection.
     *
     * @param source source collection
     * @param left   represents a range of elements from a source collection starting from 0 to source.size() / 2
     * @param right  represents a range of elements from a source collection starting from source.size() / 2 to source.size()
     * @param <T>    parameter type
     * @return sorted collection
     */
    private static <T extends Comparable<? super T>> List<T> merge(final List<T> source,
                                                                   final List<T> left,
                                                                   final List<T> right) {
        var sourceIndex = 0;
        var leftIndex = 0;
        var rightIndex = 0;
        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).compareTo(right.get(rightIndex)) <= 0) {
                source.set(sourceIndex++, left.get(leftIndex++));
            } else {
                source.set(sourceIndex++, right.get(rightIndex++));
            }
        }
        while (leftIndex < left.size()) {
            source.set(sourceIndex++, left.get(leftIndex++));
        }
        while (rightIndex < right.size()) {
            source.set(sourceIndex++, right.get(rightIndex++));
        }
        return source;
    }

    /**
     * Implementation of merge sort using Fork-Join framework.
     *
     * @param <T> type parameter
     */
    public static class MergeSortTask<T extends Comparable<? super T>> extends RecursiveTask<List<T>> {

        private final List<T> source;

        public MergeSortTask(final List<T> source) {
            Objects.requireNonNull(source, "Source collection must be provided!");
            this.source = source;
        }

        @Override
        protected List<T> compute() {
            if (this.source.size() <= 1) {
                return this.source;
            }

            var start = 0;
            var size = this.source.size();
            var middle = size / 2;
            var left = new MergeSortTask<>(new ArrayList<T>(this.source.subList(start, middle)));
            var right = new MergeSortTask<>(new ArrayList<T>(this.source.subList(middle, size)));

            left.fork();
            right.fork();

            return merge(this.source, left.join(), right.join());
        }

        /**
         * Sorting out the given parts of left and right collection and store the elements in a source collection.
         *
         * @param source source collection
         * @param left   represents a range of elements from a source collection starting from 0 to source.size() / 2
         * @param right  represents a range of elements from a source collection starting from source.size() / 2 to source.size()
         * @return sorted collection
         */
        private List<T> merge(final List<T> source,
                              final List<T> left,
                              final List<T> right) {
            int sourceIndex = 0;
            int leftIndex = 0;
            int rightIndex = 0;
            while (leftIndex < left.size() && rightIndex < right.size()) {
                final T leftElement = left.get(leftIndex);
                final T rightElement = right.get(rightIndex);
                if (leftElement.compareTo(rightElement) <= 0) {
                    source.set(sourceIndex++, leftElement);
                    leftIndex++;
                } else {
                    source.set(sourceIndex++, rightElement);
                    rightIndex++;
                }
            }
            while (leftIndex < left.size()) {
                source.set(sourceIndex++, left.get(leftIndex++));
            }
            while (rightIndex < right.size()) {
                source.set(sourceIndex++, right.get(rightIndex++));
            }
            return source;
        }
    }

    public static void main(String[] args) {
        final List<Integer> integers = SortUtils.generateRandomNums(10);
        System.out.println(integers);

        final List<Integer> sorted = ForkJoinPool.commonPool().invoke(new MergeSortTask<>(integers));
        System.out.println(sorted);
    }
}
