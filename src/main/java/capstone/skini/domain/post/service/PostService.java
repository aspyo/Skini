package capstone.skini.domain.post.service;

import capstone.skini.domain.post.dto.RequestEditPostDto;
import capstone.skini.domain.post.entity.Post;
import capstone.skini.domain.post.entity.PostType;
import capstone.skini.domain.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return postRepository.findPostFetchById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find Post By Id :" + id));
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findPostFetchById(postId).orElseThrow(() -> new EntityNotFoundException("Cannot find Post By Id :" + postId));
        if (post.getUser().getId() != userId) {
            throw new IllegalStateException("로그인한 유저의 id와 게시글 작성자의 id가 다릅니다.");
        }
        postRepository.delete(post);
    }

    public Post editPost(Long userId, Long postId, RequestEditPostDto requestEditPostDto) {
        Post post = postRepository.findPostFetchById(postId).orElseThrow(() -> new EntityNotFoundException("Cannot find Post By Id :" + postId));
        if (post.getUser().getId() != userId) {
            throw new IllegalStateException("로그인한 유저의 id와 게시글 작성자의 id가 다릅니다.");
        }
        post.edit(requestEditPostDto.getTitle(), requestEditPostDto.getContent());
        return post;
    }

    public Page<Post> findPostsByPostType(PostType postType, int page) {
        PageRequest pageRequest = PageRequest.of(page-1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.findAllByPostType(postType, pageRequest);
    }
}
