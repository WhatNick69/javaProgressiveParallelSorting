package progressive_parallel_sorting;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/*
 * Created by WhatNick on 17.08.2016.
 * 18:40
 *
 * Average loading CPU on FX-8300 is: 100%
 * Speed for 50kk elements: 7564 - 8092ms.
 */

public class MainMethod {
    private static void forkJoinThreads(int[] array) {
        ProgressiveParallelSorting pPS = new ProgressiveParallelSorting(array,0,array.length);
        ForkJoinPool fjPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        fjPool.invoke(pPS);
    }

    public static void main(String[] args) throws InterruptedException
    {
        Date genAr1 = new Date();
        Random rnd = new Random();
        int[] array = new int[50000001];
        for (int i = 0;i<array.length;i++) {
            array[i] = rnd.nextInt(Integer.MAX_VALUE)-(Integer.MAX_VALUE/2);
        }
        Date genAr2 = new Date();
        System.out.println("Massive has been generated from: " + (genAr2.getTime()-genAr1.getTime()) + "ms.");

        Date fJ1 = new Date();
        forkJoinThreads(array);
        Date fJ2 = new Date();
        checker(array);
        System.out.println("Fork-Join time is: " + (fJ2.getTime()-fJ1.getTime()) + "ms.");
    }

    private static void checker(int[] array) {
        for (int i = 0;i<array.length-1;i++) {
            if (array[i] > array[i+1]) {
                System.out.println("Error in sorted array! Element: " + array[i] + " " + array[i+1] + ", count: " + i);
                break;
            }
        }
    }
}
