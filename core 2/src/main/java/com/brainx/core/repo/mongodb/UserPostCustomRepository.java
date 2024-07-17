package com.brainx.core.repo.mongodb;

import com.brainx.core.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserPostCustomRepository {
    List<Post> searchUserPosts(String userId, String keywords);
}
