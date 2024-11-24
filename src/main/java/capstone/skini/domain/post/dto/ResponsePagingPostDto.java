package capstone.skini.domain.post.dto;

import capstone.skini.domain.post.entity.Post;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResponsePagingPostDto {
    private int totalPages;
    private List<PagingPostDto> posts;

    public ResponsePagingPostDto(int totalPages, List<Post> posts) {
        this.totalPages = totalPages;
        this.posts = posts.stream().map(post -> new PagingPostDto(post)).collect(Collectors.toList());
    }
}
