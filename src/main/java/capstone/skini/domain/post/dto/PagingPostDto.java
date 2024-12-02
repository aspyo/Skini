package capstone.skini.domain.post.dto;

import capstone.skini.domain.post.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PagingPostDto {
    private Long id;
    private String title;
    private String username;
    private int commentsCount;
    private LocalDateTime createdAt;

    public PagingPostDto(Post post) {
        id = post.getId();
        title = post.getTitle();
        username = post.getUser().getUsername();
        commentsCount = post.getComments().size();
        createdAt = post.getCreatedAt();
    }
}
