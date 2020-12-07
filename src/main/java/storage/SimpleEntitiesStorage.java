package storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleEntitiesStorage<K, T> implements BankEntitiesStorage<K, T> {

    private final Map<K, T> storage = new HashMap();
    private final KeyExtractor<? extends K, ? super T> keyExtractor;

    public SimpleEntitiesStorage(KeyExtractor<? extends K, ? super T> keyExtractor) {
        this.keyExtractor = keyExtractor;
    }

    @Override
    public void save(T entity) {
        storage.put(keyExtractor.extract(entity), entity);
    }

    @Override
    public void saveAll(Collection<? extends T> entities) {
        for (T entity : entities) {
            storage.put(keyExtractor.extract(entity), entity);
        }
    }

    @Override
    public T findByKey(Object key) {
        return storage.get(key);
    }

    @Override
    public Collection<T> findAll() {
        return storage.values();
    }

    @Override
    public void deleteByKey(Object key) {
        if (key != null && storage.containsKey(key)) {
            storage.remove(key);
        }
    }

    @Override
    public void deleteAll(Collection<? extends T> entities) {
        for (T entity : entities) {
            deleteByKey(keyExtractor.extract(entity));
        }
    }
}

