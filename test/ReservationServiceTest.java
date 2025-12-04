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

import java.time.LocalDate;
import java.util.Optional;

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
        userRepository = new UserRepository();
        fieldRepository = new FieldRepository();
        reservationRepository = new ReservationRepository();
        reservationService = new ReservationService(userRepository, fieldRepository, reservationRepository);

        userRepository.save(new User(userId1, "Joko Santoso", "08123456789"));
        
        Field futsalField = FieldFactory.createField("FUTSAL", fieldId1, "Futsal A", 100000.0, "10");
        Field badmintonField = FieldFactory.createField("BADMINTON", fieldId2, "Badminton B", 50000.0, "true");
        fieldRepository.save(futsalField);
        fieldRepository.save(badmintonField);
    }

    @Test
    @DisplayName("Test Strategy Pattern - Weekday vs Weekend Pricing")
    void testPriceCalculationStrategy() {
        LocalDate weekday = LocalDate.of(2025, 1, 10);
        LocalDate weekend = LocalDate.of(2025, 1, 11);
        int duration = 2;

        TariffStrategy weekdayStrategy = reservationService.getTariffStrategy(weekday.getDayOfWeek());
        double weekdayPrice = weekdayStrategy.calculatePrice(fieldRepository.findById(fieldId1).get(), weekday.getDayOfWeek(), 10, duration);
        assertEquals(210000.0, weekdayPrice, 0.01, "Harga Weekday harus sesuai (Base Price * Jam)");

        TariffStrategy weekendStrategy = reservationService.getTariffStrategy(weekend.getDayOfWeek());
        double weekendPrice = weekendStrategy.calculatePrice(fieldRepository.findById(fieldId1).get(), weekend.getDayOfWeek(), 10, duration);
        assertEquals(241500.0, weekendPrice, 0.01, "Harga Weekend harus ada surcharge 15%");
    }

    @Test
    @DisplayName("Test Time Slot Conflict Detection")
    void testTimeSlotConflict() {
        LocalDate testDate = LocalDate.now();

        Optional<Reservation> res1 = reservationService.bookField(userId1, fieldId1, testDate, 14, 2, "CASH", false, false);
        assertTrue(res1.isPresent(), "Reservasi 1 harus berhasil.");

        assertTrue(reservationService.isTimeSlotConflicted(fieldId1, testDate, 14, 1), "Harus bentrok (overlap start)");
        assertTrue(reservationService.isTimeSlotConflicted(fieldId1, testDate, 13, 2), "Harus bentrok (overlap end)");
        assertFalse(reservationService.isTimeSlotConflicted(fieldId1, testDate, 16, 1), "Tidak boleh bentrok jika tepat setelah jam selesai");
        assertFalse(reservationService.isTimeSlotConflicted(fieldId2, testDate, 14, 2), "Tidak boleh bentrok jika lapangan berbeda");
    }
    
    @Test
    @DisplayName("Test Cancellation and Refund Process")
    void testCancellationAndRefund() {
        LocalDate testDate = LocalDate.now();
        Optional<Reservation> resOpt = reservationService.bookField(userId1, fieldId1, testDate, 10, 2, "CASH", false, false);
        assertTrue(resOpt.isPresent(), "Reservasi harus berhasil untuk dibatalkan.");
        
        String resId = resOpt.get().getId();
        double initialFee = resOpt.get().getTotalFee();
        
        boolean isCanceled = reservationService.cancelReservation(resId);
        assertTrue(isCanceled, "Pembatalan harus berhasil.");
        
        Reservation canceledRes = reservationRepository.findById(resId).get();
        assertEquals("CANCELED", canceledRes.getStatus(), "Status harus berubah menjadi CANCELED.");
        assertEquals(initialFee * 0.50, canceledRes.getPayment().getAmount(), 0.01, "Jumlah payment harus berkurang 50% (refund amount).");
        assertEquals("REFUNDED", canceledRes.getPayment().getStatus(), "Status payment harus REFUNDED.");
        
        boolean isCanceledAgain = reservationService.cancelReservation(resId);
        assertFalse(isCanceledAgain, "Pembatalan kedua harus gagal.");
    }
    
    @Test
    @DisplayName("Test Decorator Pattern - Tax and Insurance")
    void testPaymentDecorator() {
        Payment basePayment = new Payment("PAY001", 100000.0, "CASH");
        assertEquals(100000.0, basePayment.getAmount(), 0.01);
        
        TaxPaymentDecorator withTax = new TaxPaymentDecorator(basePayment);
        assertEquals(10000.0, withTax.getTaxAmount(), 0.01, "Tax harus 10% dari base");
        assertEquals(110000.0, withTax.getTotalWithTax(), 0.01, "Total dengan tax harus 110000");
        
        InsurancePaymentDecorator withInsurance = new InsurancePaymentDecorator(basePayment);
        assertEquals(15000.0, withInsurance.getInsuranceFee(), 0.01, "Insurance fee harus 15000");
        assertEquals(115000.0, withInsurance.getTotalWithInsurance(), 0.01, "Total dengan insurance harus 115000");
        
        TaxPaymentDecorator withBoth = new TaxPaymentDecorator(withInsurance);
        assertEquals(126500.0, withBoth.getTotalWithTax(), 0.01, "Stacked decorator: Base + Insurance + Tax");
    }
    
    @Test
    @DisplayName("Test Generic Repository - CRUD Operations")
    void testGenericRepository() {
        int initialCount = userRepository.count();
        User testUser = new User("TEST001", "Test User", "081234567890");
        userRepository.save(testUser);
        assertEquals(initialCount + 1, userRepository.count(), "Count harus bertambah 1");
        
        Optional<User> foundUser = userRepository.findById("TEST001");
        assertTrue(foundUser.isPresent(), "User harus ditemukan");
        assertEquals("Test User", foundUser.get().getName());
        
        assertTrue(userRepository.existsById("TEST001"), "User TEST001 harus exist");
        assertFalse(userRepository.existsById("NOTEXIST"), "User NOTEXIST tidak boleh exist");
        
        userRepository.deleteById("TEST001");
        assertFalse(userRepository.existsById("TEST001"), "User TEST001 harus sudah terhapus");
    }
}