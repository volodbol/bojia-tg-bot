package com.volod.bojia.tg.cron;

import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoverLetterJobCron {

    private final JobLauncher jobLauncher;
    private final Job coverLettersJob;
    private final BojiaExceptionHandlerService exceptionHandlerService;

    @Scheduled(cron = "${bojia.cover-letter-job-cron}")
    public void execute() {
        try {
            this.jobLauncher.run(this.coverLettersJob, new JobParameters());
        } catch (Exception ex) {
            this.exceptionHandlerService.publishException(ex);
        }
    }

}
