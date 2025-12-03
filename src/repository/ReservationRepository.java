package repository;

import model.Reservation;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// Implementasi Generic Programming dan JCF (Java Collections Framework)
public class ReservationRepository extends GenericRepository<Reservation, String> {
    
    @Override
    protected String getId(Reservation entity) {
        return entity.getId();
    }
    
    // Metode penting untuk validasi bentrok jadwal (Core business logic)
    public List<Reservation> findByFieldAndDate(String fieldId, LocalDate date) {
        return dataMap.values().stream()
                .filter(r -> r.getField().getId().equals(fieldId) && r.getDate().isEqual(date) && r.getStatus().equals("BOOKED"))
                .collect(Collectors.toList());
    }
}