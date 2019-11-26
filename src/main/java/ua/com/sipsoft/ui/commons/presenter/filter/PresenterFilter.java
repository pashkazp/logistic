package ua.com.sipsoft.ui.commons.presenter.filter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import ua.com.sipsoft.ui.commons.presenter.toolbar.PresenterToolbar;
import ua.com.sipsoft.ui.commons.presenter.toolbar.PresenterToolbar.PresenterToolbarBuilder;
import ua.com.sipsoft.utils.NestedBuilder;

public class PresenterFilter extends TextField {

    private static final long serialVersionUID = -8606656201843855174L;

    private PresenterFilter(PresenterFilterBuilder builder) {

	if (builder.label != null) {
	    setLabel(builder.label);
	}
	if (builder.placeHolder != null) {
	    setPlaceholder(builder.placeHolder);
	}
	if (builder.prefixComponent != null) {
	    setPrefixComponent(builder.prefixComponent);
	}
	if (builder.listener != null) {
	    addValueChangeListener(builder.listener);
	}
	setValueChangeMode(builder.changeMode);
    }

    public static PresenterFilterBuilder builder() {
	return new PresenterFilterBuilder();
    }

    public static final class PresenterFilterBuilder
	    implements NestedBuilder<PresenterToolbar.PresenterToolbarBuilder, PresenterFilter> {
	private String label;
	private String placeHolder;
	private Component prefixComponent;
	private ValueChangeMode changeMode = ValueChangeMode.LAZY;
	private ValueChangeListener<? super ComponentValueChangeEvent<TextField, String>> listener;

	private PresenterToolbar.PresenterToolbarBuilder parentBuilder;

	public PresenterFilterBuilder withLabel(String label) {
	    this.label = label;
	    return this;
	}

	public PresenterFilterBuilder withPalceHolder(String placeHolder) {
	    this.placeHolder = placeHolder;
	    return this;
	}

	public PresenterFilterBuilder withPrefixComponent(Component prefixComponent) {
	    this.prefixComponent = prefixComponent;
	    return this;
	}

	public PresenterFilterBuilder withValueChangeMode(ValueChangeMode changeMode) {
	    this.changeMode = changeMode;
	    return this;
	}

	public PresenterFilterBuilder withValueChangeListener(
		ValueChangeListener<? super ComponentValueChangeEvent<TextField, String>> listener) {
	    this.listener = listener;
	    return this;
	}

	@Override
	public PresenterToolbarBuilder getParentBuilder() {

	    return this.parentBuilder;
	}

	@Override
	public PresenterFilter getThisBuilder() {
	    return this.build();
	}

	@Override
	public void setParentBuilder(PresenterToolbarBuilder parentBuilder) {
	    this.parentBuilder = parentBuilder;

	}

	@Override
	public PresenterFilter build() {
	    return new PresenterFilter(this);
	}

    }
}
