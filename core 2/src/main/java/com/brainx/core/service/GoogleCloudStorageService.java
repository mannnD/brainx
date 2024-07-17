package com.brainx.core.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class GoogleCloudStorageService {

    @Autowired
    private Storage storage;

    private String bucketName="brainxbucket"; // Replace with your GCS bucket name

    public GoogleCloudStorageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }
    public Blob getFile(String fileName) {
        System.out.println("filename :: " + fileName);
        BlobId blobId = BlobId.of(bucketName, fileName);
        Blob blob = storage.get(blobId);

        if (blob != null) {
            return blob;
        } else {
            throw new RuntimeException("File not found: " + fileName);
        }
    }

    public URL generateSignedUrl(String fileName) {
        BlobId blobId = BlobId.of(bucketName, fileName);

        URL signedUrl = storage.signUrl(
                storage.get(blobId).toBuilder().build(),
                15, TimeUnit.MINUTES,
                Storage.SignUrlOption.withV4Signature()
        );

        return signedUrl;
    }

    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        // Upload file to GCS
        storage.create(blobInfo, file.getBytes());

        // Return the public URL of the uploaded file
        return fileName;
    }


}
