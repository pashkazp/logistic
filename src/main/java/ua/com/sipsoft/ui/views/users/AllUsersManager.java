package ua.com.sipsoft.ui.views.users;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.ui.MainView;
import ua.com.sipsoft.ui.views.users.components.UserEditor;
import ua.com.sipsoft.ui.views.users.components.UsersGridViewer;
import ua.com.sipsoft.ui.views.users.prototype.AbstractSelectedUsersManager;
import ua.com.sipsoft.utils.AppURL;
import ua.com.sipsoft.utils.messages.AppTitleMsg;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Class AllUsersManager.
 *
 * @author Pavlo Degtyaryev
 */

//@Slf4j
@UIScope
@SpringComponent
@Route(value = AppURL.USERS_ALL, layout = MainView.class)
public class AllUsersManager extends AbstractSelectedUsersManager<UsersGridViewer> implements HasDynamicTitle {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3356617930255862777L;

    /**
     * Instantiates a new all users manager.
     *
     * @param usersGridViewer the all users grid viewer
     * @param userEditor         the user editor
     */
    @Autowired
    public AllUsersManager(UsersGridViewer usersGridViewer, UserEditor<User> userEditor) {
	super(usersGridViewer, userEditor);
	usersGridViewer.setFilterRoles(Arrays.asList(Role.values()));
    }

    /**
     * Gets the page title.
     *
     * @return the page title
     */
    @Override
    public String getPageTitle() {
	return getTranslation(AppTitleMsg.APP_TITLE_USERS_ALL, UI.getCurrent().getLocale());
    }
}
