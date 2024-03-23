package com.volod.bojia.tg.configuration;

import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BojiaSpringBatchConfiguration {

    // https://github.com/spring-projects/spring-batch/issues/4519#issuecomment-1895372351
    // https://github.com/spring-projects/spring-batch/issues/4519#issuecomment-1860530016
    @Bean
    public static BeanDefinitionRegistryPostProcessor jobRegistryBeanPostProcessorRemover() {
        return registry -> registry.removeBeanDefinition("jobRegistryBeanPostProcessor");
    }

}
