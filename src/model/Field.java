package model;

public abstract class Field {
    private String id;
    private String name;
    private double basePricePerHour;

    public Field(String id, String name, double basePricePerHour) {
        this.id = id;
        this.name = name;
        this.basePricePerHour = basePricePerHour;
    }

    public abstract double getPricePerHour();
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBasePricePerHour() {
        return basePricePerHour;
    }
    
    public void setBasePricePerHour(double basePricePerHour) {
        this.basePricePerHour = basePricePerHour;
    }
    
    public abstract String getType();

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | Nama: %s | Harga Dasar/Jam: %,.0f", getType(), id, name, basePricePerHour);
    }
}