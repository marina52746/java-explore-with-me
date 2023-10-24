package ru.practicum.comments.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.comments.dto.InputCommentDto;
import ru.practicum.comments.dto.OutputCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

@Component
public class CommentMapper {

    public static OutputCommentDto fromCommentToOutputCommentDto(Comment comment) {
        if (comment == null) return null;
        return new OutputCommentDto(
                comment.getUser().getName(),
                comment.getText(),
                comment.getCreatedOn()
        );
    }

    public static Comment fromInputCommentDtoToComment(InputCommentDto inputCommentDto, User user, Event event) {
        if (inputCommentDto == null) return null;
        return new Comment(
                inputCommentDto.getText(),
                user,
                event
        );
    }
}
