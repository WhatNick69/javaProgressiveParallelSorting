package progressive_parallel_sorting;

import java.util.concurrent.RecursiveAction;

/*
 * Created by WhatNick on 27.08.2016.
 */

class ProgressiveParallelSorting extends RecursiveAction {
    private int low;
    private int high;
    private int[] array;
    private int TRESHOLD;
    byte loadFactor = 1;

    ProgressiveParallelSorting(int[] arr, int low, int high, byte loadFactor) {
        array = arr;
        this.low   = low;
        this.high  = high;
        this.loadFactor = loadFactor;
        this.TRESHOLD = array.length / (this.loadFactor*Runtime.getRuntime().availableProcessors());
    }

    ProgressiveParallelSorting(int[] arr, int low, int high) {
        array = arr;
        this.low   = low;
        this.high  = high;
        this.TRESHOLD = array.length / (this.loadFactor*Runtime.getRuntime().availableProcessors());
    }

    @Override
    protected void compute() {
        if (high - low <= TRESHOLD) {
            try {
                quickSorting(array,0,array.length-1);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error in parallel sorting. Retry...");
                quickSorting(array,0,array.length-1);
            }
        } else {
            int mid = low + (high - low) / 2;
            ProgressiveParallelSorting leftSide  = new ProgressiveParallelSorting(array, low, mid);
            ProgressiveParallelSorting rightSide = new ProgressiveParallelSorting(array, mid, high);
            rightSide.fork();
            leftSide.invoke();
            rightSide.join();
        }
    }

    private void quickSorting(int[] mas, int start, int end) {
        if (start >= end)
            return;
        int i = start, j = end;
        int cur = i - (i - j) / 2;
        while (i < j) {
            while (i < cur && (mas[i] <= mas[cur])) {
                i++;
            }
            while (j > cur && (mas[cur] <= mas[j])) {
                j--;
            }
            if (i < j) {
                int temp = mas[i];
                mas[i] = mas[j];
                mas[j] = temp;
                if (i == cur)
                    cur = j;
                else if (j == cur)
                    cur = i;
            }
        }
        quickSorting(mas,start, cur);
        quickSorting(mas,cur+1, end);
    }
}
