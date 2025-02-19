package ru.practicum.ewm.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pinned")
    private Boolean pinned; // Закреплена ли подборка на главной странице сайта

    @Column(name = "title")
    private String title; // Заголовок подборки

    // добавляем другую таблицу, где событие-подборка
    // может быть несколько событий к одной подборке
    // может быть несколько подборок с одинаковым событием
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;
}
