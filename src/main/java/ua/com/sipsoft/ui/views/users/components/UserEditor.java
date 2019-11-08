package ua.com.sipsoft.ui.views.users.components;

import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.ui.commons.entityedit.AbstractBindedEntityEditor;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.messages.UserEntityCheckMsg;
import ua.com.sipsoft.utils.messages.UserEntityMsg;
import ua.com.sipsoft.utils.security.AgreedEmailCheck;
import ua.com.sipsoft.utils.security.AgreedPasswordCheck;
import ua.com.sipsoft.utils.security.AgreedUsernameCheck;

/**
 * The Class UserEditor.
 *
 * @author Pavlo Degtyaryev
 * @param <T> the generic type
 */

@Scope(value = "prototype")
@SpringComponent
@Slf4j
public class UserEditor<T extends User> extends AbstractBindedEntityEditor<T>
	implements AgreedPasswordCheck, AgreedEmailCheck, AgreedUsernameCheck {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2209381064166335239L;

    /** The roles presenter. */
    private final RolesPresenter rolesPresenter;

    /** The password encoder. */
    private final PasswordEncoder passwordEncoder;

    /** The id. */
    private final TextField id = new TextField();

    /** The username. */
    private final TextField username = new TextField();

    /** The first name. */
    private final TextField firstName = new TextField();

    /** The last name. */
    private final TextField lastName = new TextField();

    /** The patronymic. */
    private final TextField patronymic = new TextField();

    /** The password. */
    private final PasswordField password = new PasswordField();

    /** The confirm password. */
    private final PasswordField confirmPassword = new PasswordField();

    /** The email. */
    private final EmailField email = new EmailField();

    /** The enabled cb. */
    private final Checkbox enabledCb = new Checkbox();

    /** The verified cb. */
    private final Checkbox verifiedCb = new Checkbox();

    /**
     * Instantiates a new user editor.
     *
     * @param rolesPresenter  the roles presenter
     * @param passwordEncoder the password encoder
     */
    public UserEditor(RolesPresenter rolesPresenter, PasswordEncoder passwordEncoder) {
	log.debug("UserEditor constructor");

	this.rolesPresenter = rolesPresenter;
	this.passwordEncoder = passwordEncoder;

	id.setReadOnly(true);
	id.blur();
	id.setWidthFull();

	username.setValueChangeMode(ValueChangeMode.EAGER);
	username.setRequiredIndicatorVisible(true);
	username.setWidthFull();
	username.focus();

	firstName.setValueChangeMode(ValueChangeMode.EAGER);
	firstName.setWidthFull();

	lastName.setValueChangeMode(ValueChangeMode.EAGER);
	lastName.setWidthFull();

	patronymic.setValueChangeMode(ValueChangeMode.EAGER);
	patronymic.setWidthFull();

	password.setValueChangeMode(ValueChangeMode.EAGER);
	password.setClearButtonVisible(true);
	password.setWidthFull();

	confirmPassword.setValueChangeMode(ValueChangeMode.EAGER);
	confirmPassword.setClearButtonVisible(true);
	confirmPassword.setWidthFull();

	email.setValueChangeMode(ValueChangeMode.EAGER);
	email.setRequiredIndicatorVisible(true);
	email.setWidthFull();

	addFormItem(id, getTranslation(UserEntityMsg.USERID));
	addFormItem(username, getTranslation(UserEntityMsg.USERNAME));
	addFormItem(firstName, getTranslation(UserEntityMsg.FIRSNAME));
	addFormItem(lastName, getTranslation(UserEntityMsg.LASTNAME));
	addFormItem(patronymic, getTranslation(UserEntityMsg.PATRONYMIC));
	addFormItem(password, getTranslation(UserEntityMsg.PASSWORD));
	addFormItem(confirmPassword, getTranslation(UserEntityCheckMsg.CONFPASS));
	addFormItem(email, getTranslation(UserEntityMsg.EMAIL));
	addFormItem(verifiedCb, getTranslation(UserEntityMsg.VERIFIED));
	addFormItem(enabledCb, getTranslation(UserEntityMsg.ENABLED));

	addFormItem(rolesPresenter, getTranslation(UserEntityMsg.ROLES));

	setSizeUndefined();
	getStyle().set(Props.MARGIN, Props.EM_0_5);
	bindFields();

	setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep(Props.EM_06_25, 1));
    }

    /**
     * Bind fields.
     */
    private void bindFields() {
	log.debug("UserEditor bind fields");
	getBinder().forField(id).asRequired().withConverter(Long::valueOf, String::valueOf).bind(User::getId, null);
	getBinder().forField(username)
		.withValidator(username -> agreedUsernameCheck(username),
			getTranslation(UserEntityCheckMsg.USERNAME_CHR))
		.asRequired().bind(User::getUsername, User::setUsername);
	getBinder().forField(firstName)
		.withValidator(firstName -> firstName
			.length() <= 75, getTranslation(UserEntityCheckMsg.LONG_FIRSTNAME))
		.bind(User::getFirstName, User::setFirstName);
	getBinder().forField(lastName)
		.withValidator(lastName -> lastName
			.length() <= 75, getTranslation(UserEntityCheckMsg.LONG_SECONDNAME))
		.bind(User::getLastName, User::setLastName);
	getBinder().forField(patronymic)
		.withValidator(patronymic -> patronymic
			.length() <= 75, getTranslation(UserEntityCheckMsg.LONG_PATRONYMIC))
		.bind(User::getPatronymic, User::setPatronymic);

	getBinder().forField(password)
		.withValidator(
			password -> adreedPasswordCheck(password),
			getTranslation(UserEntityCheckMsg.PASS_CHR))
		.bind(user -> password.getEmptyValue(), (user, pass) -> {
		    if (!password.getEmptyValue().equals(pass)) {
			user.setPassword(passwordEncoder.encode(pass));
		    }
		});

	getBinder().forField(confirmPassword)
		.withValidator(pass -> (!pass.isEmpty() && pass.equals(password.getValue()))
			|| (password.getValue().isEmpty() && pass.isEmpty()),
			getTranslation(UserEntityCheckMsg.PASS_EQUAL))
		.bind(user -> password.getEmptyValue(), (user, pass) -> {
		});

	getBinder().forField(email).asRequired()
		.withValidator(email -> agreedEmailCheck(email), getTranslation(UserEntityCheckMsg.EMAIL_CHR))
		.bind(User::getEmail, User::setEmail);

	getBinder().forField(verifiedCb).bind(User::getVerified,
		(user, ena) -> user.setVerified(verifiedCb.getValue()));
	getBinder().forField(enabledCb).bind(User::getEnabled, (user, ena) -> user.setEnabled(enabledCb.getValue()));

	getBinder().forField(rolesPresenter).asRequired()
		.withValidator(roles -> (rolesPresenter.counter() > 0),
			getTranslation(UserEntityCheckMsg.ROLE_QTY))
		.bind(User::getRoles, (user, ena) -> user.setRoles(rolesPresenter.getValue()));
    }

    /**
     * Reset operation data.
     */
    public void resetOperationData() {
	getBinder().readBean(operationData);
    }

    /**
     * Inits the binder.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void initBinder() {
	setBinder(new Binder(User.class));
    }
}
