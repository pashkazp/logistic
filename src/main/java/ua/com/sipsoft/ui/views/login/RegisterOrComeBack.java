package ua.com.sipsoft.ui.views.login;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.users.UsersService;
import ua.com.sipsoft.ui.commons.AppNotificator;
import ua.com.sipsoft.utils.AppURL;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.messages.AppNotifyMsg;
import ua.com.sipsoft.utils.messages.AppTitleMsg;
import ua.com.sipsoft.utils.messages.ButtonMsg;
import ua.com.sipsoft.utils.messages.LoginMsg;
import ua.com.sipsoft.utils.messages.UserEntityCheckMsg;
import ua.com.sipsoft.utils.messages.UserEntityMsg;
import ua.com.sipsoft.utils.security.AgreedEmailCheck;
import ua.com.sipsoft.utils.security.AgreedPasswordCheck;
import ua.com.sipsoft.utils.security.AgreedUsernameCheck;
import ua.com.sipsoft.utils.security.OnRegistrationCompleteEvent;
import ua.com.sipsoft.utils.security.OnRememberPasswordEvent;
import ua.com.sipsoft.utils.security.Role;

@UIScope
@SpringComponent
@Route(AppURL.LOGIN_REGISTRATION)
@Slf4j
public class RegisterOrComeBack extends VerticalLayout
	implements HasDynamicTitle, AfterNavigationObserver, AgreedPasswordCheck, AgreedEmailCheck,
	AgreedUsernameCheck {

    private static final long serialVersionUID = 8147366992440026453L;

    /** The user. */
    private User user = new User();

    /** The users service. */
    private UsersService usersService;

    private PasswordEncoder passwordEncoder;
    private ApplicationEventPublisher eventPublisher;

    /** The binder. */
    private Binder<User> binder = new Binder<>(User.class);

    /** The username. */
    private final TextField username = new TextField();

    /** The username or email. */
    private final TextField usernameOrEmail = new TextField();

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

    /** The register btn. */
    private Button regBtn = new Button(getTranslation(ButtonMsg.BTN_SIGN_UP), UIIcon.BTN_SIGN_UP.createIcon());

    /** The send btn. */
    private Button sendBtn = new Button(getTranslation(ButtonMsg.BTN_SEND_MAIL), UIIcon.MAIL.createIcon());

    /** The cancel btn. */
    private Button cancelRegBtn = new Button(getTranslation(ButtonMsg.BTN_CANCEL), UIIcon.BTN_CANCEL.createIcon());

    /** The cancel btn. */
    private Button cancelForgotBtn = new Button(getTranslation(ButtonMsg.BTN_CANCEL), UIIcon.BTN_CANCEL.createIcon());

    /** The buttons panel. */
    private HorizontalLayout regButtons = new HorizontalLayout(regBtn, cancelRegBtn);

    /** The buttons panel. */
    private HorizontalLayout forgotButtons = new HorizontalLayout(sendBtn, cancelForgotBtn);

    private Dialog dialog;
    private Component registerForm;
    private Component forgotForm;

    @Autowired
    RegisterOrComeBack(UsersService usersService, PasswordEncoder passwordEncoder,
	    ApplicationEventPublisher eventPublisher) {

	log.info("Create RegisterOrComeBack view");
	this.usersService = usersService;
	this.passwordEncoder = passwordEncoder;
	this.eventPublisher = eventPublisher;

	dialog = new Dialog();
	dialog.setWidth(Props.EM_28);
	dialog.setCloseOnOutsideClick(false);
	dialog.setCloseOnEsc(false);
	Tab tab1 = new Tab(getTranslation(LoginMsg.SIGNUP_TAB));
	Tab tab2 = new Tab(getTranslation(LoginMsg.FORGOT_TAB));
	Tabs tabs = new Tabs(tab1, tab2);
	tabs.setFlexGrowForEnclosedTabs(1);
	dialog.add(tabs);

	registerForm = getRegisterForm();
	forgotForm = getForgotForm();
	forgotForm.setVisible(false);
	username.focus();

	Map<Tab, Component> tabsToForms = new HashMap<>();
	tabsToForms.put(tab1, registerForm);
	tabsToForms.put(tab2, forgotForm);

	Set<Component> formsShown = Stream.of(registerForm)
		.collect(Collectors.toSet());

	tabs.addSelectedChangeListener(event -> {
	    formsShown.forEach(page -> page.setVisible(false));
	    formsShown.clear();
	    Component selectedForm = tabsToForms.get(tabs.getSelectedTab());
	    selectedForm.setVisible(true);
	    formsShown.add(selectedForm);
	});

	cancelRegBtn.addClickListener(e -> cancelOperation());
	cancelForgotBtn.addClickListener(e -> cancelOperation());
	regBtn.addClickListener(e -> performRegisterUser());
	sendBtn.addClickListener(e -> rememberPassword());

	dialog.add(registerForm);
	dialog.add(forgotForm);
	dialog.open();

    }

    private Component getRegisterForm() {
	log.debug("Create User Register form");
	FormLayout form = new FormLayout();
	VerticalLayout panel = new VerticalLayout();

	username.setValueChangeMode(ValueChangeMode.EAGER);
	username.setRequiredIndicatorVisible(true);
	username.setWidthFull();

	email.setValueChangeMode(ValueChangeMode.EAGER);
	email.setRequiredIndicatorVisible(true);
	email.setWidthFull();

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

	form.addFormItem(username, getTranslation(UserEntityMsg.USERNAME));
	form.addFormItem(email, getTranslation(UserEntityMsg.EMAIL));
	form.addFormItem(password, getTranslation(UserEntityMsg.PASSWORD));
	form.addFormItem(confirmPassword, getTranslation(UserEntityCheckMsg.CONFPASS));
	form.addFormItem(firstName, getTranslation(UserEntityMsg.FIRSNAME));
	form.addFormItem(lastName, getTranslation(UserEntityMsg.LASTNAME));
	form.addFormItem(patronymic, getTranslation(UserEntityMsg.PATRONYMIC));

	form.setSizeFull();
	form.setMinWidth(Props.EM_21);
	form.setMaxWidth(Props.EM_40);
	form.setWidth(Props.EM_30);
	form.getStyle().set(Props.MARGIN, Props.EM_0_5);

	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);

	regBtn.setWidthFull();
	cancelRegBtn.setWidthFull();

	regButtons.setSizeUndefined();
	regButtons.setDefaultVerticalComponentAlignment(Alignment.STRETCH);

	regButtons.setMargin(false);
	regButtons.setPadding(false);
	regButtons.setSpacing(true);

	regButtons.setFlexGrow(1, regBtn);
	regButtons.setFlexGrow(1, cancelRegBtn);

	form.add(panel, regButtons);

	form.setSizeFull();
	form.setWidth(Props.EM_34);
	form.setMaxWidth(getWidth());
	form.setMinWidth(getWidth());

	prepareRegisterForm();

	return form;
    }

    private Component getForgotForm() {
	log.debug("Create User Forgot form");
	FormLayout form = new FormLayout();
	VerticalLayout panel = new VerticalLayout();

	usernameOrEmail.setValueChangeMode(ValueChangeMode.EAGER);
	usernameOrEmail.setRequiredIndicatorVisible(true);
	usernameOrEmail.setWidthFull();

	form.addFormItem(usernameOrEmail, getTranslation(LoginMsg.USERNAME_OR_EMAIL));

	sendBtn.setWidthFull();
	cancelForgotBtn.setWidthFull();

	forgotButtons.setSizeUndefined();
	forgotButtons.setDefaultVerticalComponentAlignment(Alignment.STRETCH);

	forgotButtons.setMargin(false);
	forgotButtons.setPadding(false);
	forgotButtons.setSpacing(true);

	forgotButtons.setFlexGrow(1, sendBtn);
	forgotButtons.setFlexGrow(1, cancelForgotBtn);

	form.add(panel, forgotButtons);

	form.setSizeFull();
	form.setWidth(Props.EM_34);
	form.setMaxWidth(getWidth());
	form.setMinWidth(getWidth());

	prepareForgotForm();

	return form;
    }

    private void prepareRegisterForm() {
	binder.forField(username)
		.withValidator(username -> agreedUsernameCheck(username),
			getTranslation(UserEntityCheckMsg.USERNAME_CHR))
		.asRequired().bind(User::getUsername, User::setUsername);
	binder.forField(email).asRequired()
		.withValidator(email -> agreedEmailCheck(email), getTranslation(UserEntityCheckMsg.EMAIL_CHR))
		.bind(User::getEmail, User::setEmail);
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
	binder.readBean(user);

    }

    private void prepareForgotForm() {
	sendBtn.setEnabled(false);
	usernameOrEmail.setRequired(true);
	usernameOrEmail.setRequiredIndicatorVisible(true);
	usernameOrEmail.setMinLength(1);
	usernameOrEmail.setValueChangeMode(ValueChangeMode.EAGER);
	usernameOrEmail.addValueChangeListener(e -> {
	    usernameOrEmail.setInvalid(e.getValue().length() < 1);
	    sendBtn.setEnabled(e.getValue().length() > 0);
	});
	usernameOrEmail.setValue("");
	// usernameOrEmail.setInvalid(true);
	usernameOrEmail.setErrorMessage(getTranslation(UserEntityCheckMsg.SMALL_USERNAME));

    }

    private void cancelOperation() {
	dialog.close();
	UI.getCurrent().navigate(LoginView.class);
    }

    private void performRegisterUser() {
	if (!binder.writeBeanIfValid(user)) {
	    return;
	}
	if (usersService.fetchByEmail(user.getEmail()).isPresent()) {
	    AppNotificator.notify(5000, getTranslation(LoginMsg.SIGNUP_EMAIL_TAKEN));
	    return;
	}
	if (usersService.fetchByUsername(user.getUsername()).isPresent()) {
	    AppNotificator.notify(5000, getTranslation(LoginMsg.SIGNUP_USERNAME_TAKEN));
	    return;
	}
	user.getRoles().add(Role.ROLE_REGISTERED);
	Optional<User> registered = usersService.registerNewUser(user);
	if (!registered.isPresent()) {
	    AppNotificator.notify(5000, getTranslation(LoginMsg.SIGNUP_FAILED));
	    return;
	}
	eventPublisher.publishEvent(
		new OnRegistrationCompleteEvent(registered.get(), UI.getCurrent().getLocale(),
			AppURL.APP_URL));

	AppNotificator.notify(getTranslation(LoginMsg.SIGNUP_SUCCESS));
	dialog.close();
	UI.getCurrent().navigate(LoginView.class);

    }

    private void rememberPassword() {
	if (usernameOrEmail.isInvalid() || usernameOrEmail.isEmpty()) {
	    return;
	}
	User user;
	String remember = usernameOrEmail.getValue();

	if (agreedEmailCheck(remember)) {
	    user = usersService.fetchByEmail(remember).orElse(null);
	} else {
	    user = usersService.fetchByUsername(remember).orElse(null);
	}
	if (user != null) {
	    eventPublisher.publishEvent(
		    new OnRememberPasswordEvent(user, UI.getCurrent().getLocale(),
			    AppURL.APP_URL));
	}
	AppNotificator.notify(getTranslation(AppNotifyMsg.EMAIL_SENDING));
	dialog.close();
	UI.getCurrent().navigate(LoginView.class);
    }

    /** The logistic theme style. */
    @Value("${application.theme.style}")
    private String logisticThemeStyle;

    /**
     * On attach.
     *
     * @param attachEvent the attach event
     */
    @Override
    protected void onAttach(AttachEvent attachEvent) {
	super.onAttach(attachEvent);
	/**
	 * Using the @Theme Annotation to set the Dark Theme causes issues with shadows
	 * which will appear in the wrong color making them seemingly invisible. Instead
	 * do it the following way as long as the issue is not solved
	 * (https://github.com/vaadin/flow/issues/4765)
	 */
	getUI().get().getPage()
		.executeJavaScript("document.documentElement.setAttribute(\"theme\",\"" + logisticThemeStyle + "\")");
    }

    @Override
    public String getPageTitle() {
	return getTranslation(AppTitleMsg.APP_TITLE_SIGNUP, UI.getCurrent().getLocale());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
	if (!dialog.isOpened()) {
	    dialog.open();
	    user = new User();
	    // binder = new Binder<>(User.class);
	    binder.readBean(user);
	}
    }

}
