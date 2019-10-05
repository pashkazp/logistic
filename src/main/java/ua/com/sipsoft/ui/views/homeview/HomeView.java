package ua.com.sipsoft.ui.views.homeview;

import java.util.Locale;

import org.springframework.context.ApplicationContext;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.ui.MainView;
import ua.com.sipsoft.utils.AppURL;
import ua.com.sipsoft.utils.messages.AppTitleMsg;
import ua.com.sipsoft.utils.security.Role;
import ua.com.sipsoft.utils.security.SecurityUtils;

/**
 * The Class HomeView.
 *
 * @author Pavlo Degtyaryev
 */

/** The Constant log. */
@Slf4j
@UIScope
@SpringComponent
@Route(value = AppURL.HOME_URL, layout = MainView.class)
public class HomeView extends VerticalLayout implements HasDynamicTitle {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4942667214278985187L;

    /**
     * Instantiates a new home view.
     *
     * @param applicationContext the application context
     */
    public HomeView(ApplicationContext applicationContext) {
	log.info("Construct HomeView");
	Component homeViewPresenter = new VerticalLayout(new Paragraph("NOTHING THERE"));

	if (SecurityUtils.getUserRoles().contains(Role.ROLE_REGISTERED)) {
	    homeViewPresenter = applicationContext.getBean(HomeViewPresenterRegistered.class);
	    log.info("Prepare HomeView information section for Registered Users");
	}

	if (SecurityUtils.getUserRoles().contains(Role.ROLE_ADMIN)) {
	    homeViewPresenter = applicationContext.getBean(HomeViewPresenterAdmin.class);
	    log.info("Prepare HomeView information section for Admin Users");
	}
	add(homeViewPresenter);
	setAlignItems(Alignment.STRETCH);
	setFlexGrow(1, homeViewPresenter);
	setSizeFull();
    }

    /**
     * Gets the page title.
     *
     * @return the page title
     */
    @Override
    public String getPageTitle() {
	UI current = UI.getCurrent();
	Locale locale = current.getLocale();
	return getTranslation(AppTitleMsg.APP_TITLE_HOME, locale);
    }
}
