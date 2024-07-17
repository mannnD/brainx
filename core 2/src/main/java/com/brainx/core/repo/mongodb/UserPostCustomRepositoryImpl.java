package com.brainx.core.repo.mongodb;

import com.brainx.core.entity.Post;
import com.brainx.core.entity.UserPost;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserPostCustomRepositoryImpl implements UserPostCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Post> searchUserPosts(String userId, String keywords) {
        // Construct the text search query
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId)
                .andOperator(Criteria.where("posts").elemMatch(Criteria.where("$text").is(new Document("$search", keywords)))));

        List<UserPost> userPosts = mongoTemplate.find(query, UserPost.class);

        List<Post> posts = new ArrayList<>();
        for (UserPost userPost : userPosts) {
            for (Post post : userPost.getPosts()) {
                if (post.getTitle().contains(keywords) || post.getDescription().contains(keywords) || post.getTags().stream().anyMatch(tag -> tag.contains(keywords))) {
                    posts.add(post);
                }
            }
        }

        return posts;
    }
}
