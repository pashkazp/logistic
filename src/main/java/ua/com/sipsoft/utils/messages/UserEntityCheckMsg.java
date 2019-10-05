package ua.com.sipsoft.utils.messages;

import ua.com.sipsoft.model.entity.user.User;

/**
 * Set of standard messages for check data of entity {@link User}
 *
 * @author Pavlo Degtyaryev
 */
public class UserEntityCheckMsg {
    public static final String CONFPASS = "entity.user.check.confirmpassword";
    public static final String PASS_CHR = "entity.user.check.password.characters";
    public static final String PASS_EQUAL = "entity.user.check.password.equal";
    public static final String EMAIL_CHR = "entity.user.check.email.characters";
    public static final String ROLE_QTY = "entity.user.check.role.qty";
    public static final String USERNAME_CHR = "entity.user.check.username.characters";

    public static final String SMALL_USERNAME = "entity.user.check.username.short";
    public static final String LONG_FIRSTNAME = "entity.user.check.firstname.long";
    public static final String LONG_SECONDNAME = "entity.user.check.secondname.long";
    public static final String LONG_PATRONYMIC = "entity.user.check.patronymic.long";

    /**
     * Instantiates a new user entity check msg.
     */
    private UserEntityCheckMsg() {
    }
}
