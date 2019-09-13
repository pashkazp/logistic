package ua.com.sipsoft.ui.views.users;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import ua.com.sipsoft.ui.MainView;
import ua.com.sipsoft.ui.views.users.components.UserEditor;
import ua.com.sipsoft.ui.views.users.prototype.AbstractSelectedUsersManager;
import ua.com.sipsoft.utils.AppURL;
import ua.com.sipsoft.utils.messages.AppTitleMsg;

/**
 * The Class AllRegisteredManager.
 *
 * @author Pavlo Degtyaryev
 */

//@Slf4j
@UIScope
@SpringComponent
@Route(value = AppURL.REGISTERED_ALL, layout = MainView.class)
public class AllRegisteredManager extends AbstractSelectedUsersManager<AllRegisteredGridViewer>
		implements HasDynamicTitle {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5183086821739075631L;

	/**
	 * Instantiates a new all registered manager.
	 *
	 * @param allRegisteredGridViewer the all registered grid viewer
	 * @param userEditor              the user editor
	 */
	@Autowired
	public AllRegisteredManager(AllRegisteredGridViewer allRegisteredGridViewer, UserEditor userEditor) {
		super(allRegisteredGridViewer, userEditor);
	}

	/**
	 * Gets the page title.
	 *
	 * @return the page title
	 */
	@Override
	public String getPageTitle() {
		return getTranslation(AppTitleMsg.APP_TITLE_USERS_REGISTERED, UI.getCurrent().getLocale());
	}
}
