package ua.com.sipsoft.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 *
 * @author Pavlo Degtyaryev
 */
public class ButtonPreparer {
    private ButtonPreparer() {

    }

    public static <T extends Button> T prepare(T button) {
	button.setSizeUndefined();
	button.setWidthFull();
	button.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);

	return button;
    }

    /**
     * Prepare.
     *
     * @param <T>    the generic type
     * @param layout the layout
     * @return the t
     */
    public static <T extends HorizontalLayout> T prepare(T layout) {
	layout.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	layout.setMargin(false);
	layout.setPadding(false);
	layout.setSpacing(true);
	return layout;
    }
}
