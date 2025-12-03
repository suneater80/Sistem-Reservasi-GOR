package repository;

import model.Reservation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Implementasi JCF (Java Collections Framework) dan Clean Code
public class ReservationRepository {
    // Menggunakan HashMap untuk penyimpanan data in-memory (JCF)
    private final Map<String, Reservation> reservationMap;

    public ReservationRepository() {
        this.reservationMap = new HashMap<>();
    }

    public void save(Reservation reservation) {
        reservationMap.put(reservation.getId(), reservation);
    }

    public Optional<Reservation> findById(String id) {
        return Optional.ofNullable(reservationMap.get(id));
    }

    public List<Reservation> findAll() {
        return reservationMap.values().stream().collect(Collectors.toList());
    }

    public void deleteById(String id) {
        reservationMap.remove(id);
    }
    
    // Metode penting untuk validasi bentrok jadwal (Core business logic)
    public List<Reservation> findByFieldAndDate(String fieldId, LocalDate date) {
        return reservationMap.values().stream()
                .filter(r -> r.getField().getId().equals(fieldId) && r.getDate().isEqual(date) && r.getStatus().equals("BOOKED"))
                .collect(Collectors.toList());
    }
    
    public int count() {
        return reservationMap.size();
    }
}