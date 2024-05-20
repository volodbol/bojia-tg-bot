package com.volod.bojia.tg;

import com.volod.bojia.tg.property.BojiaApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        BojiaApplicationProperties.class
})
public class BojiaTgBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BojiaTgBotApplication.class, args);
    }

}
