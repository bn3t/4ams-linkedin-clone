package com.ecoleit.linkedin.bff.controller;

import com.ecoleit.linkedin.bff.config.ConfigProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.sql.Timestamp;
import java.util.List;

record PostApiDTO(Integer id, Integer profileId, String title, String content, Timestamp creationDate){}
record CommentApiDTO (Integer id, Integer profileId, String content, Timestamp creationDate){
}
record PostWithCommentApiDTO(Integer id, Integer profileId, String title,
                          String content, Timestamp creationDate, List<CommentApiDTO> comments) {
}

//http://localhost:8080/api

@RestController
@RequestMapping("/api")
public class PostBffController {
    private final RestClient restClient;
    private final ConfigProperties configProperties;

    public PostBffController(RestClient restClient, ConfigProperties configProperties) {
        this.restClient = restClient;
        this.configProperties = configProperties;
    }

    @GetMapping("/posts/{postId}")
    public PostWithCommentApiDTO getPostById(@PathVariable Integer postId) {
        try{
            PostWithCommentApiDTO post = restClient
                    .get()
                    .uri("http://"
                            + configProperties
                            .getPostHost()
                            + ":8083/posts/{postId}/with-comments", postId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(PostWithCommentApiDTO.class);
            if (post != null) {
                return new PostWithCommentApiDTO(
                        post.id(),
                        post.profileId(),
                        post.title(),
                        post.content(),
                        post.creationDate(),
                        post.comments());
            } else {
                return null;
            }
        }catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @PostMapping("/posts")
    public PostApiDTO addPost(@RequestBody PostApiDTO postApiDTO) {
        try{
            PostApiDTO post = restClient
                    .post()
                    .uri("http://"
                            + configProperties
                            .getPostHost()
                            + ":8083/posts")
                    .accept(MediaType.APPLICATION_JSON)
                    .body(postApiDTO)
                    .retrieve()
                    .body(PostApiDTO.class);
            if (post != null) {
                return new PostApiDTO(
                        post.id(),
                        post.profileId(),
                        post.title(),
                        post.content(),
                        post.creationDate());
            } else {
                return null;
            }
        }catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @PutMapping("/posts/{postId}")
    public PostApiDTO editPost(@PathVariable Integer postId,@RequestBody PostApiDTO postApiDTO) {
        try{
            PostApiDTO post = restClient
                    .put()
                    .uri("http://"
                            + configProperties
                            .getPostHost()
                            + ":8083/posts/{postId}")
                    .accept(MediaType.APPLICATION_JSON)
                    .body(postApiDTO)
                    .retrieve()
                    .body(PostApiDTO.class);
            if (post != null) {
                return new PostApiDTO(
                        post.id(),
                        post.profileId(),
                        post.title(),
                        post.content(),
                        post.creationDate());
            } else {
                return null;
            }
        }catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }


    @PostMapping("/posts/{postId}/comments")
    public CommentApiDTO addComment(
            @PathVariable Integer postId,
            @RequestBody CommentApiDTO commentApiDTO) {
        try{
            CommentApiDTO newComment = restClient
                    .post()
                    .uri("http://"
                            + configProperties
                            .getPostHost()
                            + ":8083/comments/{postId}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(commentApiDTO)
                    .retrieve()
                    .body(CommentApiDTO.class);

            if (newComment != null) {
                return new CommentApiDTO(
                        newComment.id(),
                        newComment.profileId(),
                        newComment.content(),
                        newComment.creationDate());
            } else {
                return null;
            }
        }catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @PutMapping("/comments/{commentId}")
    public CommentApiDTO EditComment(
            @PathVariable Integer commentId,
            @RequestBody CommentApiDTO commentApiDTO) {
        try{
            CommentApiDTO newComment = restClient
                    .put()
                    .uri("http://"
                            + configProperties
                            .getPostHost()
                            + ":8083/comments/{commentId}", commentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(commentApiDTO)
                    .retrieve()
                    .body(CommentApiDTO.class);

            if (newComment != null) {
                return new CommentApiDTO(
                        newComment.id(),
                        newComment.profileId(),
                        newComment.content(),
                        newComment.creationDate());
            } else {
                return null;
            }
        }catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }


    @DeleteMapping("/comments/{commentId}")
    ResponseEntity<String> deleteComment(@PathVariable Integer commentId) {
        try{
            restClient
                    .delete()
                    .uri("http://"
                            + configProperties
                            .getPostHost()
                            + ":8083/comments/{commentId}", commentId)
                    .retrieve();
            return ResponseEntity.ok("Comment deleted");
        }catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @DeleteMapping("/posts/{postId}")
    ResponseEntity<String> deletePost(@PathVariable Integer postId) {
        try{
            restClient
                    .delete()
                    .uri("http://"
                            + configProperties
                            .getPostHost()
                            + ":8083/posts/{postId}", postId)
                    .retrieve();
            return ResponseEntity.ok("Post deleted");
        }catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}
