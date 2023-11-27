package com.server.ptitFood.config.dsrouting.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "client.datasource")
public class ClientConnection {
        private String name;
        private String script;
}
