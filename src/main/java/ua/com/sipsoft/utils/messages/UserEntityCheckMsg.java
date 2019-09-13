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

	public static final String SMALL_USERNAME = "entity.user.check.username.short";
	public static final String LONG_USERNAME = "entity.user.check.username.big";
	public static final String LONG_FIRSTNAME = "entity.user.check.firstname.big";
	public static final String LONG_SECONDNAME = "entity.user.check.secondname.big";
	public static final String LONG_PATRONYMIC = "entity.user.check.patronymic.big";
	public static final String LONG_PASSWORD = "entity.user.check.password.big";
	public static final String LONG_EMAIL = "entity.user.check.email.big";

	/**
	 * Instantiates a new user entity check msg.
	 */
	private UserEntityCheckMsg() {
	}
}
