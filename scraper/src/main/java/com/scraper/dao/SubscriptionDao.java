package com.scraper.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import com.scraper.models.TgAccount;
import com.scraper.models.TgChannel;
import com.scraper.models.TgSubscription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ParametersAreNonnullByDefault
public interface SubscriptionDao extends JpaRepository<TgSubscription, Long> {

    default SubscriptionStatus subscribe(TgAccount account, TgChannel channel) {
        TgSubscription subscription = TgSubscription.builder()
                .accountId(account.getId())
                .channelId(channel.getId())
                .build();
        Optional<TgSubscription> subscriptionFromDb = this.findOne(Example.of(subscription));
        if (subscriptionFromDb.isEmpty()) {
            this.save(subscription);
            return SubscriptionStatus.SUCCESSFUL_SUBSCRIBED;
        }
        return SubscriptionStatus.ALREADY_BEEN_SUBSCRIBED;
    }

    default SubscriptionStatus unsubscribe(TgAccount account, TgChannel channel) {
        Optional<TgSubscription> subscription = this.findOne(Example.of(
                TgSubscription.builder()
                        .accountId(account.getId())
                        .channelId(channel.getId())
                        .build()
        ));
        if (subscription.isPresent()) {
            this.delete(subscription.get());
            return SubscriptionStatus.SUCCESSFUL_UNSUBSCRIBED;
        }
        return SubscriptionStatus.ALREADY_BEEN_UNSUBSCRIBED;
    }

    List<TgSubscription> findByAccountId(Long accountId);

    enum SubscriptionStatus {
        UNDEFINED,
        ALREADY_BEEN_SUBSCRIBED,
        SUCCESSFUL_SUBSCRIBED,
        ALREADY_BEEN_UNSUBSCRIBED,
        SUCCESSFUL_UNSUBSCRIBED;

        public SubscriptionStatusObject toObject() {
            return new SubscriptionStatusObject(this);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubscriptionStatusObject {
        SubscriptionStatus status;
    }
}
