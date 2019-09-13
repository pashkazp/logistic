package ua.com.sipsoft.utils.security;

import ua.com.sipsoft.utils.RoleIcon;
import ua.com.sipsoft.utils.messages.RolesMsg;

/**
 * Simple JavaBean domain object that represents {@link User}'s role.
 *
 * @author Pavlo Degtyaryev
 */

public enum Role /* implements GrantedAuthority */ {

	// Warning! Sequence matters for safety. Higher suits have lower numbers.
	ROLE_ADMIN(RoleName.ROLE_ADMIN, RoleIcon.ADMIN, RolesMsg.ROLE_ADMIN),
	ROLE_DISPATCHER(RoleName.ROLE_DISPATCHER, RoleIcon.DISPATCHER, RolesMsg.ROLE_DISPATCHER),
	ROLE_MANAGER(RoleName.ROLE_MANAGER, RoleIcon.MANAGER, RolesMsg.ROLE_MANAGER),
	ROLE_PRODUCTOPER(RoleName.ROLE_PRODUCTOPER, RoleIcon.PRODUCTOPER, RolesMsg.ROLE_PRODUCTOPER),
	ROLE_COURIER(RoleName.ROLE_COURIER, RoleIcon.COURIER, RolesMsg.ROLE_COURIER),
	ROLE_CLIENT(RoleName.ROLE_CLIENT, RoleIcon.CLIENT, RolesMsg.ROLE_CLIENT),
	ROLE_REGISTERED(RoleName.ROLE_REGISTERED, RoleIcon.USER, RolesMsg.ROLE_REGISTERED);

	/** The string. */
	private final String string;

	/** The role name. */
	private final String roleName;

	/** The icon. */
	private final RoleIcon icon;

	/**
	 * Instantiates a new role.
	 *
	 * @param string   the string
	 * @param icon     the icon
	 * @param roleName the role name
	 */
	private Role(String string, RoleIcon icon, String roleName) {
		this.string = string;
		this.icon = icon;
		this.roleName = roleName;
	}

	/**
	 * Gets the authority.
	 *
	 * @return the authority
	 */
//	@Override
//	public String getAuthority() {
//		return string;
//	}
//
	/**
	 * Gets the string.
	 *
	 * @return the string
	 */
	public final String getString() {
		return string;
	}

	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public RoleIcon getIcon() {
		return icon;
	}

	/**
	 * Gets the role name.
	 *
	 * @return the role name
	 */
	public String getRoleName() {
		return roleName;
	}
}
