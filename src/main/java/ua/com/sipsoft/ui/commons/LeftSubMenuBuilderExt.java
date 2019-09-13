package ua.com.sipsoft.ui.commons;

import java.util.ArrayList;
import java.util.List;

import com.github.appreciated.app.layout.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.builder.ComponentBuilder;
import com.github.appreciated.app.layout.component.menu.left.LeftSubmenu;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.Icon;

/**
 * A Builder to build {@link LeftSubmenu} this builder is meant to be used in
 * combination with the {@link AppLayoutBuilder}.
 */
public class LeftSubMenuBuilderExt implements ComponentBuilder {

	/** The title. */
	private final String title;

	/** The icon. */
	private final Icon icon;

	/** The components. */
	private List<Component> components = new ArrayList<>();

	/**
	 * Instantiates a new left sub menu builder ext.
	 *
	 * @param title the title
	 * @param icon  the icon
	 */
	private LeftSubMenuBuilderExt(String title, Icon icon) {
		this.title = title;
		this.icon = icon;
	}

	/**
	 * Gets the Builder.
	 *
	 * @param title the title
	 * @param icon  the icon
	 * @return the left sub menu builder ext
	 */
	public static LeftSubMenuBuilderExt get(String title, Icon icon) {
		return new LeftSubMenuBuilderExt(title, icon);
	}

	/**
	 * returns a SubmenuBuilder with a predefined expanding element that only has a
	 * title.
	 *
	 * @param title the title
	 * @return the left sub menu builder ext
	 */
	public static LeftSubMenuBuilderExt get(String title) {
		return new LeftSubMenuBuilderExt(title, null);
	}

	/**
	 * returns a SubmenuBuilder with a predefined expanding element that only has an
	 * icon.
	 *
	 * @param icon the icon
	 * @return the left sub menu builder ext
	 */
	public static LeftSubMenuBuilderExt get(Icon icon) {
		return new LeftSubMenuBuilderExt(null, icon);
	}

	/**
	 * Adds a MenuElement.
	 *
	 * @param element the element
	 * @return the left sub menu builder ext
	 */
	public LeftSubMenuBuilderExt add(Component element) {
		if (element != null) {
			components.add(element);
		}
		return this;
	}

	/**
	 * Builds the Component.
	 *
	 * @return the component
	 */
	@Override
	public Component build() {
		return new LeftSubmenu(title, icon, components);
	}
}
