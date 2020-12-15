package storage;

import java.util.Collection;

public interface BankEntitiesStorage<K, T> {
    void save(T entity);

    void saveAll(Collection<? extends T> entities);

    T findByKey(K key);

    Collection<T> findAll();

    void deleteByKey(K key);

    void deleteAll(Collection<? extends T> entities);

}
