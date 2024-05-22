package com.volod.bojia.tg.service.letter.impl;

import com.volod.bojia.tg.domain.letter.VacancyCoverLetter;
import com.volod.bojia.tg.domain.vacancy.Vacancy;
import com.volod.bojia.tg.entity.BojiaBotUser;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import com.volod.bojia.tg.service.letter.CoverLetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import static org.springframework.ai.openai.api.OpenAiApi.ChatModel.GPT_3_5_TURBO_0125;

@Service
@RequiredArgsConstructor
public class CoverLetterServiceImpl implements CoverLetterService {

    private static final PromptTemplate PROMPT_TEMPLATE = new PromptTemplate("""
            "{description}"
            Generate a small cover letter to this vacancy description above as an senior HR specialist who saw a lot of nice cover
            letters. To generate a cover letter, use this information, which was provided by a person who is interested in a vacancy:
            "{prompt}"
            """
    );

    // Services
    private final BojiaExceptionHandlerService exceptionHandlerService;
    // Client
    private final OpenAiChatClient chatClient;

    @Override
    public VacancyCoverLetter generateVacancyCoverLetter(BojiaBotUser user, Vacancy vacancy) {
        try {
            PROMPT_TEMPLATE.add("description", vacancy.description());
            PROMPT_TEMPLATE.add("prompt", user.getPrompt());
            var response = this.chatClient.call(
                    new Prompt(
                            PROMPT_TEMPLATE.createMessage(),
                            OpenAiChatOptions.builder()
                                    .withModel(GPT_3_5_TURBO_0125.getValue())
                                    .build()
                    )
            );
            return new VacancyCoverLetter(
                    user,
                    vacancy,
                    response.getResult().getOutput().getContent()
            );
        } catch (RuntimeException ex) {
            this.exceptionHandlerService.publishException(ex);
            return new VacancyCoverLetter(
                    user,
                    vacancy,
                    "Error generating cover letter, contact development team"
            );
        }
    }

}
