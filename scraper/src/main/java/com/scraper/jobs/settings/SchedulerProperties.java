package com.scraper.jobs.settings;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.support.CronExpression;

@Data
@ConfigurationProperties(prefix = "scheduler")
@RequiredArgsConstructor
class SchedulerProperties {
    String permanentJobsGroupName = "com.scraper.jobs";
    String scrapJobCron = "0 */10 * * * ?";
    CronExpression scrapJobCron1 = CronExpression.parse(scrapJobCron);
}

