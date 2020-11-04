package ru.dm;

import java.util.Random;

public class Application {
    public static void main(String[] args) {
        System.out.println("===================================");
        System.out.println("==========FixedThreadPool==========");
        System.out.println("===================================");
        fixedThreadPoolExample();
        System.out.println("====================================");
        System.out.println("=========ScalableThreadPool=========");
        System.out.println("====================================");
        scalableThreadPoolExample();
    }

    private static void fixedThreadPoolExample() {
        FixedThreadPool threadPool = new FixedThreadPool(3);
        threadPool.start();
        for (Integer i = 0; i < 10; i++) {
            int taskNumber = i;
            threadPool.execute(() -> {
                try {
                    System.out.println("+ Task " + taskNumber + " start in thread " + Thread.currentThread().getName());
                    Thread.sleep(1_000);
                    System.out.println("% Task " + taskNumber + " processed in thread " + Thread.currentThread().getName());
                    Thread.sleep(1_000);
                    System.out.println("- Task " + taskNumber + " end in thread " + Thread.currentThread().getName());
                } catch (InterruptedException ignored) { }
            });
        }
        while (threadPool.getCountUnprocessedTasks() != 0) { }
        sleep(5_000);
        threadPool.stop();
    }

    private static void scalableThreadPoolExample() {
        ScalableThreadPool threadPool = new ScalableThreadPool(3, 6);
        threadPool.start();
        Random random = new Random();
        for (Integer i = 0; i < 15; i++) {
            int taskNumber = i;
            sleep(random.nextInt(1_000));
            threadPool.execute(() -> {
                try {
                    System.out.println("+ Task " + taskNumber + " start in thread " + Thread.currentThread().getName());
                    Thread.sleep(1_000);
                    System.out.println("% Task " + taskNumber + " processed in thread " + Thread.currentThread().getName());
                    Thread.sleep(1_000);
                    System.out.println("- Task " + taskNumber + " end in thread " + Thread.currentThread().getName());
                } catch (InterruptedException ignored) { }
            });
        }

        while (threadPool.getCountUnprocessedTasks() != 0) { }
        sleep(5_000);
        threadPool.stop();
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
