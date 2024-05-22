package com.volod.bojia.tg.service.letter.impl;

import com.volod.bojia.tg.domain.vacancy.Vacancy;
import com.volod.bojia.tg.entity.BojiaBotUser;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;
import org.springframework.ai.autoconfigure.retry.SpringAiRetryAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

@Disabled("OpenAI api-key required")
@Tag("it")
@SpringBootTest(
        classes = {
                OpenAiAutoConfiguration.class,
                RestClientAutoConfiguration.class,
                SpringAiRetryAutoConfiguration.class,
                CoverLetterServiceImpl.class
        },
        properties = {
                "spring.ai.openai.api-key=key-here"
        }
)
class CoverLetterServiceImplTest {

    @MockBean
    private BojiaExceptionHandlerService exceptionHandlerService;
    @Autowired
    private CoverLetterServiceImpl componentUnderTest;

    @Test
    void generateCoverLetterTest() {
        // Arrange
        var user = BojiaBotUser.testsHardcoded();
        var vacancy = Vacancy.testsHardcoded();

        // Act
        var coverLetter = this.componentUnderTest.generateCoverLetter(user, vacancy);
        System.out.println(coverLetter);

        // Assert
        verifyNoInteractions(this.exceptionHandlerService);
        assertThat(coverLetter.value()).isNotBlank();
    }

}