package service;

import decorator.InsurancePaymentDecorator;
import decorator.TaxPaymentDecorator;
import model.*;
import repository.*;
import strategy.*;
import util.IdGenerator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReservationService {
    private final UserRepository userRepository;
    private final FieldRepository fieldRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(UserRepository userRepository, FieldRepository fieldRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.fieldRepository = fieldRepository;
        this.reservationRepository = reservationRepository;
    }
    
    public Optional<Reservation> bookField(String userId, String fieldId, LocalDate date, int startHour, int durationHours, String paymentMethod, boolean addTax, boolean addInsurance) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Field> fieldOpt = fieldRepository.findById(fieldId);

        if (!userOpt.isPresent() || !fieldOpt.isPresent()) {
            System.err.println("ERROR: User atau Lapangan tidak ditemukan.");
            return Optional.empty();
        }
        
        User user = userOpt.get();
        Field field = fieldOpt.get();
        
        if (isTimeSlotConflicted(fieldId, date, startHour, durationHours)) {
            System.err.println("ERROR: Jadwal bentrok! Slot waktu ini sudah terisi.");
            return Optional.empty();
        }

        TariffStrategy strategy = getTariffStrategy(date.getDayOfWeek());
        double totalFee = strategy.calculatePrice(field, date.getDayOfWeek(), startHour, durationHours);

        String reservationId = IdGenerator.generateUniqueId("RES");
        Reservation newReservation = new Reservation(reservationId, user, field, date, startHour, durationHours, totalFee, strategy);
        
        String paymentId = IdGenerator.generateUniqueId("PAY");
        Payment payment = new Payment(paymentId, totalFee, paymentMethod);
        
        if (addTax) {
            TaxPaymentDecorator taxDecorator = new TaxPaymentDecorator(payment);
            System.out.printf("Pajak (PPN 10%%): Rp%,.0f%n", taxDecorator.getTaxAmount());
            totalFee = taxDecorator.getTotalWithTax();
        }
        
        if (addInsurance) {
            InsurancePaymentDecorator insuranceDecorator = new InsurancePaymentDecorator(payment);
            System.out.printf("Asuransi: Rp%,.0f%n", insuranceDecorator.getInsuranceFee());
            totalFee = insuranceDecorator.getTotalWithInsurance();
        }
        
        if (addTax || addInsurance) {
            newReservation = new Reservation(reservationId, user, field, date, startHour, durationHours, totalFee, strategy);
            payment = new Payment(paymentId, totalFee, paymentMethod);
        }
        
        newReservation.setPayment(payment);

        reservationRepository.save(newReservation);
        
        return Optional.of(newReservation);
    }
    
    public TariffStrategy getTariffStrategy(DayOfWeek day) {
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            return new WeekendTariff();
        } else {
            return new WeekdayTariff();
        }
    }
    
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

        double refundAmount = reservation.getTotalFee() * 0.50;
        
        reservation.setStatus("CANCELED");
        if (reservation.getPayment() != null) {
            reservation.getPayment().refund(refundAmount);
        }
        
        reservationRepository.save(reservation);
        
        System.out.printf("SUCCESS: Reservasi ID %s dibatalkan. Refund 50%% sebesar Rp%,.0f diproses.\n", reservationId, refundAmount);
        return true;
    }
    
    public boolean isTimeSlotConflicted(String fieldId, LocalDate date, int startHour, int durationHours) {
        int endHour = startHour + durationHours;
        
        List<Reservation> existingReservations = reservationRepository.findByFieldAndDate(fieldId, date);
        
        for (Reservation existing : existingReservations) {
            boolean overlapStart = startHour < existing.getEndTimeHour() && startHour >= existing.getStartTimeHour();
            boolean overlapEnd = endHour > existing.getStartTimeHour() && endHour <= existing.getEndTimeHour();
            boolean oldContainsNew = startHour <= existing.getStartTimeHour() && endHour >= existing.getEndTimeHour();
            
            if (overlapStart || overlapEnd || oldContainsNew) {
                return true;
            }
        }
        return false;
    }
    
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