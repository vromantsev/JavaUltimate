package com.bobocode.concurrency.deadlock;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

public class DeadlockDemo {

    public final static Object resourceA = new Object();
    public final static Object resourceB = new Object();

    @SneakyThrows
    public static void doA() {
        synchronized (resourceA) {
            System.out.println("ResourceA is locked by " + Thread.currentThread().getName() + ", thread state: " + Thread.currentThread().getState());
            new WorkerA().doSomeWork();
            TimeUnit.MILLISECONDS.sleep(100);
            synchronized (resourceB) {
                System.out.println("ResourceB is locked by " + Thread.currentThread().getName() + ", thread state: " + Thread.currentThread().getState());
                new WorkerB().doSomeWork();
            }
        }
    }

    @SneakyThrows
    public static void doB() {
        synchronized (resourceB) {
            System.out.println("ResourceB is locked by " + Thread.currentThread().getName() + ", thread state: " + Thread.currentThread().getState());
            new WorkerB().doSomeWork();
            TimeUnit.MILLISECONDS.sleep(100);
            synchronized (resourceA) {
                System.out.println("ResourceA is locked by " + Thread.currentThread().getName() + ", thread state: " + Thread.currentThread().getState());
                new WorkerA().doSomeWork();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final Thread t1 = new Thread(DeadlockDemo::doA, "WorkerA-thread");
        final Thread t2 = new Thread(DeadlockDemo::doB, "WorkerB-thread");
        t1.start();
        t2.start();
        while (t1.isAlive() || t2.isAlive()) {
            System.out.println(t1.getName() + " state: " + t1.getState());
            System.out.println(t2.getName() + " state: " + t2.getState());
            TimeUnit.MILLISECONDS.sleep(500);
        }
    }
}
