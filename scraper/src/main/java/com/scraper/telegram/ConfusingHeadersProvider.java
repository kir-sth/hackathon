package com.scraper.telegram;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class ConfusingHeadersProvider {

    private static final Random random = new Random();

    private static final List<String> userAgents = List.of("Mozilla/5.0 (Windows NT; rv:86.0) Gecko/20100101 Firefox/84.0",
            "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:86.0) Gecko/20100101 Firefox/86.0",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36",
            "Mozilla/5.0 (Windows NT) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36",
            "Safari/537.36",
            "Chrome/87.0.1323.182",
            "Chrome/78.0.624.506",
            "Chrome/51.0.2011.981");

    public static ConfusingHeaders provide() {
        return ConfusingHeaders.builder()
                .accept(nextAccept())
                .userAgent(nextUserAgent())
                .uniqueUserAgent(nextUniqueAgent())
                .build();
    }

    private static String nextUserAgent() {
        return userAgents.get(random.nextInt(userAgents.size()));
    }

    private static String nextUniqueAgent() {
        String userAgent = nextUserAgent();
        if (random.nextBoolean()) {
            return Stream.of(userAgent.split("\\s+"))
                    .map(s -> {
                        var lastDigitPos = s.length() - 1;
                        if (!s.isBlank() && Character.isDigit(s.charAt(lastDigitPos))) {
                            return s + random.nextInt(10);
                        }
                        return s;
                    }).collect(Collectors.joining(" "));
        }
        return userAgent;
    }

    private static String nextAccept() {
        return "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
    }
}
