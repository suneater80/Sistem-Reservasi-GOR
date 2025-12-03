package strategy;

import java.time.DayOfWeek;
import model.Field;

// Design Pattern: Strategy Interface (Behavioral)
public interface TariffStrategy {
    // Metode utama untuk menghitung harga total berdasarkan Field, tanggal, dan durasi jam
    double calculatePrice(Field field, DayOfWeek day, int startHour, int durationHours);
    
    // Metode helper untuk mendapatkan nama strategi
    String getName();
}