package ua.com.sipsoft.ui.commons.presenter.button;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;

import ua.com.sipsoft.ui.commons.presenter.toolbar.PresenterToolbar;
import ua.com.sipsoft.ui.commons.presenter.toolbar.PresenterToolbar.PresenterToolbarBuilder;
import ua.com.sipsoft.utils.NestedBuilder;

public class PresenterButtonsList {

    private List<Button> buttons;

    private PresenterButtonsList(PresenterButtonsListBuilder builder) {
	this.buttons = builder.buttons;
    }

    public static PresenterButtonsListBuilder builder() {
	return new PresenterButtonsListBuilder();
    }

    public List<Button> getButtons() {
	return buttons;
    }

    public static final class PresenterButtonsListBuilder
	    implements NestedBuilder<PresenterToolbar.PresenterToolbarBuilder, PresenterButtonsList> {

	private List<Button> buttons = new ArrayList<Button>();
	private PresenterToolbar.PresenterToolbarBuilder parentBuilder;

	private PresenterButton presenterButton;
	private PresenterButton.PresenterButtonBuilder buttonBuilder = PresenterButton.builder()
		.withParentBuilder(this);

	private PresenterButtonsListBuilder() {
	}

	@Override
	public void setParentBuilder(PresenterToolbarBuilder parentBuilder) {
	    this.parentBuilder = parentBuilder;
	}

	@Override
	public PresenterButtonsList build() {
	    return new PresenterButtonsList(this);
	}

	@Override
	public PresenterToolbarBuilder getParentBuilder() {

	    return this.parentBuilder;
	}

	@Override
	public PresenterButtonsList getThisBuilder() {
	    return this.build();
	}

	public PresenterButtonsListBuilder withPresenterButton(PresenterButton button) {
	    this.buttons.add(button);
	    return this;
	}

	public PresenterButton.PresenterButtonBuilder withPresenterButton() {
	    return this.buttonBuilder;
	}

    }

}
