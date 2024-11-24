package capstone.skini.domain.post.dto;

import capstone.skini.domain.comment.entity.Comment;
import capstone.skini.domain.post.entity.Post;
import capstone.skini.domain.post.entity.PostType;
import lombok.Data;

import java.util.List;

@Data
public class ResponsePostDto {
    private String title;
    private String content;
    private PostType postType;
    private String username;
    private List<Comment> comments;

    protected ResponsePostDto() {

    }

    public ResponsePostDto(Post post) {
        title = post.getTitle();
        content = post.getContent();
        postType = post.getPostType();
        username = post.getUser().getUsername();
        comments = post.getComments();
    }
}
