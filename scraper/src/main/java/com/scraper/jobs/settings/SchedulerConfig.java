package com.scraper.jobs.settings;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class SchedulerConfig {
    private final SchedulerProperties schedulerProperties;

    @Bean
    @SneakyThrows
    Scheduler scheduler(List<Trigger> triggers, List<JobDetail> jobDetails, SchedulerFactoryBean factory) {
        factory.setWaitForJobsToCompleteOnShutdown(true);
        var scheduler = factory.getScheduler();
        revalidateJobs(jobDetails, scheduler);
        rescheduleTriggers(triggers, scheduler);
        scheduler.start();
        return scheduler;
    }

    @SneakyThrows
    void rescheduleTriggers(List<Trigger> triggers, Scheduler scheduler) {
        triggers.forEach(
                trigger ->
                {
                    try {
                        if (!scheduler.checkExists(trigger.getKey())) {
                            scheduler.scheduleJob(trigger);
                        } else {
                            scheduler.rescheduleJob(trigger.getKey(), trigger);
                        }
                    } catch (SchedulerException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @SneakyThrows
    void revalidateJobs(List<JobDetail> jobDetails, Scheduler scheduler) {
        Set<JobKey> jobKeys = jobDetails.stream()
                .map(JobDetail::getKey)
                .collect(Collectors.toSet());
        scheduler.getJobKeys(GroupMatcher.jobGroupEquals(schedulerProperties.permanentJobsGroupName)).forEach(it -> {
            if (!jobKeys.contains(it)) {
                try {
                    scheduler.deleteJob(it);
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
