package ua.com.sipsoft.ui.commons;

import java.util.ArrayList;
import java.util.List;

import com.github.appreciated.app.layout.component.menu.left.LeftMenuComponentWrapper;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.entity.Section;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * The Class LeftAppMenuBuilderExt.
 */
public class LeftAppMenuBuilderExt {

    /** The components. */
    private final List<Component> components = new ArrayList<>();

    /** The header. */
    private final List<Component> header = new ArrayList<>();

    /** The body. */
    private final List<Component> body = new ArrayList<>();

    /** The footer. */
    private final List<Component> footer = new ArrayList<>();

    /** The sticky. */
    private boolean sticky;

    /**
     * Instantiates a new left app menu builder ext.
     */
    private LeftAppMenuBuilderExt() {
    }

    /**
     * Gets the builder.
     *
     * @return the left app menu builder ext
     */
    public static LeftAppMenuBuilderExt get() {
	return new LeftAppMenuBuilderExt();
    }

    /**
     * Adds the component.
     *
     * @param element the element
     * @return the left app menu builder ext
     */
    public LeftAppMenuBuilderExt add(Component element) {
	if (element == null) {
	    return this;
	}
	return addToSection(element, Section.DEFAULT);
    }

    /**
     * Adds the component.
     *
     * @param caption   the caption
     * @param icon      the icon
     * @param className the class name
     * @return the left app menu builder ext
     */
    public LeftAppMenuBuilderExt add(String caption, VaadinIcon icon, Class<? extends Component> className) {
	return add(new LeftNavigationItem(caption, icon, className));
    }

    /**
     * Adds the component.
     *
     * @param caption   the caption
     * @param icon      the icon
     * @param className the class name
     * @return the left app menu builder ext
     */
    public LeftAppMenuBuilderExt add(String caption, Icon icon, Class<? extends Component> className) {
	return add(new LeftNavigationItem(caption, icon, className));
    }

    /**
     * Adds the to section.
     *
     * @param element the element
     * @param section the section
     * @return the left app menu builder ext
     */
    public LeftAppMenuBuilderExt addToSection(Component element, Section section) {
	switch (section) {
	case HEADER:
	    header.add(element);
	    break;
	case FOOTER:
	    footer.add(element);
	    break;
	default:
	    body.add(element);
	}
	return this;
    }

    /**
     * With sticky footer.
     *
     * @return the left app menu builder ext
     */
    public LeftAppMenuBuilderExt withStickyFooter() {
	this.sticky = true;
	return this;
    }

    /**
     * Builds the set.
     *
     * @return the component
     */
    public Component build() {
	components.addAll(header);
	LeftMenuComponentWrapper menu = new LeftMenuComponentWrapper();
	components.addAll(body);
	if (sticky) {
	    menu.getMenu().getStyle().set("display", "flex");
	    Div div = new Div();
	    div.setWidth("100%");
	    div.setHeight("0px");
	    div.getStyle().set("flex", "1 1");
	    components.add(div);
	}
	components.addAll(footer);
	menu.add(components.toArray(new Component[0]));
	return menu;
    }
}
