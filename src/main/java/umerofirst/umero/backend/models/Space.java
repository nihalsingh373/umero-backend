package umerofirst.umero.backend.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "spaces")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String area;
    private String city;
    private String type;

    @ElementCollection
    @CollectionTable(name = "space_activities",
            joinColumns = @JoinColumn(name = "space_id"))
    @Column(name = "activity")
    private List<String> activities;

    private Integer pricePerHour;
    private Integer minHours;

    private String availableFrom;
    private String availableTo;

    @ElementCollection
    @CollectionTable(name = "space_days",
            joinColumns = @JoinColumn(name = "space_id"))
    @Column(name = "day")
    private List<String> availableDays;

    private Integer capacity;
    private Integer sizeSqft;

    @Column(columnDefinition = "TEXT")
    private String whatsIncluded;

    @Column(columnDefinition = "TEXT")
    private String rules;

    private String parking;
    private Boolean hasAC;
    private Boolean hasGreenRoom;

    @ElementCollection
    @CollectionTable(name = "space_photos",
            joinColumns = @JoinColumn(name = "space_id"))
    @Column(name = "photo_url")
    private List<String> photos;

    private String hostId;
    private String hostEmail;

    private Boolean instantBook = false;
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;
}