package umerofirst.umero.backend.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String spaceId;
    private String spaceName;
    private String hostEmail;
    private String renterId;
    private String renterEmail;

    private String bookingDate;
    private String startTime;
    private String endTime;

    private Integer totalHours;
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum BookingStatus {
        PENDING, CONFIRMED, DECLINED, COMPLETED
    }
}