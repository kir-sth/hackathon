package scraper.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import scraper.models.TgChannel;
import scraper.rest.services.ScraperService;
import scraper.rest.services.getchannelposts.ChannelPostsRequest;
import scraper.rest.services.getchannelposts.ChannelPostsResponse;
import scraper.rest.services.getuserchannels.UserChannelsRequest;
import scraper.rest.services.getuserchannels.UserChannelsResponse;
import scraper.rest.services.subscribe.SubscribeRequest;

@Slf4j
@CrossOrigin(allowedHeaders = "*")
@RestController
@RequestMapping("api")
public class ScraperController {

    private final ScraperService scraperService;

    @Autowired
    public ScraperController(
            @Autowired ScraperService scraperService
    ) {
        this.scraperService = scraperService;
    }

    @PostMapping("subscribe")
    public ResponseEntity<Object> subscribe(
            @RequestBody SubscribeRequest subscribeRequest
    ) {
        return new ResponseEntity<>(new Object(), HttpStatus.OK);
    }

    @PostMapping("unsubscribe")
    public ResponseEntity<Object> unsubscribe(
            @RequestBody SubscribeRequest subscribeRequest
    ) {
        return new ResponseEntity<>(new Object(), HttpStatus.OK);
    }

    @GetMapping("user-channel")
    public ResponseEntity<UserChannelsResponse> getUserChannels(
            @RequestParam(name = "user_id", required = false) String userId,
            @RequestParam(required = false) String username
    ) {
        UserChannelsRequest userChannelsRequest = UserChannelsRequest.builder()
                .userId(userId)
                .username(username)
                .build();
        UserChannelsResponse userChannelsResponse = scraperService.getUserChannels(userChannelsRequest);
        return new ResponseEntity<>(userChannelsResponse, HttpStatus.OK);
    }

    @GetMapping("channel-posts")
    public ResponseEntity<ChannelPostsResponse> getChannelPosts(
            @RequestParam(name = "channel_name", required = false) String channelName
    ) {
        ChannelPostsRequest channelPostsRequest = ChannelPostsRequest.builder()
                .channel(TgChannel.builder()
                        .channelName(channelName)
                        .build())
                .build();
        ChannelPostsResponse channelPostsResponse = scraperService.getChannelPostsResponse(channelPostsRequest);
        if (channelPostsResponse.getChannel() == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(channelPostsResponse, HttpStatus.OK);
    }
}
