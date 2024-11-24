package capstone.skini.domain.post.repository;

import capstone.skini.domain.post.entity.Post;
import capstone.skini.domain.post.entity.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);

    @Query("select p from Post p left join fetch p.user u left join fetch p.comments c where p.id = :id")
    Optional<Post> findPostFetchById(@Param("id") Long id);

    @Query("select p from Post p where p.postType = :postType")
    Page<Post> findAllByPostType(@Param("postType") PostType postType, Pageable pageable);

}
