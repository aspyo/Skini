package capstone.skini.domain.post.controller;

import capstone.skini.domain.post.dto.ResponsePagingPostDto;
import capstone.skini.domain.post.dto.RequestEditPostDto;
import capstone.skini.domain.post.dto.RequestPostDto;
import capstone.skini.domain.post.dto.ResponsePostDto;
import capstone.skini.domain.post.entity.Post;
import capstone.skini.domain.post.entity.PostType;
import capstone.skini.domain.post.service.PostService;
import capstone.skini.domain.user.entity.User;
import capstone.skini.domain.user.service.UserService;
import capstone.skini.security.user.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    /**
     * 게시글 생성
     */
    @PostMapping("/post")
    public ResponseEntity<?> createPost(@AuthenticationPrincipal CustomPrincipal principal,
                                        @RequestBody RequestPostDto requestPostDto) {
        User user = userService.findByLoginId(principal.getLoginId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cannot find User By LoginId : " + principal.getLoginId());
        }

        Post newPost = Post.builder()
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .postType(requestPostDto.getPostType())
                .user(user)
                .build();
        postService.createPost(newPost);

        return ResponseEntity.ok().build();
    }

    /**
     * 특정 게시글 조회
     */
    @GetMapping("/post/{post_id}")
    @Operation(summary = "커뮤니티 게시글 조회", description = "커뮤니티 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "커뮤니티 게시글 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePostDto.class))})
    })
    public ResponseEntity<?> getPost(@PathVariable("post_id") Long postId) {
        try {
            Post findPost = postService.findById(postId);
            return ResponseEntity.ok(new ResponsePostDto(findPost));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * 게시글 종류별 페이징 조회
     */
    @GetMapping("/posts")
    public ResponseEntity<?> pagingPosts(@RequestParam("postType") PostType postType,
                                         @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Post> posts = postService.findPostsByPostType(postType, page);
        return ResponseEntity.ok(new ResponsePagingPostDto(posts.getTotalPages(), posts.getContent()));
    }

    /**
     * 게시글 수정
     */
    @PatchMapping("/post/{post_id}")
    @Operation(summary = "커뮤니티 게시글 수정", description = "커뮤니티 게시글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "커뮤니티 게시글 수정 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePostDto.class))})
    })
    public ResponseEntity<?> editPost(@AuthenticationPrincipal CustomPrincipal principal,
                                      @PathVariable("post_id") Long postId,
                                      @RequestBody RequestEditPostDto requestEditPostDto) {
        try {
            User user = userService.findByLoginId(principal.getLoginId());
            Post editedPost = postService.editPost(user.getId(), postId, requestEditPostDto);
            return ResponseEntity.ok(new ResponsePostDto(editedPost));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/post/{post_id}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal CustomPrincipal principal,
                                        @PathVariable("post_id") Long postId) {
        try {
            User user = userService.findByLoginId(principal.getLoginId());
            postService.deletePost(user.getId(), postId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
