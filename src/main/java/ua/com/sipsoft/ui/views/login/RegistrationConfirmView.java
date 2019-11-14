package ua.com.sipsoft.ui.views.login;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
import ua.com.sipsoft.utils.security.VerificationTokenType;

@UIScope
@SpringComponent
@Route(AppURL.REGISTRATION_CONFIRM)
@Slf4j
public class RegistrationConfirmView extends VerticalLayout
	implements HasDynamicTitle, HasUrlParameter<String>, AfterNavigationObserver {

    private static final long serialVersionUID = 7517743481611138193L;
    private User user;
    private VerificationToken vToken;

    VerificationTokenService tokenService;
    UsersService userService;
    /** The register btn. */
    private Button okBtn = new Button(getTranslation(ButtonMsg.BTN_OK), UIIcon.BTN_OK.createIcon());
    /** The buttons panel. */
    private HorizontalLayout regButtons = new HorizontalLayout(okBtn);

    private String token;

    private Dialog dialog;
    private VerticalLayout infoPanel;

    @Autowired
    RegistrationConfirmView(VerificationTokenService tokenService, UsersService userService) {
	this.tokenService = tokenService;
	this.userService = userService;

	log.info("Create RegisterOrComeBack view");
	dialog = new Dialog();
	dialog.setWidth(Props.EM_28);
	dialog.setCloseOnOutsideClick(false);
	dialog.setCloseOnEsc(false);

	infoPanel = getInfoPanel();
    }

    private VerticalLayout getInfoPanel() {
	VerticalLayout panel = new VerticalLayout();
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	panel.setSizeFull();

	okBtn.setWidthFull();

	okBtn.addClickListener(e -> {
	    dialog.close();
	    UI.getCurrent().navigate(LoginView.class);
	});
	regButtons.setSizeUndefined();
	regButtons.setDefaultVerticalComponentAlignment(Alignment.STRETCH);

	regButtons.setMargin(false);
	regButtons.setPadding(false);
	regButtons.setSpacing(true);

	regButtons.setFlexGrow(1, okBtn);
	panel.add(regButtons);
	return panel;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
	Span span;
	if (token != null) {
	    vToken = tokenService.fetchByToken(token).orElse(null);
	    if (vToken != null && (vToken.getTokenType() == VerificationTokenType.REGNEWUSER)) {
		if (!vToken.getUsed() && LocalDateTime.now().isBefore(vToken.getExpiryDate())) {
		    user = vToken.getUser();
		    user.setVerified(true);
		    userService.saveUser(user);
		    vToken.setUsed(true);
		    vToken.setUsedDate(LocalDateTime.now());
		    tokenService.saveVerificationToken(vToken);
		    span = new Span(String.format(
			    getTranslation(LoginMsg.REG_WELCOM),
			    !Strings.isNullOrEmpty(user.getFirstName()) ? user.getFirstName() : user.getUsername()));
		} else {
		    user = vToken.getUser();
		    span = new Span(
			    getTranslation(LoginMsg.TOKEN_EXPIRED));
		}
	    } else {
		span = new Span(getTranslation(LoginMsg.TOKEN_MISSED));
	    }
	} else {
	    span = new Span(getTranslation(LoginMsg.TOKEN_LACKED));
	}
	infoPanel.addComponentAsFirst(span);
	dialog.add(infoPanel);
	dialog.open();
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
