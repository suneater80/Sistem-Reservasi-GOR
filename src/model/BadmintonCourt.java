package model;

// Implementasi Inheritance dan Polymorphism
public class BadmintonCourt extends Field {
    private boolean isIndoor; // Lapangan Indoor atau Outdoor

    public BadmintonCourt(String id, String name, double basePricePerHour, boolean isIndoor) {
        super(id, name, basePricePerHour);
        this.isIndoor = isIndoor;
    }

    @Override
    public double getPricePerHour() {
        // Implementasi Polimorfisme: Harga Badminton = Harga Dasar (asumsi sudah termasuk shuttlecock)
        // Tidak ada surcharge khusus seperti futsal
        return getBasePricePerHour();
    }
    
    @Override
    public String getType() {
        return "BADMINTON";
    }

    public boolean isIndoor() {
        return isIndoor;
    }

    public void setIndoor(boolean indoor) {
        isIndoor = indoor;
    }

    @Override
    public String toString() {
        String indoorStatus = isIndoor ? "Indoor" : "Outdoor";
        return String.format("%s | Status: %s | Harga Final/Jam: %,.0f", 
            super.toString(), indoorStatus, getPricePerHour());
    }
}