package ua.com.sipsoft.ui.views.users;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import ua.com.sipsoft.services.users.UsersService;
import ua.com.sipsoft.ui.views.users.prototype.AbstractSelectedUsersGridViewer;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Class AllAdminsGridViewer.
 *
 * @author Pavlo Degtyaryev
 */
//@Slf4j
@UIScope
@SpringComponent
public class AllAdminsGridViewer extends AbstractSelectedUsersGridViewer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -755974581926270912L;

	/**
	 * Instantiates a new all admins grid viewer.
	 *
	 * @param usersService the users service
	 */
	@Autowired
	public AllAdminsGridViewer(UsersService usersService) {
		super(usersService, Arrays.asList(Role.ROLE_ADMIN));
	}

}
