package ru.practicum.ewm.model;

import lombok.*;
import ru.practicum.ewm.enums.RequestStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created")
    private LocalDateTime created; // Дата и время создания заявки

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event; // Идентификатор события

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester; // Идентификатор пользователя, отправившего заявку

    @Enumerated(EnumType.STRING)
    private RequestStatus status; // Статус заявки

    public Request(LocalDateTime created, Event event, User requester, RequestStatus status) {
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }
}
