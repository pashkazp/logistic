package ua.com.sipsoft.utils.security;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * The Interface AgreedRegistrationCheck.
 */
public interface AgreedRegistrationCheck {

    /** The password regexp. */
    String passwordRegexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,50})|(^$)";
    String usernameRegexp = "^([A-Za-z0-9]){1,32}$";

    /**
     * Adreed password regexp check.
     *
     * @param password the password
     * @return true, if successful
     */
    default boolean adreedPasswordCheck(String password) {
	return (password != null) && (password.matches(passwordRegexp));
    }

    /**
     * Agreed email regexp check.
     *
     * @param email the email
     * @return true, if successful
     */
    default boolean agreedEmailCheck(String email) {
	return (email != null) && (email.length() <= 100) && EmailValidator.getInstance().isValid(email);
    }

    default boolean agreedUsernameCheck(String username) {
	return (username != null) && (username.matches(usernameRegexp));
    }

}
