package umerofirst.umero.backend.controllers;

import umerofirst.umero.backend.models.Booking;
import umerofirst.umero.backend.repositories.BookingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        booking.setStatus(Booking.BookingStatus.PENDING);
        Booking saved = bookingRepository.save(booking);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Booking> approveBooking(@PathVariable String id) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(Booking.BookingStatus.CONFIRMED);
                    return ResponseEntity.ok(bookingRepository.save(booking));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<Booking> declineBooking(@PathVariable String id) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(Booking.BookingStatus.DECLINED);
                    return ResponseEntity.ok(bookingRepository.save(booking));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/renter/{email}")
    public List<Booking> getRenterBookings(@PathVariable String email) {
        return bookingRepository.findByRenterEmail(email);
    }

    @GetMapping("/host/{email}")
    public List<Booking> getHostBookings(@PathVariable String email) {
        return bookingRepository.findByHostEmail(email);
    }
}