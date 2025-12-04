package repository;

import model.Reservation;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepository extends GenericRepository<Reservation, String> {
    
    @Override
    protected String getId(Reservation entity) {
        return entity.getId();
    }
    
    public List<Reservation> findByFieldAndDate(String fieldId, LocalDate date) {
        return dataMap.values().stream()
                .filter(r -> r.getField().getId().equals(fieldId) && r.getDate().isEqual(date) && r.getStatus().equals("BOOKED"))
                .collect(Collectors.toList());
    }
}