package ru.practicum.ewm.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.NewCommentDto;
import ru.practicum.ewm.dto.comment.UpdateCommentDto;
import ru.practicum.ewm.mapper.CommentMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        return null;
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentDto, UpdateCommentDto updateCommentDto) {
        return null;
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {

    }

    @Override
    public List<CommentDto> getAllEventComments(Long eventId) {
        return List.of();
    }
}
