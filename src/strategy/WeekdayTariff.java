package strategy;

import java.time.DayOfWeek;
import model.Field;

public class WeekdayTariff implements TariffStrategy {

    @Override
    public double calculatePrice(Field field, DayOfWeek day, int startHour, int durationHours) {
        return field.getPricePerHour() * durationHours;
    }

    @Override
    public String getName() {
        return "Tarif Hari Kerja (Weekday)";
    }
}