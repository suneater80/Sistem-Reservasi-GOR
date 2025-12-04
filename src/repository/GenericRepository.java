package repository;

import java.util.*;

public abstract class GenericRepository<T, K> {
    protected final Map<K, T> dataMap;

    protected GenericRepository() {
        this.dataMap = new HashMap<>();
    }

    public void save(T entity) {
        dataMap.put(getId(entity), entity);
    }

    public Optional<T> findById(K id) {
        return Optional.ofNullable(dataMap.get(id));
    }

    public List<T> findAll() {
        return new ArrayList<>(dataMap.values());
    }

    public void deleteById(K id) {
        dataMap.remove(id);
    }

    public int count() {
        return dataMap.size();
    }

    public boolean existsById(K id) {
        return dataMap.containsKey(id);
    }

    protected abstract K getId(T entity);
}
