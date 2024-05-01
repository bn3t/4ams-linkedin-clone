package com.ecoleit.linkedin.post.controller;

import com.ecoleit.linkedin.post.modele.PostDTO;
import com.ecoleit.linkedin.post.modele.PostWithCommentDTO;
import com.ecoleit.linkedin.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    List<PostDTO> getPostList(){
        return postService.getPostList();
    }
  /*  @GetMapping("/{postId}")
    PostDTO getPostById(@PathVariable Integer postId) {
        return postService.getPostById(postId);
    }
*/
    @PostMapping("")
    PostDTO createPost(@RequestBody PostDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @PutMapping("/{postId}")
    PostDTO updatePost(@PathVariable Integer postId, @RequestBody PostDTO postDTO) {
        return postService.updatePost(postId, postDTO);
    }

    @DeleteMapping("/{postId}")
    ResponseEntity<String> deletePost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("Post deleted");
    }

    @GetMapping("/{postId}/with-comments")
    PostWithCommentDTO getPostWithComments(@PathVariable Integer postId) {
        return postService.getPostWithComments(postId);
    }

    @GetMapping("/{profileId}")
    PostWithCommentDTO getPostByProfileId(@PathVariable Integer profileId) {
        return postService.getPostByProfileId(profileId);
    }
}
