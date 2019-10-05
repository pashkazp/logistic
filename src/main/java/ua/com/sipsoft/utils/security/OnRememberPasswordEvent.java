package ua.com.sipsoft.utils.security;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.user.User;

@Getter
@Setter
@Slf4j
public class OnRememberPasswordEvent extends ApplicationEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8908814679946292298L;

    /** The app url. */
    private String appUrl;

    /** The locale. */
    private Locale locale;

    /** The user. */
    private User user;

    /**
     * Instantiates a new on registration complete event.
     *
     * @param user   the user
     * @param locale the locale
     * @param appUrl the app url
     */
    public OnRememberPasswordEvent(
	    User user, Locale locale, String appUrl) {
	super(user);
	log.info("Create completition registration event for Locale \"{}\", AppURL \"{}\", new User \"{}\"",
		locale, appUrl, user);
	this.user = user;
	this.locale = locale;
	this.appUrl = appUrl;
    }

}
