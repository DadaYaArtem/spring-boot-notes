package com.groupone.mail.token;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {
    private final VerificationTokenRepository tokenRepository;

    @Scheduled(fixedDelay = 60000) // runs every minute
    public void cleanupExpiredEntities() {
        Date now = new Date();
        List<VerificationToken> expiredEntities = tokenRepository.findByExpiryDateBefore(now);
        tokenRepository.deleteAll(expiredEntities);
    }
}
