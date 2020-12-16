package ru.dm;

public interface ThreadPool {
    void start();
    void execute(Runnable runnable);
}
