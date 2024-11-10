package capstone.skini.domain.post.service;

import capstone.skini.domain.post.entity.Post;
import capstone.skini.domain.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
        return postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find Post By Id :" + id));
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Post post) {
        postRepository.delete(post);
    }
}
