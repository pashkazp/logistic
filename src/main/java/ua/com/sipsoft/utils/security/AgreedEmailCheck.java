package ua.com.sipsoft.utils.security;

import org.apache.commons.validator.routines.EmailValidator;

public interface AgreedEmailCheck {

    /**
     * @param email the email
     * @return true, if successful
     */
    default boolean agreedEmailCheck(String email) {
	return (email != null) && (email.length() <= 100) && EmailValidator.getInstance().isValid(email);
    }


}