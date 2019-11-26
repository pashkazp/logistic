package ua.com.sipsoft.ui.commons.presenter.toolbar;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import ua.com.sipsoft.ui.commons.presenter.button.PresenterButtonsBuilder;
import ua.com.sipsoft.ui.commons.presenter.filter.PresenterFilterBuilder;

public class PresenterToolbarBuilder {
    private PresenterButtonsBuilder buttonsBuilder;
    private PresenterFilterBuilder filterBuilder;

    private PresenterToolbarBuilder() {

    }

    public static PresenterToolbarBuilder builder() {
	return new PresenterToolbarBuilder();
    }

    public PresenterToolbarBuilder withButtons(List<Button> buttons) {
	if (buttons == null || buttons.isEmpty()) {
	    throw new IllegalArgumentException("Buttons must be not null and has to have at least 1 button.");
	}
	this.buttonsBuilder = PresenterButtonsBuilder.builder();
	this.getButtonsBuilder().setButtons(buttons);
	return this;
    }

    public PresenterToolbarBuilder withButtons(PresenterButtonsBuilder buttonsBuilder) {
	this.buttonsBuilder = buttonsBuilder;
	return this;
    }

    public PresenterButtonsBuilder withButtons() {
	this.buttonsBuilder = PresenterButtonsBuilder.builder();
	return this.getButtonsBuilder();
    }

    public PresenterFilterBuilder withFilter() {
	this.filterBuilder = PresenterFilterBuilder.builder();
	return this.getFilterBuilder();
    }

    public HorizontalLayout build() {
	if (getButtonsBuilder() == null || getButtonsBuilder().getButtons().isEmpty()) {
	    throw new IllegalArgumentException("Toolbar has to have at least 1 button.");
	}

	return PresenterToolbar.build(this);
    }

    /**
     * @return the filterBuilder
     */
    public PresenterFilterBuilder getFilterBuilder() {
	return filterBuilder;
    }

    /**
     * @return the buttonsBuilder
     */
    public PresenterButtonsBuilder getButtonsBuilder() {
	return buttonsBuilder;
    }

}
