package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import service.ReservationService;
import repository.*;
import factory.FieldFactory;
import model.*;
import decorator.*;
import strategy.TariffStrategy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

// Penerapan JUnit untuk Pengujian Unit
@DisplayName("Reservation Service Test Suite")
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
        
        // No need to clear - repositories are newly created each test
    }

    // =======================================================================
    // Test Case 1: Pengujian Strategy Pattern (Tarif Weekend/Weekday)
    // =======================================================================
    @Test
    @DisplayName("Test Strategy Pattern - Weekday vs Weekend Pricing")
    void testPriceCalculationStrategy() {
        LocalDate weekday = LocalDate.of(2025, 1, 10); // Jumat (Weekday)
        LocalDate weekend = LocalDate.of(2025, 1, 11); // Sabtu (Weekend)
        int duration = 2;
        
        // Futsal A (Harga/Jam = 100000 + 5000 = 105000)
        // Removed unused variable basePricePerHour

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
    @DisplayName("Test Time Slot Conflict Detection")
    void testTimeSlotConflict() {
        LocalDate testDate = LocalDate.now();

        // 1. Booking pertama: Futsal A, 14:00 - 16:00 (Durasi 2 jam)
        Optional<Reservation> res1 = reservationService.bookField(userId1, fieldId1, testDate, 14, 2, "CASH", false, false);
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
    @DisplayName("Test Cancellation and Refund Process")
    void testCancellationAndRefund() {
        LocalDate testDate = LocalDate.now();
        // Booking Futsal A, 10:00 - 12:00 (2 jam)
        Optional<Reservation> resOpt = reservationService.bookField(userId1, fieldId1, testDate, 10, 2, "CASH", false, false);
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
    
    // =======================================================================
    // Test Case 4: Pengujian Decorator Pattern (Payment dengan Tax/Insurance)
    // =======================================================================
    @Test
    @DisplayName("Test Decorator Pattern - Tax and Insurance")
    void testPaymentDecorator() {
        // Create base payment
        Payment basePayment = new Payment("PAY001", 100000.0, "CASH");
        assertEquals(100000.0, basePayment.getAmount(), 0.01);
        
        // Apply Tax Decorator (10%)
        TaxPaymentDecorator withTax = new TaxPaymentDecorator(basePayment);
        assertEquals(10000.0, withTax.getTaxAmount(), 0.01, "Tax harus 10% dari base");
        assertEquals(110000.0, withTax.getTotalWithTax(), 0.01, "Total dengan tax harus 110000");
        
        // Apply Insurance Decorator (flat 15000)
        InsurancePaymentDecorator withInsurance = new InsurancePaymentDecorator(basePayment);
        assertEquals(15000.0, withInsurance.getInsuranceFee(), 0.01, "Insurance fee harus 15000");
        assertEquals(115000.0, withInsurance.getTotalWithInsurance(), 0.01, "Total dengan insurance harus 115000");
        
        // Can stack decorators: Tax on top of Insurance
        TaxPaymentDecorator withBoth = new TaxPaymentDecorator(withInsurance);
        // Insurance: 100000 + 15000 = 115000
        // Tax on 115000: 115000 * 0.10 = 11500
        // Total: 115000 + 11500 = 126500
        assertEquals(126500.0, withBoth.getTotalWithTax(), 0.01, "Stacked decorator: Base + Insurance + Tax");
    }
    
    // =======================================================================
    // Test Case 5: Pengujian Generic Repository
    // =======================================================================
    @Test
    @DisplayName("Test Generic Repository - CRUD Operations")
    void testGenericRepository() {
        // Test save and count
        int initialCount = userRepository.count();
        User testUser = new User("TEST001", "Test User", "081234567890");
        userRepository.save(testUser);
        assertEquals(initialCount + 1, userRepository.count(), "Count harus bertambah 1");
        
        // Test findById
        Optional<User> foundUser = userRepository.findById("TEST001");
        assertTrue(foundUser.isPresent(), "User harus ditemukan");
        assertEquals("Test User", foundUser.get().getName());
        
        // Test existsById
        assertTrue(userRepository.existsById("TEST001"), "User TEST001 harus exist");
        assertFalse(userRepository.existsById("NOTEXIST"), "User NOTEXIST tidak boleh exist");
        
        // Test delete
        userRepository.deleteById("TEST001");
        assertFalse(userRepository.existsById("TEST001"), "User TEST001 harus sudah terhapus");
    }
}