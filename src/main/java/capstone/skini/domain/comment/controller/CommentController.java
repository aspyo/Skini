package capstone.skini.domain.comment.controller;

import capstone.skini.domain.comment.dto.ResponseCommentDto;
import capstone.skini.domain.comment.entity.Comment;
import capstone.skini.domain.comment.service.CommentService;
import capstone.skini.domain.post.dto.ResponsePostDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    /**
     * 댓글 생성
     */
    @PostMapping("/comment")
    @Operation(summary = "댓글 생성", description = "댓글을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 생성 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseCommentDto.class))})
    })
    public ResponseEntity<?> createComment(@AuthenticationPrincipal CustomPrincipal principal,
                                           @RequestParam("post_id") Long postId,
                                           @RequestParam("content") String content) {
        try {
            User user = userService.findByLoginId(principal.getLoginId());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cannot find User By LoginId : " + principal.getLoginId());
            }
            Comment comment = commentService.create(user, postId, content);
            return ResponseEntity.ok(new ResponseCommentDto(comment));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * 댓글 수정
     */
    @PatchMapping("/comment/{comment_id}")
    public ResponseEntity<?> editComment(@AuthenticationPrincipal CustomPrincipal principal,
                                         @PathVariable("comment_id") Long commentId,
                                         @RequestParam("content") String content) {
        try {
            User user = userService.findByLoginId(principal.getLoginId());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cannot find User By LoginId : " + principal.getLoginId());
            }
            Comment editedComment = commentService.edit(user.getId(), commentId, content);
            return ResponseEntity.ok(new ResponseCommentDto(editedComment));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/comment/{comment_id}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal CustomPrincipal principal,
                                           @PathVariable("comment_id") Long commentId) {
        try {
            User user = userService.findByLoginId(principal.getLoginId());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cannot find User By LoginId : " + principal.getLoginId());
            }
            commentService.delete(user.getId(), commentId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
