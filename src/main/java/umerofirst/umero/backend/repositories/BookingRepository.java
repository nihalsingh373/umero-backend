package umerofirst.umero.backend.repositories;

import umerofirst.umero.backend.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByRenterEmail(String renterEmail);
    List<Booking> findByHostEmail(String hostEmail);
    List<Booking> findBySpaceId(String spaceId);
}