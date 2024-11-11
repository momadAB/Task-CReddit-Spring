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
    public ResponseEntity<Response> getPosts(@RequestParam(required = false) String title) {
        if (title != null) {
            List<Post> queriedPosts = new ArrayList<>();
            for (Post post : posts) {
                if(post.getTitle().contains(title)) {
                    queriedPosts.add(post);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(new Response(queriedPosts));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Response(posts));
    }

    // Get post by id
    @GetMapping("/{id}")
    public ResponseEntity<Response> getPostById(@PathVariable String id) {
        for (Post post : posts) {
            if (post.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.OK).body(new Response(post));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Post with ID " + id + " not found"));
    }

    // Add a new post
    @PostMapping
    public ResponseEntity<Response> addPost(@RequestBody Post post) {
        // If field types are incorrect or values are missing, return 400
        if (post.getTitle() == null || post.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Post title and description are required"));
        }
        if (post.getTitle().isEmpty() || post.getDescription().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Post title and description are required"));
        }

        // Post ID is null, so it must be set manually
        if (post.getId() == null) {
            post.setId(java.util.UUID.randomUUID().toString());
        }

        posts.add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(post));
    }

    // Delete a post
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deletePost(@PathVariable String id) {
        for (Post post : posts) {
            if (post.getId().equals(id)) {
                posts.remove(post);
                return ResponseEntity.status(HttpStatus.OK).body(new Response("Post has been deleted successfully"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Post with id " + id + " not found"));
    }

    // Add a comment to a post by ID
    @PostMapping("/{id}/comments")
    public ResponseEntity<Response> addComment(@PathVariable String id, @RequestBody Comment comment) {
        // 400 bad request if comment is invalid/empty
        if (comment.getComment() == null || comment.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Comment cannot be empty"));
        }

        if (comment.getComment().isEmpty() || comment.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Comment cannot be empty"));
        }
        // 404 not found if post not found
        for (Post post : posts) {
            if (post.getId().equals(id)) {
                post.addComment(comment);
                return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Comment has been added successfully"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Post with id " + id + " not found"));
    }

    // Delete a comment by comment ID
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Response> deleteComment(@PathVariable String commentId) {
        // 404 not found if comment not found
        for (Post post : posts) {
            for (Comment comment : post.getComments()) {
                if (comment.getId().equals(commentId)) {
                    post.getComments().remove(comment);
                    return ResponseEntity.status(HttpStatus.OK).body(new Response("Comment has been deleted successfully"));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Comment with id " + commentId + " not found"));
    }
}




