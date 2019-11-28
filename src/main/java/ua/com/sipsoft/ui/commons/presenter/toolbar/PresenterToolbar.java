package ua.com.sipsoft.ui.commons.presenter.toolbar;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import lombok.Getter;
import ua.com.sipsoft.ui.commons.presenter.button.PresenterButtonsList;
import ua.com.sipsoft.ui.commons.presenter.filter.PresenterFilter;
import ua.com.sipsoft.utils.Props;

public final class PresenterToolbar extends HorizontalLayout {

    private static final long serialVersionUID = -6379304136135669075L;

    @Getter
    private TextField filter;

    private PresenterToolbar(PresenterToolbarBuilder builder) {

	if (builder.filterBuilder != null) {
	    TextField field = builder.filterBuilder.build();
	    add(field);
	    filter = field;
	    setFlexGrow(1, field);
	}

	List<Button> buttons = builder.buttonList.getButtons();
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
	return new PresenterToolbarBuilder();
    }

    public static PresenterToolbar build(PresenterToolbarBuilder builder) {
	return new PresenterToolbar(builder);
    }

    public static final class PresenterToolbarBuilder {

	private PresenterFilter presenterFilter;
	private PresenterFilter.PresenterFilterBuilder filterBuilder = PresenterFilter.builder()
		.withParentBuilder(this);

	private PresenterButtonsList buttonList;
	private PresenterButtonsList.PresenterButtonsListBuilder listBuilder = PresenterButtonsList.builder()
		.withParentBuilder(this);

	private PresenterToolbarBuilder() {

	}

	public PresenterToolbarBuilder withPresenterFilter(PresenterFilter presenterFilter) {
	    this.presenterFilter = presenterFilter;
	    return this;
	}

	public PresenterFilter.PresenterFilterBuilder withPresenterFilter() {
	    return this.filterBuilder;
	}

	public PresenterToolbarBuilder withPresenterButtonsList(PresenterButtonsList buttonList) {
	    this.buttonList = buttonList;
	    return this;
	}

	public PresenterButtonsList.PresenterButtonsListBuilder withPresenterButtonsList() {
	    return this.listBuilder;
	}

	public PresenterToolbar build() {
	    return new PresenterToolbar(this);
	}

    }

}
