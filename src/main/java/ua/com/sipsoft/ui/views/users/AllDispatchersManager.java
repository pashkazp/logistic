package ua.com.sipsoft.ui.views.users;

import java.util.Arrays;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.ui.commons.forms.viewform.ViewForm;
import ua.com.sipsoft.ui.views.users.components.UserEditor;
import ua.com.sipsoft.ui.views.users.components.UsersGridViewer;
import ua.com.sipsoft.ui.views.users.prototype.AbstractSelectedUsersManager;
import ua.com.sipsoft.utils.messages.AppTitleMsg;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Class AllDispatchersManager.
 *
 * @author Pavlo Degtyaryev
 */

@Slf4j
@UIScope
@SpringComponent
//@Route(value = AppURL.DISPATCHERS_ALL, layout = MainView.class)
public class AllDispatchersManager extends AbstractSelectedUsersManager
	implements HasDynamicTitle {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1578164728569421737L;

    /**
     * Instantiates a new all dispatchers manager.
     *
     * @param usersGridViewer the all dispatchers grid viewer
     * @param userEditor      the user editor
     */
    public AllDispatchersManager(UsersGridViewer usersGridViewer, ViewForm viewForm,
	    UserEditor<User> userEditor) {
	super(usersGridViewer, viewForm, userEditor);
	log.info("Create All dispatchers users manager");
	usersGridViewer.setFilterRoles(Arrays.asList(Role.ROLE_DISPATCHER));
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
