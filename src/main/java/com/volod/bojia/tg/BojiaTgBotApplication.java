package com.volod.bojia.tg;

import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		exclude = OpenAiAutoConfiguration.class
)
public class BojiaTgBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(BojiaTgBotApplication.class, args);
	}

}
