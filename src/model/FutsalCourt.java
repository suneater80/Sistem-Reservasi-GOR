package model;

public class FutsalCourt extends Field {
    private int capacity;
    private static final double FUTSAL_SURCHARGE = 5000.0;

    public FutsalCourt(String id, String name, double basePricePerHour, int capacity) {
        super(id, name, basePricePerHour);
        this.capacity = capacity;
    }

    @Override
    public double getPricePerHour() {
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