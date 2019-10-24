package ua.com.sipsoft.ui.views.users.components;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.users.UsersService;
import ua.com.sipsoft.ui.commons.AppNotificator;
import ua.com.sipsoft.ui.commons.ChangeHandler;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.messages.AppNotifyMsg;
import ua.com.sipsoft.utils.messages.ButtonMsg;
import ua.com.sipsoft.utils.messages.UserEntityCheckMsg;
import ua.com.sipsoft.utils.messages.UserEntityMsg;
import ua.com.sipsoft.utils.security.AgreedEmailCheck;
import ua.com.sipsoft.utils.security.AgreedPasswordCheck;
import ua.com.sipsoft.utils.security.AgreedUsernameCheck;

/**
 * The Class UserEditor.
 *
 * @author Pavlo Degtyaryev
 */

@Scope("prototype")
@SpringComponent
//@Tag("user-editor-form")

/** The Constant log. */
@Slf4j
public class UserEditor extends FormLayout implements AgreedPasswordCheck, AgreedEmailCheck, AgreedUsernameCheck {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2209381064166335239L;

    /** The users service. */
    private UsersService usersService;

    /** The roles presenter. */
    private RolesPresenter rolesPresenter;

    private PasswordEncoder passwordEncoder;

    /** The user. */
    private User user;

    /** The binder. */
    private final Binder<User> binder = new Binder<>(User.class);

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

    /** The save btn. */
    // @Getter
    private Button saveBtn = new Button(getTranslation(ButtonMsg.BTN_SAVE), UIIcon.BTN_PUT.createIcon());

    /** The reset btn. */
    // @Getter
    private Button resetBtn = new Button(getTranslation(ButtonMsg.BTN_REFRESH),
	    UIIcon.BTN_REFRESH.createIcon());

    /** The cancel btn. */
    // @Getter
    private Button cancelBtn = new Button(getTranslation(ButtonMsg.BTN_CANCEL),
	    UIIcon.BTN_CANCEL.createIcon());

    /** The buttons. */
    private HorizontalLayout buttons = new HorizontalLayout(saveBtn, resetBtn, cancelBtn);

    /**
     * Sets the change handler.
     *
     * @param changeHandler the new change handler
     */
    @Setter
    private ChangeHandler<User> changeHandler;

    /**
     * Instantiates a new user editor.
     *
     * @param usersService   the users service
     * @param rolesPresenter the roles presenter
     */
    @Autowired
    public UserEditor(UsersService usersService, RolesPresenter rolesPresenter, PasswordEncoder passwordEncoder) {
	log.debug("UserEditor constructor");

	this.usersService = usersService;
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

	setSizeFull();
	setMinWidth(Props.EM_21);
	setMaxWidth(Props.EM_40);
	setWidth(Props.EM_30);
	getStyle().set(Props.MARGIN, Props.EM_0_5);
	bindFields();

	saveBtn.addClickListener(e -> saveUser());
	resetBtn.addClickListener(e -> binder.readBean(user));
	cancelBtn.addClickListener(e -> this.setVisible(false));

	saveBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
	resetBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
	cancelBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
	saveBtn.addClassName(Props.SIZE_XS);
	resetBtn.addClassName(Props.SIZE_XS);
	cancelBtn.addClassName(Props.SIZE_XS);
	saveBtn.setWidthFull();
	resetBtn.setWidthFull();
	cancelBtn.setWidthFull();

	buttons.add(saveBtn, resetBtn, cancelBtn);
	buttons.setWidthFull();
	add(buttons);
	setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep(Props.EM_06_25, 1));
    }

    /**
     * Bind fields.
     */
    private void bindFields() {
	log.debug("UserEditor bind fields");
	binder.forField(id).asRequired().withConverter(Long::valueOf, String::valueOf).bind(User::getId, null);
	binder.forField(username)
		.withValidator(username -> agreedUsernameCheck(username),
			getTranslation(UserEntityCheckMsg.USERNAME_CHR))
		.asRequired().bind(User::getUsername, User::setUsername);
	binder.forField(firstName)
		.withValidator(firstName -> firstName
			.length() <= 75, getTranslation(UserEntityCheckMsg.LONG_FIRSTNAME))
		.bind(User::getFirstName, User::setFirstName);
	binder.forField(lastName)
		.withValidator(lastName -> lastName
			.length() <= 75, getTranslation(UserEntityCheckMsg.LONG_SECONDNAME))
		.bind(User::getLastName, User::setLastName);
	binder.forField(patronymic)
		.withValidator(patronymic -> patronymic
			.length() <= 75, getTranslation(UserEntityCheckMsg.LONG_PATRONYMIC))
		.bind(User::getPatronymic, User::setPatronymic);

	binder.forField(password)
		.withValidator(
			password -> adreedPasswordCheck(password),
			getTranslation(UserEntityCheckMsg.PASS_CHR))
		.bind(user -> password.getEmptyValue(), (user, pass) -> {
		    if (!password.getEmptyValue().equals(pass)) {
			user.setPassword(passwordEncoder.encode(pass));
		    }
		});

	binder.forField(confirmPassword)
		.withValidator(pass -> (!pass.isEmpty() && pass.equals(password.getValue()))
			|| (password.getValue().isEmpty() && pass.isEmpty()),
			getTranslation(UserEntityCheckMsg.PASS_EQUAL))
		.bind(user -> password.getEmptyValue(), (user, pass) -> {
		});

	binder.forField(email).asRequired()
		.withValidator(email -> agreedEmailCheck(email), getTranslation(UserEntityCheckMsg.EMAIL_CHR))
		.bind(User::getEmail, User::setEmail);

	binder.forField(verifiedCb).bind(User::getVerified, (user, ena) -> user.setVerified(verifiedCb.getValue()));
	binder.forField(enabledCb).bind(User::getEnabled, (user, ena) -> user.setEnabled(enabledCb.getValue()));

	binder.forField(rolesPresenter).asRequired()
		.withValidator(roles -> (rolesPresenter.counter() > 0),
			getTranslation(UserEntityCheckMsg.ROLE_QTY))
		.bind(User::getRoles, (user, ena) -> user.setRoles(rolesPresenter.getValue()));
    }

    /**
     * Save user.
     */
    private void saveUser() {
	try {
	    if (binder.writeBeanIfValid(user)) {
		user = usersService.saveUser(user);
		if (changeHandler != null) {
		    changeHandler.onChange(user);
		}
		AppNotificator.notify(getTranslation(AppNotifyMsg.USER_SAVED));
	    }
	} catch (ValidationException e) {
	    log.debug("User is not saved. Exception is thrown. " + e.getMessage());
	    AppNotificator.notify(5000, e.getMessage());
	}
    }

    /**
     * Edits the User.
     *
     * @param user the user
     */
    public void editUser(User user) {
	log.debug("UserEditor edit user: {}", user);
	if (user == null) {
	    setVisible(false);
	    return;
	}
	this.user = user;
	binder.readBean(user);
	username.focus();
    }
}
