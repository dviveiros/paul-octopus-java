package com.ciandt.paul;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "general")
public class GeneralConfig {

    private String debug;

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public boolean isDebugEnabled() {
        return "true".equals(this.getDebug());
    }
}
