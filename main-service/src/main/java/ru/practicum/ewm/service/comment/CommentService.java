package ru.practicum.ewm.service.comment;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.NewCommentDto;

import java.util.List;

public interface CommentService {

    // private:
    // добавление комментария к событию
    CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    // удаление комментария к событию
    void deleteComment(Long userId, Long commentId);

    // изменение комментария
    CommentDto updateComment(Long userId, Long commentId, NewCommentDto newCommentDto);

    // public:
    // получение всех комментариев к событию
    List<CommentDto> getAllCommentsToEvent(Long eventId, Pageable pageable);

    // admin:
    // удаление комментария админом
    void deleteCommentByAdmin(Long commentId);
}
