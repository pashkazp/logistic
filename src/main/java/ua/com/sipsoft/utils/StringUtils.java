package ua.com.sipsoft.utils;

/**
 * Standard application component properties.
 *
 * @author Pavlo Degtyaryev
 */
public enum StringUtils {
    MENU_USER("Users"),
    MENU_ADMIN("Admins"),

    PROP_0_1EM("0.1em"),
    PROP_0_2EM("0.2em"),
    PROP_0_5EM("0.5em"),
    PROP_0_85EM("0.85em"),
    PROP_1EM("1em"),
    PROP_AUTO("auto"),
    PROP_PADDING("padding"),
    PROP_MARGIN("margin"),
    PROP_MARGIN_LEFT("margin-left"),
    PROP_MARGIN_RIGHT("margin-right");

    /** The string. */
    private final String string;

    /**
     * Instantiates a new string utils.
     *
     * @param string the string
     */
    private StringUtils(String string) {
	this.string = string;
    }

    /**
     * Gets the string.
     *
     * @return the string
     */
    public String getString() {
	return string;
    }
}
