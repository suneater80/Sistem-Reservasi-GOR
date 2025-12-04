package strategy;

import java.time.DayOfWeek;
import model.Field;

public interface TariffStrategy {
    double calculatePrice(Field field, DayOfWeek day, int startHour, int durationHours);
    String getName();
}