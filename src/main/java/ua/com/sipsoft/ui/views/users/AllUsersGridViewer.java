package ua.com.sipsoft.ui.views.users;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import ua.com.sipsoft.services.users.UsersService;
import ua.com.sipsoft.ui.views.users.prototype.AbstractSelectedUsersGridViewer;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Class AllUsersGridViewer.
 *
 * @author Pavlo Degtyaryev
 */
//@Slf4j
@UIScope
@SpringComponent
public class AllUsersGridViewer extends AbstractSelectedUsersGridViewer {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7674273489045197758L;

    /**
     * Instantiates a new all users grid viewer.
     *
     * @param usersService the users service
     */
    @Autowired
    public AllUsersGridViewer(UsersService usersService) {
	super(usersService, Arrays.asList(Role.values()));
    }

}
