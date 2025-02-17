package ru.practicum.ewm.model;


import jakarta.persistence.*;
import lombok.*;
import ru.practicum.ewm.enums.EventState;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation", nullable = false)
    private String annotation;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @OneToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participation_limit")
    private Long participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "title")
    private String title;

}
