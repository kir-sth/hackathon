package com.scraper.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import com.scraper.models.TgAccount;
import com.scraper.models.TgChannel;
import com.scraper.models.TgSubscription;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ParametersAreNonnullByDefault
public interface SubscriptionDao extends JpaRepository<TgSubscription, Long> {

    default void subscribe(TgAccount account, TgChannel channel) {
        TgSubscription subscription = TgSubscription.builder()
                .accountId(account.getId())
                .channelId(channel.getId())
                .build();
        Optional<TgSubscription> subscriptionFromDb = this.findOne(Example.of(subscription));
        if (subscriptionFromDb.isEmpty()) {
            this.save(subscription);
        }
    }

    default void unsubscribe(TgAccount account, TgChannel channel) {
        this.findOne(Example.of(
                TgSubscription.builder()
                        .accountId(account.getId())
                        .channelId(channel.getId())
                        .build()
        )).ifPresent(this::delete);
    }

    List<TgSubscription> findByAccountId(Long accountId);
}
