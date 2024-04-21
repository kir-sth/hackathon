package com.scraper.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import com.scraper.models.TgChannel;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@ParametersAreNonnullByDefault
public interface ChannelDao extends JpaRepository<TgChannel, Long> {

    default TgChannel upsert(TgChannel channelReq) {
        TgChannel channel = Optional.of(channelReq.getChannelName())
                .map(l -> this.findOne(Example.of(channelReq)))
                .get()
                .orElse(channelReq);
        return this.saveAndFlush(channel);
    }

    @Query(
            value = "SELECT c.* FROM channel as c WHERE id in (select distinct channelId from subscription)",
            nativeQuery = true
    )
    List<TgChannel> findWithSubscribers();
}
