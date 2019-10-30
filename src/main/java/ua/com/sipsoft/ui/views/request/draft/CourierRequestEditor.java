package ua.com.sipsoft.ui.views.request.draft;

import java.util.Collections;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBox.ItemFilter;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.common.Facility;
import ua.com.sipsoft.model.entity.common.FacilityAddress;
import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.services.common.FacilitiesService;
import ua.com.sipsoft.ui.commons.entityedit.AbstractBindedEntityEditor;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.messages.CourierRequestsMsg;

/**
 * The Class CourierRequestEditor.
 *
 * @param <T> the generic type
 */
@Scope("prototype")
@Tag("courier-request-editor-form")

/** The Constant log. */
@Slf4j
@SpringComponent
public class CourierRequestEditor<T extends CourierRequest> extends AbstractBindedEntityEditor<T> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3371271741841875886L;

    /** The author. */
    private TextField author;

    /** The facility from. */
    private ComboBox<Facility> facilityFrom;

    /** The from point. */
    @PropertyId("fromPoint")
    private ComboBox<FacilityAddress> fromPoint;

    /** The facility to. */
    private ComboBox<Facility> facilityTo;

    /** The to point. */
    @PropertyId("toPoint")
    private ComboBox<FacilityAddress> toPoint;

    /** The description. */
    private TextField description;

    /** The facilities service. */
    @Autowired
    private transient FacilitiesService facilitiesService;

    /** The filter facility addr. */
    private ItemFilter<FacilityAddress> filterFacilityAddr = (fas,
	    filterString) -> fas.getAddressesAlias().toLowerCase()
		    .contains(filterString.toLowerCase())
		    || fas.getAddress().toLowerCase()
			    .contains(filterString.toLowerCase());

    /**
     * Instantiates a new courier request editor.
     */
    CourierRequestEditor() {
	super();
	initBinder();
	log.info("Instantiates a new courier request editor");
	VerticalLayout panelFields = new VerticalLayout();
	author = new TextField();
	author.setEnabled(false);
	author.setRequiredIndicatorVisible(false);
	author.setWidth(Props.EM_19);
	author.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);
	author.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	author.getStyle().set(Props.PADDING, null);

	facilityFrom = new ComboBox<>();
	facilityFrom.setItems(Collections.emptyList());
	facilityFrom.setRequiredIndicatorVisible(true);
	facilityFrom.setRequired(true);
	facilityFrom.setWidth(Props.EM_19);
	facilityFrom.setPlaceholder(getTranslation(CourierRequestsMsg.SELECT_FACILITY_FROM));
	facilityFrom.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);
	facilityFrom.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	facilityFrom.getStyle().set(Props.PADDING, null);
	facilityFrom.setItemLabelGenerator(Facility::getName);
	facilityFrom.addValueChangeListener(this::facilityFromValueChangeListener);
	facilityFrom.focus();

	fromPoint = new ComboBox<>();
	fromPoint.setItems(Collections.emptyList());
	fromPoint.setRequiredIndicatorVisible(true);
	fromPoint.setRequired(true);
	fromPoint.setEnabled(false);
	fromPoint.setWidth(Props.EM_19);
	fromPoint.setPlaceholder(getTranslation(CourierRequestsMsg.SELECT_FACILITY_ADDR_FROM));
	fromPoint.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);
	fromPoint.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	fromPoint.getStyle().set(Props.PADDING, null);
	fromPoint.setItemLabelGenerator(FacilityAddress::getAddress);
	fromPoint.addValueChangeListener(this::facilityAddrFromValueChangeListener);
	fromPoint.setRenderer(new ComponentRenderer<>(fad -> {
	    VerticalLayout container = new VerticalLayout();

	    Label addAl = new Label(fad.getAddressesAlias());
	    container.add(addAl);

	    Label addr = new Label(fad.getAddress());
	    addr.getStyle().set("fontSize", "smaller");

	    Div div = new Div();
	    if (fad.isDefaultAddress()) {
		Icon icon = new Icon();
		icon = VaadinIcon.CHECK.create();
		icon.setColor("green");
		icon.setSize(Props.EM_0_9);
		div.add(icon);
	    }
	    div.add(addr);

	    container.add(div);
	    container.setMargin(false);
	    container.setPadding(false);
	    container.setSpacing(true);
	    return container;
	}));

	facilityTo = new ComboBox<>();
	facilityTo.setItems(Collections.emptyList());
	facilityTo.setRequired(true);
	facilityTo.setRequiredIndicatorVisible(true);
	facilityTo.setWidth(Props.EM_19);
	facilityTo.setPlaceholder(getTranslation(CourierRequestsMsg.FACILITY_TO));
	facilityTo.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);
	facilityTo.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	facilityTo.getStyle().set(Props.PADDING, null);
	facilityTo.setItemLabelGenerator(Facility::getName);
	facilityTo.addValueChangeListener(this::facilityToValueChangeListener);

	toPoint = new ComboBox<>();
	toPoint.setItems(Collections.emptyList());
	toPoint.setRequiredIndicatorVisible(true);
	toPoint.setRequired(true);
	toPoint.setEnabled(false);
	toPoint.setWidth(Props.EM_19);
	toPoint.setPlaceholder(getTranslation(CourierRequestsMsg.FACILITYADDR_TO));
	toPoint.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);
	toPoint.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	toPoint.getStyle().set(Props.PADDING, null);
	toPoint.setItemLabelGenerator(FacilityAddress::getAddress);
	toPoint.addValueChangeListener(this::facilityAddrToValueChangeListener);
	toPoint.setRenderer(new ComponentRenderer<>(fad -> {
	    VerticalLayout container = new VerticalLayout();

	    Label addAl = new Label(fad.getAddressesAlias());
	    container.add(addAl);

	    Label addr = new Label(fad.getAddress());
	    addr.getStyle().set("fontSize", "smaller");

	    Div div = new Div();
	    if (fad.isDefaultAddress()) {
		Icon icon = new Icon();
		icon = VaadinIcon.CHECK.create();
		icon.setColor("green");
		icon.setSize(Props.EM_0_9);
		div.add(icon);
	    }
	    div.add(addr);

	    container.add(div);
	    container.setMargin(false);
	    container.setPadding(false);
	    container.setSpacing(true);
	    return container;
	}));

	description = new TextField();
	description.setValueChangeMode(ValueChangeMode.EAGER);
	description.setRequired(true);
	description.setRequiredIndicatorVisible(true);
	description.setWidth(Props.EM_19);
	description.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);
	description.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	description.getStyle().set(Props.PADDING, null);

	getBinder().forField(author).bind(courierRequest -> courierRequest.getAuthor().getUsername(), null);
	getBinder().forField(fromPoint).asRequired(getTranslation(CourierRequestsMsg.SELECT_FACILITY_SENDER_CHECK))
		.bind("fromPoint");
	getBinder().forField(toPoint).asRequired(getTranslation(CourierRequestsMsg.SELECT_FACILITY_RECIEVER_CHECK))
		.bind("toPoint");
	getBinder().forField(description).asRequired(getTranslation(CourierRequestsMsg.SELECT_REQUEST_DESCR_CHECK))
		.withValidator(description -> description
			.length() <= 100, getTranslation(CourierRequestsMsg.SELECT_REQUEST_DESCR_CHECK_LONG))
		.withValidator(
			description -> description.length() >= 10,
			getTranslation(CourierRequestsMsg.SELECT_REQUEST_DESCR_CHECK))
		.bind(
			CourierRequest::getDescription,
			CourierRequest::setDescription);

	addFormItem(author, getTranslation(CourierRequestsMsg.AUTHOR));
	addFormItem(facilityFrom, getTranslation(CourierRequestsMsg.FACILITY_FROM));
	addFormItem(fromPoint, getTranslation(CourierRequestsMsg.FACILITYADDR_FROM));
	addFormItem(facilityTo, getTranslation(CourierRequestsMsg.FACILITY_TO));
	addFormItem(toPoint, getTranslation(CourierRequestsMsg.FACILITYADDR_TO));
	addFormItem(description, getTranslation(CourierRequestsMsg.DESCRIPTION));
	panelFields.setMargin(false);
	panelFields.setPadding(false);
	panelFields.setSpacing(false);
	panelFields.getStyle().set(Props.MARGIN, Props.EM_0_5);

	add(panelFields);

	setResponsiveSteps(new ResponsiveStep("0", 1));
	setMaxWidth(getWidth());
	setMinWidth(getWidth());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void initBinder() {
	setBinder(new Binder(CourierRequest.class));
    }

    /**
     * Post construct.
     */
    @PostConstruct
    private void postConstruct() {
	log.info("Post construct. Prepare dataproviders.");
	facilityFrom.setDataProvider(facilitiesService::getOrderedFilteredFacilities,
		facilitiesService::getOrderedFilteredFacilitiesCount);

	facilityTo.setDataProvider(facilitiesService::getOrderedFilteredFacilities,
		facilitiesService::getOrderedFilteredFacilitiesCount);
    }

    /**
     * Facility from value change listener.
     *
     * @param event the event
     */
    private void facilityFromValueChangeListener(ComponentValueChangeEvent<ComboBox<Facility>, Facility> event) {
	log.info("Value of Field Facility From has been changed");
	fromPoint.setItems(Collections.emptyList());
	fromPoint.setEnabled(!event.getSource().isEmpty());

	if (event.getSource().isEmpty()) {
	    log.debug("Value of Field Facility is empty");
	    return;
	}
	log.debug("Try to set Value of Field Facility Address From has been changed");
	Set<FacilityAddress> facilityAddrSet = event.getSource().getValue().getFacilityAddresses();
	if (facilityAddrSet != null && !(facilityAddrSet.isEmpty())) {
	    fromPoint.setItems(filterFacilityAddr, event.getSource().getValue().getFacilityAddresses());
	    FacilityAddress facilityAddress = facilityAddrSet.stream()
		    .filter(FacilityAddress::isDefaultAddress)
		    .findFirst()
		    .orElse(facilityAddrSet.stream()
			    .findFirst()
			    .orElse(null));
	    if (facilityAddress != null) {
		fromPoint.setValue(facilityAddress);
	    }
	}
    }

    /**
     * Facility to value change listener.
     *
     * @param event the event
     */
    private void facilityToValueChangeListener(ComponentValueChangeEvent<ComboBox<Facility>, Facility> event) {
	log.info("Value of Field Facility To has been changed");

	toPoint.setItems(Collections.emptyList());
	toPoint.setEnabled(!event.getSource().isEmpty());
	if (event.getSource().isEmpty()) {
	    log.debug("Value of Field Facility is empty");
	    return;
	}
	log.debug("Try to set Value of Field Facility Address To has been changed");
	Set<FacilityAddress> facilityAdrrSet = event.getSource().getValue().getFacilityAddresses();
	toPoint.setItems(filterFacilityAddr, event.getSource().getValue().getFacilityAddresses());
	if (facilityAdrrSet != null && !facilityAdrrSet.isEmpty()) {
	    FacilityAddress facilityAddress = facilityAdrrSet.stream()
		    .filter(FacilityAddress::isDefaultAddress)
		    .findFirst()
		    .orElse(facilityAdrrSet.stream()
			    .findFirst()
			    .orElse(null));
	    if (facilityAddress != null) {
		toPoint.setValue(facilityAddress);
	    }
	}
    }

    /**
     * Facility addr from value change listener.
     *
     * @param event the event
     */
    private void facilityAddrFromValueChangeListener(
	    ComponentValueChangeEvent<ComboBox<FacilityAddress>, FacilityAddress> event) {
	log.info("Facility addr from value has been changed");
	if (facilityFrom.getValue() == facilityFrom.getEmptyValue()
		&& fromPoint.getValue() != fromPoint.getEmptyValue()) {
	    FacilityAddress fa = fromPoint.getValue();
	    facilityFrom.setValue(fa.getFacility());
	    fromPoint.setValue(fa);
	}
    }

    /**
     * Facility addr to value change listener.
     *
     * @param event the event
     */
    private void facilityAddrToValueChangeListener(
	    ComponentValueChangeEvent<ComboBox<FacilityAddress>, FacilityAddress> event) {
	log.info("Facility addr to value has been changed");
	if (facilityTo.getValue() == facilityTo.getEmptyValue()
		&& toPoint.getValue() != toPoint.getEmptyValue()) {
	    FacilityAddress fa = toPoint.getValue();
	    facilityTo.setValue(fa.getFacility());
	    toPoint.setValue(fa);
	}
    }
}
