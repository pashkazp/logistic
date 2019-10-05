package ua.com.sipsoft.ui.views.users;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import ua.com.sipsoft.services.users.UsersService;
import ua.com.sipsoft.ui.views.users.prototype.AbstractSelectedUsersGridViewer;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Class AllRegisteredGridViewer.
 *
 * @author Pavlo Degtyaryev
 */
@UIScope
@SpringComponent
//@Slf4j
public class AllRegisteredGridViewer extends AbstractSelectedUsersGridViewer {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3229161951777614811L;

    /**
     * Instantiates a new all registered Users grid viewer.
     *
     * @param usersService the users service
     */
    @Autowired
    public AllRegisteredGridViewer(UsersService usersService) {
	super(usersService, Arrays.asList(Role.ROLE_REGISTERED));
    }

}
