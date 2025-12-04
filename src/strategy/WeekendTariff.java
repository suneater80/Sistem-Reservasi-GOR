package strategy;

import java.time.DayOfWeek;
import model.Field;

public class WeekendTariff implements TariffStrategy {
    private static final double WEEKEND_SURCHARGE_FACTOR = 1.15;

    @Override
    public double calculatePrice(Field field, DayOfWeek day, int startHour, int durationHours) {
        return (field.getPricePerHour() * durationHours) * WEEKEND_SURCHARGE_FACTOR;
    }
    
    @Override
    public String getName() {
        return "Tarif Akhir Pekan (Weekend +15%)";
    }
}