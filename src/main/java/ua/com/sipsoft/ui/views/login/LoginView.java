package ua.com.sipsoft.ui.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import ua.com.sipsoft.utils.AppURL;
import ua.com.sipsoft.utils.messages.AppTitleMsg;
import ua.com.sipsoft.utils.messages.LoginMsg;

/**
 * Logistic Login Form.
 *
 * @author Pavlo Degtyaryev
 */
@UIScope
@SpringComponent
@Route(AppURL.LOGIN_URL)
public class LoginView extends VerticalLayout
	implements HasDynamicTitle, LocaleChangeObserver, AfterNavigationObserver {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1714117238446328956L;

    /** The login. */
    private LoginOverlay login = new LoginOverlay();

    /**
     * Gets the page title.
     *
     * @return the page title
     */
    @Override
    public String getPageTitle() {
	return getTranslation(AppTitleMsg.APP_TITLE_LOGIN, UI.getCurrent().getLocale());
    }

    /**
     * Instantiates a new login view.
     */
    public LoginView() {
	login.setI18n(createTranslatedI18N());
	login.setForgotPasswordButtonVisible(true);
	login.setAction("login");
	login.addForgotPasswordListener(e -> extracted());
	login.setOpened(true);
	getElement().appendChild(login.getElement());
    }

    private void extracted() {
	// getElement().removeChild(login.getElement());
	login.setOpened(false);
	getUI().ifPresent(ui -> ui.navigate(AppURL.LOGIN_REGISTRATION));
    }

    /**
     * Creates the translated I 18 N.
     *
     * @return the login I 18 n
     */
    private LoginI18n createTranslatedI18N() {
	LoginI18n i18n = LoginI18n.createDefault();
	i18n.setHeader(new LoginI18n.Header());
	i18n.setForm(new LoginI18n.Form());

	i18n.getHeader().setTitle(getTranslation(LoginMsg.MY_APP_NAME));
	i18n.getHeader().setDescription(getTranslation(LoginMsg.HEADER_DESCRIPTION));
	i18n.getForm().setSubmit(getTranslation(LoginMsg.SIGNIN));
	i18n.getForm().setTitle(getTranslation(LoginMsg.LOGIN_TITLE));
	i18n.getForm().setUsername(getTranslation(LoginMsg.USERNAME));
	i18n.getForm().setPassword(getTranslation(LoginMsg.PASSWORD));
	i18n.getForm().setForgotPassword(getTranslation(LoginMsg.FORGOT_SIGN));
	i18n.getErrorMessage().setTitle(getTranslation(LoginMsg.LOGIN_ERROR_TITLE));
	i18n.getErrorMessage().setMessage(getTranslation(LoginMsg.LOGIN_ERROR));
	i18n.setAdditionalInformation(getTranslation(LoginMsg.LOGIN_INFO));
	return i18n;
    }

    /**
     * Locale change.
     *
     * @param event the event
     */
    @Override
    public void localeChange(LocaleChangeEvent event) {
	login.setI18n(createTranslatedI18N());
    }

    /**
     * After navigation.
     *
     * @param event the event
     */
    @Override
    public void afterNavigation(AfterNavigationEvent event) {
	if (!login.isOpened()) {
	    login.setOpened(true);
	}
	login.setError(
		event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }

}