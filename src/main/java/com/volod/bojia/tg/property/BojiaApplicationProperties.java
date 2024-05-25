package com.volod.bojia.tg.property;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.List;

@ConfigurationProperties(prefix = "bojia")
@RequiredArgsConstructor(onConstructor_ = {@ConstructorBinding})
@Data
public class BojiaApplicationProperties {
    private final String botToken;
    private final List<String> adminChatIds;
    private final String coverLetterJobCron;
}
