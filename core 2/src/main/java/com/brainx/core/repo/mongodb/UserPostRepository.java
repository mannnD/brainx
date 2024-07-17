package com.brainx.core.repo.mongodb;

import com.brainx.core.entity.UserPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostRepository extends MongoRepository<UserPost, String>, UserPostCustomRepository {
    @Query("{ 'userId': ?0}")
    UserPost findByUserId(String userId);//    @Query("{ $text: { $search: ?0 } }")
//    List<Post> searchByText(String keyword);
//    List<Post> findBy(TextCriteria criteria);
}
