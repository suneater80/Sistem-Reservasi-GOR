package model;

// Implementasi Inheritance dan Polymorphism
public class FutsalCourt extends Field {
    private int capacity; // Kapasitas maksimal pemain
    private static final double FUTSAL_SURCHARGE = 5000.0; // Biaya tambahan tetap untuk kebersihan/peralatan

    public FutsalCourt(String id, String name, double basePricePerHour, int capacity) {
        super(id, name, basePricePerHour);
        this.capacity = capacity;
    }

    @Override
    public double getPricePerHour() {
        // Implementasi Polimorfisme: Harga Futsal = Harga Dasar + Surcharge
        return getBasePricePerHour() + FUTSAL_SURCHARGE;
    }
    
    @Override
    public String getType() {
        return "FUTSAL";
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return String.format("%s | Kapasitas: %d org | Harga Final/Jam: %,.0f", 
            super.toString(), capacity, getPricePerHour());
    }
}