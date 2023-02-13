package com.nhnacademy.booklay.batch.booklaybatch.scheduler;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * @Author : 양승아
 * @Date : 12/02/2023
 */
@EnableScheduling
@Component
@AllArgsConstructor
@Slf4j
public class Scheduler {
    private final JobLauncher jobLauncher;
    private final Job job;

    @Scheduled(cron = "0 0 12 1 1/1 ?")
    public void run() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
        JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
            .addDate("date", new Date())
            .toJobParameters();

        log.error("run-job");
        jobLauncher.run(job, jobParameters);
    }
}
