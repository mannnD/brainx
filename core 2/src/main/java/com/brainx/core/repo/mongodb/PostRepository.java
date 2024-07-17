package com.brainx.core.repo.mongodb;

import com.brainx.core.entity.Post;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findBy(TextCriteria criteria);
}
