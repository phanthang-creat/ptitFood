package com.server.ptitFood.config.reCaptcha;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class CaptchaSettings {

    private String site;
    private String secret;

    //reCAPTCHA V3
    private String siteV3;
    private String secretV3;
    private float threshold;

    public CaptchaSettings() {
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setSiteV3(String siteV3) {
        this.siteV3 = siteV3;
    }

    public void setSecretV3(String secretV3) {
        this.secretV3 = secretV3;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }
}