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
 * The Class AllAdminsManager.
 *
 * @author Pavlo Degtyaryev
 */

//@Slf4j
@UIScope
@SpringComponent
@Route(value = AppURL.ADMINS_ALL, layout = MainView.class)
public class AllAdminsManager extends AbstractSelectedUsersManager<UsersGridViewer> implements HasDynamicTitle {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6413236308489307604L;

    /**
     * Instantiates a new all admins manager.
     *
     * @param usersGridViewer the all admins grid viewer
     * @param userEditor      the user editor
     */
    @Autowired
    public AllAdminsManager(UsersGridViewer usersGridViewer, UserEditor userEditor) {
	super(usersGridViewer, userEditor);
	usersGridViewer.setFilterRoles(Arrays.asList(Role.ROLE_ADMIN));
    }

    /**
     * Gets the page title.
     *
     * @return the page title
     */
    @Override
    public String getPageTitle() {
	return getTranslation(AppTitleMsg.APP_TITLE_USERS_ADMINS, UI.getCurrent().getLocale());
    }
}
