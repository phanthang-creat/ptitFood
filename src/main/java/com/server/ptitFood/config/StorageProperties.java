package com.server.ptitFood.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties(value = "storage")
@Data
public class StorageProperties {
    private String location; //Luu file upload len server
}
