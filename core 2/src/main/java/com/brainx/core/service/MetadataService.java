//package com.brainx.core.service;
//
//import com.brainx.core.entity.Post;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Service
//public class MetadataService {
//
//    public Post fetchMetadata(String url) throws IOException {
//        Document doc = Jsoup.connect(url).get();
//        String title = doc.title();
//        String description = "";
//        Element metaDescription = doc.select("meta[name=description]").first();
//        if (metaDescription != null) {
//            description = metaDescription.attr("content");
//        }
//        String thumbnail = "";
//        Element metaThumbnail = doc.select("meta[property=og:image]").first();
//        if (metaThumbnail != null) {
//            thumbnail = metaThumbnail.attr("content");
//        }
//
//        Post post = new Post();
//        post.setFileName(url);
//        post.setTitle(title);
//        post.setDescription(description);
//
//        return post;
//    }
//}
