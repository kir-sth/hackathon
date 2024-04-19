package com.scraper.dao;

import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import com.scraper.models.TgAccount;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ParametersAreNonnullByDefault
public interface AccountDao extends JpaRepository<TgAccount, Long> {
    default TgAccount upsert(TgAccount accountReq) {
        TgAccount account = Optional.of(accountReq.getLogin())
                .map(l -> this.findOne(Example.of(accountReq)))
                .get()
                .orElse(accountReq);
        return this.saveAndFlush(account);
    }

    Optional<TgAccount> findByLogin(String login);
}
