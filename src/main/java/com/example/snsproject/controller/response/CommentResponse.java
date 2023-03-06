package com.example.snsproject.controller.response;

import com.example.snsproject.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;

    private String comment;

    private String userName;

    private Long postId;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static CommentResponse fromComment(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getComment(), comment.getUserName(), comment.getPostId(), comment.getRegisteredAt(), comment.getUpdatedAt(), comment.getDeletedAt());
    }
}
