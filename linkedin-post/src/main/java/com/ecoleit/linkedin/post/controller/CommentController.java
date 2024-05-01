package com.ecoleit.linkedin.post.controller;

import com.ecoleit.linkedin.post.modele.CommentDTO;
import com.ecoleit.linkedin.post.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;



    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("")
    List<CommentDTO> getCommentList(){
        return commentService.getCommentList();
    }

    @PostMapping("/{postId}")
    CommentDTO createComment(@PathVariable Integer postId, @RequestBody CommentDTO commentDTO) {
        return commentService.createComment(postId, commentDTO);
    }

    @PutMapping("/{commentId}")
    CommentDTO updateComment(@PathVariable Integer commentId, @RequestBody CommentDTO commentDTO) {
        return commentService.updateComment(commentId, commentDTO);
    }

    @DeleteMapping("/{commentId}")
    ResponseEntity<String>deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted");
    }
}
