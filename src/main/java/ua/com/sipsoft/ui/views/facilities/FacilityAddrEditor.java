package ua.com.sipsoft.ui.views.facilities;

import java.util.Objects;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.common.FacilityAddress;
import ua.com.sipsoft.ui.commons.entityedit.AbstractBindedEntityEditor;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.messages.FacilityAddrEntityMsg;

/**
 * The Class FacilityAddrEditor.
 *
 * @author Pavlo Degtyaryev
 * @param <T> the generic type
 */
@Scope("prototype")
@Slf4j
@SpringComponent
public class FacilityAddrEditor<T extends FacilityAddress> extends AbstractBindedEntityEditor<T> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3291395147592860970L;

    /** The addresses alias. */
    private final TextField addressesAlias;

    /** The address. */
    private final TextField address;

    /** The coordinates. */
    private final TextField coordinates;

    /** The default address. */
    private final Checkbox defaultAddress;

    /**
     * Instantiates a new facility addr editor.
     */
    public FacilityAddrEditor() {
	super();
	initBinder();
	VerticalLayout panelFields = new VerticalLayout();

	addressesAlias = new TextField();
	addressesAlias.setValueChangeMode(ValueChangeMode.EAGER);
	addressesAlias.setRequired(true);
	addressesAlias.setRequiredIndicatorVisible(true);
	addressesAlias.setWidthFull();
	addressesAlias.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);
	addressesAlias.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	addressesAlias.getStyle().set(Props.PADDING, null);
	addressesAlias.focus();

	address = new TextField();
	address.setValueChangeMode(ValueChangeMode.EAGER);
	address.setRequired(true);
	address.setRequiredIndicatorVisible(true);
	address.setWidthFull();
	address.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);
	address.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	address.getStyle().set(Props.PADDING, null);

	coordinates = new TextField();
	coordinates.setValueChangeMode(ValueChangeMode.EAGER);
	coordinates.setRequiredIndicatorVisible(true);
	coordinates.setWidthFull();
	coordinates.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);
	coordinates.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	coordinates.getStyle().set(Props.PADDING, null);

	defaultAddress = new Checkbox();
	defaultAddress.setRequiredIndicatorVisible(true);
	defaultAddress.setWidthFull();
	defaultAddress.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);
	defaultAddress.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	defaultAddress.getStyle().set(Props.PADDING, null);

	getBinder().forField(addressesAlias)
		.withValidator(description -> description
			.length() <= 100, getTranslation(FacilityAddrEntityMsg.CHK_ALIAS_LONG))
		.bind(FacilityAddress::getAddressesAlias, FacilityAddress::setAddressesAlias);
	getBinder().forField(address)
		.withValidator(description -> description
			.length() >= 5, getTranslation(FacilityAddrEntityMsg.CHK_ADDRESS_SHORT))
		.withValidator(description -> description
			.length() <= 250, getTranslation(FacilityAddrEntityMsg.CHK_ADDRESS_LONG))
		.asRequired(getTranslation(FacilityAddrEntityMsg.CHK_ADDRESS_SHORT))
		.bind(FacilityAddress::getAddress, FacilityAddress::setAddress);
	getBinder().forField(coordinates)
		.withValidator(description -> description
			.length() <= 50, getTranslation(FacilityAddrEntityMsg.CHK_GEO_LONG))
		.bind(FacilityAddress::getGeoCoordinates, FacilityAddress::setGeoCoordinates);
	getBinder().forField(defaultAddress)
		.bind(FacilityAddress::isDefaultAddress, FacilityAddress::setDefaultAddress);

	FormItem fi = addFormItem(addressesAlias, getTranslation(FacilityAddrEntityMsg.ALIAS));
	panelFields.setFlexGrow(1, fi);
	fi = addFormItem(address, getTranslation(FacilityAddrEntityMsg.ADDRESS));
	panelFields.setFlexGrow(1, fi);
	fi = addFormItem(coordinates, getTranslation(FacilityAddrEntityMsg.GEO));
	panelFields.setFlexGrow(1, fi);
	fi = addFormItem(defaultAddress, getTranslation(FacilityAddrEntityMsg.DEFAULT));
	panelFields.setFlexGrow(1, fi);

	panelFields.setMargin(false);
	panelFields.setPadding(false);
	panelFields.setSpacing(false);
	panelFields.getStyle().set(Props.MARGIN, Props.EM_0_5);

	add(panelFields);

	setSizeFull();
	setWidth(Props.EM_34);
	setMaxWidth(getWidth());
	setMinWidth(getWidth());

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void initBinder() {
	setBinder(new Binder(FacilityAddress.class));
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
	return "FacilityEditor [binder=" + getBinder() + ", name=" + addressesAlias + ", readOnlyMode="
		+ isReadOnlyMode()
		+ ", toString()=" + super.toString() + "]";
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
	return Objects.hash(getBinder(), addressesAlias, isReadOnlyMode());
    }

}
