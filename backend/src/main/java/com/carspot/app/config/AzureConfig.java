package com.carspot.app.config;

import com.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {

    @Value("${azure.storage.connection.string}")
    public String connectionString;

    @Value("${azure.storage.container.name}")
    public String containerName;

    @Bean
    public BlobServiceClient blobServiceClient() {
        System.out.println("blobServiceClient");
        return new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
    }

    @Bean
    public BlobContainerClient blobClient() {
        System.out.println("blobClient");
        return blobServiceClient().getBlobContainerClient(containerName);
    }
}
