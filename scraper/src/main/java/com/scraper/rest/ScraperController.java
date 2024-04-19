package com.scraper.rest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.scraper.ScraperService;
import com.scraper.dao.AccountDao;
import com.scraper.dao.ChannelDao;
import com.scraper.dao.PostDao;
import com.scraper.dao.SubscriptionDao;
import com.scraper.models.TgAccount;
import com.scraper.models.TgChannel;
import com.scraper.models.TgPost;
import com.scraper.models.TgSubscription;
import com.scraper.rest.services.getaccountposts.AccountPostsResponse;
import com.scraper.rest.services.subscribe.SubscribeRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobPersistenceException;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@CrossOrigin(allowedHeaders = "*")
@RestController
@RequestMapping("api")
@ParametersAreNonnullByDefault
public class ScraperController {

    private final ScraperService scraperService;
    private final AccountDao accountDao;
    private final ChannelDao channelDao;
    private final SubscriptionDao subscriptionDao;
    private final PostDao postDao;
    private final Scheduler scheduler;
//    private final JobsRegistrar registrar;

    public ScraperController(
            @Autowired ScraperService scraperService,
            @Autowired AccountDao accountDao,
            @Autowired ChannelDao channelDao,
            @Autowired SubscriptionDao subscriptionDao,
            @Autowired PostDao postDao,
            @Autowired Scheduler scheduler
    ) {
        this.scraperService = scraperService;
        this.accountDao = accountDao;
        this.channelDao = channelDao;
        this.subscriptionDao = subscriptionDao;
        this.postDao = postDao;
        this.scheduler = scheduler;
    }

    @PostMapping("subscribe")
    public ResponseEntity<Object> subscribe(
            @RequestBody SubscribeRequest subscribeRequest
    ) {
        TgAccount account = TgAccount.builder()
                .login(subscribeRequest.getAccountLogin())
                .build();
        account = accountDao.upsert(account);
        TgChannel channel = TgChannel.builder()
                .channelName(subscribeRequest.getChannelName())
                .build();
        channel = channelDao.upsert(channel);
        subscriptionDao.subscribe(account, channel);
        return ResponseEntity.ok().build();
    }

    @PostMapping("unsubscribe")
    public ResponseEntity<Object> unsubscribe(
            @RequestBody SubscribeRequest subscribeRequest
    ) {
        TgAccount account = TgAccount.builder()
                .login(subscribeRequest.getAccountLogin())
                .build();
        account = accountDao.findOne(Example.of(account))
                .orElse(null);
        TgChannel channel = TgChannel.builder()
                .channelName(subscribeRequest.getChannelName())
                .build();
        channel = channelDao.findOne(Example.of(channel))
                .orElse(null);
        if (account == null || channel == null) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }
        subscriptionDao.unsubscribe(account, channel);
        return ResponseEntity.ok().build();
    }

    @GetMapping("account-posts")
    public ResponseEntity<AccountPostsResponse> getAccountPosts(
            @RequestParam(name = "account_login", required = true) String accountLogin,
            @Nullable @RequestParam(name = "from_date", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate
    ) {
        // получить пользователя
        Optional<TgAccount> account = accountDao.findByLogin(accountLogin);
        if (account.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // получить подписки на каналы пользователя
        List<TgSubscription> subscriptions = subscriptionDao.findByAccountId(account.get().getId());

        LocalDateTime afterMoment = Optional.ofNullable(fromDate)
                .map(d -> LocalDateTime.ofEpochSecond(d.toInstant().toEpochMilli()/1000, 0, ZoneOffset.UTC))
                .orElse(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        // получить посты по каждому каналу
        List<TgPost> posts = subscriptions.stream()
                .map(TgSubscription::getChannelId)
                .map(id -> postDao.findByChannelIdAndMomentAfter(id, afterMoment))
                .flatMap(Collection::stream)
                .toList();

        List<Long> channelIds = subscriptions.stream()
                .map(TgSubscription::getChannelId)
                .toList();
        Map<Long, String> channels = channelDao.findAllById(channelIds).stream()
                .collect(Collectors.toMap(TgChannel::getId, TgChannel::getChannelName));

        Map<String, List<TgPost>> postsByChannelName = posts.stream()
                .collect(Collectors.groupingBy(p -> channels.get(p.getChannelId())));

        AccountPostsResponse result = AccountPostsResponse.builder()
                .accountLogin(account.get().getLogin())
                .posts(postsByChannelName)
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("scrap")
    public ResponseEntity<Object> scrap() {
        String jobName = "scrapJob";
        final JobKey jobKey = scheduler.getJobKeys(GroupMatcher.anyGroup()).stream()
                .filter(key -> jobName.equalsIgnoreCase(
                        StringUtils.substringAfterLast(String.format("%s.%s", key.getGroup(), key.getName()), ".")
                ))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("No job with name " + jobName));

        final JobDetail jobDetail = scheduler.getJobDetail(jobKey);

        final Trigger startNowTrigger = newTrigger().withIdentity(jobName)
                .forJob(jobDetail)
                .startNow()
                .build();

        try {
            scheduler.scheduleJob(startNowTrigger);
        } catch (JobPersistenceException jobPersistenceException) {
            log.info("Job {} is already running", jobName);
        }

        return ResponseEntity.ok().body("OK");
    }
}
