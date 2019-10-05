package ua.com.sipsoft.services.security;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.sipsoft.model.entity.common.VerificationToken;

/**
 * The Interface VerificationTokenService.
 */
@Service
public interface VerificationTokenService {

    /**
     * Save verification token.
     *
     * @param token the token
     * @return the verification token
     */
    VerificationToken saveVerificationToken(VerificationToken token);

    public Optional<VerificationToken> fetchByToken(String token);

    void deleteByToken(String token);

}
