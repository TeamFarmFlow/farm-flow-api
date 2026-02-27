package com.example.app.auth.presentation.cookie;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cookie.refresh-token")
public record RefreshCookieProperties(
        String name,
        String path,
        String sameSite,
        boolean secure) {
}