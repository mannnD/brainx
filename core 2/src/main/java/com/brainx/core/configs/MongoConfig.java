package com.brainx.core.configs;

import com.brainx.core.entity.Post;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.brainx.core.repo.mongodb", // Adjust to your MongoDB repository package
        mongoTemplateRef = "mongoTemplate" // Use the appropriate MongoTemplate bean
)
public class MongoConfig {
    private final MongoTemplate mongoTemplate;
    @Autowired
    public MongoConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void createIndexes() {
        mongoTemplate.indexOps(Post.class).ensureIndex(new Index().on("title", Direction.ASC));
        mongoTemplate.indexOps(Post.class).ensureIndex(new Index().on("description", Direction.ASC));
        mongoTemplate.indexOps(Post.class).ensureIndex(new Index().on("tags", Direction.ASC));
    }
}
