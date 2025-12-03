package factory;

import model.*;

// Design Pattern: Factory Method (Creational)
public class FieldFactory {

    // Metode statis yang bertindak sebagai Factory Method
    public static Field createField(String type, String id, String name, double basePricePerHour, String... specificParams) {
        switch (type.toUpperCase()) {
            case "FUTSAL":
                // Parameter spesifik: capacity (index 0)
                int capacity = Integer.parseInt(specificParams[0]);
                return new FutsalCourt(id, name, basePricePerHour, capacity);
            case "BADMINTON":
                // Parameter spesifik: isIndoor (index 0)
                boolean isIndoor = Boolean.parseBoolean(specificParams[0]);
                return new BadmintonCourt(id, name, basePricePerHour, isIndoor);
            // Tambahkan jenis lapangan lain di masa depan dengan mudah
            default:
                throw new IllegalArgumentException("Jenis lapangan tidak valid: " + type);
        }
    }
}