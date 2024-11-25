package capstone.skini.domain.post.dto;

import capstone.skini.domain.comment.dto.ResponseCommentDto;
import capstone.skini.domain.comment.entity.Comment;
import capstone.skini.domain.post.entity.Post;
import capstone.skini.domain.post.entity.PostType;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResponsePostDto {
    private Long id;
    private String title;
    private String content;
    private PostType postType;
    private String username;
    private List<ResponseCommentDto> comments;

    protected ResponsePostDto() {

    }

    public ResponsePostDto(Post post) {
        id = post.getId();
        title = post.getTitle();
        content = post.getContent();
        postType = post.getPostType();
        username = post.getUser().getUsername();
        comments = post.getComments().stream().map(c -> new ResponseCommentDto(c)).collect(Collectors.toList());
    }
}
