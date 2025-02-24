package ru.practicum.ewm.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.NewCommentDto;
import ru.practicum.ewm.dto.comment.UpdateCommentDto;
import ru.practicum.ewm.errorHandler.exceptions.NotFoundException;
import ru.practicum.ewm.errorHandler.exceptions.ValidationException;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Event с id " + eventId + " не найден"));

        Comment comment = CommentMapper.toComment(newCommentDto, user, event);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto updateCommentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new NotFoundException("Comment с id " + commentId + " не найден."));

        if (!comment.getCreator().getId().equals(userId)) {
            throw new ValidationException("Пользователь с id " + userId + " не оставлял этот комментарий.");
        }

        if (StringUtils.hasText(updateCommentDto.getText())) {
            comment.setText(updateCommentDto.getText());
            commentRepository.save(comment);
        }

        return CommentMapper.toCommentDto((comment));
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new NotFoundException("Comment с id " + commentId + " не найден."));

        if (!comment.getCreator().getId().equals(userId)) {
            throw new ValidationException("Пользователь с id " + userId + " не оставлял этот комментарий.");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getAllEventComments(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event с id " + eventId + " не найден.");
        }

        List<Comment> commentList = commentRepository.findAllByEventId(eventId);

        return commentList.stream()
                .map(comment -> CommentMapper.toCommentDto(comment))
                .toList();
    }
}
