package factory;

import model.*;

public class FieldFactory {

    public static Field createField(String type, String id, String name, double basePricePerHour, String... specificParams) {
        switch (type.toUpperCase()) {
            case "FUTSAL":
                int capacity = Integer.parseInt(specificParams[0]);
                return new FutsalCourt(id, name, basePricePerHour, capacity);
            case "BADMINTON":
                boolean isIndoor = Boolean.parseBoolean(specificParams[0]);
                return new BadmintonCourt(id, name, basePricePerHour, isIndoor);
            default:
                throw new IllegalArgumentException("Jenis lapangan tidak valid: " + type);
        }
    }
}