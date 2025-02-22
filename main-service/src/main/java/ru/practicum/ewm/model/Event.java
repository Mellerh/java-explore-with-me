package ru.practicum.ewm.model;

import lombok.*;
import ru.practicum.ewm.enums.EventState;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation", nullable = false)
    private String annotation; // Краткое описание события

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category; // категории к которой относится событие

    @Column(name = "created_on")
    private LocalDateTime createdOn; // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")

    @Column(name = "description", nullable = false)
    private String description; // Полное описание события

    @Column(name = "event_date")
    private LocalDateTime eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    @OneToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator; // Пользователь (краткая информация)

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location; // Широта и долгота места проведения события

    @Column(name = "paid")
    private Boolean paid; // Нужно ли оплачивать участие

    @Column(name = "participation_limit")
    private Long participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    @Column(name = "published_on")
    private LocalDateTime publishedOn; // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")

    @Column(name = "request_moderation")
    private Boolean requestModeration; // Нужна ли пре-модерация заявок на участие

    @Enumerated(EnumType.STRING)
    private EventState state; // Список состояний жизненного цикла события

    @Column(name = "title")
    private String title; // Заголовок
}
