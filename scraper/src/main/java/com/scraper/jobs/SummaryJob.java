package com.scraper.jobs;

import javax.annotation.ParametersAreNonnullByDefault;

import com.scraper.ScraperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@DisallowConcurrentExecution
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class SummaryJob extends QuartzJobBean {

    private final ScraperService scraperService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Scraping started");
        scraperService.scrapAll();
        log.info("Scraping finished");
    }
}
