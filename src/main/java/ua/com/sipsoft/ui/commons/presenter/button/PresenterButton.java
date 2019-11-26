package ua.com.sipsoft.ui.commons.presenter.button;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;

import ua.com.sipsoft.ui.commons.presenter.button.PresenterButtonsList.PresenterButtonsListBuilder;
import ua.com.sipsoft.utils.NestedBuilder;

public class PresenterButton extends Button {

    private static final long serialVersionUID = 1281664817320607626L;

    private PresenterButton(PresenterButtonBuilder builder) {
	if (builder.icon != null) {
	    setIcon(builder.icon);
	}
	if (builder.listener != null) {
	    addClickListener(builder.listener);
	}
	setEnabled(builder.enabled);
    }

    public static PresenterButtonBuilder builder() {
	return new PresenterButtonBuilder();
    }

    public static final class PresenterButtonBuilder
	    implements NestedBuilder<PresenterButtonsList.PresenterButtonsListBuilder, PresenterButton> {

	private Icon icon;

	private ComponentEventListener<ClickEvent<Button>> listener;

	private boolean enabled = true;

	private PresenterButtonsList.PresenterButtonsListBuilder parentBuilder;

	private PresenterButtonBuilder() {
	}

	public static PresenterButtonBuilder builder() {
	    return new PresenterButtonBuilder();
	}

	public PresenterButtonBuilder withIcon(Icon icon) {
	    this.icon = icon;
	    return this;
	}

	public PresenterButtonBuilder withClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
	    this.listener = listener;
	    return this;
	}

	public PresenterButtonBuilder withEnabled(boolean enabled) {
	    this.enabled = enabled;
	    return this;
	}

	@Override
	public PresenterButtonsListBuilder getParentBuilder() {
	    return this.parentBuilder;
	}

	@Override
	public PresenterButton getThisBuilder() {
	    return this.build();
	}

	@Override
	public void setParentBuilder(PresenterButtonsListBuilder parentBuilder) {
	    this.parentBuilder = parentBuilder;

	}

	@Override
	public PresenterButton build() {
	    return new PresenterButton(this);
	}

    }

}
