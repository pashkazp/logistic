package ua.com.sipsoft.utils;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Vaadin icons that represents standard icons of user roles.
 *
 * @author Pavlo Degtyaryev
 */
public enum RoleIcon {

	USER(VaadinIcon.USER),
	CLIENT(VaadinIcon.USER_CHECK),
	COURIER(VaadinIcon.TRUCK),
	MANAGER(VaadinIcon.USER_STAR),
	PRODUCTOPER(VaadinIcon.USER_CLOCK),
	DISPATCHER(VaadinIcon.RANDOM),
	ADMIN(VaadinIcon.GOLF);

	/** The icon. */
	private final VaadinIcon icon;

	/**
	 * Instantiates a new role icon.
	 *
	 * @param icon the icon
	 */
	private RoleIcon(VaadinIcon icon) {
		this.icon = icon;
	}

	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public VaadinIcon getIcon() {
		return icon;
	}

	/**
	 * Creates the icon.
	 *
	 * @return the icon
	 */
	public Icon createIcon() {
		return icon.create();
	}
}
