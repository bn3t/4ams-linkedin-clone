package com.ecoleit.linkedin.post.service;

import com.ecoleit.linkedin.post.entity.Post;
import com.ecoleit.linkedin.post.modele.CommentDTO;
import com.ecoleit.linkedin.post.modele.PostDTO;
import com.ecoleit.linkedin.post.modele.PostWithCommentDTO;
import com.ecoleit.linkedin.post.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentService commentService;

    public PostService(PostRepository postRepository, CommentService commentService) {
        this.postRepository = postRepository;
        this.commentService = commentService;
    }
    private PostDTO convertToPostDTO(Post post){
        return new PostDTO(
                post.getId(),
                post.getProfileId(),
                post.getTitle(),
                post.getContent(),
                post.getCreationDate());
    }

    public List<PostDTO> getPostList(){
        return postRepository
                .findAll()
                .stream()
                .map(this::convertToPostDTO)
                .toList();
    }

    public PostDTO getPostById(Integer postId) {
        return postRepository.findById(postId)
                .map(this::convertToPostDTO)
                .orElse(null);
    }

    public PostDTO createPost(PostDTO postDTO) {
        Post newPost = new Post();
        newPost.setProfileId(postDTO.profileId());
        newPost.setTitle(postDTO.title());
        newPost.setContent(postDTO.content());
        newPost.setCreationDate(new Timestamp(System.currentTimeMillis()));

        Post savedPost = postRepository.save(newPost);

        return new PostDTO(
                savedPost.getId(),
                savedPost.getProfileId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getCreationDate()
        );
    }

    public PostDTO updatePost(Integer postId, PostDTO postDTO) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            existingPost.setTitle(postDTO.title());
            existingPost.setContent(postDTO.content());

            Post updatedPost = postRepository.save(existingPost);

            return new PostDTO(
                    updatedPost.getId(),
                    updatedPost.getProfileId(),
                    updatedPost.getTitle(),
                    updatedPost.getContent(),
                    updatedPost.getCreationDate()
            );
        } else {
            throw new RuntimeException("Post not found with id: " + postId);
        }
    }

    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }



    public PostWithCommentDTO getPostWithComments(Integer postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            List<CommentDTO> comments =commentService.getCommentsByPostId(postId);

            return new PostWithCommentDTO(
                    post.getId(),
                    post.getProfileId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreationDate(),
                    comments
            );
        } else {
            throw new RuntimeException("Post not found with id");
        }
    }

    public PostWithCommentDTO getPostByProfileId(Integer profileId){
        Optional<Post> optionalPost = postRepository.findByProfileId(profileId);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            List<CommentDTO> comments =commentService.getCommentsByPostId(post.getId());

            return new PostWithCommentDTO(
                    post.getId(),
                    post.getProfileId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreationDate(),
                    comments);
        } else {
            throw new RuntimeException("Post not found with id");
        }
    }

}
