package ua.com.sipsoft.ui.views.request.issued;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Lookup;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridMultiSelectionModel.SelectAllCheckboxVisibility;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
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
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.issued.CourierVisit;
import ua.com.sipsoft.model.entity.requests.issued.IssuedRouteSheet;
import ua.com.sipsoft.model.entity.requests.issued.IssuedRouteSheetEvent;
import ua.com.sipsoft.services.requests.issued.CourierVisitFilter;
import ua.com.sipsoft.services.requests.issued.IssuedCourierVisitService;
import ua.com.sipsoft.services.requests.issued.IssuedRouteSheetFilter;
import ua.com.sipsoft.services.requests.issued.IssuedRouteSheetService;
import ua.com.sipsoft.ui.commons.AppNotificator;
import ua.com.sipsoft.ui.commons.forms.Modality;
import ua.com.sipsoft.ui.commons.forms.dialogform.DialogForm;
import ua.com.sipsoft.ui.views.request.common.HistoryEventViever;
import ua.com.sipsoft.utils.CourierVisitState;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.history.CourierVisitSnapshot;
import ua.com.sipsoft.utils.messages.AppNotifyMsg;
import ua.com.sipsoft.utils.messages.AppTitleMsg;
import ua.com.sipsoft.utils.messages.ButtonMsg;
import ua.com.sipsoft.utils.messages.CourierRequestsMsg;
import ua.com.sipsoft.utils.messages.GridToolMsg;
import ua.com.sipsoft.utils.messages.HistoryEventMsg;
import ua.com.sipsoft.utils.messages.IssuedRouteSheetMsg;
import ua.com.sipsoft.utils.security.SecurityUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class CourierVisitsManager.
 *
 * @author Pavlo Degtyaryev
 */

/** The Constant log. */

/** The Constant log. */
@Slf4j
@UIScope
@SpringComponent
public class CourierVisitsManager extends VerticalLayout implements HasDynamicTitle {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7539108063492609494L;

    /** The button issued sheet print. */
    private Button btnIssuedSheetPrint;

    /** The button active issued sheet done. */
    private Button btnActiveIssuedSheetDone;

    /** The button active issued sheet cancel. */
    private Button btnActiveIssuedSheetCancel;

    /** The button inactive issued sheet move to archive. */
    private Button btnInactiveIssuedSheetMoveToArchive;

    /** The button issued sheet grid reset. */
    private Button btnIssuedSheetGridReset;

    /** The button inactive sheet visits recreate. */
    private Button btnInactiveSheetVisitsRecreate;

    /** The button active sheet visits done. */
    private Button btnActiveSheetVisitsDone;

    /** The button active sheet visits cancel. */
    private Button btnActiveSheetVisitsCancel;

    /** The button sheet visit edt. */
    private Button btnSheetVisitEdt;

    /** The button sheet visits grid reset. */
    private Button btnSheetVisitsGridReset;

    /** The button inactive visits recreate. */
    private Button btnInactiveVisitsRecreate;

    /** The button active visits cancel. */
    private Button btnActiveVisitsCancel;

    /** The button active visits done. */
    private Button btnActiveVisitsDone;

    /** The button visit edit. */
    private Button btnVisitEdt;

    /** The button visits grid reset. */
    private Button btnVisitsGridReset;

    /** The issued sheets grid. */
    private Grid<IssuedRouteSheet> issuedSheetsGrid;

    /** The selected courier visits grid. */
    private Grid<CourierVisit> selectedCourierVisitsGrid;

    /** The courier visits grid. */
    private Grid<CourierVisit> courierVisitsGrid;

    /** The field issued sheet filter. */
    private TextField fieldIssuedSheetFilter;

    /** The field selected visits filter. */
    private TextField fieldSelectedVisitsFilter;

    /** The field visits filter. */
    private TextField fieldVisitsFilter;

    /** The issued route sheet service. */
    private final transient IssuedRouteSheetService issuedRouteSheetService;

    /** The issued courier visit service. */
    private final transient IssuedCourierVisitService issuedCourierVisitService;

    /** The route sheets data provider. */
    private DataProvider<IssuedRouteSheet, IssuedRouteSheetFilter> routeSheetsDataProvider;

    /** The selected courier visit data provider. */
    private DataProvider<CourierVisit, CourierVisitFilter> selectedCourierVisitDataProvider;

    /** The courier visit data provider. */
    private DataProvider<CourierVisit, CourierVisitFilter> courierVisitDataProvider;

    /**
     * Instantiates a new courier visits manager.
     *
     * @param issuedRouteSheetService   the issued route sheet service
     * @param issuedCourierVisitService the issued courier visit service
     */
    public CourierVisitsManager(IssuedRouteSheetService issuedRouteSheetService,
	    IssuedCourierVisitService issuedCourierVisitService) {
	super();
	this.issuedCourierVisitService = issuedCourierVisitService;
	this.issuedRouteSheetService = issuedRouteSheetService;

	SplitLayout leftSplitPanel = new SplitLayout();
	leftSplitPanel.setOrientation(Orientation.VERTICAL);
	leftSplitPanel.addToPrimary(getPanelIssuedSheetGridPresenter());
	leftSplitPanel.addToSecondary(getPanelSelectedVisitsGridPresenter());

	SplitLayout background = new SplitLayout();
	background.addToPrimary(leftSplitPanel);
	background.addToSecondary(getPanelVisitsGridPresenter());
	add(background);
	setAlignItems(Alignment.STRETCH);
	setFlexGrow(1, background);
	setSizeFull();

	background.setSplitterPosition(60.0);
	leftSplitPanel.setSplitterPosition(35.0);

	prepareIssuedSheetGrid();
	prepareSheetVisitsGrid();
	prepareVisitsGrid();

    }

    /**
     * Gets the panel issued sheet grid presenter.
     *
     * @return the panel issued sheet grid presenter
     */
    private Component getPanelIssuedSheetGridPresenter() {
	btnActiveIssuedSheetDone = new Button(UIIcon.BTN_OK.createIcon());
	btnActiveIssuedSheetCancel = new Button(UIIcon.BTN_NO.createIcon());
	btnIssuedSheetPrint = new Button(UIIcon.PRINTER.createIcon());
	btnInactiveIssuedSheetMoveToArchive = new Button(UIIcon.SHEET_ARCHIVE.createIcon());
	btnIssuedSheetGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());

	fieldIssuedSheetFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldIssuedSheetFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnActiveIssuedSheetDone.setSizeUndefined();
	btnIssuedSheetPrint.setSizeUndefined();
	btnActiveIssuedSheetCancel.setSizeUndefined();
	btnInactiveIssuedSheetMoveToArchive.setSizeUndefined();
	btnIssuedSheetGridReset.setSizeUndefined();

	btnActiveIssuedSheetDone.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnIssuedSheetPrint.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnActiveIssuedSheetCancel.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnInactiveIssuedSheetMoveToArchive.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnIssuedSheetGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnIssuedSheetGridReset.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);

	HorizontalLayout panelSheetsToolbar = new HorizontalLayout(fieldIssuedSheetFilter, btnIssuedSheetPrint,
		btnActiveIssuedSheetDone, btnActiveIssuedSheetCancel, btnInactiveIssuedSheetMoveToArchive,
		btnIssuedSheetGridReset);
	panelSheetsToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelSheetsToolbar.setFlexGrow(0, btnActiveIssuedSheetDone);
	panelSheetsToolbar.setFlexGrow(0, btnIssuedSheetPrint);
	panelSheetsToolbar.setFlexGrow(0, btnActiveIssuedSheetCancel);
	panelSheetsToolbar.setFlexGrow(0, btnInactiveIssuedSheetMoveToArchive);
	panelSheetsToolbar.setFlexGrow(0, btnIssuedSheetGridReset);
	panelSheetsToolbar.setFlexGrow(1, fieldIssuedSheetFilter);
	panelSheetsToolbar.setMargin(false);
	panelSheetsToolbar.setPadding(false);
	panelSheetsToolbar.setSpacing(true);

	issuedSheetsGrid = new Grid<>(IssuedRouteSheet.class);
	issuedSheetsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

	VerticalLayout panelIssuedSheetsGrigPresenter = new VerticalLayout(panelSheetsToolbar, issuedSheetsGrid);
	panelIssuedSheetsGrigPresenter.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	panelIssuedSheetsGrigPresenter.setWidthFull();
	panelIssuedSheetsGrigPresenter.setMargin(false);
	panelIssuedSheetsGrigPresenter.setPadding(false);
	panelIssuedSheetsGrigPresenter.setSpacing(false);

	HorizontalLayout panelIssuedSheetsManage = new HorizontalLayout(panelIssuedSheetsGrigPresenter);
	panelIssuedSheetsManage.setFlexGrow(1, panelIssuedSheetsGrigPresenter);
	panelIssuedSheetsManage.setMargin(false);
	panelIssuedSheetsManage.setPadding(false);
	panelIssuedSheetsManage.setSpacing(false);

	btnActiveIssuedSheetDone.addClickListener(e -> issuedSheetDoneUnfinished());
	btnActiveIssuedSheetCancel.addClickListener(e -> issuedSheetCancelUnfinished());
	btnIssuedSheetPrint.addClickListener(e -> issuedSheetPrint());
	btnInactiveIssuedSheetMoveToArchive.addClickListener(e -> issuedSheetMoveToArchive());

	return panelIssuedSheetsGrigPresenter;
    }

    /**
     * Gets the panel selected visits grid presenter.
     *
     * @return the panel selected visits grid presenter
     */
    private Component getPanelSelectedVisitsGridPresenter() {

	btnActiveSheetVisitsDone = new Button(UIIcon.BTN_OK.createIcon());
	btnActiveSheetVisitsCancel = new Button(UIIcon.BTN_NO.createIcon());
	btnInactiveSheetVisitsRecreate = new Button(UIIcon.SHEET_REDRAFT.createIcon());
	btnSheetVisitEdt = new Button(UIIcon.BTN_EDIT.createIcon());
	btnSheetVisitsGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());

	btnActiveSheetVisitsDone.setSizeUndefined();
	btnActiveSheetVisitsCancel.setSizeUndefined();
	btnInactiveSheetVisitsRecreate.setSizeUndefined();
	btnSheetVisitEdt.setSizeUndefined();
	btnSheetVisitsGridReset.setSizeUndefined();

	fieldSelectedVisitsFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldSelectedVisitsFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnActiveSheetVisitsDone.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnActiveSheetVisitsCancel.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnInactiveSheetVisitsRecreate.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnSheetVisitEdt.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnSheetVisitsGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnSheetVisitsGridReset.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);

	HorizontalLayout panelSheetVisitsToolbar = new HorizontalLayout(fieldSelectedVisitsFilter,
		btnInactiveSheetVisitsRecreate,
		btnActiveSheetVisitsCancel, btnActiveSheetVisitsDone, btnSheetVisitEdt, btnSheetVisitsGridReset);

	panelSheetVisitsToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelSheetVisitsToolbar.setFlexGrow(0, btnInactiveSheetVisitsRecreate);
	panelSheetVisitsToolbar.setFlexGrow(0, btnActiveSheetVisitsCancel);
	panelSheetVisitsToolbar.setFlexGrow(0, btnActiveSheetVisitsDone);
	panelSheetVisitsToolbar.setFlexGrow(0, btnSheetVisitsGridReset);
	panelSheetVisitsToolbar.setFlexGrow(0, btnSheetVisitEdt);
	panelSheetVisitsToolbar.setFlexGrow(1, fieldSelectedVisitsFilter);
	panelSheetVisitsToolbar.setMargin(false);
	panelSheetVisitsToolbar.setPadding(false);
	panelSheetVisitsToolbar.setSpacing(false);

	selectedCourierVisitsGrid = new Grid<>(CourierVisit.class);
	selectedCourierVisitsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

	VerticalLayout panelSheetVisitsGrigPresenter = new VerticalLayout(panelSheetVisitsToolbar,
		selectedCourierVisitsGrid);
	panelSheetVisitsGrigPresenter.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	panelSheetVisitsGrigPresenter.setMargin(false);
	panelSheetVisitsGrigPresenter.setPadding(false);
	panelSheetVisitsGrigPresenter.setSpacing(false);

	HorizontalLayout panelSheetVisitsManage = new HorizontalLayout(panelSheetVisitsGrigPresenter);
	panelSheetVisitsManage.setFlexGrow(1, panelSheetVisitsGrigPresenter);
	panelSheetVisitsManage.setMargin(false);
	panelSheetVisitsManage.setPadding(false);
	panelSheetVisitsManage.setSpacing(false);

	btnInactiveSheetVisitsRecreate.addClickListener(e -> inactiveVisitsRedraft(selectedCourierVisitsGrid));
	btnActiveSheetVisitsCancel.addClickListener(e -> activeVisitsCancel(selectedCourierVisitsGrid));
	btnActiveSheetVisitsDone.addClickListener(e -> activeVisitsToDone(selectedCourierVisitsGrid));
	btnSheetVisitEdt.addClickListener(e -> courierVisitEdit(selectedCourierVisitsGrid));
	return panelSheetVisitsGrigPresenter;
    }

    /**
     * Gets the panel visits grid presenter.
     *
     * @return the panel visits grid presenter
     */
    private Component getPanelVisitsGridPresenter() {

	btnInactiveVisitsRecreate = new Button(UIIcon.SHEET_REDRAFT.createIcon());
	btnActiveVisitsCancel = new Button(UIIcon.BTN_NO.createIcon());
	btnActiveVisitsDone = new Button(UIIcon.BTN_OK.createIcon());
	btnVisitEdt = new Button(UIIcon.BTN_EDIT.createIcon());
	btnVisitsGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());
	btnInactiveVisitsRecreate.setSizeUndefined();
	btnVisitEdt.setSizeUndefined();
	btnActiveVisitsCancel.setSizeUndefined();
	btnActiveVisitsDone.setSizeUndefined();
	btnVisitsGridReset.setSizeUndefined();

	fieldVisitsFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldVisitsFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnInactiveVisitsRecreate.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnVisitEdt.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnActiveVisitsCancel.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnActiveVisitsDone.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnVisitsGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnVisitsGridReset.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);

	HorizontalLayout panelVisitsToolbar = new HorizontalLayout(fieldVisitsFilter, btnInactiveVisitsRecreate,
		btnActiveVisitsCancel, btnActiveVisitsDone, btnVisitEdt, btnVisitsGridReset);

	panelVisitsToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelVisitsToolbar.setFlexGrow(0, btnInactiveVisitsRecreate);
	panelVisitsToolbar.setFlexGrow(0, btnVisitEdt);
	panelVisitsToolbar.setFlexGrow(0, btnActiveVisitsCancel);
	panelVisitsToolbar.setFlexGrow(0, btnActiveVisitsDone);
	panelVisitsToolbar.setFlexGrow(0, btnVisitsGridReset);
	panelVisitsToolbar.setFlexGrow(1, fieldVisitsFilter);
	panelVisitsToolbar.setMargin(false);
	panelVisitsToolbar.setPadding(false);
	panelVisitsToolbar.setSpacing(false);

	courierVisitsGrid = new Grid<>(CourierVisit.class);
	courierVisitsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

	VerticalLayout panelVisitsPresenter = new VerticalLayout(panelVisitsToolbar, courierVisitsGrid);
	panelVisitsPresenter.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	panelVisitsPresenter.setMargin(false);
	panelVisitsPresenter.setPadding(false);
	panelVisitsPresenter.setSpacing(false);

	btnInactiveVisitsRecreate.addClickListener(e -> inactiveVisitsRedraft(courierVisitsGrid));
	btnActiveVisitsCancel.addClickListener(e -> activeVisitsCancel(courierVisitsGrid));
	btnActiveVisitsDone.addClickListener(e -> activeVisitsToDone(courierVisitsGrid));
	btnVisitEdt.addClickListener(e -> courierVisitEdit(courierVisitsGrid));
	return panelVisitsPresenter;
    }

    /**
     * Gets the filtered issued route sheet query.
     *
     * @param query the {@link Query}
     * @return the filtered {@link Stream}<{@link IssuedRouteSheet}>
     */
    private Stream<IssuedRouteSheet> getFilteredIssuedRouteSheetQuery(
	    Query<IssuedRouteSheet, IssuedRouteSheetFilter> query) {
	return issuedRouteSheetService.getQueriedIssuedRouteSheetsByFilter(
		query, getIssuedRouteSheetFilter());
    }

    /**
     * Gets the filtered issued route sheet query count.
     *
     * @param query the {@link Query}
     * @return the filtered {@link Stream}<{@link IssuedRouteSheet}> records count
     */
    private int getFilteredIssuedRouteSheetQueryCount(
	    Query<IssuedRouteSheet, IssuedRouteSheetFilter> query) {
	return issuedRouteSheetService.getQueriedIssuedRouteSheetsByFilterCount(
		query, getIssuedRouteSheetFilter());
    }

    /**
     * Gets the issued route sheet filter.
     *
     * @return the {@link IssuedRouteSheetFilter} filter
     */
    private IssuedRouteSheetFilter getIssuedRouteSheetFilter() {
	return IssuedRouteSheetFilter.builder()
		.description(getSanitizedIssuedSheetFilter())
		.build();
    }

    /**
     * Gets the sanitized issued sheet filter.
     *
     * @return the sanitized issued sheet filter
     */
    private String getSanitizedIssuedSheetFilter() {
	return StringUtils.truncate(fieldIssuedSheetFilter.getValue(), 100);
    }

    /**
     * Prepare issued sheet grid.
     */
    private void prepareIssuedSheetGrid() {
	routeSheetsDataProvider = DataProvider.fromFilteringCallbacks(
		this::getFilteredIssuedRouteSheetQuery,
		this::getFilteredIssuedRouteSheetQueryCount);
	issuedSheetsGrid.setDataProvider(routeSheetsDataProvider.withConfigurableFilter());

	btnIssuedSheetPrint.setEnabled(false);
	btnActiveIssuedSheetCancel.setEnabled(false);
	btnInactiveIssuedSheetMoveToArchive.setEnabled(false);
	btnActiveIssuedSheetDone.setEnabled(false);

	fieldIssuedSheetFilter.setValueChangeMode(ValueChangeMode.LAZY);
	fieldIssuedSheetFilter.addValueChangeListener(e -> {
	    issuedSheetsGrid.getSelectionModel().deselectAll();
	    issuedSheetsGrid.getDataProvider().refreshAll();
	});

	issuedSheetsGrid.removeAllColumns();

	issuedSheetsGrid
		.addColumn("id")
		.setHeader(getTranslation(IssuedRouteSheetMsg.ISSUEDID))
		.setSortProperty("id")
		.setWidth(Props.EM_05)
		.setFlexGrow(0);
	issuedSheetsGrid
		.addColumn("description")
		.setHeader(getTranslation(IssuedRouteSheetMsg.DESCRIPTION))
		.setFlexGrow(1)
		.setSortProperty("description");
	issuedSheetsGrid
		.addColumn(new LocalDateTimeRenderer<>(IssuedRouteSheet::getCreationDate,
			DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
		.setHeader(getTranslation(IssuedRouteSheetMsg.CREATION_DATE))
		.setWidth(Props.EM_09)
		.setFlexGrow(0)
		.setSortProperty("creationDate");
	issuedSheetsGrid
		.addColumn("author.username")
		.setHeader(getTranslation(IssuedRouteSheetMsg.AUTHOR))
		.setWidth(Props.EM_10)
		.setFlexGrow(0)
		.setSortProperty("author.username");
	issuedSheetsGrid
		.addColumn(buildVisitsStatesColumn(), "states")
		.setHeader(getTranslation(IssuedRouteSheetMsg.STATES))
		.setWidth(Props.EM_05)
		.setFlexGrow(0)
		.setSortProperty("states");

	issuedSheetsGrid
		.addColumn(new ComponentRenderer<Icon, IssuedRouteSheet>(request -> {
		    Icon iconInfo = UIIcon.INFO.createIcon();
		    iconInfo.setSize(Props.EM_01);
		    iconInfo.addClickListener(event -> ConfirmDialog
			    .create()
			    .withCaption(getTranslation(HistoryEventMsg.HEADER_VEIW))
			    .withMessage(new HistoryEventViever<>(new Grid<>(IssuedRouteSheetEvent.class),
				    request.getHistoryEvents()))
			    .withCloseButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CLOSE)),
				    ButtonOption.icon(UIIcon.BTN_OK.getIcon()))
			    .open());
		    return iconInfo;
		}))
		.setHeader(getTranslation(GridToolMsg.INFO_COLUMN))
		.setWidth(Props.EM_05)
		.setFlexGrow(0);

	issuedSheetsGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});

	issuedSheetsGrid.setColumnReorderingAllowed(true);
	issuedSheetsGrid.addSelectionListener(event -> {
	    issuedRouteSheetSelectionEventProcessor(event.getFirstSelectedItem().orElse(null));

	});

	fieldIssuedSheetFilter.addValueChangeListener(e -> issuedSheetsGrid.getDataProvider().refreshAll());

	issuedSheetsGrid.setMultiSort(true);

	btnIssuedSheetGridReset.addClickListener(e -> routeSheetsDataProvider.refreshAll());
    }

    /**
     * Issued route sheet selection event processor.
     *
     * @param issuedRouteSheet the issued route sheet
     */
    private void issuedRouteSheetSelectionEventProcessor(IssuedRouteSheet issuedRouteSheet) {

	selectedCourierVisitsGrid.getSelectionModel().deselectAll();
	selectedCourierVisitDataProvider.refreshAll();

	if (issuedRouteSheet == null) {
	    btnIssuedSheetPrint.setEnabled(false);
	    btnActiveIssuedSheetCancel.setEnabled(false);
	    btnInactiveIssuedSheetMoveToArchive.setEnabled(false);
	    btnActiveIssuedSheetDone.setEnabled(false);

	} else {
	    btnIssuedSheetPrint.setEnabled(true);
	    btnActiveIssuedSheetCancel.setEnabled(issuedRouteSheet.hasActiveVisits());
	    btnActiveIssuedSheetDone.setEnabled(issuedRouteSheet.hasActiveVisits());
	    btnInactiveIssuedSheetMoveToArchive.setEnabled(!issuedRouteSheet.hasActiveVisits());
	}
    }

    /**
     * Builds the visits states column.
     *
     * @return the component renderer
     */
    private ComponentRenderer<Div, IssuedRouteSheet> buildVisitsStatesColumn() {
	return new ComponentRenderer<>(sheet -> {
	    Div div = new Div();
	    if (sheet == null) {
		return div;
	    }
	    Set<CourierVisit> courierVisits = sheet.getRequests();

	    if (courierVisits != null && !courierVisits.isEmpty()) {
		Icon[] sb = new Icon[CourierVisitState.values().length];
		Icon icon;
		for (CourierVisit s : courierVisits) {
		    if (s != null) {
			icon = s.getState().getIcon().create();
			icon.setSize(Props.EM_0_85);
			icon.getStyle().set(Props.MARGIN, Props.EM_0_1);
			icon.setColor("--lumo-primary-color");
			sb[s.getState().ordinal()] = icon;
		    }
		}
		for (Icon i : sb) {
		    if (i != null) {
			div.add(i);
		    }
		}
	    }
	    return div;
	});
    }

    /**
     * Gets the filtered selected visit query count.
     *
     * @param query the query
     * @return the filtered selected visit query count
     */
    private int getFilteredSelectedVisitQueryCount(
	    Query<CourierVisit, CourierVisitFilter> query) {
	return issuedCourierVisitService.getQueriedCourierVisitsByFilterCount(
		query, getSelectedVisitRequestFilter());
    }

    /**
     * Gets the queried courier visits by filter.
     *
     * @param query the query
     * @return the queried courier visits by filter
     */
    private Stream<CourierVisit> getQueriedCourierVisitsByFilter(
	    Query<CourierVisit, CourierVisitFilter> query) {
	return issuedCourierVisitService.getQueriedCourierVisitsByFilter(
		query, getSelectedVisitRequestFilter());
    }

    /**
     * Gets the selected visit request filter.
     *
     * @return the selected visit request filter
     */
    private CourierVisitFilter getSelectedVisitRequestFilter() {
	return CourierVisitFilter.builder()
		.sheetId(issuedSheetsGrid.getSelectionModel().getFirstSelectedItem()
			.map(IssuedRouteSheet::getId).orElse(-1L))
		.description(getSanitizedSelectedVisitFilter())
		.build();
    }

    /**
     * Gets the sanitized selected visit filter.
     *
     * @return the sanitized selected visit filter
     */
    private String getSanitizedSelectedVisitFilter() {
	return StringUtils.truncate(fieldSelectedVisitsFilter.getValue(), 100);
    }

    /**
     * Prepare sheet visits grid.
     */
    private void prepareSheetVisitsGrid() {

	selectedCourierVisitDataProvider = DataProvider.fromFilteringCallbacks(
		this::getQueriedCourierVisitsByFilter,
		this::getFilteredSelectedVisitQueryCount);
	selectedCourierVisitsGrid.setDataProvider(selectedCourierVisitDataProvider.withConfigurableFilter());

	btnActiveSheetVisitsDone.setEnabled(false);
	btnSheetVisitEdt.setEnabled(false);
	btnActiveSheetVisitsCancel.setEnabled(false);
	btnInactiveSheetVisitsRecreate.setEnabled(false);

	fieldSelectedVisitsFilter.setValueChangeMode(ValueChangeMode.LAZY);
	fieldSelectedVisitsFilter
		.addValueChangeListener(e -> {
		    selectedCourierVisitsGrid.getSelectionModel().deselectAll();
		    selectedCourierVisitsGrid.getDataProvider().refreshAll();
		});

	selectedCourierVisitsGrid.removeAllColumns();

	selectedCourierVisitsGrid
		.addColumn("id")
		.setHeader(getTranslation(CourierRequestsMsg.REQUESTID))
		.setSortProperty("id")
		.setWidth(Props.EM_05)
		.setFlexGrow(0);
	selectedCourierVisitsGrid
		.addColumn(new ComponentRenderer<>(CourierVisitsPresenter::new))
		.setHeader(getTranslation(CourierRequestsMsg.REQUEST))
		.setFlexGrow(1);

	selectedCourierVisitsGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});
	selectedCourierVisitsGrid.getColumns().forEach(column -> column.setSortable(true));
	selectedCourierVisitsGrid.setColumnReorderingAllowed(true);

	GridMultiSelectionModel<CourierVisit> multiSelection = (GridMultiSelectionModel<CourierVisit>) selectedCourierVisitsGrid
		.setSelectionMode(Grid.SelectionMode.MULTI);

	multiSelection.setSelectAllCheckboxVisibility(SelectAllCheckboxVisibility.VISIBLE);
	multiSelection.setSelectionColumnFrozen(true);
	multiSelection.addMultiSelectionListener(event -> {

	    Map<Boolean, List<CourierVisit>> visitsByStates = event.getAllSelectedItems()
		    .stream()
		    .map(v -> issuedCourierVisitService.fetchById(v.getId()))
		    .filter(Optional<CourierVisit>::isPresent)
		    .map(Optional<CourierVisit>::get)
		    .collect(Collectors.partitioningBy(CourierVisit::isActive));

	    btnSheetVisitEdt.setEnabled(
		    (event.getAllSelectedItems().size() == 1) && (!visitsByStates.get(true).isEmpty()));
	    btnInactiveSheetVisitsRecreate
		    .setEnabled(visitsByStates.get(true).isEmpty() && !event.getAllSelectedItems().isEmpty());
	    if (!visitsByStates.get(true).isEmpty()) {
		btnActiveSheetVisitsDone.setEnabled(true);
		btnActiveSheetVisitsCancel.setEnabled(true);
	    } else {
		btnActiveSheetVisitsDone.setEnabled(false);
		btnActiveSheetVisitsCancel.setEnabled(false);
	    }
	});

	btnSheetVisitsGridReset.addClickListener(e -> selectedCourierVisitDataProvider.refreshAll());
    }

    /**
     * Gets the filtered courier visit queryy count.
     *
     * @param query the query
     * @return the filtered courier visit queryy count
     */
    private int getFilteredCourierVisitQueryyCount(
	    Query<CourierVisit, CourierVisitFilter> query) {
	return issuedCourierVisitService.getQueriedCourierVisitsByFilterCount(
		query, getCourierRequestFilter());
    }

    /**
     * Gets the filtered courier visit query.
     *
     * @param query the query
     * @return the filtered courier visit query
     */
    private Stream<CourierVisit> getFilteredCourierVisitQuery(
	    Query<CourierVisit, CourierVisitFilter> query) {
	return issuedCourierVisitService.getQueriedCourierVisitsByFilter(
		query, getCourierRequestFilter());
    }

    /**
     * Gets the courier request filter.
     *
     * @return the courier request filter
     */
    private CourierVisitFilter getCourierRequestFilter() {
	return CourierVisitFilter.builder()
		.description(getSanitizadCourierVisitFilter())
		.build();
    }

    /**
     * Gets the sanitizad courier visit filter.
     *
     * @return the sanitizad courier visit filter
     */
    private String getSanitizadCourierVisitFilter() {
	return StringUtils.truncate(fieldVisitsFilter.getValue(), 100);
    }

    /**
     * Prepare visits grid.
     */
    private void prepareVisitsGrid() {

	courierVisitDataProvider = DataProvider.fromFilteringCallbacks(
		this::getFilteredCourierVisitQuery,
		this::getFilteredCourierVisitQueryyCount);
	courierVisitsGrid.setDataProvider(courierVisitDataProvider.withConfigurableFilter());

	btnActiveVisitsCancel.setEnabled(false);
	btnActiveVisitsDone.setEnabled(false);
	btnInactiveVisitsRecreate.setEnabled(false);
	btnVisitEdt.setEnabled(false);

	fieldVisitsFilter.setValueChangeMode(ValueChangeMode.LAZY);
	fieldVisitsFilter.addValueChangeListener(e -> {
	    courierVisitsGrid.getSelectionModel().deselectAll();
	    courierVisitsGrid.getDataProvider().refreshAll();
	});

	courierVisitsGrid.removeAllColumns();

	courierVisitsGrid
		.addColumn("id")
		.setHeader(getTranslation(CourierRequestsMsg.REQUESTID))
		.setSortProperty("id")
		.setWidth(Props.EM_05)
		.setFlexGrow(0);
	courierVisitsGrid
		.addColumn(new ComponentRenderer<>(CourierVisitsPresenter::new))
		.setHeader(getTranslation(CourierRequestsMsg.REQUEST))
		.setFlexGrow(1);

	courierVisitsGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});
	courierVisitsGrid.setColumnReorderingAllowed(true);

	GridMultiSelectionModel<CourierVisit> multiSelection = (GridMultiSelectionModel<CourierVisit>) courierVisitsGrid
		.setSelectionMode(Grid.SelectionMode.MULTI);
	multiSelection.setSelectionColumnFrozen(true);
	multiSelection.setSelectAllCheckboxVisibility(SelectAllCheckboxVisibility.VISIBLE);
	multiSelection.addMultiSelectionListener(event -> {

	    Map<Boolean, List<CourierVisit>> visitsByStates = event.getAllSelectedItems()
		    .stream()
		    .map(v -> issuedCourierVisitService.fetchById(v.getId()))
		    .filter(Optional<CourierVisit>::isPresent)
		    .map(Optional<CourierVisit>::get)
		    .collect(Collectors.partitioningBy(CourierVisit::isActive));

	    btnVisitEdt.setEnabled(
		    (event.getAllSelectedItems().size() == 1) && (!visitsByStates.get(true).isEmpty()));

	    btnInactiveVisitsRecreate
		    .setEnabled(visitsByStates.get(true).isEmpty() && !event.getAllSelectedItems().isEmpty());
	    if (!visitsByStates.get(true).isEmpty()) {
		btnActiveVisitsDone.setEnabled(true);
		btnActiveVisitsCancel.setEnabled(true);
	    } else {
		btnActiveVisitsDone.setEnabled(false);
		btnActiveVisitsCancel.setEnabled(false);
	    }
	});

	btnVisitsGridReset.addClickListener(e -> courierVisitDataProvider.refreshAll());
    }

    /**
     * Courier visit editor.
     *
     * @return the courier visits editor
     */
    @Lookup
    CourierVisitsEditor<CourierVisit> courierVisitEditor() {
	log.info("Create CourierVisitsEditor");
	return null;
    }

    /**
     * Gets the dialog form.
     *
     * @return the dialog form
     */
    @Lookup
    DialogForm getDialogForm() {
	return null;
    }

    /**
     * Issued sheet done unfinished.
     */
    private void issuedSheetDoneUnfinished() {
	Optional<IssuedRouteSheet> issuedSheetInO = issuedSheetsGrid.getSelectionModel().getFirstSelectedItem();
	if (!issuedSheetInO.isPresent() || issuedSheetInO.get().getRequests().isEmpty()) {
	    return;
	}
	issuedSheetInO = issuedRouteSheetService.fetchById(issuedSheetInO.get().getId());
	if (!issuedSheetInO.isPresent()) {
	    AppNotificator.notifyError(5000, getTranslation(AppNotifyMsg.ISSUED_SHEET_NOT_FOUND));
	    return;
	}
	Map<Boolean, List<CourierVisit>> visitsByStates = issuedSheetInO.get().getRequests()
		.stream()
		.map(v -> issuedCourierVisitService.fetchById(v.getId()))
		.filter(Optional<CourierVisit>::isPresent)
		.map(Optional<CourierVisit>::get)
		.collect(Collectors.partitioningBy(CourierVisit::isActive));

	if (visitsByStates.get(true).isEmpty()) {
	    AppNotificator.notifyError(5000,
		    getTranslation(AppNotifyMsg.ISSUED_SHEET_HAVE_NO_UNFINISHED));
	    return;
	}
	VerticalLayout panel = new VerticalLayout(
		new Label(getTranslation(IssuedRouteSheetMsg.ISSUED_ROUTE_SHEET_DONE_ALL_MSG)));
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	ConfirmDialog
		.create()
		.withCaption(getTranslation(IssuedRouteSheetMsg.ISSUED_ROUTE_SHEET_DONE_ALL_HEADER))
		.withMessage(panel)
		.withSaveButton(() -> {
		    try {
			Collection<CourierVisit> courierVisits = issuedCourierVisitService
				.markVisitsAsCompleted(visitsByStates.get(true), SecurityUtils.getUser());
			if (!courierVisits.isEmpty()) {
			    // TODO reaction of buttons to change of state of records
			    issuedSheetsGrid.getDataProvider().refreshAll();
			    courierVisits.forEach(v -> {
				selectedCourierVisitDataProvider.refreshItem(v);
				courierVisitDataProvider.refreshItem(v);
			    });
			    AppNotificator.notify(getTranslation(AppNotifyMsg.VISITS_MARKS_AS_COMPLETED));
			}
		    } catch (Exception e) {
			log.error(e.getMessage() + " " + e);
			AppNotificator.notifyError(5000, e.getMessage());
		    }

		}, ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_SAVE)),
			ButtonOption.icon(UIIcon.BTN_PUT.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * Issued sheet cancel unfinished.
     */
    private void issuedSheetCancelUnfinished() {
	Optional<IssuedRouteSheet> issuedSheetInO = issuedSheetsGrid.getSelectionModel().getFirstSelectedItem();
	if (!issuedSheetInO.isPresent() || issuedSheetInO.get().getRequests().isEmpty()) {
	    return;
	}
	issuedSheetInO = issuedRouteSheetService.fetchById(issuedSheetInO.get().getId());
	if (!issuedSheetInO.isPresent()) {
	    AppNotificator.notifyError(5000, getTranslation(AppNotifyMsg.ISSUED_SHEET_NOT_FOUND));
	    return;
	}
	Map<Boolean, List<CourierVisit>> inactiveActive = issuedSheetInO.get().getRequests()
		.stream()
		.map(v -> issuedCourierVisitService.fetchById(v.getId()))
		.filter(Optional<CourierVisit>::isPresent)
		.map(Optional<CourierVisit>::get)
		.collect(Collectors.partitioningBy(CourierVisit::isActive));

	if (inactiveActive.get(true).isEmpty()) {
	    AppNotificator.notifyError(5000,
		    getTranslation(AppNotifyMsg.ISSUED_SHEET_HAVE_NO_UNFINISHED));
	    return;
	}
	TextField description = new TextField();
	description.focus();
	description.setWidth(Props.EM_44);
	description.setRequired(true);
	description.setRequiredIndicatorVisible(true);
	description.setMinLength(10);
	description.setValueChangeMode(ValueChangeMode.EAGER);
	description.addValueChangeListener(e -> description.setInvalid(e.getValue().length() < 10));
	description.setValue("");
	description.setInvalid(true);
	description.setErrorMessage(getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
	VerticalLayout panel = new VerticalLayout(
		new Label(getTranslation(IssuedRouteSheetMsg.ISSUED_ROUTE_SHEET_CANCEL_ALL_MSG)), description);
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	ConfirmDialog
		.create()
		.withCaption(getTranslation(IssuedRouteSheetMsg.ISSUED_ROUTE_SHEET_CANCEL_ALL_HEADER))
		.withMessage(panel)
		.withSaveButton(() -> {
		    try {
			Collection<CourierVisit> courierVisits = issuedCourierVisitService
				.markVisitsAsCompleted(inactiveActive.get(true), SecurityUtils.getUser());
			if (!courierVisits.isEmpty()) {
			    routeSheetsDataProvider.refreshAll();
			    courierVisits.forEach(v -> {
				selectedCourierVisitDataProvider.refreshItem(v);
				courierVisitDataProvider.refreshItem(v);
			    });
			    AppNotificator.notify(getTranslation(AppNotifyMsg.VISITS_MARKS_AS_CANCELLED));
			}
		    } catch (Exception e) {
			log.error(e.getMessage() + " " + e);
			AppNotificator.notifyError(5000, e.getMessage());
		    }

		}, ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_SAVE)),
			ButtonOption.icon(UIIcon.BTN_PUT.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * Issued sheet print.
     */
    private void issuedSheetPrint() {
	VerticalLayout panel = new VerticalLayout(
		new Label(getTranslation("А Ваш принтер все еще жужжит и плюется бумажками")));
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	ConfirmDialog
		.create()
		.withCaption(getTranslation("ЭРА БЕЗБУМАЖНЫХ ТЕХНОЛОГИЙ"))
		.withMessage(panel)
		.withSaveButton(() -> {
		}, ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_OK)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * Issued sheet move to archive.
     */
    private void issuedSheetMoveToArchive() {
	Optional<IssuedRouteSheet> issuedSheetInO = issuedSheetsGrid.getSelectionModel().getFirstSelectedItem();
	if (!issuedSheetInO.isPresent() || issuedSheetInO.get().getRequests().isEmpty()) {
	    return;
	}
	issuedSheetInO = issuedRouteSheetService.fetchById(issuedSheetInO.get().getId());
	if (!issuedSheetInO.isPresent()) {
	    AppNotificator.notifyError(5000, getTranslation(AppNotifyMsg.ISSUED_SHEET_NOT_FOUND));
	    return;
	}
	Map<Boolean, List<CourierVisit>> inactiveActive = issuedSheetInO.get().getRequests()
		.stream()
		.map(v -> issuedCourierVisitService.fetchById(v.getId()))
		.filter(Optional<CourierVisit>::isPresent)
		.map(Optional<CourierVisit>::get)
		.collect(Collectors.partitioningBy(CourierVisit::isActive));

	if (!inactiveActive.get(true).isEmpty()) {
	    AppNotificator.notifyError(5000, getTranslation(AppNotifyMsg.ISSUED_IS_UNFINISHED));
	    return;
	}
	VerticalLayout panel = new VerticalLayout(
		new Label(getTranslation(IssuedRouteSheetMsg.ISSUED_ROUTE_SHEET_ARCH_MSG)));
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	IssuedRouteSheet issuedRouteSheet = issuedSheetInO.get();
	ConfirmDialog
		.create()
		.withCaption(getTranslation(IssuedRouteSheetMsg.ISSUED_ROUTE_SHEET_ARCH_HEADER))
		.withMessage(panel)
		.withSaveButton(() -> {
		    try {
			issuedRouteSheetService.moveToArchive(issuedRouteSheet,
				SecurityUtils.getUser());

			issuedSheetsGrid.getSelectionModel().deselect(issuedRouteSheet);
			issuedRouteSheet.getRequests().forEach(request -> {
			    selectedCourierVisitsGrid.getSelectionModel().deselect(request);
			    courierVisitsGrid.getSelectionModel().deselect(request);
			});
			routeSheetsDataProvider.refreshAll();
			selectedCourierVisitDataProvider.refreshAll();
			courierVisitDataProvider.refreshAll();
			AppNotificator.notify(getTranslation(AppNotifyMsg.ISSUED_IS_ARCHIVED));
		    } catch (Exception e) {
			AppNotificator.notifyError(5000, e.getMessage());
		    }

		}, ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_SAVE)),
			ButtonOption.icon(UIIcon.BTN_PUT.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * Courier visit edit.
     *
     * @param visitsGrid the visits grid
     */
    private void courierVisitEdit(Grid<CourierVisit> visitsGrid) {
	Optional<CourierVisit> courierVisitsIn = visitsGrid.getSelectionModel().getFirstSelectedItem();
	if (!courierVisitsIn.isPresent()) {
	    return;
	}

	courierVisitsIn = issuedCourierVisitService.fetchById(courierVisitsIn.get().getId());

	if (!courierVisitsIn.isPresent()) {
	    AppNotificator.notifyError(getTranslation(AppNotifyMsg.NOTHING_TO_EDIT));
	    return;
	}
	CourierVisitSnapshot visitSnapshot = new CourierVisitSnapshot(courierVisitsIn.get());

	CourierVisitsEditor<CourierVisit> editor = courierVisitEditor();
	editor.setOperationData(courierVisitsIn.get());

	DialogForm dialogForm = getDialogForm();
	dialogForm.withDataEditor(editor)
		.withHeader(getTranslation(CourierRequestsMsg.EDIT))
		.withWidth(Props.EM_28)
		.withModality(Modality.MR_SAVE, event -> {
		    if (!editor.isValidOperationData()) {
			AppNotificator.notifyError(getTranslation(AppNotifyMsg.COURIER_REQ_CHK_FAIL));
			return;
		    }
		    try {
			Optional<CourierVisit> courierRequestOut = issuedCourierVisitService.registerChangesAndSave(
				editor.getOperationData(), visitSnapshot,
				SecurityUtils.getUser());

			if (courierRequestOut.isPresent()) {
			    selectedCourierVisitsGrid.getDataCommunicator().refresh(courierRequestOut.get());
			    courierVisitsGrid.getDataCommunicator().refresh(courierRequestOut.get());
			}
			dialogForm.closeWithResult(Modality.MR_SAVE);
		    } catch (Exception e) {
			AppNotificator.notifyError(5000, e.getMessage());
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
     * Inactive visits redraft.
     *
     * @param visitsGrid the visits grid
     */
    private void inactiveVisitsRedraft(Grid<CourierVisit> visitsGrid) {
	Collection<CourierVisit> courierVisitsIn = visitsGrid.getSelectionModel().getSelectedItems();
	if (courierVisitsIn.isEmpty()) {
	    return;
	}

	Map<Boolean, List<CourierVisit>> visitsByStates = courierVisitsIn
		.stream()
		.map(v -> issuedCourierVisitService.fetchById(v.getId()))
		.filter(Optional<CourierVisit>::isPresent)
		.map(Optional<CourierVisit>::get)
		.collect(Collectors.partitioningBy(CourierVisit::isActive));

	if (!visitsByStates.get(true).isEmpty() || visitsByStates.get(false).isEmpty()) {
	    AppNotificator
		    .notify(5000,
			    getTranslation(AppNotifyMsg.VISITS_NOTHING_REDRAFTS));
	    return;
	}
	TextField description = new TextField();
	description.focus();
	description.setWidth(Props.EM_44);
	description.setRequired(true);
	description.setRequiredIndicatorVisible(true);
	description.setMinLength(10);
	description.setValueChangeMode(ValueChangeMode.EAGER);
	description.addValueChangeListener(e -> description.setInvalid(e.getValue().length() < 10));
	description.setValue("");
	description.setInvalid(true);
	description.setErrorMessage(getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
	VerticalLayout panel = new VerticalLayout(
		new Label(getTranslation(IssuedRouteSheetMsg.ISSUED_ROUTE_SHEET_REDRAFT_MSG)),
		description);
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	ConfirmDialog
		.create()
		.withCaption(getTranslation(IssuedRouteSheetMsg.ISSUED_ROUTE_SHEET_REDRAFT_HEADER))
		.withMessage(panel)
		.withSaveButton(() -> {
		    if (description.isInvalid()) {
			AppNotificator.notifyError(5000,
				getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
			return;
		    }
		    try {

			issuedCourierVisitService.redraftVisits(
				visitsByStates.get(false), description.getValue(),
				SecurityUtils.getUser());

			AppNotificator.notify(getTranslation(AppNotifyMsg.VISITS_WERE_REDRAFTS));
		    } catch (Exception e) {
			AppNotificator.notifyError(5000, e.getMessage());
		    }

		}, ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_YES)),
			ButtonOption.icon(UIIcon.BTN_YES.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * Active visits cancel.
     *
     * @param visitsGrid the visits grid
     */
    private void activeVisitsCancel(Grid<CourierVisit> visitsGrid) {
	Collection<CourierVisit> courierVisitsIn = visitsGrid.getSelectionModel().getSelectedItems();
	if (courierVisitsIn.isEmpty()) {
	    return;
	}

	Collection<CourierVisit> courierVisitsInL = courierVisitsIn
		.stream()
		.map(v -> issuedCourierVisitService.fetchById(v.getId()))
		.filter(Optional<CourierVisit>::isPresent)
		.map(Optional<CourierVisit>::get)
		.filter(CourierVisit::isActive)
		.collect(Collectors.toList());

	if (courierVisitsInL.isEmpty()) {
	    AppNotificator.notifyError(getTranslation(AppNotifyMsg.NOTHING_TO_CANCEL));
	    return;
	}
	TextField description = new TextField();
	description.focus();
	description.setWidth(Props.EM_44);
	description.setRequired(true);
	description.setRequiredIndicatorVisible(true);
	description.setMinLength(10);
	description.setValueChangeMode(ValueChangeMode.EAGER);
	description.addValueChangeListener(e -> description.setInvalid(e.getValue().length() < 10));
	description.setValue("");
	description.setInvalid(true);
	description.setErrorMessage(getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
	VerticalLayout panel = new VerticalLayout(
		new Label(getTranslation(IssuedRouteSheetMsg.ISSUED_ROUTE_SHEET_CANCEL_ALL_MSG)),
		description);
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	ConfirmDialog
		.create()
		.withCaption(getTranslation(IssuedRouteSheetMsg.ISSUED_ROUTE_SHEET_CANCEL_ALL_HEADER))
		.withMessage(panel)
		.withSaveButton(() -> {
		    if (description.isInvalid()) {
			AppNotificator.notifyError(5000,
				getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
			return;
		    }
		    try {

			Collection<CourierVisit> markedCourierVisits = issuedCourierVisitService.markVisitsAsCancelled(
				courierVisitsInL, description.getValue(),
				SecurityUtils.getUser());

			routeSheetsDataProvider.refreshAll();

			markedCourierVisits.forEach(courierVisit -> {
			    selectedCourierVisitsGrid.getDataCommunicator().refresh(courierVisit);
			    courierVisitsGrid.getDataCommunicator().refresh(courierVisit);
			});
			AppNotificator.notify(getTranslation(AppNotifyMsg.VISITS_MARKS_AS_CANCELLED));
		    } catch (Exception e) {
			AppNotificator.notifyError(5000, e.getMessage());
		    }

		}, ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_YES)),
			ButtonOption.icon(UIIcon.BTN_YES.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * Active visits to done.
     *
     * @param visitsGrid the visits grid
     */
    private void activeVisitsToDone(Grid<CourierVisit> visitsGrid) {
	Collection<CourierVisit> courierVisitsIn = visitsGrid.getSelectionModel().getSelectedItems();
	if (courierVisitsIn.isEmpty()) {
	    return;
	}

	Collection<CourierVisit> courierVisitsInL = courierVisitsIn
		.stream()
		.map(v -> issuedCourierVisitService.fetchById(v.getId()))
		.filter(Optional<CourierVisit>::isPresent)
		.map(Optional<CourierVisit>::get)
		.filter(CourierVisit::isActive)
		.collect(Collectors.toList());

	if (courierVisitsInL.isEmpty()) {
	    AppNotificator.notifyError(getTranslation(AppNotifyMsg.NOTHING_TO_COMPLETE));
	    return;
	}

	ConfirmDialog
		.create()
		.withCaption(getTranslation(IssuedRouteSheetMsg.ISSUED_ROUTE_SHEET_DONE_ALL_HEADER))
		.withMessage(getTranslation(IssuedRouteSheetMsg.ISSUED_ROUTE_SHEET_DONE_ALL_MSG))
		.withSaveButton(() -> {
		    try {

			Collection<CourierVisit> markedCourierVisits = issuedCourierVisitService
				.markVisitsAsCompleted(courierVisitsInL, SecurityUtils.getUser());

			routeSheetsDataProvider.refreshAll();
			markedCourierVisits.forEach(courierVisit -> {
			    selectedCourierVisitsGrid.getDataCommunicator().refresh(courierVisit);
			    courierVisitsGrid.getDataCommunicator().refresh(courierVisit);
			});

			AppNotificator.notify(getTranslation(AppNotifyMsg.VISITS_MARKS_AS_COMPLETED));
		    } catch (Exception e) {
			AppNotificator.notifyError(5000, e.getMessage());
		    }

		}, ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_YES)),
			ButtonOption.icon(UIIcon.BTN_YES.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * Gets the page title.
     *
     * @return the page title
     */
    @Override
    public String getPageTitle() {
	return getTranslation(AppTitleMsg.APP_TITLE_ISSUED, UI.getCurrent().getLocale());
    }
}
