package ru.practicum.ewm.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lat", nullable = false)
    private Float lat;

    @Column(name = "lon", nullable = false)
    private Float lon;
    
}
