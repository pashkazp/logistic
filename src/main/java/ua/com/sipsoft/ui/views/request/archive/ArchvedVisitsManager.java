package ua.com.sipsoft.ui.views.request.archive;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;

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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisit;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedRouteSheet;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedRouteSheetEvent;
import ua.com.sipsoft.services.requests.arcive.ArchivedSheetFilter;
import ua.com.sipsoft.services.requests.arcive.ArchivedSheetsService;
import ua.com.sipsoft.services.requests.arcive.ArchivedVisitsFilter;
import ua.com.sipsoft.services.requests.arcive.ArchivedVisitsService;
import ua.com.sipsoft.ui.MainView;
import ua.com.sipsoft.ui.commons.AppNotificator;
import ua.com.sipsoft.ui.views.request.common.HistoryEventViever;
import ua.com.sipsoft.utils.AppURL;
import ua.com.sipsoft.utils.CourierVisitState;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.messages.AppNotifyMsg;
import ua.com.sipsoft.utils.messages.AppTitleMsg;
import ua.com.sipsoft.utils.messages.ArchivedRouteSheetMsg;
import ua.com.sipsoft.utils.messages.ButtonMsg;
import ua.com.sipsoft.utils.messages.CourierRequestsMsg;
import ua.com.sipsoft.utils.messages.GridToolMsg;
import ua.com.sipsoft.utils.messages.HistoryEventMsg;
import ua.com.sipsoft.utils.security.SecurityUtils;

/**
 * The Class ArchvedVisitsManager.
 *
 * @author Pavlo Degtyaryev
 */

/** The Constant log. */
@Slf4j
@UIScope
@SpringComponent
@Route(value = AppURL.ARCHIVE, layout = MainView.class)
public class ArchvedVisitsManager extends VerticalLayout implements HasDynamicTitle {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7818254835132131885L;

    /** The btn archived sheet print. */
    private Button btnSheetPrint;

    /** The btn issued sheet grid reset. */
    private Button btnSheetGridReset;

    /** The btn inactive sheet visits recreate. */
    private Button btnSheetsVisitsRecreate;

    /** The btn sheet visits grid reset. */
    private Button btnSheetsVisitsGridReset;

    /** The btn archived visits recreate. */
    private Button btnVisitsRecreate;

    /** The btn visits grid reset. */
    private Button btnVisitsGridReset;

    /** The archived sheets grid. */
    private Grid<ArchivedRouteSheet> archivedSheetGrid;

    /** The selected courier visits grid. */
    private Grid<ArchivedCourierVisit> sheetsVisitsGrid;

    /** The courier visits grid. */
    private Grid<ArchivedCourierVisit> visitsGrid;

    /** The field archived sheet filter. */
    private TextField fieldSheetFilter;

    /** The field selected visits filter. */
    private TextField fieldSheetsVisitsFilter;

    /** The field visits filter. */
    private TextField fieldVisitsFilter;

    /** The archived route sheet service. */
    private final transient ArchivedSheetsService sheetsService;

    /** The archived courier visit service. */
    private final transient ArchivedVisitsService visitsService;

    /** The route sheets data provider. */
    private DataProvider<ArchivedRouteSheet, ArchivedSheetFilter> sheetsDataProvider;

    /** The selected courier visit data provider. */
    private DataProvider<ArchivedCourierVisit, ArchivedVisitsFilter> sheetsVisitsDataProvider;

    /** The courier visit data provider. */
    private DataProvider<ArchivedCourierVisit, ArchivedVisitsFilter> visitsDataProvider;

    /**
     * Instantiates a new courier visits manager.
     *
     * @param sheetsService the archived route sheet service
     * @param visitsService the archived courier visit service
     */
    public ArchvedVisitsManager(ArchivedSheetsService sheetsService,
	    ArchivedVisitsService visitsService) {
	super();
	this.visitsService = visitsService;
	this.sheetsService = sheetsService;

	SplitLayout leftSplitPanel = new SplitLayout();
	leftSplitPanel.setOrientation(Orientation.VERTICAL);
	leftSplitPanel.addToPrimary(getPanelSheetGridPresenter());
	leftSplitPanel.addToSecondary(getPanelSheetsVisitsGridPresenter());

	SplitLayout background = new SplitLayout();
	background.addToPrimary(leftSplitPanel);
	background.addToSecondary(getPanelVisitsGridPresenter());
	add(background);
	setAlignItems(Alignment.STRETCH);
	setFlexGrow(1, background);
	setSizeFull();

	background.setSplitterPosition(60.0);
	leftSplitPanel.setSplitterPosition(35.0);

	prepareSheetsGrid();
	prepareSheetsVisitsGrid();
	prepareVisitsGrid();

    }

    /**
     * Gets the panel archived sheet grid presenter.
     *
     * @return the panel archived sheet grid presenter
     */
    private Component getPanelSheetGridPresenter() {
	btnSheetPrint = new Button(UIIcon.PRINTER.createIcon());
	btnSheetGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());

	fieldSheetFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldSheetFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnSheetPrint.setSizeUndefined();
	btnSheetGridReset.setSizeUndefined();

	btnSheetPrint.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnSheetGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnSheetGridReset.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);

	HorizontalLayout panelSheetsToolbar = new HorizontalLayout(fieldSheetFilter, btnSheetPrint,
		btnSheetGridReset);
	panelSheetsToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelSheetsToolbar.setFlexGrow(0, btnSheetPrint);
	panelSheetsToolbar.setFlexGrow(0, btnSheetGridReset);
	panelSheetsToolbar.setFlexGrow(1, fieldSheetFilter);
	panelSheetsToolbar.setMargin(false);
	panelSheetsToolbar.setPadding(false);
	panelSheetsToolbar.setSpacing(true);

	archivedSheetGrid = new Grid<>(ArchivedRouteSheet.class);
	archivedSheetGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

	VerticalLayout panelSheetsGrigPresenter = new VerticalLayout(panelSheetsToolbar, archivedSheetGrid);
	panelSheetsGrigPresenter.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	panelSheetsGrigPresenter.setWidthFull();
	panelSheetsGrigPresenter.setMargin(false);
	panelSheetsGrigPresenter.setPadding(false);
	panelSheetsGrigPresenter.setSpacing(false);

	HorizontalLayout panelSheetsManage = new HorizontalLayout(panelSheetsGrigPresenter);
	panelSheetsManage.setFlexGrow(1, panelSheetsGrigPresenter);
	panelSheetsManage.setMargin(false);
	panelSheetsManage.setPadding(false);
	panelSheetsManage.setSpacing(false);

	btnSheetPrint.addClickListener(e -> archivedSheetPrint());

	return panelSheetsGrigPresenter;
    }

    /**
     * Gets the panel selected visits grid presenter.
     *
     * @return the panel selected visits grid presenter
     */
    private Component getPanelSheetsVisitsGridPresenter() {

	btnSheetsVisitsRecreate = new Button(UIIcon.SHEET_REDRAFT.createIcon());
	btnSheetsVisitsGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());

	btnSheetsVisitsRecreate.setSizeUndefined();
	btnSheetsVisitsGridReset.setSizeUndefined();

	fieldSheetsVisitsFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldSheetsVisitsFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnSheetsVisitsRecreate.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnSheetsVisitsGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnSheetsVisitsGridReset.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);

	HorizontalLayout panelSheetVisitsToolbar = new HorizontalLayout(fieldSheetsVisitsFilter,
		btnSheetsVisitsRecreate, btnSheetsVisitsGridReset);

	panelSheetVisitsToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelSheetVisitsToolbar.setFlexGrow(0, btnSheetsVisitsRecreate);
	panelSheetVisitsToolbar.setFlexGrow(0, btnSheetsVisitsGridReset);
	panelSheetVisitsToolbar.setFlexGrow(1, fieldSheetsVisitsFilter);
	panelSheetVisitsToolbar.setMargin(false);
	panelSheetVisitsToolbar.setPadding(false);
	panelSheetVisitsToolbar.setSpacing(false);

	sheetsVisitsGrid = new Grid<>(ArchivedCourierVisit.class);
	sheetsVisitsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

	VerticalLayout panelSheetVisitsGrigPresenter = new VerticalLayout(panelSheetVisitsToolbar,
		sheetsVisitsGrid);
	panelSheetVisitsGrigPresenter.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	panelSheetVisitsGrigPresenter.setMargin(false);
	panelSheetVisitsGrigPresenter.setPadding(false);
	panelSheetVisitsGrigPresenter.setSpacing(false);

	HorizontalLayout panelSheetVisitsManage = new HorizontalLayout(panelSheetVisitsGrigPresenter);
	panelSheetVisitsManage.setFlexGrow(1, panelSheetVisitsGrigPresenter);
	panelSheetVisitsManage.setMargin(false);
	panelSheetVisitsManage.setPadding(false);
	panelSheetVisitsManage.setSpacing(false);

	btnSheetsVisitsRecreate.addClickListener(e -> archivedVisitsRedraft(sheetsVisitsGrid));
	return panelSheetVisitsGrigPresenter;
    }

    /**
     * Gets the panel visits grid presenter.
     *
     * @return the panel visits grid presenter
     */
    private Component getPanelVisitsGridPresenter() {

	btnVisitsRecreate = new Button(UIIcon.SHEET_REDRAFT.createIcon());
	btnVisitsGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());
	btnVisitsRecreate.setSizeUndefined();
	btnVisitsGridReset.setSizeUndefined();

	fieldVisitsFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldVisitsFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnVisitsRecreate.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnVisitsGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnVisitsGridReset.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);

	HorizontalLayout panelVisitsToolbar = new HorizontalLayout(fieldVisitsFilter, btnVisitsRecreate,
		btnVisitsGridReset);

	panelVisitsToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelVisitsToolbar.setFlexGrow(0, btnVisitsRecreate);
	panelVisitsToolbar.setFlexGrow(0, btnVisitsGridReset);
	panelVisitsToolbar.setFlexGrow(1, fieldVisitsFilter);
	panelVisitsToolbar.setMargin(false);
	panelVisitsToolbar.setPadding(false);
	panelVisitsToolbar.setSpacing(false);

	visitsGrid = new Grid<>(ArchivedCourierVisit.class);
	visitsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

	VerticalLayout panelVisitsPresenter = new VerticalLayout(panelVisitsToolbar, visitsGrid);
	panelVisitsPresenter.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	panelVisitsPresenter.setMargin(false);
	panelVisitsPresenter.setPadding(false);
	panelVisitsPresenter.setSpacing(false);

	btnVisitsRecreate.addClickListener(e -> archivedVisitsRedraft(visitsGrid));
	return panelVisitsPresenter;
    }

    /**
     * Gets the filtered archived route sheet query.
     *
     * @param query the {@link Query}
     * @return the filtered {@link Stream}<{@link ArchivedRouteSheet}>
     */
    private Stream<ArchivedRouteSheet> getFilteredArchivedRouteSheetQuery(
	    Query<ArchivedRouteSheet, ArchivedSheetFilter> query) {
	return sheetsService.getQueriedArchivedSheetsByFilter(
		query, getArchivedSheetFilter());
    }

    /**
     * Gets the filtered archived route sheet query count.
     *
     * @param query the {@link Query}
     * @return the filtered {@link Stream}<{@link ArchivedRouteSheet}> records count
     */
    private int getFilteredArchivedRouteSheetQueryCount(
	    Query<ArchivedRouteSheet, ArchivedSheetFilter> query) {
	return sheetsService.ggetQueriedArchivedSheetsByFilterCount(
		query, getArchivedSheetFilter());
    }

    /**
     * Gets the archived route sheet filter.
     *
     * @return the {@link ArchivedSheetFilter} filter
     */
    private ArchivedSheetFilter getArchivedSheetFilter() {
	return ArchivedSheetFilter.builder()
		.description(getSanitizedSheetFilter())
		.build();
    }

    /**
     * Gets the sanitized archived sheet filter.
     *
     * @return the sanitized archived sheet filter
     */
    private String getSanitizedSheetFilter() {
	return StringUtils.truncate(fieldSheetFilter.getValue(), 100);
    }

    /**
     * Prepare archived sheet grid.
     */
    private void prepareSheetsGrid() {
	sheetsDataProvider = DataProvider.fromFilteringCallbacks(
		this::getFilteredArchivedRouteSheetQuery,
		this::getFilteredArchivedRouteSheetQueryCount);
	archivedSheetGrid.setDataProvider(sheetsDataProvider.withConfigurableFilter());

	btnSheetPrint.setEnabled(false);

	fieldSheetFilter.setValueChangeMode(ValueChangeMode.EAGER);
	fieldSheetFilter.addValueChangeListener(e -> {
	    archivedSheetGrid.getSelectionModel().deselectAll();
	    archivedSheetGrid.getDataProvider().refreshAll();
	});

	archivedSheetGrid.removeAllColumns();

	archivedSheetGrid
		.addColumn("id")
		.setHeader(getTranslation(ArchivedRouteSheetMsg.ARCHIVED))
		.setSortProperty("id")
		.setWidth(Props.EM_05)
		.setFlexGrow(0);
	archivedSheetGrid
		.addColumn("description")
		.setHeader(getTranslation(ArchivedRouteSheetMsg.DESCRIPTION))
		.setFlexGrow(1)
		.setSortProperty("description");
	archivedSheetGrid
		.addColumn(new LocalDateTimeRenderer<>(ArchivedRouteSheet::getCreationDate,
			DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
		.setHeader(getTranslation(ArchivedRouteSheetMsg.CREATION_DATE))
		.setWidth(Props.EM_09)
		.setFlexGrow(0)
		.setSortProperty("creationDate");
	archivedSheetGrid
		.addColumn("author.username")
		.setHeader(getTranslation(ArchivedRouteSheetMsg.AUTHOR))
		.setWidth(Props.EM_10)
		.setFlexGrow(0)
		.setSortProperty("author.username");
	archivedSheetGrid
		.addColumn(buildVisitsStatesColumn(), "states")
		.setHeader(getTranslation(ArchivedRouteSheetMsg.STATES))
		.setWidth(Props.EM_05)
		.setFlexGrow(0)
		.setSortProperty("states");

	archivedSheetGrid
		.addColumn(new ComponentRenderer<Icon, ArchivedRouteSheet>(request -> {
		    Icon iconInfo = UIIcon.INFO.createIcon();
		    iconInfo.setSize(Props.EM_01);
		    iconInfo.addClickListener(event -> ConfirmDialog
			    .create()
			    .withCaption(getTranslation(HistoryEventMsg.HEADER_VEIW))
			    .withMessage(new HistoryEventViever<>(new Grid<>(ArchivedRouteSheetEvent.class),
				    request.getHistoryEvents()))
			    .withCloseButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CLOSE)),
				    ButtonOption.icon(UIIcon.BTN_OK.getIcon()))
			    .open());
		    return iconInfo;
		}))
		.setHeader(getTranslation(GridToolMsg.INFO_COLUMN))
		.setWidth(Props.EM_05)
		.setFlexGrow(0);

	archivedSheetGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});

	archivedSheetGrid.setColumnReorderingAllowed(true);
	archivedSheetGrid.addSelectionListener(event -> {
	    ArchivedRouteSheet archivedRouteSheet = event.getFirstSelectedItem().orElse(null);

	    sheetsVisitsGrid.getSelectionModel().deselectAll();
	    sheetsVisitsDataProvider.refreshAll();

	    if (archivedRouteSheet == null) {
		btnSheetPrint.setEnabled(false);

	    } else {
		btnSheetPrint.setEnabled(true);
	    }

	});

	fieldSheetFilter.addValueChangeListener(e -> archivedSheetGrid.getDataProvider().refreshAll());
	archivedSheetGrid.setMultiSort(true);

	btnSheetGridReset.addClickListener(e -> sheetsDataProvider.refreshAll());
    }

    /**
     * Builds the visits states column.
     *
     * @return the component renderer
     */
    private ComponentRenderer<Div, ArchivedRouteSheet> buildVisitsStatesColumn() {
	return new ComponentRenderer<>(sheet -> {
	    Div div = new Div();
	    if (sheet == null) {
		return div;
	    }
	    Set<ArchivedCourierVisit> courierVisits = sheet.getRequests();

	    if (courierVisits != null && !courierVisits.isEmpty()) {
		Icon[] sb = new Icon[CourierVisitState.values().length];
		Icon icon;
		for (ArchivedCourierVisit s : courierVisits) {
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
	    Query<ArchivedCourierVisit, ArchivedVisitsFilter> query) {
	return visitsService.getQueriedCourierVisitsByFilterCount(
		query, getSheetVisitsRequestFilter());
    }

    /**
     * Gets the queried courier visits by filter.
     *
     * @param query the query
     * @return the queried courier visits by filter
     */
    private Stream<ArchivedCourierVisit> getQueriedCourierVisitsByFilter(
	    Query<ArchivedCourierVisit, ArchivedVisitsFilter> query) {
	return visitsService.getQueriedCourierVisitsByFilter(
		query, getSheetVisitsRequestFilter());
    }

    /**
     * Gets the selected visit request filter.
     *
     * @return the selected visit request filter
     */
    private ArchivedVisitsFilter getSheetVisitsRequestFilter() {
	return ArchivedVisitsFilter.builder()
		.sheetId(archivedSheetGrid.getSelectionModel().getFirstSelectedItem()
			.map(ArchivedRouteSheet::getId).orElse(-1L))
		.description(getSanitizedSheetVisitsFilter())
		.build();
    }

    /**
     * Gets the sanitized selected visit filter.
     *
     * @return the sanitized selected visit filter
     */
    private String getSanitizedSheetVisitsFilter() {
	return StringUtils.truncate(fieldSheetsVisitsFilter.getValue(), 100);
    }

    /**
     * Prepare sheet visits grid.
     */
    private void prepareSheetsVisitsGrid() {

	sheetsVisitsDataProvider = DataProvider.fromFilteringCallbacks(
		this::getQueriedCourierVisitsByFilter,
		this::getFilteredSelectedVisitQueryCount);
	sheetsVisitsGrid.setDataProvider(sheetsVisitsDataProvider.withConfigurableFilter());

	btnSheetsVisitsRecreate.setEnabled(false);

	fieldSheetsVisitsFilter.setValueChangeMode(ValueChangeMode.EAGER);
	fieldSheetsVisitsFilter
		.addValueChangeListener(e -> {
		    sheetsVisitsGrid.getSelectionModel().deselectAll();
		    sheetsVisitsGrid.getDataProvider().refreshAll();
		});

	sheetsVisitsGrid.removeAllColumns();

	sheetsVisitsGrid
		.addColumn("id")
		.setHeader(getTranslation(CourierRequestsMsg.REQUESTID))
		.setSortProperty("id")
		.setWidth(Props.EM_05)
		.setFlexGrow(0);
	sheetsVisitsGrid
		.addColumn(new ComponentRenderer<>(ArchivedVisitsPresenter::new))
		.setHeader(getTranslation(CourierRequestsMsg.REQUEST))
		.setFlexGrow(1);

	sheetsVisitsGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});
	sheetsVisitsGrid.getColumns().forEach(column -> column.setSortable(true));
	sheetsVisitsGrid.setColumnReorderingAllowed(true);

	GridMultiSelectionModel<ArchivedCourierVisit> multiSelection = (GridMultiSelectionModel<ArchivedCourierVisit>) sheetsVisitsGrid
		.setSelectionMode(Grid.SelectionMode.MULTI);

	multiSelection.setSelectAllCheckboxVisibility(SelectAllCheckboxVisibility.VISIBLE);
	multiSelection.setSelectionColumnFrozen(true);
	multiSelection.addMultiSelectionListener(event -> {
	    btnSheetsVisitsRecreate
		    .setEnabled(!event.getAllSelectedItems().isEmpty());
	});

	btnSheetsVisitsGridReset.addClickListener(e -> sheetsVisitsDataProvider.refreshAll());
    }

    /**
     * Gets the filtered courier visit queryy count.
     *
     * @param query the query
     * @return the filtered courier visit queryy count
     */
    private int getFilteredCourierVisitQueryCount(
	    Query<ArchivedCourierVisit, ArchivedVisitsFilter> query) {
	return visitsService.getQueriedCourierVisitsByFilterCount(
		query, getCourierRequestFilter());
    }

    /**
     * Gets the filtered courier visit query.
     *
     * @param query the query
     * @return the filtered courier visit query
     */
    private Stream<ArchivedCourierVisit> getFilteredCourierVisitQuery(
	    Query<ArchivedCourierVisit, ArchivedVisitsFilter> query) {
	return visitsService.getQueriedCourierVisitsByFilter(
		query, getCourierRequestFilter());
    }

    /**
     * Gets the courier request filter.
     *
     * @return the courier request filter
     */
    private ArchivedVisitsFilter getCourierRequestFilter() {
	return ArchivedVisitsFilter.builder()
		.description(getSanitizadVisitFilter())
		.build();
    }

    /**
     * Gets the sanitizad courier visit filter.
     *
     * @return the sanitizad courier visit filter
     */
    private String getSanitizadVisitFilter() {
	return StringUtils.truncate(fieldVisitsFilter.getValue(), 100);
    }

    /**
     * Prepare visits grid.
     */
    private void prepareVisitsGrid() {

	visitsDataProvider = DataProvider.fromFilteringCallbacks(
		this::getFilteredCourierVisitQuery,
		this::getFilteredCourierVisitQueryCount);
	visitsGrid.setDataProvider(visitsDataProvider.withConfigurableFilter());

	btnVisitsRecreate.setEnabled(false);

	fieldVisitsFilter.setValueChangeMode(ValueChangeMode.EAGER);
	fieldVisitsFilter.addValueChangeListener(e -> {
	    visitsGrid.getSelectionModel().deselectAll();
	    visitsGrid.getDataProvider().refreshAll();
	});

	visitsGrid.removeAllColumns();

	visitsGrid
		.addColumn("id")
		.setHeader(getTranslation(CourierRequestsMsg.REQUESTID))
		.setSortProperty("id")
		.setWidth(Props.EM_05)
		.setFlexGrow(0);
	visitsGrid
		.addColumn(new ComponentRenderer<>(ArchivedVisitsPresenter::new))
		.setHeader(getTranslation(CourierRequestsMsg.REQUEST))
		.setFlexGrow(1);

	visitsGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});
	visitsGrid.setColumnReorderingAllowed(true);

	GridMultiSelectionModel<ArchivedCourierVisit> multiSelection = (GridMultiSelectionModel<ArchivedCourierVisit>) visitsGrid
		.setSelectionMode(Grid.SelectionMode.MULTI);
	multiSelection.setSelectionColumnFrozen(true);
	multiSelection.setSelectAllCheckboxVisibility(SelectAllCheckboxVisibility.VISIBLE);
	multiSelection.addMultiSelectionListener(event -> {
	    btnVisitsRecreate.setEnabled(!event.getAllSelectedItems().isEmpty());
	});

	btnVisitsGridReset.addClickListener(e -> visitsDataProvider.refreshAll());
    }

    /**
     * Archived sheet print.
     */
    private void archivedSheetPrint() {
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
     * Inactive visits redraft.
     *
     * @param visitsGrid the visits grid
     */
    private void archivedVisitsRedraft(Grid<ArchivedCourierVisit> visitsGrid) {
	Collection<ArchivedCourierVisit> courierVisitsIn = visitsGrid.getSelectionModel().getSelectedItems();
	if (courierVisitsIn.isEmpty()) {
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
		new Label(getTranslation(ArchivedRouteSheetMsg.ARCHIVED_REDRAFT_MSG)),
		description);
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	ConfirmDialog
		.create()
		.withCaption(getTranslation(ArchivedRouteSheetMsg.ARCHIVED_REDRAFT_HEADER))
		.withMessage(panel)
		.withSaveButton(() -> {
		    if (description.isInvalid()) {
			AppNotificator.notify(5000,
				getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
			return;
		    }
		    try {

			visitsService.redraftVisits(
				courierVisitsIn, description.getValue(),
				SecurityUtils.getUser());

			AppNotificator.notify(getTranslation(AppNotifyMsg.VISITS_WERE_REDRAFTS));
		    } catch (Exception e) {
			AppNotificator.notify(5000, e.getMessage());
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
	return getTranslation(AppTitleMsg.APP_TITLE_ARCHIVED, UI.getCurrent().getLocale());
    }
}
