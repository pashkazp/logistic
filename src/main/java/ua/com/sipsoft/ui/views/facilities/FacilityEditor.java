package ua.com.sipsoft.ui.views.facilities;

import java.util.Objects;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.common.Facility;
import ua.com.sipsoft.ui.commons.entityedit.AbstractBindedEntityEditor;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.messages.FacilityEntityMsg;

/**
 * The Class FacilityEditor.
 *
 * @author Pavlo Degtyaryev
 * @param <T> the generic type
 */
@Scope("prototype")
@Slf4j
@SpringComponent
public class FacilityEditor<T extends Facility> extends AbstractBindedEntityEditor<T> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3291395147592860970L;

    /** The name. */
    private final TextField name;

    /**
     * Instantiates a new facility editor.
     */
    public FacilityEditor() {
	super();
	initBinder();
	VerticalLayout panelFields = new VerticalLayout();

	name = new TextField();
	name.setValueChangeMode(ValueChangeMode.EAGER);
	name.setRequired(true);
	name.setRequiredIndicatorVisible(true);
	name.setWidthFull();
	name.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);
	name.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	name.getStyle().set(Props.PADDING, null);
	name.focus();

	getBinder().forField(name)
		.withValidator(description -> description
			.length() >= 5, getTranslation(FacilityEntityMsg.CHK_NAME_SHORT))
		.withValidator(description -> description
			.length() <= 100, getTranslation(FacilityEntityMsg.CHK_NAME_LONG))
		.asRequired(getTranslation(FacilityEntityMsg.CHK_NAME_SHORT))
		.bind(Facility::getName, Facility::setName);

	FormItem fi = addFormItem(name, getTranslation(FacilityEntityMsg.NAME));
	panelFields.setFlexGrow(1, fi);
	panelFields.setMargin(false);
	panelFields.setPadding(false);
	panelFields.setSpacing(false);
	panelFields.getStyle().set(Props.MARGIN, Props.EM_0_5);

	add(panelFields);

	setSizeFull();
	setWidth(Props.EM_20);
	setMaxWidth(getWidth());
	setMinWidth(getWidth());

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void initBinder() {
	setBinder(new Binder(Facility.class));
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
	return "FacilityEditor [binder=" + getBinder() + ", name=" + name + ", readOnlyMode=" + isReadOnlyMode()
		+ ", toString()=" + super.toString() + "]";
    }

    @Override
    public int hashCode() {
	return Objects.hash(getBinder(), name, isReadOnlyMode());
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof FacilityEditor)) {
	    return false;
	}
	FacilityEditor<?> other = (FacilityEditor<?>) obj;
	return Objects.equals(getBinder(), other.getBinder()) && Objects.equals(name, other.name)
		&& isReadOnlyMode() == other.isReadOnlyMode();
    }

}
