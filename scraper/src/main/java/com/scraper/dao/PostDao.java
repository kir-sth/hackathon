package com.scraper.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.scraper.models.TgPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

;

@Repository
@ParametersAreNonnullByDefault
public interface PostDao extends JpaRepository<TgPost, Long> {

    @Query(value = "SELECT * FROM post WHERE channelId = :channelId AND moment > :moment", nativeQuery = true)
    List<TgPost> findByChannelIdAndMomentAfter(Long channelId, LocalDateTime moment);
}
