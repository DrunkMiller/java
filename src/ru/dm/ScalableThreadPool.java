package ru.dm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

public class ScalableThreadPool implements ThreadPool {
    private final Deque<Runnable> tasks;
    private final Collection<Thread> workers;
    private final int minPoolSize;
    private final int maxPoolSize;
    private Integer countWorking;

    public ScalableThreadPool(int minPoolSize, int maxPoolSize) {
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
        tasks = new ArrayDeque<>();
        workers = new ArrayList<>();
        countWorking = 0;
    }

    @Override
    public void start() {
        if (workers.size() == minPoolSize) return;
        int countToAdd = minPoolSize - workers.size();
        for (int i = 0; i < countToAdd; i++) {
            addWorker();
        }
    }

    public void stop() {
        for (Thread worker : workers) {
            worker.interrupt();
        }
        synchronized (tasks) {
            tasks.notifyAll();
        }
        synchronized (workers) {
            workers.clear();
        }
    }

    int getCountUnprocessedTasks() {
        synchronized (tasks) {
            return tasks.size();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (tasks) {
            tasks.add(runnable);
            tasks.notify();
        }
        synchronized (workers) {
            if (workers.size() < maxPoolSize && countWorking == workers.size()) {
                addWorker();
                System.out.println("# Pool size changed: total:" + workers.size()+ " working:" + countWorking);
            }
        }
    }

    private void addWorker() {
        synchronized (workers) {
            Thread thread = new Thread(new Worker());
            workers.add(thread);
            thread.start();
        }
    }

    private boolean workFurther(Thread thread) {
        synchronized (workers) {
            if (getCountUnprocessedTasks() == 0 && workers.size() > minPoolSize) {
                workers.remove(thread);
                System.out.println("# Pool size changed: total:" + workers.size() + " working:" + countWorking);
                return false;
            }
        }
        return true;
    }

    private void work() {
        synchronized (workers) {
            countWorking++;
        }
    }

    private void freed() {
        synchronized (workers) {
            countWorking--;
        }
    }

    class Worker implements Runnable {
        @Override
        public void run() {
            Runnable currentTask = null;
            while (true) {
                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        if (Thread.currentThread().isInterrupted()) return;
                        try {
                            tasks.wait();
                        } catch (InterruptedException exception) {
                            exception.printStackTrace();
                        }
                    }
                    currentTask = tasks.isEmpty() ? null : tasks.removeFirst();
                }
                work();
                if (currentTask != null) currentTask.run();
                freed();
                if (!workFurther(Thread.currentThread())) return;
            }
        }
    }
}
