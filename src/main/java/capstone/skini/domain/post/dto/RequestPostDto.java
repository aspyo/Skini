package capstone.skini.domain.post.dto;

import capstone.skini.domain.post.entity.PostType;
import lombok.Data;

@Data
public class RequestPostDto {
    private String title;
    private String content;
    private PostType postType;
}
