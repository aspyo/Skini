package capstone.skini.domain.comment.service;

import capstone.skini.domain.comment.entity.Comment;
import capstone.skini.domain.comment.repository.CommentRepository;
import capstone.skini.domain.post.entity.Post;
import capstone.skini.domain.post.repository.PostRepository;
import capstone.skini.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Comment create(User user, Long postId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("cannot find Post By Id : " + postId));
        Comment newComment = Comment.builder()
                .content(content)
                .user(user)
                .post(post)
                .build();
        return commentRepository.save(newComment);
    }

    public Comment edit(Long userId, Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("cannot find Comment By Id : " + commentId));
        if (comment.getUser().getId() != userId) {
            throw new IllegalStateException("댓글 작성자의 id와 현재 유저의 id가 다릅니다.");
        }
        comment.change(content);
        return comment;
    }

    public void delete(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("cannot find Comment By Id : " + commentId));
        if (comment.getUser().getId() != userId) {
            throw new IllegalStateException("댓글 작성자의 id와 현재 유저의 id가 다릅니다.");
        }
        commentRepository.delete(comment);
    }
}
