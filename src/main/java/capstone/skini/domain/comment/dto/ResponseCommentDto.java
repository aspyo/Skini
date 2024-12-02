package capstone.skini.domain.comment.dto;

import capstone.skini.domain.comment.entity.Comment;
import lombok.Data;

@Data
public class ResponseCommentDto {
    private String content;
    private String username;
    private Long postId;

    public ResponseCommentDto(Comment comment) {
        content = comment.getContent();
        username = comment.getUser().getUsername();
        postId = comment.getPost().getId();
    }
}
