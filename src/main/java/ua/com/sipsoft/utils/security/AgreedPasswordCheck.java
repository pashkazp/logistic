package ua.com.sipsoft.utils.security;

/**
 * The Interface AgreedPasswordCheck.
 */
public interface AgreedPasswordCheck {

    /** The password regexp. */
    String passwordRegexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,50})|(^$)";

    /**
     * Adreed password regexp check.
     *
     * @param password the password
     * @return true, if successful
     */
    default boolean adreedPasswordCheck(String password) {
	return (password != null) && (password.matches(passwordRegexp));
    }

}
