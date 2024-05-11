package com.volod.bojia.tg.service.vacancy.impl;

import com.volod.bojia.tg.configuration.BojiaApplicationConfiguration;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("it")
@SpringBootTest(classes = {
        BojiaApplicationConfiguration.class,
        DjinniVacancyService.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DjinniVacancyServiceTest {

    private final DjinniVacancyService componentUnderTest;

    @Test
    void getNumberOfVacanciesTest() {
        // Act
        var actual = this.componentUnderTest.getNumberOfVacancies(List.of("java"));

        // Assert
        assertThat(actual).isNotZero();
    }

}