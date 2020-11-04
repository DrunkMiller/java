package ru.dm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

public class FixedThreadPool implements ThreadPool {
    private final int poolSize;
    private final Deque<Runnable> tasks;
    private final Collection<Thread> workers;

    public FixedThreadPool(int poolSize) {
        this.poolSize = poolSize;
        tasks = new ArrayDeque<>();
        workers = new ArrayList<>();
    }

    @Override
    public void start() {
        if (workers.size() == poolSize) return;
        int countToAdd = poolSize - workers.size();
        for (int i = 0; i < countToAdd; i++) {
            Thread thread = new Thread(new Worker());
            workers.add(thread);
            thread.start();
        }
    }

    public void stop() {
        for (Thread worker : workers) {
            worker.interrupt();
        }
        synchronized (tasks) {
            tasks.notifyAll();
        }
        workers.clear();
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
                if (currentTask != null) currentTask.run();
            }
        }
    }

}
