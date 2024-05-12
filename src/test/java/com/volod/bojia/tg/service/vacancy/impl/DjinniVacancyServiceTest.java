package com.volod.bojia.tg.service.vacancy.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("it")
@SpringBootTest(classes = {
        DjinniVacancyService.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DjinniVacancyServiceTest {

    private final DjinniVacancyService componentUnderTest;

    @Test
    void getVacanciesTest() {
        // Arrange
        var searchKeywords = List.of("middle", "java");
        var from = Instant.parse("2024-05-05T10:15:30.00Z");

        // Act
        var actual = this.componentUnderTest.getVacancies(searchKeywords, from);

        // Assert
        assertThat(actual.values()).isNotEmpty();
    }

    @Test
    void getNumberOfVacanciesTest() {
        // Arrange
        var searchKeywords = List.of("java");

        // Act
        var actual = this.componentUnderTest.getNumberOfVacancies(searchKeywords);

        // Assert
        assertThat(actual).isNotZero();
    }

}