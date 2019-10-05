package ua.com.sipsoft.services.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.common.VerificationToken;
import ua.com.sipsoft.model.repository.user.VerificationTokenRepository;

/** The Constant log. */
@Slf4j
@Service
@Transactional
public class VerificationTokenServiceImpl implements VerificationTokenService {

    /** The dao. */
    @Autowired
    private VerificationTokenRepository dao;

    /**
     * Save verification token.
     *
     * @param token the token
     * @return the verification token
     */
    @Override
    public VerificationToken saveVerificationToken(VerificationToken token) {
	log.info("Save verification token \"{}\"", token);
	if (token == null) {
	    log.warn("Store verification token impossible. Missing some data. ");
	}
	try {
	    return dao.save(token);
	} catch (Exception e) {
	    log.warn(e.getMessage());
	}
	return null;
    }

    @Override
    public Optional<VerificationToken> fetchByToken(String token) {
	log.debug("Get Verification token by token: '{}'", token);
	if (token == null) {
	    log.debug("Gets Verification token by token is impossible. Token is null.");
	    return Optional.of(null);
	}
	try {
	    return dao.findByToken(token);
	} catch (Exception e) {
	    log.error("The Verification token by token is not received for a reason: {}", e.getMessage());
	    return Optional.of(null);
	}
    }

    @Override
    public void deleteByToken(String token) {
	log.debug("Delete Verification token by token: '{}'", token);
	if (token == null) {
	    log.debug("Delete Verification token by token is impossible. Token is null.");
	}
	try {
	    dao.deleteByToken(token);
	} catch (Exception e) {
	    log.error(" Verification token by token is not deleted for a reason: {}", e.getMessage());
	}
    }

}
