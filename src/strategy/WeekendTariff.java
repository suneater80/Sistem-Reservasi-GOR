package strategy;

import java.time.DayOfWeek;
import model.Field;

// Concrete Strategy 2: Tarif Akhir Pekan (Sabtu-Minggu)
public class WeekendTariff implements TariffStrategy {
    private static final double WEEKEND_SURCHARGE_FACTOR = 1.15; // Kenaikan 15%

    @Override
    public double calculatePrice(Field field, DayOfWeek day, int startHour, int durationHours) {
        // Weekend (Akhir pekan) ada kenaikan 15%
        return (field.getPricePerHour() * durationHours) * WEEKEND_SURCHARGE_FACTOR;
    }
    
    @Override
    public String getName() {
        return "Tarif Akhir Pekan (Weekend +15%)";
    }
}