package ua.com.sipsoft.ui.views.users;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import ua.com.sipsoft.services.users.UsersService;
import ua.com.sipsoft.ui.views.users.prototype.AbstractSelectedUsersGridViewer;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Class AllDispatchersGridViewer.
 *
 * @author Pavlo Degtyaryev
 */
//@Slf4j
@UIScope
@SpringComponent
public class AllDispatchersGridViewer extends AbstractSelectedUsersGridViewer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8254484758665361757L;

	/**
	 * Instantiates a new all dispatchers grid viewer.
	 *
	 * @param usersService the users service
	 */
	@Autowired
	public AllDispatchersGridViewer(UsersService usersService) {
		super(usersService, Arrays.asList(Role.ROLE_DISPATCHER));
	}

}
