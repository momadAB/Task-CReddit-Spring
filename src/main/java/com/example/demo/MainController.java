package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// Each post will have its own comments
@RestController
@RequestMapping("/posts")
public class MainController {
    private final List<Post> posts = new ArrayList<>();

    // Get all posts
    // Can optionally search by post title
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(@RequestParam(required = false) String title) {
        for(Post post : posts) {
            if (post.getTitle().equals(title)) {
                return ResponseEntity.status(HttpStatus.OK).body(List.of(post));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    // Get post by id
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        for (Post post : posts) {
            if (post.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.OK).body(post);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Add a new post
    @PostMapping
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        // If field types are incorrect or values are missing, return 400
        if (post.getTitle() == null || post.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (post.getTitle().isEmpty() || post.getDescription().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Post ID is null, so it must be set manually
        if (post.getId() == null) {
            post.setId(java.util.UUID.randomUUID().toString());
        }

        posts.add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    // Delete a post
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable String id) {
        for (Post post : posts) {
            if (post.getId().equals(id)) {
                posts.remove(post);
                return ResponseEntity.status(HttpStatus.OK).body("Post has been deleted successfully");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post with id " + id + " not found");
    }

    // Add a comment to a post by ID
    @PostMapping("/{id}/comments")
    public ResponseEntity<String> addComment(@PathVariable String id, @RequestBody Comment comment) {
        // 400 bad request if comment is invalid/empty
        if (comment.getComment() == null || comment.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (comment.getComment().isEmpty() || comment.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Comment cannot be empty");
        }
        // 404 not found if post not found
        for (Post post : posts) {
            if (post.getId().equals(id)) {
                post.addComment(comment);
                return ResponseEntity.status(HttpStatus.CREATED).body("Comment has been added successfully");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post with id " + id + " not found");
    }

    // Delete a comment by comment ID
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable String commentId) {
        // 404 not found if comment not found
        for (Post post : posts) {
            for (Comment comment : post.getComments()) {
                if (comment.getId().equals(commentId)) {
                    post.getComments().remove(comment);
                    return ResponseEntity.status(HttpStatus.OK).body("Comment has been deleted successfully");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment with id " + commentId + " not found");
    }
}




