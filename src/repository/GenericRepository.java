package repository;

import java.util.*;

// Generic Repository - Implementasi Generic Programming
public abstract class GenericRepository<T, K> {
    protected final Map<K, T> dataMap;

    protected GenericRepository() {
        this.dataMap = new HashMap<>();
    }

    // CRUD Operations dengan Generic Type
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

    // Abstract method yang harus diimplementasikan oleh subclass
    // untuk mendapatkan ID dari entity
    protected abstract K getId(T entity);
}
