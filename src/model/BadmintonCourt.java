package model;

public class BadmintonCourt extends Field {
    private boolean isIndoor;

    public BadmintonCourt(String id, String name, double basePricePerHour, boolean isIndoor) {
        super(id, name, basePricePerHour);
        this.isIndoor = isIndoor;
    }

    @Override
    public double getPricePerHour() {
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