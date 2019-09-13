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
 * The Class AllManagersManager.
 *
 * @author Pavlo Degtyaryev
 */

//@Slf4j
@UIScope
@SpringComponent
@Route(value = AppURL.MANAGERS_ALL, layout = MainView.class)
public class AllManagersManager extends AbstractSelectedUsersManager<AllManagersGridViewer> implements HasDynamicTitle {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -259169370614225170L;

	/**
	 * Instantiates a new all managers manager.
	 *
	 * @param allManagersGridViewer the all managers grid viewer
	 * @param userEditor            the user editor
	 */
	@Autowired
	public AllManagersManager(AllManagersGridViewer allManagersGridViewer, UserEditor userEditor) {
		super(allManagersGridViewer, userEditor);
	}

	/**
	 * Gets the page title.
	 *
	 * @return the page title
	 */
	@Override
	public String getPageTitle() {
		return getTranslation(AppTitleMsg.APP_TITLE_USERS_MANAGERS, UI.getCurrent().getLocale());
	}
}
