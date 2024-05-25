package com.volod.bojia.tg.domain.bot;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MessageMarkdownV2Test {

    private static Stream<Arguments> addTextTest() {
        return Stream.of(
                Arguments.of(
                        "To generate cover letter we have to know something about you.",
                        "To generate cover letter we have to know something about you\\."
                )
        );
    }

    private static Stream<Arguments> addInlineCodeTest() {
        return Stream.of(
                Arguments.of(
                        "To generate cover letter we have to know something about you.",
                        "`To generate cover letter we have to know something about you\\.`"
                )
        );
    }

    private static Stream<Arguments> addBoldTest() {
        return Stream.of(
                Arguments.of(
                        "To generate cover letter we have to know something about you.",
                        "*To generate cover letter we have to know something about you\\.*"
                )
        );
    }

    private static Stream<Arguments> addItalicTest() {
        return Stream.of(
                Arguments.of(
                        "To generate cover letter we have to know something about you.",
                        "_To generate cover letter we have to know something about you\\._"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("addTextTest")
    void addTextTest(String text, String expected) {
        // Arrange
        var builder = MessageMarkdownV2.builder().chatId(1).text(text);

        // Act
        var actual = builder.build();

        // Assert
        assertThat(actual.getValue()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("addInlineCodeTest")
    void addInlineCodeTest(String text, String expected) {
        // Arrange
        var builder = MessageMarkdownV2.builder().chatId(1).inlineCode(text);

        // Act
        var actual = builder.build();

        // Assert
        assertThat(actual.getValue()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("addBoldTest")
    void addBoldTest(String text, String expected) {
        // Arrange
        var builder = MessageMarkdownV2.builder().chatId(1).bold(text);

        // Act
        var actual = builder.build();

        // Assert
        assertThat(actual.getValue()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("addItalicTest")
    void addItalicTest(String text, String expected) {
        // Arrange
        var builder = MessageMarkdownV2.builder().chatId(1).italic(text);

        // Act
        var actual = builder.build();

        // Assert
        assertThat(actual.getValue()).isEqualTo(expected);
    }

}