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
 * The Class AllDispatchersManager.
 *
 * @author Pavlo Degtyaryev
 */

//@Slf4j
@UIScope
@SpringComponent
@Route(value = AppURL.DISPATCHERS_ALL, layout = MainView.class)
public class AllDispatchersManager extends AbstractSelectedUsersManager<AllDispatchersGridViewer>
	implements HasDynamicTitle {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1578164728569421737L;

    /**
     * Instantiates a new all dispatchers manager.
     *
     * @param allDispatchersGridViewer the all dispatchers grid viewer
     * @param userEditor               the user editor
     */
    @Autowired
    public AllDispatchersManager(AllDispatchersGridViewer allDispatchersGridViewer, UserEditor userEditor) {
	super(allDispatchersGridViewer, userEditor);
    }

    /**
     * Gets the page title.
     *
     * @return the page title
     */
    @Override
    public String getPageTitle() {
	return getTranslation(AppTitleMsg.APP_TITLE_USERS_DISPATCHERS, UI.getCurrent().getLocale());
    }
}
