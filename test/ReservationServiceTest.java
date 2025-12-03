package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import service.ReservationService;
import repository.*;
import factory.FieldFactory;
import model.*;
import strategy.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

// Penerapan JUnit untuk Pengujian Unit
public class ReservationServiceTest {

    private ReservationService reservationService;
    private UserRepository userRepository;
    private FieldRepository fieldRepository;
    private ReservationRepository reservationRepository;
    
    private String userId1 = "U1";
    private String fieldId1 = "FUT1";
    private String fieldId2 = "BAD1";

    @BeforeEach
    void setUp() {
        // Setup Repositories dan Service sebelum setiap test
        userRepository = new UserRepository();
        fieldRepository = new FieldRepository();
        reservationRepository = new ReservationRepository();
        reservationService = new ReservationService(userRepository, fieldRepository, reservationRepository);

        // Data Dummy User
        userRepository.save(new User(userId1, "Joko Santoso", "08123456789"));
        
        // Data Dummy Lapangan menggunakan Factory
        Field futsalField = FieldFactory.createField("FUTSAL", fieldId1, "Futsal A", 100000.0, "10");
        Field badmintonField = FieldFactory.createField("BADMINTON", fieldId2, "Badminton B", 50000.0, "true");
        fieldRepository.save(futsalField);
        fieldRepository.save(badmintonField);
        
        // Clear reservations for clean test environment
        reservationRepository.findAll().clear();
    }

    // =======================================================================
    // Test Case 1: Pengujian Strategy Pattern (Tarif Weekend/Weekday)
    // =======================================================================
    @Test
    void testPriceCalculationStrategy() {
        LocalDate weekday = LocalDate.of(2025, 1, 10); // Jumat (Weekday)
        LocalDate weekend = LocalDate.of(2025, 1, 11); // Sabtu (Weekend)
        int duration = 2;
        
        // Futsal A (Harga/Jam = 100000 + 5000 = 105000)
        double basePricePerHour = fieldRepository.findById(fieldId1).get().getPricePerHour(); // 105000.0

        // Test Weekday Price (105000 * 2 jam = 210000)
        TariffStrategy weekdayStrategy = reservationService.getTariffStrategy(weekday.getDayOfWeek());
        double weekdayPrice = weekdayStrategy.calculatePrice(fieldRepository.findById(fieldId1).get(), weekday.getDayOfWeek(), 10, duration);
        assertEquals(210000.0, weekdayPrice, 0.01, "Harga Weekday harus sesuai (Base Price * Jam)");

        // Test Weekend Price (210000 * 1.15 = 241500)
        TariffStrategy weekendStrategy = reservationService.getTariffStrategy(weekend.getDayOfWeek());
        double weekendPrice = weekendStrategy.calculatePrice(fieldRepository.findById(fieldId1).get(), weekend.getDayOfWeek(), 10, duration);
        assertEquals(241500.0, weekendPrice, 0.01, "Harga Weekend harus ada surcharge 15%");
    }

    // =======================================================================
    // Test Case 2: Pengujian Validasi Bentrok Jadwal (Core Feature)
    // =======================================================================
    @Test
    void testTimeSlotConflict() {
        LocalDate testDate = LocalDate.now();

        // 1. Booking pertama: Futsal A, 14:00 - 16:00 (Durasi 2 jam)
        Optional<Reservation> res1 = reservationService.bookField(userId1, fieldId1, testDate, 14, 2, "CASH");
        assertTrue(res1.isPresent(), "Reservasi 1 harus berhasil.");

        // 2. Cek bentrok TEPAT DI TENGAH: 14:30 - 15:30 (start jam 14) -> Konflik
        assertTrue(reservationService.isTimeSlotConflicted(fieldId1, testDate, 14, 1), "Harus bentrok (overlap start)");

        // 3. Cek bentrok OVERLAP START: 13:00 - 15:00 (start jam 13) -> Konflik
        assertTrue(reservationService.isTimeSlotConflicted(fieldId1, testDate, 13, 2), "Harus bentrok (overlap end)");
        
        // 4. Cek bentrok TEPAT setelah: 16:00 - 17:00 -> Tidak Konflik
        assertFalse(reservationService.isTimeSlotConflicted(fieldId1, testDate, 16, 1), "Tidak boleh bentrok jika tepat setelah jam selesai");

        // 5. Cek bentrok LAPANGAN LAIN: Badminton B, 14:00 - 16:00 -> Tidak Konflik
        assertFalse(reservationService.isTimeSlotConflicted(fieldId2, testDate, 14, 2), "Tidak boleh bentrok jika lapangan berbeda");
    }
    
    // =======================================================================
    // Test Case 3: Pengujian Pembatalan dan Refund (CRUD/Fitur Wajib)
    // =======================================================================
    @Test
    void testCancellationAndRefund() {
        LocalDate testDate = LocalDate.now();
        // Booking Futsal A, 10:00 - 12:00 (2 jam)
        Optional<Reservation> resOpt = reservationService.bookField(userId1, fieldId1, testDate, 10, 2, "CASH");
        assertTrue(resOpt.isPresent(), "Reservasi harus berhasil untuk dibatalkan.");
        
        String resId = resOpt.get().getId();
        double initialFee = resOpt.get().getTotalFee();
        
        // 1. Batalkan reservasi
        boolean isCanceled = reservationService.cancelReservation(resId);
        assertTrue(isCanceled, "Pembatalan harus berhasil.");
        
        // 2. Cek Status Reservasi dan Refund Amount
        Reservation canceledRes = reservationRepository.findById(resId).get();
        assertEquals("CANCELED", canceledRes.getStatus(), "Status harus berubah menjadi CANCELED.");
        assertEquals(initialFee * 0.50, canceledRes.getPayment().getAmount(), 0.01, "Jumlah payment harus berkurang 50% (refund amount).");
        assertEquals("REFUNDED", canceledRes.getPayment().getStatus(), "Status payment harus REFUNDED.");
        
        // 3. Cek pembatalan kedua (gagal)
        boolean isCanceledAgain = reservationService.cancelReservation(resId);
        assertFalse(isCanceledAgain, "Pembatalan kedua harus gagal.");
    }
}