package ua.com.sipsoft.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

/**
 * Interface for components thats has the popup title.
 *
 * @author Pavlo Degtyaryev
 */
public interface TooltippedComponent {
    default Component wrapWithTooltip(Component component, String tooltip) {
	Div div = new Div(component);
	div.setSizeUndefined();
	div.setTitle(tooltip == null ? "" : tooltip);
	return div;
    }
}
