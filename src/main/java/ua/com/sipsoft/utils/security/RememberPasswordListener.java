
package ua.com.sipsoft.utils.security;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.common.VerificationToken;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.users.UsersService;
import ua.com.sipsoft.ui.commons.AppNotificator;
import ua.com.sipsoft.utils.AppURL;
import ua.com.sipsoft.utils.I18N.VaadinI18NProvider;
import ua.com.sipsoft.utils.messages.LoginMsg;

/**
 * The listener interface for receiving registration events. The class that is
 * interested in processing a registration event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addRegistrationListener<code> method. When the registration
 * event occurs, that object's appropriate method is invoked.
 *
 * @see RegistrationEvent
 */
@Component
@Slf4j
public class RememberPasswordListener implements
	ApplicationListener<OnRememberPasswordEvent> {

    /** The User userService. */
    @Autowired
    private UsersService userService;

    /** The mail sender. */
    @Autowired
    private JavaMailSender mailSender;

    /** The I18N Provider */
    @Autowired
    private VaadinI18NProvider i18n;

    /**
     * On application event.
     *
     * @param event the event
     */
    @Override
    public void onApplicationEvent(OnRememberPasswordEvent event) {
	this.confirmRegistration(event);
    }

    /** The from string. */
    @Value("${spring.mail.username}")
    private String fromString;

    /**
     * Confirm registration.
     *
     * @param event the event
     */
    private void confirmRegistration(OnRememberPasswordEvent event) {
	log.info("Create Remember Password Token and Send confirmation email.");
	SimpleMailMessage email = new SimpleMailMessage();
	User user = event.getUser();
	String token = UUID.randomUUID().toString();

	VerificationToken vToken = userService.createVerificationToken(user, token, VerificationTokenType.FORGOTPASS);
	if (vToken == null) {
	    log.warn("Verification Token was not created. Confirmation email was not sended");
	    return;
	}

	String recipientAddress = user.getEmail();

	String subject = i18n.getTranslation(LoginMsg.REMEMBER_PASS_SUBJ, event.getLocale());

	String confirmationUrl = AppURL.SITE_ADDRESS + event.getAppUrl() + "/" + AppURL.REGISTRATION_FORGOT + "?token="
		+ token;

	String message = i18n.getTranslation(LoginMsg.REMEMBER_PASS_MSG_HELLO, event.getLocale())
		+ " \r\n\r\n"
		+ i18n.getTranslation(LoginMsg.REMEMBER_PASS_MSG_BODY1, event.getLocale())
		+ " \r\n\r\n"
		+ confirmationUrl
		+ " \r\n\r\n"
		+ i18n.getTranslation(LoginMsg.REMEMBER_PASS_MSG_BODY2, event.getLocale());

	message = String.format(message, user.getUsername(), confirmationUrl);

	email.setTo(recipientAddress);
	email.setSubject(subject);
	email.setText(message);
	email.setFrom(fromString);
	mailSender.send(email);

	AppNotificator.notify(i18n.getTranslation(LoginMsg.REMEMBER_PASS_MSG_SENDED, event.getLocale()));
    }
}
