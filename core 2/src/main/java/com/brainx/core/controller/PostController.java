package com.brainx.core.controller;

import com.brainx.core.domain.PostWithMedia;
import com.brainx.core.entity.Post;
import com.brainx.core.entity.UserPost;
import com.brainx.core.service.GoogleCloudStorageService;
import com.brainx.core.service.PostService;
import com.google.cloud.storage.Blob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final GoogleCloudStorageService googleCloudStorageService;
    @Autowired
    public PostController(PostService postService, GoogleCloudStorageService googleCloudStorageService) {
        this.postService = postService;
        this.googleCloudStorageService = googleCloudStorageService;
    }
    @PostMapping("/{userId}")
    public ResponseEntity<UserPost> createPost(@PathVariable("userId") String userId,
                                           @RequestParam("file") MultipartFile file,
                                           @RequestParam("title") String title,
                                           @RequestParam("description") String description,
                                           @RequestParam("tags") List<String> tags) {
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setTags(tags);
        UserPost savedPost = postService.savePost(userId, post, file);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/search")
    public ResponseEntity<List<Post>> searchPostsForUser(@PathVariable("userId") String userId,@RequestParam String keyword) {
        List<Post> posts = postService.searchPosts(keyword);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        Blob blob = googleCloudStorageService.getFile(fileName);

        byte[] fileContent = blob.getContent();
        String contentType = blob.getContentType();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(fileContent.length);
        headers.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }
    @GetMapping("/view/{fileName}")
    public String viewFile(@PathVariable String fileName) {
        Blob blob = googleCloudStorageService.getFile(fileName);

        byte[] fileContent = blob.getContent();
        String base64Image = Base64.getEncoder().encodeToString(fileContent);
        String contentType = blob.getContentType();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(fileContent.length);

        return base64Image;
    }
    @GetMapping("/{userId}/{keyword}")
    public List<PostWithMedia> getPostWithMediaForUser(@PathVariable String keyword, @PathVariable String userId) {
        System.out.println(keyword);
        return postService.getPostsWithMedia(keyword, userId);
    }
    @GetMapping
    public List<Post> searchPosts(@RequestParam String userId,
                                  @RequestParam String keyword,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "title") String sortField,
                                  @RequestParam(defaultValue = "ASC") String sortDirection) {
        return postService.searchPosts(userId, keyword, page, size, sortField, sortDirection);
    }
}
