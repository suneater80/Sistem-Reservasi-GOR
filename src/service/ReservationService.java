package service;

import model.*;
import repository.*;
import strategy.*;
import util.IdGenerator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Design Pattern: Facade (Structural) - Menyederhanakan proses reservasi
public class ReservationService {
    private final UserRepository userRepository;
    private final FieldRepository fieldRepository;
    private final ReservationRepository reservationRepository;

    // Inisialisasi Repositories (Subsistem Facade)
    public ReservationService(UserRepository userRepository, FieldRepository fieldRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.fieldRepository = fieldRepository;
        this.reservationRepository = reservationRepository;
    }
    
    // ====================================================================================
    // 1. Facade Method: Proses Utama Reservasi
    // ====================================================================================
    public Optional<Reservation> bookField(String userId, String fieldId, LocalDate date, int startHour, int durationHours, String paymentMethod) {
        // 1. Validasi Input (Bagian dari Clean Code/Error Handling)
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Field> fieldOpt = fieldRepository.findById(fieldId);

        if (!userOpt.isPresent() || !fieldOpt.isPresent()) {
            System.err.println("ERROR: User atau Lapangan tidak ditemukan.");
            return Optional.empty();
        }
        
        User user = userOpt.get();
        Field field = fieldOpt.get();
        
        // 2. Cek Bentrok Jadwal (Delegasi ke ReservationRepository)
        if (isTimeSlotConflicted(fieldId, date, startHour, durationHours)) {
            System.err.println("ERROR: Jadwal bentrok! Slot waktu ini sudah terisi.");
            return Optional.empty();
        }

        // 3. Tentukan Strategy Tarif (Delegasi ke Strategy Pattern)
        TariffStrategy strategy = getTariffStrategy(date.getDayOfWeek());
        
        // 4. Hitung Total Biaya (Delegasi ke Strategy Pattern)
        double totalFee = strategy.calculatePrice(field, date.getDayOfWeek(), startHour, durationHours);

        // 5. Buat dan Simpan Objek Reservation (Delegasi ke Factory/Repository)
        String reservationId = IdGenerator.generateUniqueId("RES");
        Reservation newReservation = new Reservation(reservationId, user, field, date, startHour, durationHours, totalFee, strategy);
        
        // 6. Proses Pembayaran (Simulasi Payment)
        String paymentId = IdGenerator.generateUniqueId("PAY");
        Payment payment = new Payment(paymentId, totalFee, paymentMethod);
        newReservation.setPayment(payment);

        reservationRepository.save(newReservation);
        
        return Optional.of(newReservation);
    }
    
    // ====================================================================================
    // 2. Strategy Selector (Untuk Logic Tariff Strategy)
    // ====================================================================================
    // Memilih strategi berdasarkan hari
    public TariffStrategy getTariffStrategy(DayOfWeek day) {
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            return new WeekendTariff();
        } else {
            return new WeekdayTariff();
        }
    }
    
    // ====================================================================================
    // 3. Logika Pembatalan dan Refund
    // ====================================================================================
    public boolean cancelReservation(String reservationId) {
        Optional<Reservation> resOpt = reservationRepository.findById(reservationId);
        
        if (!resOpt.isPresent()) {
            System.err.println("ERROR: Reservasi tidak ditemukan.");
            return false;
        }
        
        Reservation reservation = resOpt.get();
        if (reservation.getStatus().equals("CANCELED")) {
            System.err.println("ERROR: Reservasi ini sudah dibatalkan sebelumnya.");
            return false;
        }

        // Batasan: Pembatalan + refund 50%
        double refundAmount = reservation.getTotalFee() * 0.50;
        
        reservation.setStatus("CANCELED");
        if (reservation.getPayment() != null) {
            reservation.getPayment().refund(refundAmount);
        }
        
        // Simpan perubahan status (override data lama)
        reservationRepository.save(reservation);
        
        System.out.printf("SUCCESS: Reservasi ID %s dibatalkan. Refund 50%% sebesar Rp%,.0f diproses.\n", reservationId, refundAmount);
        return true;
    }
    
    // ====================================================================================
    // 4. Helper Method: Cek Bentrok Jadwal
    // ====================================================================================
    public boolean isTimeSlotConflicted(String fieldId, LocalDate date, int startHour, int durationHours) {
        int endHour = startHour + durationHours;
        
        List<Reservation> existingReservations = reservationRepository.findByFieldAndDate(fieldId, date);
        
        for (Reservation existing : existingReservations) {
            // Cek apakah slot waktu yang diminta tumpang tindih dengan reservasi yang sudah ada
            // Slot baru: [startHour, endHour)
            // Slot lama: [existing.getStartTimeHour(), existing.getEndTimeHour())
            boolean overlapStart = startHour < existing.getEndTimeHour() && startHour >= existing.getStartTimeHour();
            boolean overlapEnd = endHour > existing.getStartTimeHour() && endHour <= existing.getEndTimeHour();
            boolean oldContainsNew = startHour <= existing.getStartTimeHour() && endHour >= existing.getEndTimeHour();
            
            if (overlapStart || overlapEnd || oldContainsNew) {
                return true; // Bentrok
            }
        }
        return false; // Tidak bentrok
    }
    
    // ====================================================================================
    // 5. Reporting / Viewing (Delegasi ke Repositories)
    // ====================================================================================
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    
    public List<Field> getAllFields() {
        return fieldRepository.findAll();
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}