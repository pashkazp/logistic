package ua.com.sipsoft.ui.commons.presenter.toolbar;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import ua.com.sipsoft.utils.Props;

public final class PresenterToolbar extends HorizontalLayout {

    private static final long serialVersionUID = -6379304136135669075L;

    private PresenterToolbar(PresenterToolbarBuilder builder) {

	if (builder.getFilterBuilder() != null) {
	    TextField field = builder.getFilterBuilder().build();
	    add(field);
	    setFlexGrow(1, field);
	}

	List<Button> buttons = builder.getButtonsBuilder().getButtons();
	for (int i = 0; i < buttons.size(); i++) {
	    Button button = buttons.get(i);
	    button.setSizeUndefined();
	    button.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	    add(button);
	    setFlexGrow(0, button);
	}
	buttons.get(buttons.size() - 1).getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);

	setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	setMargin(false);
	setPadding(false);
	setSpacing(false);
    }

    public static PresenterToolbarBuilder builder() {
	return PresenterToolbarBuilder.builder();
    }

    public static PresenterToolbar build(PresenterToolbarBuilder builder) {
	return new PresenterToolbar(builder);
    }

}
