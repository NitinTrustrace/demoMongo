package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;



@Data
@AllArgsConstructor
@NoArgsConstructor

@Builder
@Document(collection = "subreddit" )
public class Subreddit {

    @Id
    private String id;
    @NotBlank(message = "Community name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @DBRef
    private List<Post> posts;
    private Instant createdDate;
    @DBRef
    private User user;
}