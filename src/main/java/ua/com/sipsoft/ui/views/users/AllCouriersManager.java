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
 * The Class AllCouriersManager.
 *
 * @author Pavlo Degtyaryev
 */

@Slf4j
@UIScope
@SpringComponent
public class AllCouriersManager extends AbstractSelectedUsersManager implements HasDynamicTitle {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -404507111374215492L;

    /**
     * Instantiates a new all couriers manager.
     *
     * @param usersGridViewer the all couriers grid viewer
     * @param userEditor      the user editor
     */
    public AllCouriersManager(UsersGridViewer usersGridViewer, ViewForm viewForm,
	    UserEditor<User> userEditor) {
	super(usersGridViewer, viewForm, userEditor);
	log.info("Create All couriers users manager");
	usersGridViewer.setFilterRoles(Arrays.asList(Role.ROLE_COURIER));
    }

    /**
     * Gets the page title.
     *
     * @return the page title
     */
    @Override
    public String getPageTitle() {
	return getTranslation(AppTitleMsg.APP_TITLE_USERS_COURIERS, UI.getCurrent().getLocale());
    }
}
