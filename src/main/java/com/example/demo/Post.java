package com.example.demo;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

import java.util.ArrayList;
import java.util.List;

public class Post {
    @Id
    @GeneratedValue
    private String id;

    private String title;
    private String description;
    private List<Comment> comments = new ArrayList<>();

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        // Comment ID is null, so it must be set manually
        if (comment.getId() == null) {
            comment.setId(java.util.UUID.randomUUID().toString());
        }
        comments.add(comment);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
