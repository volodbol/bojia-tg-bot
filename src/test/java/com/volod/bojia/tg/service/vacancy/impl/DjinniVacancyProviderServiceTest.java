package com.volod.bojia.tg.service.vacancy.impl;

import com.volod.bojia.tg.configuration.BojiaApplicationConfiguration;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

@Tag("it")
@SpringBootTest(classes = {
        BojiaApplicationConfiguration.class,
        DjinniVacancyProviderService.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DjinniVacancyProviderServiceTest {

    @MockBean
    private BojiaExceptionHandlerService exceptionHandlerService;
    @Autowired
    private DjinniVacancyProviderService componentUnderTest;

    @Test
    void getLastVacanciesTest() {
        // Arrange
        var searchKeywords = List.of("middle", "java");
        var from = Instant.parse("2024-05-05T10:15:30.00Z");

        // Act
        var actual = this.componentUnderTest.getLastVacancies(searchKeywords, from);

        // Assert
        verifyNoInteractions(this.exceptionHandlerService);
        assertThat(actual.values()).isNotEmpty();
        assertThat(actual.values()).allSatisfy(vacancy -> {
            assertThat(vacancy.company()).isNotBlank();
            assertThat(vacancy.title()).isNotBlank();
            assertThat(vacancy.shortDetails()).isNotBlank();
            assertThat(vacancy.description()).isNotBlank();
            assertThat(vacancy.url()).isNotBlank();
            assertThat(vacancy.published()).isAfter(Instant.parse("2024-05-05T10:15:30.00Z"));
        });
    }

    @Test
    void getNumberOfVacanciesTest() {
        // Arrange
        var searchKeywords = List.of("java");

        // Act
        var actual = this.componentUnderTest.getNumberOfVacancies(searchKeywords);

        // Assert
        verifyNoInteractions(this.exceptionHandlerService);
        assertThat(actual).isPositive();
    }

}