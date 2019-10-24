package ua.com.sipsoft.ui.views.login;

import java.time.LocalDateTime;

import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.common.VerificationToken;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.security.VerificationTokenService;
import ua.com.sipsoft.services.users.UsersService;
import ua.com.sipsoft.utils.AppURL;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.messages.AppTitleMsg;
import ua.com.sipsoft.utils.messages.ButtonMsg;
import ua.com.sipsoft.utils.messages.LoginMsg;
import ua.com.sipsoft.utils.messages.UserEntityCheckMsg;
import ua.com.sipsoft.utils.messages.UserEntityMsg;
import ua.com.sipsoft.utils.security.AgreedPasswordCheck;
import ua.com.sipsoft.utils.security.VerificationTokenType;

@UIScope
@SpringComponent
@Route(AppURL.REGISTRATION_FORGOT)
@Slf4j
public class RememberPasswordView extends VerticalLayout
	implements HasDynamicTitle, HasUrlParameter<String>, AfterNavigationObserver, AgreedPasswordCheck {

    private static final long serialVersionUID = -5532342441179556241L;

    private User user;

    private VerificationToken vToken;

    private PasswordEncoder passwordEncoder;
    private VerificationTokenService tokenService;
    private UsersService userService;

    /** The binder. */
    private Binder<User> binder = new Binder<>(User.class);

    /** The username. */
    private final TextField username = new TextField();

    /** The password. */
    private final PasswordField password = new PasswordField();

    /** The confirm password. */
    private final PasswordField confirmPassword = new PasswordField();

    /** The register btn. */
    private Button okBtn = new Button(getTranslation(ButtonMsg.BTN_OK), UIIcon.BTN_OK.createIcon());

    private Button cancelBtn = new Button(getTranslation(ButtonMsg.BTN_CANCEL), UIIcon.BTN_CANCEL.createIcon());

    /** The buttons panel. */
    private HorizontalLayout regButtons = new HorizontalLayout(okBtn, cancelBtn);

    private String token;

    private Dialog dialog;

    private Component infoPanel;

    @Autowired
    RememberPasswordView(VerificationTokenService tokenService, UsersService userService,
	    PasswordEncoder passwordEncoder) {
	this.tokenService = tokenService;
	this.userService = userService;
	this.passwordEncoder = passwordEncoder;

	log.info("Create RememberPassword view");
	dialog = new Dialog();
	dialog.setWidth(Props.EM_28);
	dialog.setCloseOnOutsideClick(false);
	dialog.setCloseOnEsc(false);

	dialog.add(getInfoPanel());
    }

    private Component getInfoPanel() {

	FormLayout form = new FormLayout();

	VerticalLayout panel = new VerticalLayout();

	username.setValueChangeMode(ValueChangeMode.EAGER);
	username.setRequiredIndicatorVisible(true);
	username.setWidthFull();

	password.setValueChangeMode(ValueChangeMode.EAGER);
	password.setClearButtonVisible(true);
	password.setWidthFull();

	confirmPassword.setValueChangeMode(ValueChangeMode.EAGER);
	confirmPassword.setClearButtonVisible(true);
	confirmPassword.setWidthFull();

	form.addFormItem(username, getTranslation(UserEntityMsg.USERNAME));
	form.addFormItem(password, getTranslation(UserEntityMsg.PASSWORD));
	form.addFormItem(confirmPassword, getTranslation(UserEntityCheckMsg.CONFPASS));

	form.setSizeFull();
	form.setMinWidth(Props.EM_21);
	form.setMaxWidth(Props.EM_40);
	form.setWidth(Props.EM_30);
	form.getStyle().set(Props.MARGIN, Props.EM_0_5);

	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	panel.setSizeFull();

	okBtn.setWidthFull();
	cancelBtn.setWidthFull();

	okBtn.addClickListener(e -> {
	    if (!binder.writeBeanIfValid(user)) {
		return;
	    }
	    dialog.close();
	    user.setVerified(true);
	    userService.saveUser(user);
	    vToken.setUsed(true);
	    vToken.setUsedDate(LocalDateTime.now());
	    tokenService.saveVerificationToken(vToken);
	    UI.getCurrent().navigate(LoginView.class);
	});
	cancelBtn.addClickListener(e -> {
	    dialog.close();
	    UI.getCurrent().navigate(LoginView.class);
	});
	regButtons.setSizeUndefined();
	regButtons.setDefaultVerticalComponentAlignment(Alignment.STRETCH);

	regButtons.setMargin(false);
	regButtons.setPadding(false);
	regButtons.setSpacing(true);

	regButtons.setFlexGrow(1, okBtn);
	regButtons.setFlexGrow(1, cancelBtn);
	panel.add(regButtons);

	form.add(panel);

	form.setSizeFull();
	form.setWidth(Props.EM_34);
	form.setMaxWidth(getWidth());
	form.setMinWidth(getWidth());

	binder.forField(username)
		.bind(User::getUsername, null);
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

	return form;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
	Span span;
	if (token != null) {
	    vToken = tokenService.fetchByToken(token).orElse(null);
	    if (vToken != null && (vToken.getTokenType() == VerificationTokenType.FORGOTPASS)) {
		if (!vToken.getUsed() && LocalDateTime.now().isBefore(vToken.getExpiryDate())) {
		    user = vToken.getUser();
		    binder.readBean(user);
		    dialog.open();
		    return;
		} else {
		    span = new Span(getTranslation(LoginMsg.TOKEN_EXPIRED));
		}
	    } else {
		span = new Span(getTranslation(LoginMsg.TOKEN_MISSED));
	    }
	} else {
	    span = new Span(getTranslation(LoginMsg.TOKEN_LACKED));
	}

	ConfirmDialog
		.createWarning()
		.withCaption(getTranslation(LoginMsg.PASSWORD_RESET_WRONG))
		.withMessage(span)
		.withCancelButton(() -> {
		    UI.getCurrent().navigate(LoginView.class);
		},
			ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
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
	return getTranslation(AppTitleMsg.APP_TITLE_REGCONFIRM, UI.getCurrent().getLocale());
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
	this.token = event
		.getLocation()
		.getQueryParameters()
		.getParameters()
		.get("token")
		.stream()
		.findFirst()
		.orElse(null);
    }

}
