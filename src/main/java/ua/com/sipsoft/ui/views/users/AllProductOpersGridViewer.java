package ua.com.sipsoft.ui.views.users;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import ua.com.sipsoft.services.users.UsersService;
import ua.com.sipsoft.ui.views.users.prototype.AbstractSelectedUsersGridViewer;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Class AllProductOpersGridViewer.
 *
 * @author Pavlo Degtyaryev
 */
@UIScope
@SpringComponent
//@Slf4j
public class AllProductOpersGridViewer extends AbstractSelectedUsersGridViewer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 122167612532264708L;

	/**
	 * Instantiates a new all productOpers grid viewer.
	 *
	 * @param usersService the users service
	 */
	@Autowired
	public AllProductOpersGridViewer(UsersService usersService) {
		super(usersService, Arrays.asList(Role.ROLE_PRODUCTOPER));
	}

}
