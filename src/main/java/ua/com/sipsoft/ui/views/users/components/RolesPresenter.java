package ua.com.sipsoft.ui.views.users.components;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Class RolesPresenter.
 *
 * @author Pavlo Degtyaryev
 */
@Scope(value = "prototype")
@UIScope
@SpringComponent
@Tag("roleIcons-presenter")
@Slf4j
public class RolesPresenter extends HorizontalLayout
		implements HasValue<ValueChangeEvent<Collection<Role>>, Collection<Role>> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2822401652807169766L;

	/** The color active. */
	private String colorActive = "var(--lumo-body-text-color)";

	/** The color inactive. */
	private String colorInactive = "var(--lumo-disabled-text-color)";

	/** The role icons. */
	private Icon[] roleIcons = new Icon[Role.values().length];

	/** The roles. */
	private Role[] roles = new Role[roleIcons.length];

	/** The read only. */
	private boolean readOnly = false;

	/** The required indicator visible. */
	private boolean requiredIndicatorVisible = false;

	/**
	 * Instantiates a new roles presenter.
	 */
	public RolesPresenter() {
		int i = 0;
		for (Role role : Role.values()) {
			roles[i] = role;
			roleIcons[i] = role.getIcon().createIcon();
			roleIcons[i].getStyle().set(Props.MARGIN, Props.EM_0_125);
			roleIcons[i].setColor(colorInactive);

			Span span = new Span(roleIcons[i]);
			span.setTitle(getTranslation(role.getRoleName()));
			roleIcons[i].addClickListener(event -> {
				Icon icon = event.getSource();
				log.debug("getcolor: {}", icon.getColor());
				icon.setColor(icon.getColor().equals(colorActive) ? colorInactive : colorActive);
				log.debug("get setted color: {}", icon.getColor());
			});
			add(span);
			i++;
		}
		setAlignItems(Alignment.STRETCH);
		setMargin(true);
		setPadding(false);
		setSpacing(true);
		getStyle().set("size", "100%");
	}

	/**
	 * Counter of active Roles.
	 *
	 * @return the int
	 */
	public int counter() {
		int i = 0;
		for (Icon icon : roleIcons) {
			if (icon.getColor().equals(colorActive)) {
				i++;
			}
		}
		return i;
	}

	/**
	 * Unset all roles.
	 */
	private void unsetAllRoles() {

		for (Icon icon : roleIcons) {
			icon.setColor(colorInactive);
		}
	}

	/**
	 * Sets the read only.
	 *
	 * @param readOnly the new read only
	 */
	@Override
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * Checks if is read only.
	 *
	 * @return true, if is read only
	 */
	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Sets the required indicator visible.
	 *
	 * @param requiredIndicatorVisible the new required indicator visible
	 */
	@Override
	public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
		this.requiredIndicatorVisible = requiredIndicatorVisible;
	}

	/**
	 * Checks if is required indicator visible.
	 *
	 * @return true, if is required indicator visible
	 */
	@Override
	public boolean isRequiredIndicatorVisible() {
		return requiredIndicatorVisible;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	@Override
	public void setValue(Collection<Role> value) {
		unsetAllRoles();
		if (value != null) {
			for (Role role : value) {
				for (int i = 0; i < this.roles.length; i++) {
					if (role.equals(this.roles[i])) {
						roleIcons[i].setColor(colorActive);
					}
				}
			}
		}
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	@Override
	public Collection<Role> getValue() {
		Set<Role> userRoles = new HashSet<>();
		for (int i = 0; i < roleIcons.length; i++) {
			if (roleIcons[i].getColor().equals(colorActive)) {
				userRoles.add(this.roles[i]);
			}
		}
		return userRoles;
	}

	/**
	 * Adds the value change listener.
	 *
	 * @param listener the listener
	 * @return the registration
	 */
	@Override
	public Registration addValueChangeListener(
			ValueChangeListener<? super ValueChangeEvent<Collection<Role>>> listener) {
		return null;
	}

}
