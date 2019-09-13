package ua.com.sipsoft.ui.views.login;

import org.springframework.beans.factory.annotation.Value;

import com.vaadin.flow.component.AttachEvent;
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
		login.getElement().setAttribute("no-forgot-password", true);
		login.setAction("login");
		login.setOpened(true);
		getElement().appendChild(login.getElement());
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
		// i18n.getHeader().setDescription("");
		i18n.getForm().setSubmit(getTranslation(LoginMsg.LOGIN));
		i18n.getForm().setTitle(getTranslation(LoginMsg.LOGIN));
		i18n.getForm().setUsername(getTranslation(LoginMsg.USERNAME));
		i18n.getForm().setPassword(getTranslation(LoginMsg.PASSWORD));
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
		login.setError(
				event.getLocation().getQueryParameters().getParameters().containsKey("error"));
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
}