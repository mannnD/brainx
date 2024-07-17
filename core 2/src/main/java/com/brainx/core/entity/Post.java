package com.brainx.core.entity;

//import jakarta.persistence.ElementCollection;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Document(collection = "posts")
@Data

public class Post {
    @Id
    private String id;
    private String fileName;
    @TextIndexed
    private String title;
    @TextIndexed
    private String description;
    @TextIndexed
    private List<String> tags; // Tags should be indexed as text as well
    private List<String> mediaFiles;
}
