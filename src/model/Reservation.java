package model;

import java.time.LocalDate;
import strategy.TariffStrategy;

// Entitas Reservation
public class Reservation {
    private String id;
    private User user;
    private Field field;
    private LocalDate date;
    private int startTimeHour; // Jam mulai (0-23)
    private int durationHours; // Durasi jam
    private double totalFee;
    private String status; // "BOOKED", "CANCELED"
    private Payment payment;
    private TariffStrategy appliedStrategy; // Strategi tarif yang digunakan

    public Reservation(String id, User user, Field field, LocalDate date, int startTimeHour, int durationHours, double totalFee, TariffStrategy appliedStrategy) {
        this.id = id;
        this.user = user;
        this.field = field;
        this.date = date;
        this.startTimeHour = startTimeHour;
        this.durationHours = durationHours;
        this.totalFee = totalFee;
        this.status = "BOOKED";
        this.appliedStrategy = appliedStrategy;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getters
    public String getId() { return id; }
    public User getUser() { return user; }
    public Field getField() { return field; }
    public LocalDate getDate() { return date; }
    public int getStartTimeHour() { return startTimeHour; }
    public int getDurationHours() { return durationHours; }
    public int getEndTimeHour() { return startTimeHour + durationHours; }
    public double getTotalFee() { return totalFee; }
    public String getStatus() { return status; }
    public Payment getPayment() { return payment; }
    public TariffStrategy getAppliedStrategy() { return appliedStrategy; }

    @Override
    public String toString() {
        String paymentInfo = payment != null ? " (" + payment.getStatus() + ", Rp" + String.format("%,.0f", payment.getAmount()) + ")" : " (Belum Bayar)";
        return String.format("ID: %s | Tgl: %s | Jam: %02d:00 - %02d:00 (%d jam) | Lapangan: %s | Pemesan: %s | Status: %s | Total Biaya: %,.0f | Tarif: %s",
            id, date, startTimeHour, getEndTimeHour(), durationHours, field.getName(), user.getName(), status, totalFee, appliedStrategy.getName());
    }
}