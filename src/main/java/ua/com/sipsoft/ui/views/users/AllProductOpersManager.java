package ua.com.sipsoft.ui.views.users;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import ua.com.sipsoft.ui.MainView;
import ua.com.sipsoft.ui.views.users.components.UserEditor;
import ua.com.sipsoft.ui.views.users.components.UsersGridViewer;
import ua.com.sipsoft.ui.views.users.prototype.AbstractSelectedUsersManager;
import ua.com.sipsoft.utils.AppURL;
import ua.com.sipsoft.utils.messages.AppTitleMsg;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Class AllProductOpersManager.
 *
 * @author Pavlo Degtyaryev
 */

//@Slf4j
@UIScope
@SpringComponent
@Route(value = AppURL.PRODUCTOPERS_ALL, layout = MainView.class)
public class AllProductOpersManager extends AbstractSelectedUsersManager<UsersGridViewer>
	implements HasDynamicTitle {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6710377991849177825L;

    /**
     * Instantiates a new all productOpers manager.
     *
     * @param usersGridViewer the all productOpers grid viewer
     * @param userEditor      the user editor
     */
    @Autowired
    public AllProductOpersManager(UsersGridViewer usersGridViewer, UserEditor userEditor) {
	super(usersGridViewer, userEditor);
	usersGridViewer.setFilterRoles(Arrays.asList(Role.ROLE_PRODUCTOPER));
    }

    /**
     * Gets the page title.
     *
     * @return the page title
     */
    @Override
    public String getPageTitle() {
	return getTranslation(AppTitleMsg.APP_TITLE_USERS_PRODUCTOPERS, UI.getCurrent().getLocale());
    }
}
