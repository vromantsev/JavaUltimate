package com.bobocode.mergesort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.RecursiveTask;

public class MergeSortTask<T extends Comparable<? super T>> extends RecursiveTask<List<T>> {

    private static final int MIN_SIZE = 1;

    private final List<T> source;

    public MergeSortTask(List<T> source) {
        Objects.requireNonNull(source);
        this.source = source;
    }

    @Override
    protected List<T> compute() {
        if (this.source.size() <= MIN_SIZE) {
            return this.source;
        } else {
            var length = source.size();
            final int middle = length / 2;
            var left = new MergeSortTask<>(new ArrayList<>(source.subList(0, middle)));
            var right = new MergeSortTask<>(new ArrayList<>(source.subList(middle, length)));

            left.fork();
            right.fork();

            return merge(left.join(), right.join());
        }
    }

    private List<T> merge(final List<T> left, final List<T> right) {
        int leftPosition = 0;
        int rightPosition = 0;
        int sourcePosition = 0;

        while (leftPosition < left.size() && rightPosition < right.size()) {
            final T leftElement = left.get(leftPosition);
            final T rightElement = right.get(rightPosition);
            if (leftElement.compareTo(rightElement) <= 0) {
                source.set(sourcePosition++, leftElement);
                leftPosition++;
            } else {
                source.set(sourcePosition++, rightElement);
                rightPosition++;
            }
        }

        while (leftPosition < left.size()) {
            source.set(sourcePosition++, left.get(leftPosition++));
        }

        while (rightPosition < right.size()) {
            source.set(sourcePosition++, right.get(rightPosition++));
        }

        return source;
    }
}
