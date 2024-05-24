package com.volod.bojia.tg.job;

import com.volod.bojia.tg.domain.letter.VacanciesCoverLetters;
import com.volod.bojia.tg.entity.BojiaBotUserSearch;
import com.volod.bojia.tg.job.processor.BojiaBotUserSearchProcessor;
import com.volod.bojia.tg.job.processor.VacanciesCoverLettersWriter;
import com.volod.bojia.tg.service.bot.BojiaBotService;
import com.volod.bojia.tg.service.letter.CoverLetterService;
import com.volod.bojia.tg.service.search.BojiaBotUserSearchService;
import com.volod.bojia.tg.service.vacancy.VacancyProvidersService;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CoverLetterJobConfiguration extends DefaultBatchConfiguration {

    @NotNull
    @Override
    protected TaskExecutor getTaskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    Job coverLettersJob(
            JobRepository jobRepository,
            Step step1
    ) {
        return new JobBuilder("coverLettersJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    Step step1(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager,
            JpaPagingItemReader<BojiaBotUserSearch> botUserSearchReader,
            BojiaBotUserSearchProcessor botUserSearchProcessor,
            VacanciesCoverLettersWriter vacanciesCoverLettersWriter
    ) {
        return new StepBuilder("step1", jobRepository)
                .<BojiaBotUserSearch, VacanciesCoverLetters>chunk(10, platformTransactionManager)
                .reader(botUserSearchReader)
                .processor(botUserSearchProcessor)
                .writer(vacanciesCoverLettersWriter)
                .allowStartIfComplete(true)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    JpaPagingItemReader<BojiaBotUserSearch> botUserSearchReader(
            EntityManagerFactory entityManagerFactory
    ) {
        return new JpaPagingItemReaderBuilder<BojiaBotUserSearch>()
                .name("botUserSearchReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select s from BojiaBotUserSearch s left join fetch s.user")
                .pageSize(10)
                .build();
    }

    @Bean
    BojiaBotUserSearchProcessor botUserSearchProcessor(
            VacancyProvidersService vacancyProvidersService,
            CoverLetterService coverLetterService
    ) {
        return new BojiaBotUserSearchProcessor(
                vacancyProvidersService,
                coverLetterService
        );
    }

    @Bean
    VacanciesCoverLettersWriter vacanciesCoverLettersWriter(
            BojiaBotUserSearchService botUserSearchService,
            BojiaBotService botService
    ) {
        return new VacanciesCoverLettersWriter(
                botUserSearchService,
                botService
        );
    }

}
