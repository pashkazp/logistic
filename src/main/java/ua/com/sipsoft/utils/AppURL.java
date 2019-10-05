package ua.com.sipsoft.utils;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Class that represents main URL's of Logistic Application.
 *
 * @author Pavlo Degtyaryev
 */
@Component
@ConfigurationProperties("application") // prefix app, find app.* values
@Slf4j
public class AppURL {
    public static final String LOGIN_URL = "login";
    public static final String LOGIN_PROCESSING_URL = "loginprocessing";
    public static final String LOGIN_FAILURE_URL = "loginfail";
    public static final String LOGOUT_SUCCESS_URL = "loginsuccess";
    public static final String ACCESS_DENIED_URL = "accessdenied";
    public static final String LOGOUT_URL = "logout";
    public static final String LOGIN_REGISTRATION = "registration";
    public static final String REGISTRATION_CONFIRM = "registration/confirm";
    public static final String REGISTRATION_FORGOT = "registration/forgot";

    public static final String HOME_URL = "home";
    public static final String REQUESTS_ALL = "requests";

    public static final String USERS_ALL = "allusers";
    public static final String ADMINS_ALL = "alladmins";
    public static final String DISPATCHERS_ALL = "alldispatchers";
    public static final String PRODUCTOPERS_ALL = "allproductopers";
    public static final String MANAGERS_ALL = "allmanagers";
    public static final String COURIERS_ALL = "allcouriers";
    public static final String CLIENTS_ALL = "allclients";
    public static final String REGISTERED_ALL = "allregistered";
    public static final String FACILITIES_ALL = "allfacilities";
    public static final String DRAFT_SHEETS = "draftsheet";
    public static final String ISSUED = "issued";
    public static final String ARCHIVE = "archive";

    public static String SITE_ADDRESS;

    public static String APP_URL;

    /**
     * Instantiates a new app URL.
     */
    private AppURL() {
	Properties prop = new Properties();
	try {
	    InputStream resourceAsStream = AppURL.class.getClassLoader().getResourceAsStream("application.properties");
	    prop.load(resourceAsStream);
	} catch (Exception e) {
	    log.error("Unable to load property file \"application.properties\"");
	}
	SITE_ADDRESS = prop.getProperty("logistic.site.address");
	APP_URL = prop.getProperty("logistic.app.url");
    } // denied class create;

}
