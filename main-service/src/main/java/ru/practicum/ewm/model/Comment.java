package ru.practicum.ewm.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created")
    private LocalDateTime created; // Дата и время создания комментария

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event; // Cобытие, к которому написан комментарий

    @ManyToOne
    @JoinColumn(name = "commentator_id")
    private User commentator; // Пользователь, который написал комментарий

    @Column(name = "comment_text")
    private String commentText;
}
