package repository;

import factory.FieldFactory;
import model.BadmintonCourt;
import model.Field;
import model.FutsalCourt;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Implementasi JCF (Java Collections Framework) dan Clean Code
public class FieldRepository {
    // Menggunakan HashMap untuk penyimpanan data in-memory (JCF)
    private final Map<String, Field> fieldMap;

    public FieldRepository() {
        this.fieldMap = new HashMap<>();
        // Inisialisasi data dummy Lapangan dipindahkan ke GorAppCli untuk kejelasan
        // initializeDummyData();
    }
    
    // Metode ini dihapus karena inisialisasi dipindahkan ke Main Class
    /*
    private void initializeDummyData() {
        // Contoh inisialisasi data menggunakan FieldFactory
        Field f1 = FieldFactory.createField("FUTSAL", "FUT01", "Futsal A", 120000.0, "12");
        Field b1 = FieldFactory.createField("BADMINTON", "BAD01", "Badminton A", 65000.0, "true");
        fieldMap.put(f1.getId(), f1);
        fieldMap.put(b1.getId(), b1);
    }
    */

    public void save(Field field) {
        fieldMap.put(field.getId(), field);
    }

    public Optional<Field> findById(String id) {
        return Optional.ofNullable(fieldMap.get(id));
    }

    public List<Field> findAll() {
        // Menggunakan Stream API (Clean Code)
        return fieldMap.values().stream().collect(Collectors.toList());
    }

    public void deleteById(String id) {
        fieldMap.remove(id);
    }
    
    public int count() {
        return fieldMap.size();
    }
}