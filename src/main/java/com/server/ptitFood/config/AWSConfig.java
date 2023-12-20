package com.server.ptitFood.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {


    public void createAWSKMSBean() {
        AWSKMS kmsClient = AWSKMSClientBuilder.standard()
//                .withCredentials()
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
    }
}
