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
 * The Class AllClientsManager.
 *
 * @author Pavlo Degtyaryev
 */

//@Slf4j
@UIScope
@SpringComponent
@Route(value = AppURL.CLIENTS_ALL, layout = MainView.class)
public class AllClientsManager extends AbstractSelectedUsersManager<AllClientsGridViewer> implements HasDynamicTitle {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3717224936589845560L;

	/**
	 * Instantiates a new all clients manager.
	 *
	 * @param allClientsGridViewer the all clients grid viewer
	 * @param userEditor           the user editor
	 */
	@Autowired
	public AllClientsManager(AllClientsGridViewer allClientsGridViewer, UserEditor userEditor) {
		super(allClientsGridViewer, userEditor);
	}

	/**
	 * Gets the page title.
	 *
	 * @return the page title
	 */
	@Override
	public String getPageTitle() {
		return getTranslation(AppTitleMsg.APP_TITLE_USERS_CLIENTS, UI.getCurrent().getLocale());
	}
}
