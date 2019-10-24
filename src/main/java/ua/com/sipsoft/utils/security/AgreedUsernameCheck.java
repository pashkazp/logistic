package ua.com.sipsoft.utils.security;

public interface AgreedUsernameCheck {

    String usernameRegexp = "^([A-Za-z0-9]){1,32}$";

    default boolean agreedUsernameCheck(String username) {
	return (username != null) && (username.matches(usernameRegexp));
    }
}
