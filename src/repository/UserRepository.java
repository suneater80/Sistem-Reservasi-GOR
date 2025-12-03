package repository;

import model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Implementasi JCF (Java Collections Framework) dan Clean Code
public class UserRepository {
    // Menggunakan HashMap untuk penyimpanan data in-memory (JCF)
    private final Map<String, User> userMap; 

    public UserRepository() {
        this.userMap = new HashMap<>();
    }

    public void save(User user) {
        userMap.put(user.getId(), user);
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(userMap.get(id));
    }

    public List<User> findAll() {
        // Menggunakan Stream API (Clean Code)
        return userMap.values().stream().collect(Collectors.toList());
    }

    public void deleteById(String id) {
        userMap.remove(id);
    }
    
    public int count() {
        return userMap.size();
    }
}