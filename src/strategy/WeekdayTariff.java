package strategy;

import java.time.DayOfWeek;
import model.Field;

// Concrete Strategy 1: Tarif Hari Biasa (Senin-Jumat)
public class WeekdayTariff implements TariffStrategy {

    @Override
    public double calculatePrice(Field field, DayOfWeek day, int startHour, int durationHours) {
        // Weekday (Hari kerja) tidak ada surcharge tambahan dari tarif per jam Field
        return field.getPricePerHour() * durationHours;
    }

    @Override
    public String getName() {
        return "Tarif Hari Kerja (Weekday)";
    }
}