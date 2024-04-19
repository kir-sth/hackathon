package com.scraper.jobs.settings;

import com.scraper.jobs.ScrapJob;
import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class ScrapJobConfig {
        private final SchedulerProperties schedulerProperties;

    @Bean
    JobDetail scrapJobDetail() {
        return JobBuilder
                .newJob(ScrapJob.class)
                .withIdentity("scrapJob", schedulerProperties.permanentJobsGroupName)
                .storeDurably()
                .requestRecovery(true)
                .build();
    }

    @Bean
    Trigger scrapJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(scrapJobDetail())
                .withIdentity("scrapJobTrigger", schedulerProperties.permanentJobsGroupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(schedulerProperties.scrapJobCron))
                .build();
    }
}
