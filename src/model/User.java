package model;

// Implementasi Encapsulation dan Clean Code
public class User {
    private String id;
    private String name;
    private String phone;

    public User(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Nama: %s | HP: %s", id, name, phone);
    }
}