package com.brainx.core.service;

import com.brainx.core.domain.PostWithMedia;
import com.brainx.core.entity.Post;
import com.brainx.core.entity.UserPost;
import com.brainx.core.repo.mongodb.PostRepository;
import com.brainx.core.repo.mongodb.UserPostRepository;
import com.google.cloud.storage.Blob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final GoogleCloudStorageService googleCloudStorageService;
    private final UserPostRepository userPostRepository;


    @Autowired
    public PostService(PostRepository postRepository, GoogleCloudStorageService googleCloudStorageService, UserPostRepository userPostRepository) {
        this.postRepository = postRepository;
        this.googleCloudStorageService = googleCloudStorageService;
        this.userPostRepository = userPostRepository;
    }
    private String generateUniqueFileName(byte[] fileContent) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(fileContent);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate hash for file content", e);
        }
    }
    public UserPost savePost(String userId, Post post, MultipartFile file) {
         // Set the file URL in the post

        Optional<UserPost> optionalUserPost = userPostRepository.findById(userId);
        UserPost userPost;

        if (optionalUserPost.isPresent()) {
            userPost = optionalUserPost.get();
            List<Post> posts = userPost.getPosts();
            String fileName = getFileName(file);
            try {
                if(fileExists(fileName, posts)) {
                    fileName = googleCloudStorageService.uploadFile(file, fileName);
                }// Upload file to Google Cloud Storage
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file", e); // Handle upload errors
            }
            post.setFileName(fileName);
            posts.add(post); // Add the new post to the existing posts
            userPost.setPosts(posts);
        } else {
            String fileName = getFileName(file);
            try {
                googleCloudStorageService.uploadFile(file, fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            post.setFileName(fileName);
            userPost = new UserPost();
            userPost.setUserId(userId); // Set the user ID
            userPost.setPosts(List.of(post)); // Initialize the posts list with the new post
        }

        return userPostRepository.save(userPost); // Return the saved user post
    }
    private String getFileName(MultipartFile file) {
        byte[] fileContent = null;
        try {
            fileContent = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return generateUniqueFileName(fileContent);
    }

    private boolean fileExists(String fileName, List<Post> posts) {
        return posts.stream()
                .anyMatch(post -> post.getFileName().equals(fileName));
    }

    //    @Cacheable(value = "posts", key = "#keyword")
    public List<Post> searchPosts(String keyword) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(keyword);
        return postRepository.findBy(criteria);
    }
    public List<Post> searchPosts(String userId, String keyword, int page, int size, String sortField, String sortDirection) {
        System.out.println(userId);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        UserPost userPost = userPostRepository.findByUserId(userId);
        System.out.println(userPost);
        if (userPost == null) {
            return List.of(); // Return an empty list if the user has no posts
        }

        return userPost.getPosts().stream()
                .filter(post -> post.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                        post.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        post.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(keyword.toLowerCase())))
                .sorted((p1, p2) -> {
                    if (sort.isSorted()) {
                        if (sort.getOrderFor(sortField).getDirection() == Sort.Direction.ASC) {
                            return p1.getTitle().compareToIgnoreCase(p2.getTitle());
                        } else {
                            return p2.getTitle().compareToIgnoreCase(p1.getTitle());
                        }
                    }
                    return 0;
                })
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<PostWithMedia> getPostsWithMedia(String keyword, String userId) {
        UserPost userPost = userPostRepository.findByUserId(userId);
        return userPost.getPosts().stream()
                .filter(post -> post.getDescription().equalsIgnoreCase(keyword) ||
                        post.getTitle().equalsIgnoreCase(keyword) ||
                        post.getTags().contains(keyword))
                .map(post -> {
                    Blob blob = googleCloudStorageService.getFile(post.getFileName());
                    byte[] mediaContent = blob.getContent();
                    return new PostWithMedia(post.getTitle(), post.getDescription(), mediaContent);
                })
                .collect(Collectors.toList());
    }
}
