package com.example.demo;

import java.util.List;

public class Response {
    private Post post;
    private List<Post> posts;
    private String message;

    public Response() {
    }

    public Response(String message) {
        this.message = message;
    }

    public Response(Post post) {
        this.post = post;
    }

    public Response(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
