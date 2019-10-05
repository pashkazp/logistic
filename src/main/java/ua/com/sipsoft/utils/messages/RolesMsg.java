package ua.com.sipsoft.utils.messages;

import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.utils.security.Role;

/**
 * Set of standard messages for {@link Role} of entity {@link User}.
 *
 * @author Pavlo Degtyaryev
 */
public class RolesMsg {
    public static final String ROLE_REGISTERED = "user.role.registered";
    public static final String ROLE_CLIENT = "user.role.client";
    public static final String ROLE_COURIER = "user.role.courier";
    public static final String ROLE_MANAGER = "user.role.manager";
    public static final String ROLE_PRODUCTOPER = "user.role.productoper";
    public static final String ROLE_DISPATCHER = "user.role.dispatcher";
    public static final String ROLE_ADMIN = "user.role.admin";

    /**
     * Instantiates a new roles msg.
     */
    private RolesMsg() {
    }
}
