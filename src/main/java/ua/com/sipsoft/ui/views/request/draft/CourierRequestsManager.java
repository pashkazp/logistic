package ua.com.sipsoft.ui.views.request.draft;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridMultiSelectionModel.SelectAllCheckboxVisibility;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.model.entity.requests.draft.DraftRouteSheet;
import ua.com.sipsoft.model.entity.requests.draft.DraftRouteSheetEvent;
import ua.com.sipsoft.services.requests.draft.CourierRequestFilter;
import ua.com.sipsoft.services.requests.draft.CourierRequestService;
import ua.com.sipsoft.services.requests.draft.DraftRouteSheetFilter;
import ua.com.sipsoft.services.requests.draft.DraftRouteSheetService;
import ua.com.sipsoft.ui.MainView;
import ua.com.sipsoft.ui.commons.AppNotificator;
import ua.com.sipsoft.ui.commons.dialogform.DialogForm;
import ua.com.sipsoft.ui.commons.dialogform.Modality;
import ua.com.sipsoft.ui.views.request.common.HistoryEventViever;
import ua.com.sipsoft.utils.AppURL;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.history.CourierRequestSnapshot;
import ua.com.sipsoft.utils.messages.AppNotifyMsg;
import ua.com.sipsoft.utils.messages.AppTitleMsg;
import ua.com.sipsoft.utils.messages.ButtonMsg;
import ua.com.sipsoft.utils.messages.CourierRequestsMsg;
import ua.com.sipsoft.utils.messages.DraftRouteSheetMsg;
import ua.com.sipsoft.utils.messages.GridToolMsg;
import ua.com.sipsoft.utils.messages.HistoryEventMsg;
import ua.com.sipsoft.utils.security.SecurityUtils;

/**
 * The Class CourierRequestsManager.
 *
 * @author Pavlo Degtyaryev
 */

/** The Constant log. */
@Slf4j
@UIScope
@SpringComponent
@Route(value = AppURL.DRAFT_SHEETS, layout = MainView.class)
public class CourierRequestsManager extends VerticalLayout implements HasDynamicTitle {

    /** The Constant W_44EM. */
    private static final String W_44EM = "44EM";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -497333939332082677L;

    /** The btn draft sheet add. */
    private Button btnDraftSheetAdd;

    /** The btn draft sheet issue. */
    private Button btnDraftSheetIssue;

    /** The btn draft sheet edt. */
    private Button btnDraftSheetEdt;

    /** The btn draft sheet del. */
    private Button btnDraftSheetDel;

    /** The btn draft sheet grid reset. */
    private Button btnDraftSheetGridReset;

    /** The btn request create and add. */
    private Button btnRequestCreateAndAdd;

    /** The btn sheet request del. */
    private Button btnSheetRequestDel;

    /** The btn sheet request edt. */
    private Button btnSheetRequestEdt;

    /** The btn requests from sheet. */
    private Button btnRequestsFromSheet;

    /** The btn sheet requests grid reset. */
    private Button btnSheetRequestsGridReset;

    /** The btn request add. */
    private Button btnRequestAdd;

    /** The btn request del. */
    private Button btnRequestDel;

    /** The btn request edt. */
    private Button btnRequestEdt;

    /** The btn request to sheet. */
    private Button btnRequestToSheet;

    /** The btn request grid reset. */
    private Button btnRequestGridReset;

    /** The draft sheets grid. */
    private Grid<DraftRouteSheet> draftSheetsGrid;

    /** The linked courier requests grid. */
    private Grid<CourierRequest> linkedCourierRequestsGrid;

    /** The courier requests grid. */
    private Grid<CourierRequest> allCourierRequestsGrid;

    /** The field draft sheet filter. */
    private TextField fieldDraftSheetFilter;

    /** The field linked requests filter. */
    private TextField fieldLinkedRequestsFilter;

    /** The field requests filter. */
    private TextField fieldAllRequestsFilter;

    /**
     * Sets the draft route sheet service.
     *
     * @param draftRouteSheetService the new draft route sheet service
     */
    @Setter
    private transient DraftRouteSheetService draftRouteSheetService;

    /**
     * Sets the courier request service.
     *
     * @param courierRequestService the new courier request service
     */
    @Setter
    private transient CourierRequestService courierRequestService;

    /** The route sheets data provider. */
    private DataProvider<DraftRouteSheet, DraftRouteSheetFilter> routeSheetsDataProvider;

    /** The selected courier request data provider. */
    private DataProvider<CourierRequest, CourierRequestFilter> linkedCourierRequestDataProvider;

    /** The courier request data provider. */
    private DataProvider<CourierRequest, CourierRequestFilter> allCourierRequestDataProvider;

    /**
     * Instantiates a new courier requests manager.
     *
     * @param draftRouteSheetService the draft route sheet service
     * @param courierRequestService  the courier request service
     */
    @Autowired
    public CourierRequestsManager(DraftRouteSheetService draftRouteSheetService,
	    CourierRequestService courierRequestService) {
	super();
	log.info("Instantiates a courier requests manager.");
	this.draftRouteSheetService = draftRouteSheetService;
	this.courierRequestService = courierRequestService;

	SplitLayout leftSplitPanel = new SplitLayout();
	leftSplitPanel.setOrientation(Orientation.VERTICAL);
	leftSplitPanel.addToPrimary(getPanelDraftSheetGrigPresenter());
	leftSplitPanel.addToSecondary(getPanelRequestSheetGrigPresenter());

	SplitLayout background = new SplitLayout();
	background.addToPrimary(leftSplitPanel);
	background.addToSecondary(getPanelRequestsGridPresenter());
	add(background);
	setAlignItems(Alignment.STRETCH);
	setFlexGrow(1, background);
	setSizeFull();

	background.getStyle().set(Props.MARGIN, Props.EM_0_5);
	background.getStyle().set(Props.PADDING, null);

	background.setSplitterPosition(60.0);
	leftSplitPanel.setSplitterPosition(65.0);

	prepareDraftSheetGrid();
	prepareLinkedRequestsGrid();
	prepareAllRequestsGrid();
    }

    /**
     * Gets the panel draft sheet grig presenter.
     *
     * @return the panel draft sheet grig presenter
     */
    private Component getPanelDraftSheetGrigPresenter() {
	log.info("Instantiates Draft Sheet Grid Presenter");
	btnDraftSheetAdd = new Button(UIIcon.BTN_ADD.createIcon());
	btnDraftSheetDel = new Button(UIIcon.BTN_NO.createIcon());
	btnDraftSheetEdt = new Button(UIIcon.BTN_EDIT.createIcon());
	btnDraftSheetIssue = new Button(UIIcon.SHEET_ISSUED.createIcon());
	btnDraftSheetGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());

	fieldDraftSheetFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldDraftSheetFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnDraftSheetAdd.setSizeUndefined();
	btnDraftSheetEdt.setSizeUndefined();
	btnDraftSheetDel.setSizeUndefined();
	btnDraftSheetIssue.setSizeUndefined();
	btnDraftSheetGridReset.setSizeUndefined();
	btnDraftSheetAdd.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnDraftSheetEdt.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnDraftSheetDel.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnDraftSheetIssue.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnDraftSheetGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnDraftSheetGridReset.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);

	HorizontalLayout panelSheetsToolbar = new HorizontalLayout(fieldDraftSheetFilter, btnDraftSheetAdd,
		btnDraftSheetDel, btnDraftSheetEdt, btnDraftSheetIssue, btnDraftSheetGridReset);
	panelSheetsToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelSheetsToolbar.setFlexGrow(0, btnDraftSheetAdd);
	panelSheetsToolbar.setFlexGrow(0, btnDraftSheetEdt);
	panelSheetsToolbar.setFlexGrow(0, btnDraftSheetDel);
	panelSheetsToolbar.setFlexGrow(0, btnDraftSheetIssue);
	panelSheetsToolbar.setFlexGrow(0, btnDraftSheetGridReset);
	panelSheetsToolbar.setFlexGrow(1, fieldDraftSheetFilter);
	panelSheetsToolbar.setMargin(false);
	panelSheetsToolbar.setPadding(false);
	panelSheetsToolbar.setSpacing(true);

	draftSheetsGrid = new Grid<>(DraftRouteSheet.class);
	draftSheetsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

	VerticalLayout panelDraftSheetsGrigPresenter = new VerticalLayout(panelSheetsToolbar, draftSheetsGrid);
	panelDraftSheetsGrigPresenter.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	panelDraftSheetsGrigPresenter.setWidthFull();
	panelDraftSheetsGrigPresenter.setMargin(false);
	panelDraftSheetsGrigPresenter.setPadding(false);
	panelDraftSheetsGrigPresenter.setSpacing(false);

	HorizontalLayout panelDraftSheetsManage = new HorizontalLayout(panelDraftSheetsGrigPresenter);
	panelDraftSheetsManage.setFlexGrow(1, panelDraftSheetsGrigPresenter);
	panelDraftSheetsManage.setMargin(false);
	panelDraftSheetsManage.setPadding(false);
	panelDraftSheetsManage.setSpacing(false);

	btnDraftSheetAdd.addClickListener(e -> draftSheetAdd());
	btnDraftSheetDel.addClickListener(e -> draftSheetDel());
	btnDraftSheetEdt.addClickListener(e -> draftSheetEdit());
	btnDraftSheetIssue.addClickListener(e -> draftSheetIssue());

	return panelDraftSheetsGrigPresenter;
    }

    /**
     * Gets the panel request sheet grig presenter.
     *
     * @return the panel request sheet grig presenter
     */
    private Component getPanelRequestSheetGrigPresenter() {
	log.info("Instantiates Linked Courier Request Grid Presenter");

	btnRequestCreateAndAdd = new Button(UIIcon.BTN_ADD.createIcon());
	btnSheetRequestDel = new Button(UIIcon.BTN_NO.createIcon());
	btnSheetRequestEdt = new Button(UIIcon.BTN_EDIT.createIcon());
	btnRequestsFromSheet = new Button(UIIcon.BTN_REM_FROM_DRAFT.createIcon());
	btnSheetRequestsGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());

	btnRequestCreateAndAdd.setSizeUndefined();
	btnSheetRequestDel.setSizeUndefined();
	btnSheetRequestEdt.setSizeUndefined();
	btnRequestsFromSheet.setSizeUndefined();
	btnSheetRequestsGridReset.setSizeUndefined();

	fieldLinkedRequestsFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldLinkedRequestsFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnRequestCreateAndAdd.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnSheetRequestDel.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnSheetRequestEdt.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnRequestsFromSheet.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnSheetRequestsGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnSheetRequestsGridReset.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);

	HorizontalLayout panelSheetRequestsToolbar = new HorizontalLayout(fieldLinkedRequestsFilter,
		btnRequestCreateAndAdd, btnSheetRequestDel,
		btnSheetRequestEdt, btnRequestsFromSheet, btnSheetRequestsGridReset);

	panelSheetRequestsToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelSheetRequestsToolbar.setFlexGrow(0, btnRequestCreateAndAdd);
	panelSheetRequestsToolbar.setFlexGrow(0, btnSheetRequestDel);
	panelSheetRequestsToolbar.setFlexGrow(0, btnSheetRequestEdt);
	panelSheetRequestsToolbar.setFlexGrow(0, btnRequestsFromSheet);
	panelSheetRequestsToolbar.setFlexGrow(0, btnSheetRequestsGridReset);
	panelSheetRequestsToolbar.setFlexGrow(1, fieldLinkedRequestsFilter);
	panelSheetRequestsToolbar.setMargin(false);
	panelSheetRequestsToolbar.setPadding(false);
	panelSheetRequestsToolbar.setSpacing(false);

	linkedCourierRequestsGrid = new Grid<>(CourierRequest.class);
	linkedCourierRequestsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

	VerticalLayout panelSheetRequestsGrigPresenter = new VerticalLayout(panelSheetRequestsToolbar,
		linkedCourierRequestsGrid);
	panelSheetRequestsGrigPresenter.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	panelSheetRequestsGrigPresenter.setMargin(false);
	panelSheetRequestsGrigPresenter.setPadding(false);
	panelSheetRequestsGrigPresenter.setSpacing(false);

	HorizontalLayout panelSheetRequestsManage = new HorizontalLayout(panelSheetRequestsGrigPresenter);
	panelSheetRequestsManage.setFlexGrow(1, panelSheetRequestsGrigPresenter);
	panelSheetRequestsManage.setMargin(false);
	panelSheetRequestsManage.setPadding(false);
	panelSheetRequestsManage.setSpacing(false);

	btnRequestCreateAndAdd.addClickListener(e -> requestCreateAndAddtoSheet());
	btnSheetRequestDel.addClickListener(e -> courierRequestDel(linkedCourierRequestsGrid));
	btnSheetRequestEdt.addClickListener(e -> sheetsRequestEdit());
	btnRequestsFromSheet.addClickListener(e -> requestsRemoveFromDraftSheet());
	return panelSheetRequestsGrigPresenter;
    }

    /**
     * Gets the panel requests grid presenter.
     *
     * @return the panel requests grid presenter
     */
    private Component getPanelRequestsGridPresenter() {
	log.info("Instantiates All Courier Requests Grid Presenter");

	btnRequestAdd = new Button(UIIcon.BTN_ADD.createIcon());
	btnRequestDel = new Button(UIIcon.BTN_NO.createIcon());
	btnRequestEdt = new Button(UIIcon.BTN_EDIT.createIcon());
	btnRequestToSheet = new Button(UIIcon.BTN_ADD_TO_DRAFT.createIcon());
	btnRequestGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());
	btnRequestAdd.setSizeUndefined();
	btnRequestDel.setSizeUndefined();
	btnRequestEdt.setSizeUndefined();
	btnRequestToSheet.setSizeUndefined();
	btnRequestGridReset.setSizeUndefined();

	fieldAllRequestsFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldAllRequestsFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnRequestAdd.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnRequestDel.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnRequestEdt.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnRequestToSheet.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnRequestGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnRequestGridReset.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);

	HorizontalLayout panelRequestsToolbar = new HorizontalLayout(fieldAllRequestsFilter, btnRequestAdd,
		btnRequestDel,
		btnRequestEdt, btnRequestToSheet, btnRequestGridReset);

	panelRequestsToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelRequestsToolbar.setFlexGrow(0, btnRequestAdd);
	panelRequestsToolbar.setFlexGrow(0, btnRequestDel);
	panelRequestsToolbar.setFlexGrow(0, btnRequestEdt);
	panelRequestsToolbar.setFlexGrow(0, btnRequestToSheet);
	panelRequestsToolbar.setFlexGrow(0, btnRequestGridReset);
	panelRequestsToolbar.setFlexGrow(1, fieldAllRequestsFilter);
	panelRequestsToolbar.setMargin(false);
	panelRequestsToolbar.setPadding(false);
	panelRequestsToolbar.setSpacing(false);

	allCourierRequestsGrid = new Grid<>(CourierRequest.class);
	allCourierRequestsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

	VerticalLayout panelRequestsPresenter = new VerticalLayout(panelRequestsToolbar, allCourierRequestsGrid);
	panelRequestsPresenter.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	panelRequestsPresenter.setMargin(false);
	panelRequestsPresenter.setPadding(false);
	panelRequestsPresenter.setSpacing(false);

	btnRequestToSheet.addClickListener(e -> courierRequestsToDraftSheet());
	btnRequestAdd.addClickListener(e -> courierRequestsAdd());
	btnRequestDel.addClickListener(e -> courierRequestDel(allCourierRequestsGrid));
	btnRequestEdt.addClickListener(e -> courierRequestEdit());
	return panelRequestsPresenter;
    }

    /**
     * Gets the filtered draft route sheet query.
     *
     * @param query the query
     * @return the filtered draft route sheet query
     */
    private Stream<DraftRouteSheet> getFilteredDraftRouteSheetQuery(
	    Query<DraftRouteSheet, DraftRouteSheetFilter> query) {
	log.info("Gets the filtered draft route sheet query");
	return draftRouteSheetService.getQueriedDraftRouteSheets(
		query, getDraftRouteSheetFilter());
    }

    /**
     * Gets the filtered draft route sheet query count.
     *
     * @param query the query
     * @return the filtered draft route sheet query count
     */
    private int getFilteredDraftRouteSheetQueryCount(
	    Query<DraftRouteSheet, DraftRouteSheetFilter> query) {
	log.info("Gets the filtered draft route sheet query count");
	return draftRouteSheetService.getQueriedDraftRouteSheetsCount(
		query, getDraftRouteSheetFilter());
    }

    /**
     * Gets the draft route sheet filter.
     *
     * @return the draft route sheet filter
     */
    private DraftRouteSheetFilter getDraftRouteSheetFilter() {
	log.info("Gets the draft route sheet filter");
	return DraftRouteSheetFilter.builder()
		.description(getSanitizedDraftSheetFilter())
		.build();
    }

    /**
     * Gets the sanitized draft sheet filter.
     *
     * @return the sanitized draft sheet filter
     */
    private String getSanitizedDraftSheetFilter() {
	log.info("Gets the sanitized draft sheet filter");
	return StringUtils.truncate(fieldDraftSheetFilter.getValue(), 100);
    }

    /**
     * Prepare draft sheet grid.
     */
    private void prepareDraftSheetGrid() {
	log.info("Prepare draft sheet grid");
	routeSheetsDataProvider = DataProvider.fromFilteringCallbacks(
		this::getFilteredDraftRouteSheetQuery,
		this::getFilteredDraftRouteSheetQueryCount);
	draftSheetsGrid.setDataProvider(routeSheetsDataProvider.withConfigurableFilter());

	btnDraftSheetEdt.setEnabled(false);
	btnDraftSheetDel.setEnabled(false);
	btnDraftSheetIssue.setEnabled(false);

	fieldDraftSheetFilter.setValueChangeMode(ValueChangeMode.EAGER);
	fieldDraftSheetFilter.addValueChangeListener(e -> {
	    draftSheetsGrid.getSelectionModel().deselectAll();
	    draftSheetsGrid.getDataProvider().refreshAll();
	});

	draftSheetsGrid.removeAllColumns();

	draftSheetsGrid
		.addColumn("id")
		.setHeader(getTranslation(DraftRouteSheetMsg.REQUESTID))
		.setSortProperty("id")
		.setWidth(Props.EM_05)
		.setFlexGrow(0);
	draftSheetsGrid
		.addColumn("description")
		.setHeader(getTranslation(DraftRouteSheetMsg.DESCRIPTION))
		.setFlexGrow(1)
		.setSortProperty("description");
	draftSheetsGrid
		.addColumn(new LocalDateTimeRenderer<>(DraftRouteSheet::getCreationDate,
			DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
		.setHeader(getTranslation(DraftRouteSheetMsg.CREATION_DATE))
		.setWidth(Props.EM_09)
		.setFlexGrow(0)
		.setSortProperty("creationDate");
	draftSheetsGrid
		.addColumn("author.username")
		.setHeader(getTranslation(DraftRouteSheetMsg.AUTHOR))
		.setWidth(Props.EM_10)
		.setFlexGrow(0)
		.setSortProperty("author.username");

	draftSheetsGrid
		.addColumn(new ComponentRenderer<Icon, DraftRouteSheet>(request -> {
		    Icon iconInfo = UIIcon.INFO.createIcon();
		    iconInfo.setSize(Props.EM_01);
		    iconInfo.addClickListener(event -> ConfirmDialog
			    .create()
			    .withCaption(getTranslation(HistoryEventMsg.HEADER_VEIW))
			    .withMessage(new HistoryEventViever<>(new Grid<>(DraftRouteSheetEvent.class),
				    request.getHistoryEvents()))
			    .withCloseButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CLOSE)),
				    ButtonOption.icon(UIIcon.BTN_OK.getIcon()))
			    .open());
		    return iconInfo;
		}))
		.setHeader(getTranslation(GridToolMsg.INFO_COLUMN))
		.setWidth(Props.EM_05)
		.setFlexGrow(0);

	draftSheetsGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});

	draftSheetsGrid.setColumnReorderingAllowed(true);
	draftSheetsGrid.addSelectionListener(event -> {
	    DraftRouteSheet draftRouteSheet = event.getFirstSelectedItem().orElse(null);

	    linkedCourierRequestsGrid.getSelectionModel().deselectAll();
	    linkedCourierRequestDataProvider.refreshAll();

	    btnRequestCreateAndAdd.setEnabled(!event.getAllSelectedItems().isEmpty());

	    if (draftRouteSheet == null) {
		btnDraftSheetEdt.setEnabled(false);
		btnDraftSheetDel.setEnabled(false);
		btnDraftSheetIssue.setEnabled(false);

		btnRequestToSheet.setEnabled(false);

	    } else {
		btnDraftSheetEdt.setEnabled(true);
		btnDraftSheetDel.setEnabled(true);
		btnDraftSheetIssue.setEnabled(!draftRouteSheet.getRequests().isEmpty());

		btnRequestToSheet.setEnabled(!allCourierRequestsGrid.getSelectionModel().getSelectedItems().isEmpty());
	    }

	});

	fieldDraftSheetFilter.addValueChangeListener(e -> draftSheetsGrid.getDataProvider().refreshAll());
	draftSheetsGrid.setMultiSort(true);
	btnDraftSheetGridReset.addClickListener(e -> refreshRouteSheetGrid());
    }

    /**
     * Gets the linked courier request query count.
     *
     * @param query the query
     * @return the filtered linked courier request query count
     */
    private int getLinkedCourierRequestQueryCount(
	    Query<CourierRequest, CourierRequestFilter> query) {
	log.info("Gets the linked selected courier request query count");
	return courierRequestService.getQueriedCourierRequestsByFilterCount(
		query, getLinkedCourierRequestFilter());
    }

    /**
     * Gets the linked linked courier request query.
     *
     * @param query the query
     * @return the filtered linked courier request query
     */
    private Stream<CourierRequest> getLinkedCourierRequestQuery(
	    Query<CourierRequest, CourierRequestFilter> query) {
	log.info("Gets the linked courier request query");
	return courierRequestService.getQueriedCourierRequestsByFilter(
		query, getLinkedCourierRequestFilter());
    }

    /**
     * Gets the linked courier request filter.
     *
     * @return the linked courier request filter
     */
    private CourierRequestFilter getLinkedCourierRequestFilter() {
	log.info("Gets filter for linked courier requests");
	return CourierRequestFilter.builder()
		.sheetId(draftSheetsGrid.getSelectionModel().getFirstSelectedItem()
			.map(DraftRouteSheet::getId).orElse(-1L))
		.description(getSanitizedLinkedRequestFilter())
		.build();
    }

    /**
     * Gets the sanitized linked requests filter.
     *
     * @return the sanitized linked requests filter
     */
    private String getSanitizedLinkedRequestFilter() {
	log.info("Gets sanitized filter for linked courier requests");
	return StringUtils.truncate(fieldLinkedRequestsFilter.getValue(), 100);
    }

    /**
     * Prepare linked requests grid.
     */
    private void prepareLinkedRequestsGrid() {
	log.info("Prepare linked requests grid");
	linkedCourierRequestDataProvider = DataProvider.fromFilteringCallbacks(
		this::getLinkedCourierRequestQuery,
		this::getLinkedCourierRequestQueryCount);
	linkedCourierRequestsGrid.setDataProvider(linkedCourierRequestDataProvider.withConfigurableFilter());

	btnRequestCreateAndAdd.setEnabled(false);
	btnSheetRequestEdt.setEnabled(false);
	btnSheetRequestDel.setEnabled(false);
	btnRequestsFromSheet.setEnabled(false);

	fieldLinkedRequestsFilter.setValueChangeMode(ValueChangeMode.EAGER);
	fieldLinkedRequestsFilter
		.addValueChangeListener(e -> {
		    linkedCourierRequestsGrid.getSelectionModel().deselectAll();
		    linkedCourierRequestsGrid.getDataProvider().refreshAll();
		});

	linkedCourierRequestsGrid.removeAllColumns();

	linkedCourierRequestsGrid
		.addColumn("id")
		.setHeader(getTranslation(CourierRequestsMsg.REQUESTID))
		.setSortProperty("request.id")
		.setWidth(Props.EM_05)
		.setFlexGrow(0);
	linkedCourierRequestsGrid
		.addColumn(new ComponentRenderer<>(CourierRequestPresenter::new))
		.setHeader(getTranslation(CourierRequestsMsg.REQUEST))
		.setFlexGrow(1);

	linkedCourierRequestsGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});
	linkedCourierRequestsGrid.getColumns().forEach(column -> column.setSortable(true));
	linkedCourierRequestsGrid.setColumnReorderingAllowed(true);

	GridMultiSelectionModel<CourierRequest> multiSelection = (GridMultiSelectionModel<CourierRequest>) linkedCourierRequestsGrid
		.setSelectionMode(Grid.SelectionMode.MULTI);
	multiSelection.setSelectAllCheckboxVisibility(SelectAllCheckboxVisibility.VISIBLE);
	multiSelection.setSelectionColumnFrozen(true);
	multiSelection.addMultiSelectionListener(event -> {
	    btnSheetRequestEdt.setEnabled(event.getAllSelectedItems().size() == 1);
	    btnSheetRequestDel.setEnabled(!event.getAllSelectedItems().isEmpty());
	    btnRequestsFromSheet.setEnabled(!event.getAllSelectedItems().isEmpty());
	});
	btnSheetRequestsGridReset.addClickListener(e -> refreshLinkedCourierRequestGrid());
    }

    /**
     * Gets the filtered courier request query count.
     *
     * @param query the query
     * @return the filtered courier request query count
     */
    private int getFilteredCourierRequestQueryCount(
	    Query<CourierRequest, CourierRequestFilter> query) {
	log.info("Gets the filtered courier request query count");
	return courierRequestService.getQueriedCourierRequestsByFilterCount(
		query, getCourierRequestFilter());
    }

    /**
     * Gets the filtered courier request query.
     *
     * @param query the query
     * @return the filtered courier request query
     */
    private Stream<CourierRequest> getFilteredCourierRequestQuery(
	    Query<CourierRequest, CourierRequestFilter> query) {
	log.info("Gets the filtered courier request query");
	return courierRequestService.getQueriedCourierRequestsByFilter(
		query, getCourierRequestFilter());
    }

    /**
     * Gets the courier request filter.
     *
     * @return the courier request filter
     */
    private CourierRequestFilter getCourierRequestFilter() {
	log.info("Gets the courier request filter");
	return CourierRequestFilter.builder()
		.description(getSanitizedCourierRequestFilter())
		.build();
    }

    /**
     * Gets the sanitized courier request filter.
     *
     * @return the sanitized courier request filter
     */
    private String getSanitizedCourierRequestFilter() {
	log.info("Gets the sanitized courier request filter");
	return StringUtils.truncate(fieldAllRequestsFilter.getValue(), 100);
    }

    /**
     * Prepare requests grid.
     */
    private void prepareAllRequestsGrid() {
	log.info("Prepare all courier requests grid");

	allCourierRequestDataProvider = DataProvider.fromFilteringCallbacks(
		this::getFilteredCourierRequestQuery,
		this::getFilteredCourierRequestQueryCount);
	allCourierRequestsGrid.setDataProvider(allCourierRequestDataProvider.withConfigurableFilter());

	btnRequestDel.setEnabled(false);
	btnRequestEdt.setEnabled(false);
	btnRequestToSheet.setEnabled(false);

	fieldAllRequestsFilter.setValueChangeMode(ValueChangeMode.EAGER);
	fieldAllRequestsFilter.addValueChangeListener(e -> {
	    allCourierRequestsGrid.getSelectionModel().deselectAll();
	    allCourierRequestsGrid.getDataProvider().refreshAll();
	});

	allCourierRequestsGrid.removeAllColumns();

	allCourierRequestsGrid
		.addColumn("id")
		.setHeader(getTranslation(CourierRequestsMsg.REQUESTID))
		.setSortProperty("id")
		.setWidth(Props.EM_05)
		.setFlexGrow(0);
	allCourierRequestsGrid
		.addColumn(new ComponentRenderer<>(CourierRequestPresenter::new))
		.setHeader(getTranslation(CourierRequestsMsg.REQUEST))
		.setFlexGrow(1);

	allCourierRequestsGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});
	allCourierRequestsGrid.setColumnReorderingAllowed(true);

	GridMultiSelectionModel<CourierRequest> multiSelection = (GridMultiSelectionModel<CourierRequest>) allCourierRequestsGrid
		.setSelectionMode(Grid.SelectionMode.MULTI);
	multiSelection.setSelectAllCheckboxVisibility(SelectAllCheckboxVisibility.VISIBLE);
	multiSelection.setSelectionColumnFrozen(true);
	multiSelection.addMultiSelectionListener(event -> {
	    btnRequestEdt.setEnabled(event.getAllSelectedItems().size() == 1);
	    btnRequestDel.setEnabled(!event.getAllSelectedItems().isEmpty());
	    btnRequestToSheet.setEnabled(!(event.getAllSelectedItems().isEmpty()
		    || draftSheetsGrid.getSelectionModel().getSelectedItems().isEmpty()));
	});
	btnRequestGridReset.addClickListener(e -> refreshCourierRequestGrid());
    }

    /**
     * Creates and get Courier request editor.
     *
     * @return the courier request editor
     */
    @Lookup
    CourierRequestEditor<CourierRequest> getCourierRequestEditor() {
	log.info("Create CourierRequestEditor");
	return null;
    }

    @Lookup
    DialogForm getDialogForm() {
	return null;}

    /**
     * Draft sheet add.
     */
    private void draftSheetAdd() {
	log.info("Try to add Draft Route Sheet");
	TextField description = new TextField();

	description.focus();
	description.setWidth(W_44EM);
	description.setRequired(true);
	description.setRequiredIndicatorVisible(true);
	description.setMinLength(10);
	description.setValueChangeMode(ValueChangeMode.EAGER);
	description.addValueChangeListener(e -> description.setInvalid(e.getValue().length() < 10));
	description.setValue("");
	description.setInvalid(description.getValue().length() < 10);
	description.setErrorMessage(getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));

	VerticalLayout panel = new VerticalLayout(
		new Label(getTranslation(DraftRouteSheetMsg.DRAFT_ROUTE_SHEET_CREATE_MSG)),
		description);
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	ConfirmDialog
		.create()
		.withCaption(getTranslation(DraftRouteSheetMsg.DRAFT_ROUTE_SHEET_CREATE_HEADER))
		.withMessage(panel)
		.withSaveButton(() -> {
		    try {
			if (description.isInvalid()) {
			    AppNotificator.notify(getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
			    return;
			}
			log.info("Perform to add Draft Route Sheet");
			DraftRouteSheet draftRouteSheetIn = new DraftRouteSheet();
			draftRouteSheetIn.setAuthor(SecurityUtils.getUser());
			draftRouteSheetIn.setDescription(description.getValue());
			draftRouteSheetIn.addHistoryEvent(
				new StringBuilder()
					.append("Чернетку маршрутного листа з описом \"")
					.append(description.getValue())
					.append("\" було створено.")
					.toString(),
				draftRouteSheetIn.getCreationDate(), SecurityUtils.getUser());
			Optional<DraftRouteSheet> draftRouteSheetOut = draftRouteSheetService
				.save(draftRouteSheetIn);
			if (draftRouteSheetOut.isPresent()) {
			    draftSheetsGrid.getSelectionModel().deselectAll();
			    draftSheetsGrid.getDataProvider().refreshAll();
			    draftSheetsGrid.select(draftRouteSheetOut.get());
			    AppNotificator.notify(getTranslation(AppNotifyMsg.DRAFT_ROUTE_SHEET_CREATED));
			}
		    } catch (Exception e) {
			AppNotificator.notify(5000, e.getMessage());
		    }

		}, ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_SAVE)),
			ButtonOption.icon(UIIcon.BTN_PUT.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * Draft sheet edit.
     */
    private void draftSheetEdit() {
	log.info("Try to add Edit Route Sheet");
	Optional<DraftRouteSheet> draftSheetInO = draftSheetsGrid.getSelectionModel().getFirstSelectedItem();
	if (!draftSheetInO.isPresent()) {
	    return;
	}
	draftSheetInO = draftRouteSheetService.fetchById(draftSheetInO.get().getId());
	if (!draftSheetInO.isPresent()) {
	    AppNotificator.notify(5000, getTranslation(AppNotifyMsg.DRAFT_NOT_FOUND));
	    return;
	}

	DraftRouteSheet draftRouteSheetIn = draftSheetInO.get();
	TextField description = new TextField();
	description.focus();
	description.setWidth(W_44EM);
	description.setRequired(true);
	description.setRequiredIndicatorVisible(true);
	description.setMinLength(10);
	description.setValueChangeMode(ValueChangeMode.EAGER);
	description.addValueChangeListener(e -> description.setInvalid(e.getValue().length() < 10));
	description.setValue(draftRouteSheetIn.getDescription());
	description.setInvalid(draftRouteSheetIn.getDescription().length() < 10);
	description.setErrorMessage(getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
	VerticalLayout panel = new VerticalLayout(
		new Label(getTranslation(DraftRouteSheetMsg.DRAFT_ROUTE_SHEET_EDIT_MSG)),
		description);
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);

	ConfirmDialog
		.create()
		.withCaption(getTranslation(DraftRouteSheetMsg.DRAFT_ROUTE_SHEET_EDIT_HEADER))
		.withMessage(panel)
		.withSaveButton(() -> {
		    if (description.isInvalid()) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
			return;
		    }
		    try {
			if (!draftRouteSheetIn.getDescription().equals(description.getValue())) {
			    log.info("Perform to save changes for Draft Route Sheet");
			    draftRouteSheetIn.addHistoryEvent(
				    new StringBuilder()
					    .append("Змінено опис чернетки маршрутного листа з \"")
					    .append(draftRouteSheetIn.getDescription())
					    .append("\" на \"")
					    .append(description.getValue())
					    .append("\"")
					    .toString(),
				    LocalDateTime.now(),
				    SecurityUtils.getUser());
			    draftRouteSheetIn.setDescription(description.getValue());
			}
			Optional<DraftRouteSheet> draftRouteSheetOut = draftRouteSheetService.save(draftRouteSheetIn);
			if (draftRouteSheetOut.isPresent()) {
			    draftSheetsGrid.getDataProvider().refreshItem(draftRouteSheetOut.get());
			    AppNotificator.notify(getTranslation(AppNotifyMsg.DRAFT_ROUTE_SHEET_SAVED));
			}
		    } catch (Exception e) {
			AppNotificator.notify(5000, e.getMessage());
		    }

		}, ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_SAVE)),
			ButtonOption.icon(UIIcon.BTN_PUT.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * Draft sheet del.
     */
    private void draftSheetDel() {
	log.info("Try to add Delete Route Sheet");
	Optional<DraftRouteSheet> draftSheetInO = draftSheetsGrid.getSelectionModel().getFirstSelectedItem();
	if (!draftSheetInO.isPresent()) {
	    return;
	}
	draftSheetInO = draftRouteSheetService.fetchById(draftSheetInO.get().getId());
	if (!draftSheetInO.isPresent()) {
	    AppNotificator.notify(5000, getTranslation(AppNotifyMsg.DRAFT_NOT_FOUND));
	    return;
	}

	DraftRouteSheet draftRouteSheetIn = draftSheetInO.get();
	TextField description = new TextField();
	description.focus();
	description.setWidth(W_44EM);
	description.setRequired(true);
	description.setRequiredIndicatorVisible(true);
	description.setMinLength(10);
	description.setValueChangeMode(ValueChangeMode.EAGER);
	description.addValueChangeListener(e -> description.setInvalid(e.getValue().length() < 10));
	description.setValue("");
	description.setInvalid(true);
	description.setErrorMessage(getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
	VerticalLayout panel = new VerticalLayout(
		new Label(getTranslation(DraftRouteSheetMsg.DRAFT_ROUTE_SHEET_DEL_MSG)), description);
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	ConfirmDialog
		.create()
		.withCaption(getTranslation(DraftRouteSheetMsg.DRAFT_ROUTE_SHEET_DEL_HEADER))
		.withMessage(panel)
		.withSaveButton(() -> {
		    try {
			if (description.isInvalid()) {
			    AppNotificator.notify(getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
			    return;
			}

			draftRouteSheetService.moveToArchive(draftRouteSheetIn, description.getValue(),
				SecurityUtils.getUser());
			draftSheetsGrid.getDataProvider().refreshAll();
			AppNotificator.notify(getTranslation(AppNotifyMsg.DRAFT_ROUTE_SHEET_DELETED));

		    } catch (Exception e) {
			AppNotificator.notify(5000, e.getMessage());
		    }

		}, ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_DELETE)),
			ButtonOption.icon(UIIcon.BTN_DEL.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * Draft sheet issue.
     */
    private void draftSheetIssue() {
	Optional<DraftRouteSheet> draftSheetInO = draftSheetsGrid.getSelectionModel().getFirstSelectedItem();
	if (!draftSheetInO.isPresent() || draftSheetInO.get().getRequests().isEmpty()) {
	    return;
	}
	draftSheetInO = draftRouteSheetService.fetchById(draftSheetInO.get().getId());
	if (!draftSheetInO.isPresent()) {
	    AppNotificator.notify(5000, getTranslation(AppNotifyMsg.DRAFT_NOT_FOUND));
	    return;
	}
	DraftRouteSheet draftRouteSheetIn = draftSheetInO.get();
	TextField description = new TextField();
	description.focus();
	description.setWidth(W_44EM);
	description.setRequired(true);
	description.setRequiredIndicatorVisible(true);
	description.setMinLength(10);
	description.setValueChangeMode(ValueChangeMode.EAGER);
	description.setValue(draftRouteSheetIn.getDescription());
	description.addValueChangeListener(e -> description.setInvalid(e.getValue().length() < 10));
	description.setInvalid(draftRouteSheetIn.getDescription().length() < 10);
	description.setErrorMessage(getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
	VerticalLayout panel = new VerticalLayout(
		new Label(getTranslation(DraftRouteSheetMsg.DRAFT_ROUTE_SHEET_ISSUE_MSG)),
		description);
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	ConfirmDialog
		.create()
		.withCaption(getTranslation(DraftRouteSheetMsg.DRAFT_ROUTE_SHEET_ISSUE_HEADER))
		.withMessage(panel)
		.withSaveButton(() -> {
		    try {
			if (description.isInvalid()) {
			    AppNotificator.notify(getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
			    return;
			}

			draftRouteSheetService.issueDraftRouteSheet(draftRouteSheetIn, description.getValue(),
				SecurityUtils.getUser());

//						draftSheetsGrid.getDataProvider().refreshAll();
			routeSheetsDataProvider.refreshAll();
			linkedCourierRequestDataProvider.refreshAll();
			allCourierRequestDataProvider.refreshAll();

			AppNotificator.notify(getTranslation(AppNotifyMsg.DRAFT_ROUTE_SHEET_ISSUED));
		    } catch (Exception e) {
			AppNotificator.notify(5000, e.getMessage());
		    }

		}, ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_YES)),
			ButtonOption.icon(UIIcon.SHEET_ISSUED.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * Request create and addto sheet.
     */
    private void requestCreateAndAddtoSheet() {
	Optional<DraftRouteSheet> draftSheetInO = draftSheetsGrid.getSelectionModel().getFirstSelectedItem();
	if (!draftSheetInO.isPresent()) {
	    return;
	}
	draftSheetInO = draftRouteSheetService.fetchById(draftSheetInO.get().getId());
	if (!draftSheetInO.isPresent()) {
	    AppNotificator.notify(5000, getTranslation(AppNotifyMsg.DRAFT_NOT_FOUND));
	    return;
	}
	DraftRouteSheet draftRouteSheetIn = draftSheetInO.get();

	CourierRequest courierRequestIn = new CourierRequest();
	courierRequestIn.setAuthor(SecurityUtils.getUser());

	CourierRequestEditor<CourierRequest> editor = getCourierRequestEditor();
	editor.setOperationData(courierRequestIn);

	DialogForm dialogForm = getDialogForm();
	dialogForm

		.withDataEditor(editor)
		.withHeader(getTranslation(CourierRequestsMsg.ADD))
		.withWidth(Props.EM_28)
		.withModality(Modality.MR_SAVE, event -> {
		    if (!editor.isValidOperationData()) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.COURIER_REQ_CHK_FAIL));
		    } else {
			try {
			    Optional<DraftRouteSheet> draftSheetInR = draftRouteSheetService
				    .fetchById(draftRouteSheetIn.getId());
			    if (!draftSheetInR.isPresent()) {
				AppNotificator.notify(5000, getTranslation(AppNotifyMsg.DRAFT_NOT_FOUND));
				return;
			    }

			    Optional<CourierRequest> courierRequestOut = courierRequestService
				    .addRequest(editor.getOperationData(), SecurityUtils.getUser());
			    if (courierRequestOut.isPresent()) {
				Optional<DraftRouteSheet> draftRouteSheetOut = draftRouteSheetService
					.addCourierRequestsAndSave(draftRouteSheetIn, SecurityUtils.getUser(),
						courierRequestOut.get());
				if (draftRouteSheetOut.isPresent()) {
				    draftSheetsGrid.getDataProvider().refreshItem(draftRouteSheetOut.get());
				    allCourierRequestsGrid.getDataProvider().refreshAll();
				    linkedCourierRequestsGrid.getDataProvider().refreshAll();
				}
				dialogForm.closeWithResult(Modality.MR_SAVE);
			    }
			} catch (Exception e) {
			    AppNotificator.notify(5000, e.getMessage());
			}
		    }
		})
		.withModality(Modality.MR_CANCEL)
		.withCloseOnOutsideClick(false)
		.withOnCloseHandler(event -> {
		    if (event != null && event.getCloseMode() == Modality.MR_SAVE) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.COURIER_REQ_SAVED));
		    }
		})
		.open();
    }

    /**
     * Sheets request edit.
     */
    private void sheetsRequestEdit() {
	Optional<DraftRouteSheet> draftSheetInO = draftSheetsGrid.getSelectionModel().getFirstSelectedItem();
	if (!draftSheetInO.isPresent()) {
	    return;
	}
	Optional<CourierRequest> requestInO = linkedCourierRequestsGrid.getSelectionModel().getFirstSelectedItem();
	if (!requestInO.isPresent()) {
	    return;
	}

	draftSheetInO = draftRouteSheetService.fetchById(draftSheetInO.get().getId());
	if (!draftSheetInO.isPresent()) {
	    AppNotificator.notify(5000, getTranslation(AppNotifyMsg.DRAFT_NOT_FOUND));
	    return;
	}
	DraftRouteSheet draftRouteSheetIn = draftSheetInO.get();

	requestInO = courierRequestService.fetchById(requestInO.get().getId());
	if (!requestInO.isPresent()) {
	    AppNotificator.notify(5000, getTranslation(AppNotifyMsg.REQUEST_NOT_FOUND));
	    return;
	}
	CourierRequest courierRequestIn = requestInO.get();

	CourierRequestSnapshot requestSnapshot = new CourierRequestSnapshot(courierRequestIn);

	CourierRequestEditor<CourierRequest> editor = getCourierRequestEditor();
	editor.setOperationData(courierRequestIn);

	DialogForm dialogForm = getDialogForm();

	dialogForm
		.withDataEditor(editor)
		.withHeader(getTranslation(CourierRequestsMsg.EDIT))
		.withWidth(Props.EM_28)
		.withModality(Modality.MR_SAVE, event -> {
		    if (!editor.isValidOperationData()) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.COURIER_REQ_CHK_FAIL));
		    } else {
			try {
			    Optional<DraftRouteSheet> draftSheetInR = draftRouteSheetService
				    .fetchById(draftRouteSheetIn.getId());
			    if (!draftSheetInR.isPresent()) {
				AppNotificator.notify(5000, getTranslation(AppNotifyMsg.DRAFT_NOT_FOUND));
				return;
			    }
			    Optional<CourierRequest> requestInOO = courierRequestService
				    .fetchById(courierRequestIn.getId());
			    if (!requestInOO.isPresent()) {
				AppNotificator.notify(5000, getTranslation(AppNotifyMsg.REQUEST_NOT_FOUND));
				return;
			    }

			    Optional<CourierRequest> courierRequestOut = courierRequestService
				    .registerChangesAndSave(editor.getOperationData(), requestSnapshot,
					    SecurityUtils.getUser());

			    if (courierRequestOut.isPresent()) {
				allCourierRequestsGrid.getDataProvider().refreshItem(courierRequestOut.get());
				linkedCourierRequestsGrid.getDataProvider().refreshItem(courierRequestOut.get());
			    }
			    dialogForm.closeWithResult(Modality.MR_SAVE);
			} catch (Exception e) {
			    log.error(e.getMessage() + " " + e);
			    AppNotificator.notify(5000, e.getMessage());
			}
		    }
		})
		.withModality(Modality.MR_CANCEL)
		.withCloseOnOutsideClick(false)
		.withOnCloseHandler(event -> {
		    if (event != null && event.getCloseMode() == Modality.MR_SAVE) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.COURIER_REQ_SAVED));
		    }
		})
		.open();
    }

    /**
     * Requests remove from draft sheet.
     */
    private void requestsRemoveFromDraftSheet() {
	Optional<DraftRouteSheet> draftSheetInO = draftSheetsGrid.getSelectionModel().getFirstSelectedItem();
	if (!draftSheetInO.isPresent()) {
	    return;
	}
	Set<CourierRequest> requestInO = linkedCourierRequestsGrid.getSelectionModel().getSelectedItems();
	if (requestInO.isEmpty()) {
	    return;
	}

	draftSheetInO = draftRouteSheetService.fetchById(draftSheetInO.get().getId());
	if (!draftSheetInO.isPresent()) {
	    AppNotificator.notify(5000, getTranslation(AppNotifyMsg.DRAFT_NOT_FOUND));
	    return;
	}
	DraftRouteSheet draftRouteSheetIn = draftSheetInO.get();

	requestInO = requestInO
		.stream()
		.map(req -> courierRequestService.fetchById(req.getId()))
		.filter(Optional<CourierRequest>::isPresent)
		.map(Optional<CourierRequest>::get)
		.collect(Collectors.toSet());
	if (requestInO.isEmpty()) {
	    AppNotificator.notify(5000, getTranslation(AppNotifyMsg.REQUEST_NOT_FOUND));
	    return;
	}
	try {

	    Optional<DraftRouteSheet> draftRouteSheetOut = draftRouteSheetService.removeRequestsAndSave(
		    draftRouteSheetIn, requestInO);

	    if (draftRouteSheetOut.isPresent()) {
		draftSheetsGrid.getSelectionModel().deselectAll();
		draftSheetsGrid.getDataProvider().refreshItem(draftRouteSheetOut.get());
		draftSheetsGrid.select(draftRouteSheetOut.get());
		linkedCourierRequestsGrid.getSelectionModel().deselectAll();
		AppNotificator.notify(getTranslation(AppNotifyMsg.DRAFT_COURIER_REQ_REMOVED));
	    }

	    linkedCourierRequestsGrid.getDataProvider().refreshAll();
	} catch (Exception e) {
	    log.error(e.getMessage() + " " + e);
	    AppNotificator.notify(5000, e.getMessage());
	}
    }

    /**
     * Courier requests add.
     */
    private void courierRequestsAdd() {
	CourierRequest courierRequestIn = new CourierRequest();
	courierRequestIn.setAuthor(SecurityUtils.getUser());

	CourierRequestEditor<CourierRequest> editor = getCourierRequestEditor();
	editor.setOperationData(courierRequestIn);

	DialogForm dialogForm = getDialogForm();

	dialogForm
		.withDataEditor(editor)
		.withHeader(getTranslation(CourierRequestsMsg.ADD))
		.withWidth(Props.EM_28)
		.withModality(Modality.MR_SAVE, event -> {
		    if (!editor.isValidOperationData()) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.COURIER_REQ_CHK_FAIL));
		    } else {
			try {
			    Optional<CourierRequest> courierRequestOut = courierRequestService
				    .addRequest(editor.getOperationData(), SecurityUtils.getUser());
			    if (courierRequestOut.isPresent()) {
				allCourierRequestsGrid.getDataProvider().refreshAll();
				dialogForm.closeWithResult(Modality.MR_SAVE);
			    }
			} catch (Exception e) {
			    AppNotificator.notify(5000, e.getMessage());
			}
		    }
		})
		.withModality(Modality.MR_CANCEL)
		.withCloseOnOutsideClick(false)
		.withOnCloseHandler(event -> {
		    if (event != null && event.getCloseMode() == Modality.MR_SAVE) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.COURIER_REQ_SAVED));
		    }
		})
		.open();
    }

    /**
     * Courier requests to draft sheet.
     */
    private void courierRequestsToDraftSheet() {
	Optional<DraftRouteSheet> draftSheetInO = draftSheetsGrid.getSelectionModel().getFirstSelectedItem();
	if (!draftSheetInO.isPresent()) {
	    return;
	}
	Set<CourierRequest> requestInO = allCourierRequestsGrid.getSelectionModel().getSelectedItems();
	if (requestInO.isEmpty()) {
	    return;
	}

	draftSheetInO = draftRouteSheetService.fetchById(draftSheetInO.get().getId());
	if (!draftSheetInO.isPresent()) {
	    AppNotificator.notify(5000, getTranslation(AppNotifyMsg.DRAFT_NOT_FOUND));
	    return;
	}
	DraftRouteSheet draftRouteSheetIn = draftSheetInO.get();

	requestInO = requestInO
		.stream()
		.map(req -> courierRequestService.fetchById(req.getId()))
		.filter(Optional<CourierRequest>::isPresent)
		.map(Optional<CourierRequest>::get)
		.collect(Collectors.toSet());
	if (requestInO.isEmpty()) {
	    AppNotificator.notify(5000, getTranslation(AppNotifyMsg.REQUEST_NOT_FOUND));
	    return;
	}

	try {

	    Optional<DraftRouteSheet> draftRouteSheetOut = draftRouteSheetService.addRequestsAndSave(
		    draftRouteSheetIn, requestInO,
		    SecurityUtils.getUser());

	    if (draftRouteSheetOut.isPresent()) {
		draftSheetsGrid.getSelectionModel().deselectAll();
		draftSheetsGrid.getDataProvider().refreshItem(draftRouteSheetOut.get());
		draftSheetsGrid.select(draftRouteSheetOut.get());
		linkedCourierRequestsGrid.getDataProvider().refreshAll();
		AppNotificator.notify(getTranslation(AppNotifyMsg.DRAFT_COURIER_REQ_ADDED));
	    }
	} catch (Exception e) {
	    log.error(e.getMessage() + " " + e);
	    AppNotificator.notify(5000, e.getMessage());
	}
    }

    /**
     * Courier request edit.
     */
    private void courierRequestEdit() {
	Optional<CourierRequest> courierRequestInO = allCourierRequestsGrid.getSelectionModel().getFirstSelectedItem();
	if (!courierRequestInO.isPresent()) {
	    return;
	}
	courierRequestInO = courierRequestService.fetchById(courierRequestInO.get().getId());
	if (!courierRequestInO.isPresent()) {
	    AppNotificator.notify(getTranslation(AppNotifyMsg.REQUEST_NOT_FOUND));
	    return;
	}
	CourierRequest courierRequestIn = courierRequestInO.get();
	CourierRequestSnapshot requestSnapshot = new CourierRequestSnapshot(courierRequestIn);

	CourierRequestEditor<CourierRequest> editor = getCourierRequestEditor();
	editor.setOperationData(courierRequestIn);

	DialogForm dialogForm = getDialogForm();

	dialogForm
		.withDataEditor(editor)
		.withHeader(getTranslation(CourierRequestsMsg.EDIT))
		.withWidth(Props.EM_28)
		.withModality(Modality.MR_SAVE, event -> {
		    if (!editor.isValidOperationData()) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.COURIER_REQ_CHK_FAIL));
		    } else {
			try {
			    Optional<CourierRequest> courierRequestOut = courierRequestService.registerChangesAndSave(
				    editor.getOperationData(), requestSnapshot,
				    SecurityUtils.getUser());

			    if (courierRequestOut.isPresent()) {
				allCourierRequestsGrid.getDataProvider().refreshItem(courierRequestOut.get());
				linkedCourierRequestsGrid.getDataProvider().refreshItem(courierRequestOut.get());
			    }
			    dialogForm.setCompletitionMode(Modality.MR_SAVE);
			    dialogForm.close();
			} catch (Exception e) {
			    AppNotificator.notify(5000, e.getMessage());
			}
		    }
		})
		.withModality(Modality.MR_CANCEL)
		.withCloseOnOutsideClick(false)
		.withOnCloseHandler(event -> {
		    if (event != null && event.getCloseMode() == Modality.MR_SAVE) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.COURIER_REQ_SAVED));
		    }
		})
		.open();
    }

    /**
     * Courier request del.
     *
     * @param grid the grid
     */
    private void courierRequestDel(Grid<CourierRequest> grid) {
	List<CourierRequest> courierRequestsList = grid
		.getSelectedItems()
		.stream()
		.collect(Collectors.toList());
	if (courierRequestsList.isEmpty()) {
	    return;
	}

	TextField description = new TextField();
	description.focus();
	description.setWidth(W_44EM);
	description.setRequired(true);
	description.setRequiredIndicatorVisible(true);
	description.setMinLength(10);
	description.setValueChangeMode(ValueChangeMode.EAGER);
	description.addValueChangeListener(e -> description.setInvalid(e.getValue().length() < 10));
	description.setValue("");
	description.setInvalid(true);
	description.setErrorMessage(getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
	VerticalLayout panel = new VerticalLayout(
		new Label(getTranslation(CourierRequestsMsg.CANCEL_REQUEST_MESSAGE)),
		description);
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	ConfirmDialog
		.create()
		.withCaption(getTranslation(CourierRequestsMsg.CANCEL_REQUEST_HEADER))
		.withMessage(panel)
		.withSaveButton(() -> {
		    if (description.isInvalid()) {
			AppNotificator.notify(5000,
				getTranslation(AppNotifyMsg.COURIER_REQ_SMALL_REASON));
			return;
		    }
		    try {
			draftSheetsGrid.getDataProvider().refreshAll();
			courierRequestService
				.moveCourierRequestsToArchive(courierRequestsList, description.getValue(),
					SecurityUtils.getUser());

			allCourierRequestsGrid.getDataProvider().refreshAll();
			linkedCourierRequestsGrid.getDataProvider().refreshAll();
			AppNotificator.notify(getTranslation(AppNotifyMsg.COURIER_REQ_DELETED));
		    } catch (Exception e) {
			AppNotificator.notify(5000, e.getMessage());
		    }

		}, ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_DELETE)),
			ButtonOption.icon(UIIcon.BTN_DEL.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * Refresh route sheet grid.
     */
    private void refreshRouteSheetGrid() {
	routeSheetsDataProvider.refreshAll();
    }

    /**
     * Refresh selected courier request grid.
     */
    private void refreshLinkedCourierRequestGrid() {
	linkedCourierRequestDataProvider.refreshAll();
    }

    /**
     * Refresh courier request grid.
     */
    private void refreshCourierRequestGrid() {
	allCourierRequestDataProvider.refreshAll();
    }

    /**
     * Gets the page title.
     *
     * @return the page title
     */
    @Override
    public String getPageTitle() {
	return getTranslation(AppTitleMsg.APP_TITLE_DRAFTSHEET, UI.getCurrent().getLocale());
    }
}
