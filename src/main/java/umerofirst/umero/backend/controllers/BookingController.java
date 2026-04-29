package umerofirst.umero.backend.controllers;

import umerofirst.umero.backend.models.Booking;
import umerofirst.umero.backend.repositories.BookingRepository;
import umerofirst.umero.backend.services.EmailService;

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

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        booking.setStatus(Booking.BookingStatus.PENDING);
        Booking saved = bookingRepository.save(booking);

        // Email to HOST
        emailService.sendEmail(
                saved.getHostEmail(),
                "New Booking Request — Umero",
                "Hi,\n\nSomeone has requested to book your space on Umero.\n\n" +
                        "Space: " + saved.getSpaceName() + "\n" +
                        "Date: " + saved.getBookingDate() + "\n" +
                        "Time: " + saved.getStartTime() + " to " + saved.getEndTime() + "\n" +
                        "Duration: " + saved.getTotalHours() + " hours\n" +
                        "Total: Rs." + saved.getTotalPrice() + "\n\n" +
                        "Log in to approve or decline:\n" +
                        "https://umero.in/dashboard\n\n" +
                        "Team Umero"
        );

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Booking> approveBooking(@PathVariable String id) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(Booking.BookingStatus.CONFIRMED);
                    Booking saved = bookingRepository.save(booking);

                    // Email to RENTER
                    emailService.sendEmail(
                            saved.getRenterEmail(),
                            "Booking Confirmed — Umero",
                            "Hi,\n\nYour booking has been confirmed!\n\n" +
                                    "Space: " + saved.getSpaceName() + "\n" +
                                    "Date: " + saved.getBookingDate() + "\n" +
                                    "Time: " + saved.getStartTime() + " to " + saved.getEndTime() + "\n" +
                                    "Total Paid: Rs." + saved.getTotalPrice() + "\n\n" +
                                    "See your booking at:\n" +
                                    "https://umero.in/booking\n\n" +
                                    "See you there!\nTeam Umero"
                    );

                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<Booking> declineBooking(@PathVariable String id) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(Booking.BookingStatus.DECLINED);
                    Booking saved = bookingRepository.save(booking);

                    // Email to RENTER
                    emailService.sendEmail(
                            saved.getRenterEmail(),
                            "Booking Update — Umero",
                            "Hi,\n\nUnfortunately the host was unable to confirm your booking for " +
                                    saved.getSpaceName() + " on " + saved.getBookingDate() + ".\n\n" +
                                    "Please search for another space:\n" +
                                    "https://umero.in\n\n" +
                                    "Team Umero"
                    );

                    return ResponseEntity.ok(saved);
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
