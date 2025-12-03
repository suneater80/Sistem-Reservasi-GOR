package model;

// Implementasi Abstraction dan Inheritance
public abstract class Field {
    private String id;
    private String name;
    private double basePricePerHour; // Harga dasar per jam

    public Field(String id, String name, double basePricePerHour) {
        this.id = id;
        this.name = name;
        this.basePricePerHour = basePricePerHour;
    }

    // Metode abstrak untuk Polimorfisme.
    // Setiap jenis lapangan mungkin memiliki perhitungan harga spesifik (misal: termasuk biaya bola/shuttlecock)
    public abstract double getPricePerHour();
    
    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBasePricePerHour() {
        return basePricePerHour;
    }
    
    // Setters
    public void setBasePricePerHour(double basePricePerHour) {
        this.basePricePerHour = basePricePerHour;
    }
    
    public abstract String getType(); // Untuk identifikasi jenis lapangan

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | Nama: %s | Harga Dasar/Jam: %,.0f", getType(), id, name, basePricePerHour);
    }
}