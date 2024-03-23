package com.volod.bojia.tg.property;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "bojia")
@RequiredArgsConstructor(onConstructor_ = {@ConstructorBinding})
@Data
public class BojiaApplicationProperties {
    private final String botToken;
}
