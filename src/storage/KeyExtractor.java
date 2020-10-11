package storage;

public interface KeyExtractor<K, T> {
    K extract(T entity);
}
