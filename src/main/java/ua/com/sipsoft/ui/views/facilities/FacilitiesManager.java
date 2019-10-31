package ua.com.sipsoft.ui.views.facilities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Lookup;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridMultiSelectionModel.SelectAllCheckboxVisibility;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.common.Facility;
import ua.com.sipsoft.model.entity.common.FacilityAddress;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.common.FacilitiesFilter;
import ua.com.sipsoft.services.common.FacilitiesService;
import ua.com.sipsoft.services.common.FacilityAddrService;
import ua.com.sipsoft.services.common.FacilityAddressFilter;
import ua.com.sipsoft.services.users.UserFilter;
import ua.com.sipsoft.services.users.UsersService;
import ua.com.sipsoft.ui.MainView;
import ua.com.sipsoft.ui.commons.AppNotificator;
import ua.com.sipsoft.ui.commons.dialogform.DialogForm;
import ua.com.sipsoft.ui.commons.dialogform.Modality;
import ua.com.sipsoft.utils.AppURL;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.messages.AppNotifyMsg;
import ua.com.sipsoft.utils.messages.AppTitleMsg;
import ua.com.sipsoft.utils.messages.ButtonMsg;
import ua.com.sipsoft.utils.messages.FacilityAddrEntityMsg;
import ua.com.sipsoft.utils.messages.FacilityEntityMsg;
import ua.com.sipsoft.utils.messages.FacilityUsersMsg;
import ua.com.sipsoft.utils.messages.GridToolMsg;
import ua.com.sipsoft.utils.messages.UserEntityMsg;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Class FacilitiesManager.
 *
 * @author Pavlo Degtyaryev
 */

/** The Constant log. */
@Slf4j
@UIScope
@SpringComponent
@Route(value = AppURL.FACILITIES_ALL, layout = MainView.class)
public class FacilitiesManager extends VerticalLayout implements HasDynamicTitle {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -497333939332082677L;

    /** The btn facilities add. */
    private Button btnFacilitiesAdd;

    /** The btn facilities del. */
    private Button btnFacilitiesDel;

    /** The btn facilities edt. */
    private Button btnFacilitiesEdt;

    /** The btn facilities grid reset. */
    private Button btnFacilitiesGridReset;

    /** The btn facility addrr add. */
    private Button btnFacilityAddrrAdd;

    /** The btn facility addrr del. */
    private Button btnFacilityAddrrDel;

    /** The btn facility addrr edt. */
    private Button btnFacilityAddrrEdt;

    /** The btn facility addr grid reset. */
    private Button btnFacilityAddrGridReset;

    /** The btn users add. */
    private Button btnUsersAdd;

    /** The btn users del. */
    private Button btnUsersDel;

    /** The btn users grid reset. */
    private Button btnUsersGridReset;

    /** The facilities grid. */
    private Grid<Facility> facilitiesGrid;

    /** The facility addr grid. */
    private Grid<FacilityAddress> facilityAddrGrid;

    /** The users grid. */
    private Grid<User> usersGrid;

    /** The field facilities filter. */
    private TextField fieldFacilitiesFilter;

    /** The field facilities addr filter. */
    private TextField fieldFacilitiesAddrFilter;

    /** The field users filter. */
    private TextField fieldUsersFilter;

    /** The facilities service. */
    private final transient FacilitiesService facilitiesService;

    /** The facility adrr service. */
    private final transient FacilityAddrService facilityAdrrService;

    /** The users service. */
    private final transient UsersService usersService;

    /** The facillity data provider. */
    private DataProvider<Facility, FacilitiesFilter> facillityDataProvider;

    /** The facility address data provider. */
    private DataProvider<FacilityAddress, FacilityAddressFilter> facilityAddressDataProvider;

    /** The user data provider. */
    private DataProvider<User, UserFilter> userDataProvider;

    /**
     * Instantiates a new facilities manager.
     *
     * @param facilitiesService   the facilities service
     * @param facilityAdrrService the facility adrr service
     * @param usersService        the users service
     */
    public FacilitiesManager(FacilitiesService facilitiesService, FacilityAddrService facilityAdrrService,
	    UsersService usersService) {
	super();

	this.facilitiesService = facilitiesService;
	this.facilityAdrrService = facilityAdrrService;
	this.usersService = usersService;

	SplitLayout leftSplitPanel = new SplitLayout();
	leftSplitPanel.setOrientation(Orientation.VERTICAL);
	leftSplitPanel.addToPrimary(getPanelFacilitiesGrigPresenter());
	leftSplitPanel.addToSecondary(getPanelFacilityAdrrGrigPresenter());

	SplitLayout background = new SplitLayout();
	background.addToPrimary(leftSplitPanel);
	background.addToSecondary(getPanelUsersGridPresenter());
	add(background);
	setAlignItems(Alignment.STRETCH);
	setFlexGrow(1, background);
	setSizeFull();

	background.setSplitterPosition(60.0);
	leftSplitPanel.setSplitterPosition(65.0);

	prepareFacilitiesGrid();
	prepareFacilityAddrGrid();
	prepareUsersGrid();

    }

    /**
     * Gets the panel facilities grig presenter.
     *
     * @return the panel facilities grig presenter
     */
    private Component getPanelFacilitiesGrigPresenter() {
	btnFacilitiesAdd = new Button(UIIcon.BTN_ADD.createIcon());
	btnFacilitiesDel = new Button(UIIcon.BTN_NO.createIcon());
	btnFacilitiesEdt = new Button(UIIcon.BTN_EDIT.createIcon());
	btnFacilitiesGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());

	fieldFacilitiesFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldFacilitiesFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnFacilitiesAdd.setSizeUndefined();
	btnFacilitiesEdt.setSizeUndefined();
	btnFacilitiesDel.setSizeUndefined();
	btnFacilitiesGridReset.setSizeUndefined();

	btnFacilitiesAdd.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnFacilitiesEdt.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnFacilitiesDel.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnFacilitiesGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnFacilitiesGridReset.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);

	HorizontalLayout panelFacilitiesToolbar = new HorizontalLayout(fieldFacilitiesFilter, btnFacilitiesAdd,
		btnFacilitiesDel, btnFacilitiesEdt, btnFacilitiesGridReset);
	panelFacilitiesToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelFacilitiesToolbar.setFlexGrow(0, btnFacilitiesAdd);
	panelFacilitiesToolbar.setFlexGrow(0, btnFacilitiesEdt);
	panelFacilitiesToolbar.setFlexGrow(0, btnFacilitiesDel);
	panelFacilitiesToolbar.setFlexGrow(0, btnFacilitiesGridReset);
	panelFacilitiesToolbar.setFlexGrow(1, fieldFacilitiesFilter);
	panelFacilitiesToolbar.setMargin(false);
	panelFacilitiesToolbar.setPadding(false);
	panelFacilitiesToolbar.setSpacing(true);

	facilitiesGrid = new Grid<>(Facility.class);
	facilitiesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

	VerticalLayout panelFacilitiesGrigPresenter = new VerticalLayout(panelFacilitiesToolbar, facilitiesGrid);
	panelFacilitiesGrigPresenter.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	panelFacilitiesGrigPresenter.setWidthFull();
	panelFacilitiesGrigPresenter.setMargin(false);
	panelFacilitiesGrigPresenter.setPadding(false);
	panelFacilitiesGrigPresenter.setSpacing(false);

	HorizontalLayout panelFacilitiesManage = new HorizontalLayout(panelFacilitiesGrigPresenter);
	panelFacilitiesManage.setFlexGrow(1, panelFacilitiesGrigPresenter);
	panelFacilitiesManage.setMargin(false);
	panelFacilitiesManage.setPadding(false);
	panelFacilitiesManage.setSpacing(false);

	btnFacilitiesEdt.addClickListener(e -> faclilityEdit());
	btnFacilitiesAdd.addClickListener(e -> facilityAdd());
	btnFacilitiesDel.addClickListener(e -> facilityDel());

	return panelFacilitiesGrigPresenter;
    }

    /**
     * Gets the panel facility adrr grig presenter.
     *
     * @return the panel facility adrr grig presenter
     */
    private Component getPanelFacilityAdrrGrigPresenter() {

	btnFacilityAddrrAdd = new Button(UIIcon.BTN_ADD.createIcon());
	btnFacilityAddrrEdt = new Button(UIIcon.BTN_EDIT.createIcon());
	btnFacilityAddrrDel = new Button(UIIcon.BTN_NO.createIcon());
	btnFacilityAddrGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());

	btnFacilityAddrrAdd.setSizeUndefined();
	btnFacilityAddrrEdt.setSizeUndefined();
	btnFacilityAddrrDel.setSizeUndefined();
	btnFacilityAddrGridReset.setSizeUndefined();

	fieldFacilitiesAddrFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldFacilitiesAddrFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnFacilityAddrrAdd.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnFacilityAddrrEdt.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnFacilityAddrrDel.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnFacilityAddrGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnFacilityAddrGridReset.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);

	HorizontalLayout panelFacilityAddrToolbar = new HorizontalLayout(fieldFacilitiesAddrFilter, btnFacilityAddrrAdd,
		btnFacilityAddrrDel, btnFacilityAddrrEdt, btnFacilityAddrGridReset);

	panelFacilityAddrToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelFacilityAddrToolbar.setFlexGrow(0, btnFacilityAddrrAdd);
	panelFacilityAddrToolbar.setFlexGrow(0, btnFacilityAddrrDel);
	panelFacilityAddrToolbar.setFlexGrow(0, btnFacilityAddrrEdt);
	panelFacilityAddrToolbar.setFlexGrow(0, btnFacilityAddrGridReset);
	panelFacilityAddrToolbar.setFlexGrow(1, fieldFacilitiesAddrFilter);
	panelFacilityAddrToolbar.setMargin(false);
	panelFacilityAddrToolbar.setPadding(false);
	panelFacilityAddrToolbar.setSpacing(false);

	facilityAddrGrid = new Grid<>(FacilityAddress.class);
	facilityAddrGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

	VerticalLayout panelFacilityAdrrGrigPresenter = new VerticalLayout(panelFacilityAddrToolbar, facilityAddrGrid);
	panelFacilityAdrrGrigPresenter.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	panelFacilityAdrrGrigPresenter.setMargin(false);
	panelFacilityAdrrGrigPresenter.setPadding(false);
	panelFacilityAdrrGrigPresenter.setSpacing(false);

	HorizontalLayout panelFacilityAdrrManage = new HorizontalLayout(panelFacilityAdrrGrigPresenter);
	panelFacilityAdrrManage.setFlexGrow(1, panelFacilityAdrrGrigPresenter);
	panelFacilityAdrrManage.setMargin(false);
	panelFacilityAdrrManage.setPadding(false);
	panelFacilityAdrrManage.setSpacing(false);
	panelFacilityAdrrManage.getStyle().set(Props.MARGIN, Props.EM_0_5);
	panelFacilityAdrrManage.getStyle().set(Props.PADDING, null);

	btnFacilityAddrrEdt.addClickListener(e -> faclityAddrEdit());
	btnFacilityAddrrAdd.addClickListener(e -> facilityAddrAdd());
	btnFacilityAddrrDel.addClickListener(e -> facilityAddrDel());

	return panelFacilityAdrrGrigPresenter;
    }

    /**
     * Gets the panel users grid presenter.
     *
     * @return the panel users grid presenter
     */
    private Component getPanelUsersGridPresenter() {

	btnUsersAdd = new Button(UIIcon.BTN_ADD.createIcon());
	btnUsersDel = new Button(UIIcon.BTN_NO.createIcon());
	btnUsersGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());
	btnUsersAdd.setSizeUndefined();
	btnUsersDel.setSizeUndefined();
	btnUsersGridReset.setSizeUndefined();

	fieldUsersFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldUsersFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnUsersAdd.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnUsersDel.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnUsersGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnUsersGridReset.getStyle().set(Props.MARGIN_RIGHT, Props.EM_0_2);

	HorizontalLayout panelUsersToolbar = new HorizontalLayout(fieldUsersFilter, btnUsersAdd, btnUsersDel,
		btnUsersGridReset);

	panelUsersToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelUsersToolbar.setFlexGrow(0, btnUsersAdd);
	panelUsersToolbar.setFlexGrow(0, btnUsersDel);
	panelUsersToolbar.setFlexGrow(0, btnUsersGridReset);
	panelUsersToolbar.setFlexGrow(1, fieldUsersFilter);
	panelUsersToolbar.setMargin(false);
	panelUsersToolbar.setPadding(false);
	panelUsersToolbar.setSpacing(false);

	usersGrid = new Grid<>(User.class);
	usersGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

	VerticalLayout panelUsersPresenter = new VerticalLayout(panelUsersToolbar, usersGrid);
	panelUsersPresenter.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	panelUsersPresenter.setMargin(false);
	panelUsersPresenter.setPadding(false);
	panelUsersPresenter.setSpacing(false);

	btnUsersAdd.addClickListener(e -> facilityUsersLinksAdd());
	btnUsersDel.addClickListener(e -> facilityUsersLinksDrop());

	return panelUsersPresenter;
    }

    /**
     * Gets the filtered facility query.
     *
     * @param query the query
     * @return the filtered facility query
     */
    private Stream<Facility> getFilteredFacilityQuery(Query<Facility, FacilitiesFilter> query) {
	return facilitiesService.getQueriedFacilities(
		query, getFacilitiyFilter());
    }

    /**
     * Gets the facilitiy filter.
     *
     * @return the facilitiy filter
     */
    private FacilitiesFilter getFacilitiyFilter() {
	return FacilitiesFilter.builder()
		.name(getSanitizedFacilitiyFilter())
		.build();
    }

    /**
     * Gets the sanitized facilitiy filter.
     *
     * @return the sanitized facilitiy filter
     */
    private String getSanitizedFacilitiyFilter() {
	return StringUtils.truncate(fieldFacilitiesFilter.getValue(), 100);
    }

    /**
     * Gets the filtered facility query count.
     *
     * @param query the query
     * @return the filtered facility query count
     */
    private int getFilteredFacilityQueryCount(Query<Facility, FacilitiesFilter> query) {
	return facilitiesService.getQueriedFacilitiesCount(
		query, getFacilitiyFilter());
    }

    /**
     * Prepare facilities grid.
     */
    private void prepareFacilitiesGrid() {
	facillityDataProvider = DataProvider
		.fromFilteringCallbacks(
			this::getFilteredFacilityQuery,
			this::getFilteredFacilityQueryCount);

	facilitiesGrid.setDataProvider(facillityDataProvider.withConfigurableFilter());

	btnFacilitiesEdt.setEnabled(false);
	btnFacilitiesDel.setEnabled(false);

	fieldFacilitiesFilter.setValueChangeMode(ValueChangeMode.EAGER);
	facilitiesGrid.setColumnReorderingAllowed(true);

	facilitiesGrid.removeAllColumns();
	facilitiesGrid.setMultiSort(true);

	facilitiesGrid
		.addColumn("name")
		.setHeader(getTranslation(FacilityEntityMsg.NAME))
		.setSortProperty("name")
		.setFlexGrow(1);
	facilitiesGrid
		.addColumn("id")
		.setHeader(getTranslation(FacilityEntityMsg.FACILITYID))
		.setSortProperty("id")
		.setWidth(Props.EM_05)
		.setFlexGrow(0);

	facilitiesGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});
	facilitiesGrid.addSelectionListener(this::facilitiesGridSelectionListener);

	facilitiesGrid.addItemDoubleClickListener(listener -> {
	    listener.getSource()
		    .getSelectionModel()
		    .select(listener.getItem());
	    faclilityEdit();
	});

	fieldFacilitiesFilter.addValueChangeListener(e -> facilitiesGrid.getDataProvider().refreshAll());
	btnFacilitiesGridReset.addClickListener(e -> facilitiesGrid.getDataProvider().refreshAll());

    }

    /**
     * Gets the filtered facility adrr query count.
     *
     * @param query the query
     * @return the filtered facility adrr query count
     */
    private int getFilteredFacilityAdrrQueryCount(Query<FacilityAddress, FacilityAddressFilter> query) {
	return facilityAdrrService.getQueriedFacilityAddrByFilterCount(
		query, getFacilityAddrFilter());
    }

    /**
     * Gets the filtered facility adrr query.
     *
     * @param query the query
     * @return the filtered facility adrr query
     */
    private Stream<FacilityAddress> getFilteredFacilityAdrrQuery(Query<FacilityAddress, FacilityAddressFilter> query) {
	return facilityAdrrService.getQueriedFacilityAddrByFilter(
		query, getFacilityAddrFilter());
    }

    /**
     * Gets the facility addr filter.
     *
     * @return the facility addr filter
     */
    private FacilityAddressFilter getFacilityAddrFilter() {
	return FacilityAddressFilter.builder()
		.facilityId(facilitiesGrid.getSelectionModel().getFirstSelectedItem()
			.map(Facility::getId).orElse(-1L))
		.address(getSanitizedFacilityAddrFilter())
		.build();
    }

    /**
     * Gets the sanitized facility addr filter.
     *
     * @return the sanitized facility addr filter
     */
    private String getSanitizedFacilityAddrFilter() {
	return StringUtils.truncate(fieldFacilitiesAddrFilter.getValue(), 100);
    }

    /**
     * Prepare facility addr grid.
     */
    private void prepareFacilityAddrGrid() {
	facilityAddressDataProvider = DataProvider
		.fromFilteringCallbacks(
			this::getFilteredFacilityAdrrQuery,
			this::getFilteredFacilityAdrrQueryCount);

	facilityAddrGrid.setDataProvider(facilityAddressDataProvider.withConfigurableFilter());

	btnFacilityAddrrAdd.setEnabled(false);
	btnFacilityAddrrEdt.setEnabled(false);
	btnFacilityAddrrDel.setEnabled(false);

	fieldFacilitiesAddrFilter.setValueChangeMode(ValueChangeMode.EAGER);
	fieldFacilitiesAddrFilter
		.addValueChangeListener(e -> {
		    facilityAddrGrid.getSelectionModel().deselectAll();
		    facilityAddrGrid.getDataProvider().refreshAll();
		});

	facilityAddrGrid.removeAllColumns();
	facilityAddrGrid
		.addColumn("address")
		.setSortProperty("address")
		.setFlexGrow(8)
		.setHeader(getTranslation(FacilityAddrEntityMsg.ADDRESS));

	facilityAddrGrid
		.addColumn(buildDefaultFacilityAddressStatesColumn())
		.setSortProperty("defaultAddress")
		.setFlexGrow(1)
		.setHeader(getTranslation(FacilityAddrEntityMsg.DEFAULT))
		.setTextAlign(ColumnTextAlign.CENTER);

	facilityAddrGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});
	facilityAddrGrid.setColumnReorderingAllowed(true);

	facilityAddrGrid.addSelectionListener(event -> {
	    if (event.getAllSelectedItems().isEmpty()) {
		btnFacilityAddrrEdt.setEnabled(false);
		btnFacilityAddrrDel.setEnabled(false);
	    } else {
		btnFacilityAddrrEdt.setEnabled(true);
		btnFacilityAddrrDel.setEnabled(true);
	    }
	});

	btnFacilityAddrGridReset.addClickListener(e -> facilityAddressDataProvider.refreshAll());
    }

    /**
     * Builds the default facility address states column.
     *
     * @return the component renderer
     */
    private ComponentRenderer<Div, FacilityAddress> buildDefaultFacilityAddressStatesColumn() {
	return new ComponentRenderer<>(facilityAddress -> {
	    Icon icon = new Icon();
	    Div div = new Div();
	    if (facilityAddress.isDefaultAddress()) {
		icon = VaadinIcon.CHECK.create();
		icon.setColor("var(--lumo-success-color)");
		div.add(icon);
	    }
	    icon.setSize(Props.EM_01);
	    return div;
	});
    }

    /**
     * Gets the filtered users qurey count.
     *
     * @param query the query
     * @return the filtered users qurey count
     */
    private int getFilteredUsersQureyCount(Query<User, UserFilter> query) {
	return usersService.getQueriedUsersByFilterCount(
		query, getUsersFilter());
    }

    /**
     * Gets the filtered user query.
     *
     * @param query the query
     * @return the filtered user query
     */
    private Stream<User> getFilteredUserQuery(Query<User, UserFilter> query) {
	return usersService.getQueriedUsersbyFilter(
		query, getUsersFilter());
    }

    /**
     * Gets the users filter.
     *
     * @return the users filter
     */
    private UserFilter getUsersFilter() {
	return UserFilter.builder()
		.facilityId(facilitiesGrid.getSelectionModel().getFirstSelectedItem()
			.map(Facility::getId).orElse(-1L))
		.username(getSanitizedUsersFilter())
		.build();
    }

    /**
     * Gets the sanitized users filter.
     *
     * @return the sanitized users filter
     */
    private String getSanitizedUsersFilter() {
	return StringUtils.truncate(fieldUsersFilter.getValue(), 100);
    }

    /**
     * Prepare users grid.
     */
    private void prepareUsersGrid() {
	userDataProvider = DataProvider
		.fromFilteringCallbacks(
			this::getFilteredUserQuery,
			this::getFilteredUsersQureyCount);

	usersGrid.setDataProvider(userDataProvider.withConfigurableFilter());

	btnUsersAdd.setEnabled(false);
	btnUsersDel.setEnabled(false);

	fieldUsersFilter.setValueChangeMode(ValueChangeMode.EAGER);
	fieldUsersFilter
		.addValueChangeListener(e -> {
		    usersGrid.getSelectionModel().deselectAll();
		    usersGrid.getDataProvider().refreshAll();
		});
	usersGrid.removeAllColumns();

	usersGrid
		.addColumn("username")
		.setSortProperty("username")
		.setFrozen(true)
		.setHeader(getTranslation(UserEntityMsg.USERNAME));
	usersGrid
		.addColumn("firstName")
		.setSortProperty("firstName")
		.setHeader(getTranslation(UserEntityMsg.FIRSNAME));
	usersGrid
		.addColumn("lastName")
		.setSortProperty("lastName")
		.setHeader(getTranslation(UserEntityMsg.LASTNAME));
	usersGrid
		.addColumn("patronymic")
		.setSortProperty("patronymic")
		.setHeader(getTranslation(UserEntityMsg.PATRONYMIC));
	usersGrid
		.addColumn("email")
		.setSortProperty("patronymic")
		.setHeader(getTranslation(UserEntityMsg.EMAIL));
	usersGrid
		.addColumn(buildUserStatesColumn())
		.setSortProperty("enabled")
		.setFlexGrow(1)
		.setHeader(getTranslation(UserEntityMsg.ENABLED))
		.setTextAlign(ColumnTextAlign.CENTER);
	usersGrid
		.addColumn(buildUsersRoleColumn())
		.setFlexGrow(1)
		.setSortProperty("role")
		.setHeader(getTranslation(UserEntityMsg.ROLES))
		.setTextAlign(ColumnTextAlign.CENTER);

	GridMultiSelectionModel<User> multiSelection = (GridMultiSelectionModel<User>) usersGrid
		.setSelectionMode(Grid.SelectionMode.MULTI);

	usersGrid.setMultiSort(true);
	usersGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});
	usersGrid.setColumnReorderingAllowed(true);
	multiSelection.setSelectAllCheckboxVisibility(SelectAllCheckboxVisibility.VISIBLE);
	multiSelection.setSelectionColumnFrozen(true);
	multiSelection
		.addMultiSelectionListener(event -> btnUsersDel.setEnabled(!event.getFirstSelectedItem().isEmpty()));

	btnUsersGridReset.addClickListener(e -> usersGrid.getDataProvider().refreshAll());
    }

    /**
     * Builds the user states column.
     *
     * @return the component renderer
     */
    private ComponentRenderer<Div, User> buildUserStatesColumn() {
	return new ComponentRenderer<>(user -> {
	    Icon icon = new Icon();
	    if (user.getEnabled()) {
		icon = VaadinIcon.CHECK.create();
		icon.setColor("green");
	    } else {
		icon = VaadinIcon.CLOSE.create();
		icon.setColor("red");
	    }
	    icon.setSize(Props.EM_01);
	    return new Div(icon);
	});
    }

    /**
     * Builds the users role column.
     *
     * @return the component renderer
     */
    private ComponentRenderer<Div, User> buildUsersRoleColumn() {
	return new ComponentRenderer<>(user -> {
	    Div div = new Div();
	    if (user == null) {
		return div;
	    }
	    Set<Role> roles = user.getRoles();

	    StringBuilder sb = new StringBuilder("[ ");

	    if (roles != null && !roles.isEmpty()) {
		Icon icon;
		for (Role r : roles) {
		    if (r != null) {
			sb.append(r.getRoleName()).append(" ");
			icon = r.getIcon().createIcon();
			icon.setSize(Props.EM_0_85);
			icon.getStyle().set(Props.MARGIN, Props.EM_0_125);
			div.add(icon);
		    }
		}
		div.setTitle(sb.append("]").toString());
	    }
	    return div;
	});
    }

    /**
     * Facility editor.
     *
     * @return the facility editor
     */
    @Lookup
    FacilityEditor<Facility> facilityEditor() {
	log.info("Create FacilityEditor");
	return null;
    }

    /**
     * Facility addr editor.
     *
     * @return the facility addr editor
     */
    @Lookup
    FacilityAddrEditor<FacilityAddress> facilityAddrEditor() {
	log.info("Create FacilityAddrEditor");
	return null;
    }

    /**
     * User editor.
     *
     * @return the facility users editor
     */
    @Lookup
    FacilityUsersEditor<Set<User>> userEditor() {
	log.info("Create FacilityUsersEditor");
	return null;
    }


    @Lookup
    DialogForm getDialogForm() {
	return null;
    }

    /**
     * Facility add.
     */
    private void facilityAdd() {
	Facility facilityIn = new Facility();

	FacilityEditor<Facility> editor = facilityEditor();
	editor.setOperationData(facilityIn);

	DialogForm dialogForm = getDialogForm();

	dialogForm
		.withDataEditor(editor)
		.withHeader(getTranslation(FacilityEntityMsg.ADD))
		.withWidth(Props.EM_21)
		.withModality(Modality.MR_SAVE, event -> {
		    if (!editor.isValidOperationData()) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_CHK_FAIL));
		    } else {
			try {
			    Optional<Facility> facilityOut = facilitiesService.save(editor.getOperationData());
			    if (facilityOut.isPresent()) {
				facilitiesGrid.deselectAll();
				facilitiesGrid.getDataProvider().refreshAll();
				facilitiesGrid.select(facilityOut.get());
				dialogForm.closeWithResult(Modality.MR_SAVE);
			    }
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
			AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_SAVED));
		    }
		})
		.open();
    }

    /**
     * Faclility edit.
     */
    private void faclilityEdit() {
	Optional<Facility> facilityInO = facilitiesGrid.getSelectionModel().getFirstSelectedItem();
	if (!facilityInO.isPresent()) {
	    return;
	}
	facilityInO = facilitiesService.fetchById(facilityInO.get().getId());
	if (!facilityInO.isPresent()) {
	    AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_NOT_FOUND));
	    return;
	}
	Facility facilityIn = facilityInO.get();

	FacilityEditor<Facility> editor = facilityEditor();
	editor.setOperationData(facilityIn);

	DialogForm dialogForm = getDialogForm();

	dialogForm
		.withDataEditor(editor)
		.withHeader(getTranslation(FacilityEntityMsg.EDIT))
		.withWidth(Props.EM_21)
		.withModality(Modality.MR_SAVE, event -> {
		    if (!editor.isValidOperationData()) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_CHK_FAIL));
		    } else {
			try {
			    Optional<Facility> facilityOut = facilitiesService.save(editor.getOperationData());

			    if (facilityOut.isPresent()) {
				facilitiesGrid.getDataProvider().refreshItem(facilityOut.get());
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
			AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_SAVED));
		    }
		})
		.open();
    }

    /**
     * Facility del.
     */
    private void facilityDel() {
	Optional<Facility> facilityInO = facilitiesGrid.getSelectionModel().getFirstSelectedItem();
	if (!facilityInO.isPresent()) {
	    return;
	}
	facilityInO = facilitiesService.fetchById(facilityInO.get().getId());
	if (!facilityInO.isPresent()) {
	    AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_NOT_FOUND));
	    return;
	}
	Facility facilityIn = facilityInO.get();

	FacilityEditor<Facility> editor = facilityEditor();
	editor.setReadOnlyMode(true);
	editor.setOperationData(facilityIn);

	DialogForm dialogForm = getDialogForm();

	dialogForm
		.withDataEditor(editor)
		.withWidth(Props.EM_21)
		.withHeader(getTranslation(FacilityEntityMsg.DEL))
		.withModality(Modality.MR_DELETE, event -> {
		    try {
			facilitiesService.delete(facilityIn);
			facilitiesGrid.getDataProvider().refreshAll();
			facilityAddrGrid.getDataProvider().refreshAll();
			usersGrid.getDataProvider().refreshAll();
			dialogForm.closeWithResult(Modality.MR_DELETE);
		    } catch (Exception e) {
			log.error(e.getMessage() + " " + e);
			AppNotificator.notify(5000, e.getMessage());
		    }
		})
		.withModality(Modality.MR_CANCEL)
		.withCloseOnOutsideClick(false)
		.withOnCloseHandler(event -> {
		    if (event != null && event.getCloseMode() == Modality.MR_DELETE) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_DELETED));
		    }
		})
		.open();
    }

    /**
     * Facility addr add.
     */
    private void facilityAddrAdd() {

	Optional<Facility> facilityInO = facilitiesGrid.getSelectionModel().getFirstSelectedItem();
	if (!facilityInO.isPresent()) {
	    return;
	}
	facilityInO = facilitiesService.fetchById(facilityInO.get().getId());
	if (!facilityInO.isPresent()) {
	    AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_NOT_FOUND));
	    return;
	}
	Facility facilityIn = facilityInO.get();

	FacilityAddress facilityAddressIn = new FacilityAddress();

	FacilityAddrEditor<FacilityAddress> editor = facilityAddrEditor();
	editor.setOperationData(facilityAddressIn);

	DialogForm dialogForm = getDialogForm();

	dialogForm
		.withDataEditor(editor)
		.withHeader(getTranslation(FacilityAddrEntityMsg.ADD) + " \""
			+ facilityIn.getName() + "\"")
		.withWidth(Props.EM_34)
		.withModality(Modality.MR_SAVE, event -> {
		    if (!editor.isValidOperationData()) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITYADDR_CHK_FAIL));
		    } else {

			try {
			    Optional<Facility> facilityInR = facilitiesService
				    .fetchById(facilityIn.getId());
			    if (!facilityInR.isPresent()) {
				AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_NOT_FOUND));
				return;
			    }

			    Optional<Facility> facilityOut = facilitiesService.addAddrToFacility(facilityInR.get(),
				    editor.getOperationData());
			    if (facilityOut.isPresent()) {
				facilitiesGrid.getDataProvider().refreshItem(facilityOut.get());
				facilityAddrGrid.getDataProvider().refreshAll();
				dialogForm.closeWithResult(Modality.MR_SAVE);
			    }
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
			AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITYADDR_SAVED));
		    }
		})
		.open();
    }

    /**
     * Faclity addr edit.
     */
    private void faclityAddrEdit() {

	Optional<Facility> facilityInO = facilitiesGrid.getSelectionModel().getFirstSelectedItem();
	if (!facilityInO.isPresent()) {
	    return;
	}
	Optional<FacilityAddress> facilityAddrInO = facilityAddrGrid.getSelectionModel().getFirstSelectedItem();
	if (!facilityAddrInO.isPresent()) {
	    return;
	}

	facilityInO = facilitiesService.fetchById(facilityInO.get().getId());
	if (!facilityInO.isPresent()) {
	    AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_NOT_FOUND));
	    return;
	}
	Facility facilityIn = facilityInO.get();

	facilityAddrInO = facilityAdrrService.fetchById(facilityAddrInO.get().getId());
	if (!facilityAddrInO.isPresent()) {
	    AppNotificator.notify(5000, getTranslation(AppNotifyMsg.FACILITYADDR_NOT_FOUND));
	    return;
	}
	FacilityAddress facilityAddressIn = facilityAddrInO.get();

	FacilityAddrEditor<FacilityAddress> editor = facilityAddrEditor();
	editor.setOperationData(facilityAddressIn);

	DialogForm dialogForm = getDialogForm();

	dialogForm
		.withDataEditor(editor)
		.withHeader(getTranslation(FacilityAddrEntityMsg.EDIT) + " \""
			+ facilityAddressIn.getFacility().getName() + "\"")
		.withWidth(Props.EM_34)
		.withModality(Modality.MR_SAVE, event -> {
		    if (!editor.isValidOperationData()) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITYADDR_CHK_FAIL));
		    } else {

			try {
			    Optional<Facility> facilityInR = facilitiesService
				    .fetchById(facilityIn.getId());
			    if (!facilityInR.isPresent()) {
				AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_NOT_FOUND));
				return;
			    }
			    Optional<FacilityAddress> facilityAdrrInO = facilityAdrrService
				    .fetchById(facilityAddressIn.getId());
			    if (!facilityAdrrInO.isPresent()) {
				AppNotificator.notify(5000, getTranslation(AppNotifyMsg.FACILITYADDR_NOT_FOUND));
				return;
			    }
			    Optional<FacilityAddress> facilityAdrrOut = facilityAdrrService
				    .save(editor.getOperationData());

			    if (facilityAdrrOut.isPresent()) {
				facilityAddrGrid.getDataProvider().refreshItem(facilityAdrrOut.get());
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
			AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITYADDR_SAVED));
		    }
		})
		.open();
    }

    /**
     * Facility addr del.
     */
    private void facilityAddrDel() {
	Optional<Facility> facilityInO = facilitiesGrid.getSelectionModel().getFirstSelectedItem();
	if (!facilityInO.isPresent()) {
	    return;
	}
	Optional<FacilityAddress> facilityAddrInO = facilityAddrGrid.getSelectionModel().getFirstSelectedItem();
	if (!facilityAddrInO.isPresent()) {
	    return;
	}

	facilityInO = facilitiesService.fetchById(facilityInO.get().getId());
	if (!facilityInO.isPresent()) {
	    AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_NOT_FOUND));
	    return;
	}
	Facility facilityIn = facilityInO.get();

	facilityAddrInO = facilityAdrrService.fetchById(facilityAddrInO.get().getId());
	if (!facilityAddrInO.isPresent()) {
	    AppNotificator.notify(5000, getTranslation(AppNotifyMsg.FACILITYADDR_NOT_FOUND));
	    return;
	}
	FacilityAddress facilityAddressIn = facilityAddrInO.get();

	if (facilityAddressIn.getFacility().getFacilityAddresses().size() < 2) {
	    AppNotificator.notify(5000, getTranslation(AppNotifyMsg.FACILITYADDR_LASTDEL_FAIL));
	    return;
	}

	FacilityAddrEditor<FacilityAddress> editor = facilityAddrEditor();
	editor.setReadOnlyMode(true);
	editor.setOperationData(facilityAddressIn);

	DialogForm dialogForm = getDialogForm();

	dialogForm
		.withDataEditor(editor)
		.withHeader(getTranslation(FacilityAddrEntityMsg.DEL) + " \""
			+ facilityAddressIn.getFacility().getName() + "\"")
		.withWidth(Props.EM_34)
		.withModality(Modality.MR_DELETE, event -> {
		    if (!editor.isValidOperationData()) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITYADDR_DEL_FAIL));
		    } else {
			try {
			    Optional<Facility> facilityOut = facilitiesService.delAddrFromFacility(facilityIn,
				    facilityAddressIn);

			    if (facilityOut.isPresent()) {
				facilitiesGrid.getDataProvider().refreshItem(facilityOut.get());
				facilityAddrGrid.getSelectionModel().deselectAll();
				facilityAddrGrid.getDataProvider().refreshAll();
			    }
			    dialogForm.closeWithResult(Modality.MR_DELETE);
			} catch (Exception e) {
			    log.error(e.getMessage() + " " + e);
			    AppNotificator.notify(5000, e.getMessage());
			}
		    }
		})
		.withModality(Modality.MR_CANCEL)
		.withCloseOnOutsideClick(false)
		.withOnCloseHandler(event -> {
		    if (event != null && event.getCloseMode() == Modality.MR_DELETE) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITYADDR_DELETED));
		    }
		})
		.open();

    }

    /**
     * Facility users links add.
     */
    private void facilityUsersLinksAdd() {
	Optional<Facility> facilityInO = facilitiesGrid.getSelectionModel().getFirstSelectedItem();
	if (!facilityInO.isPresent()) {
	    return;
	}

	facilityInO = facilitiesService.fetchById(facilityInO.get().getId());
	if (!facilityInO.isPresent()) {
	    AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_NOT_FOUND));
	    return;
	}
	Facility facilityIn = facilityInO.get();

	FacilityUsersEditor<Set<User>> editor = userEditor();
	editor.setOperationData(new HashSet<User>());

	DialogForm dialogForm = getDialogForm();

	dialogForm
		.withDataEditor(editor)
		.withHeader(getTranslation(FacilityUsersMsg.FACILITY_USR_ADD_TITLE) + " \""
			+ facilityIn.getName() + "\"")
		.withWidth(Props.EM_56)
		.withModality(Modality.MR_SAVE, event -> {
		    if (!editor.isValidOperationData()) {
			AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_USR_ADD_CHK_FAIL));
		    } else {
			try {

			    Set<User> users = editor.getOperationData();
			    Optional<Facility> facilityOut = facilitiesService.addLinksToUsers(facilityIn, users);
			    if (facilityOut.isPresent()) {
				facilitiesGrid.getDataProvider().refreshItem(facilityOut.get());
				usersGrid.getDataProvider().refreshAll();
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
			AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_USR_ADDED));
		    }
		})
		.open();
    }

    /**
     * Facility users links drop.
     */
    private void facilityUsersLinksDrop() {
	Optional<Facility> facilityInO = facilitiesGrid.getSelectionModel().getFirstSelectedItem();
	if (!facilityInO.isPresent()) {
	    return;
	}
	Collection<User> usersInO = usersGrid.getSelectionModel().getSelectedItems();
	if (usersInO.isEmpty()) {
	    return;
	}

	facilityInO = facilitiesService.fetchById(facilityInO.get().getId());
	if (!facilityInO.isPresent()) {
	    AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_NOT_FOUND));
	    return;
	}
	Facility facilityIn = facilityInO.get();

	Collection<User> users = usersGrid.getSelectedItems();
	if (users.isEmpty()) {
	    return;
	}
	ConfirmDialog
		.createQuestion()
		.withCaption(getTranslation(FacilityUsersMsg.FACILITY_USR_DROP_TITLE))
		.withMessage(getTranslation(FacilityUsersMsg.FACILITY_USR_DROP_QUESTION))
		.withOkButton(() -> {
		    try {
			Optional<Facility> facilityOut = facilitiesService.dropLinksToUsers(facilityIn, users);
			if (facilityOut.isPresent()) {
			    facilitiesGrid.getDataCommunicator().refresh(facilityOut.get());
			    usersGrid.getDataProvider().refreshAll();
			    AppNotificator.notify(getTranslation(AppNotifyMsg.FACILITY_USR_DELETED));
			}
		    } catch (Exception e) {
			AppNotificator.notify(5000, e.getMessage());
			log.debug(
				"Users connections to objects are not dropped due to an error.. Exception is thrown. "
					+ e.getMessage());
		    }
		},
			ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_YES)),
			ButtonOption.icon(UIIcon.BTN_OK.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();

    }

    /**
     * Facilities grid selection listener.
     *
     * @param event the event
     */
    private void facilitiesGridSelectionListener(SelectionEvent<Grid<Facility>, Facility> event) {

	Optional<Facility> facility = event.getFirstSelectedItem();

	facilityAddrGrid.getDataProvider().refreshAll();
	usersGrid.getSelectionModel().deselectAll();
	usersGrid.getDataProvider().refreshAll();
	if (facility.isPresent()) {
	    btnFacilitiesEdt.setEnabled(true);
	    btnFacilitiesDel.setEnabled(true);

	    btnFacilityAddrrAdd.setEnabled(true);
	    btnUsersAdd.setEnabled(true);
	} else {
	    btnFacilitiesEdt.setEnabled(false);
	    btnFacilitiesDel.setEnabled(false);

	    btnFacilityAddrrAdd.setEnabled(false);
	    btnUsersAdd.setEnabled(false);
	}
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
	return "CourierRequestsManager [btnFacilitiesAdd=" + btnFacilitiesAdd + ", btnFacilitiesEdt=" + btnFacilitiesEdt
		+ ", btnFacilitiesDel=" + btnFacilitiesDel + ", btnFacilitiesGridReset=" + btnFacilitiesGridReset
		+ ", btnFacilityAddrrAdd=" + btnFacilityAddrrAdd + ", btnFacilityAddrrEdt=" + btnFacilityAddrrEdt
		+ ", btnFacilityAddrrDel=" + btnFacilityAddrrDel + ", btnFacilityAddrGridReset="
		+ btnFacilityAddrGridReset + ", btnUsersAdd=" + btnUsersAdd + ", btnUsersDel=" + btnUsersDel
		+ ", btnUsersGridReset=" + btnUsersGridReset + ", facilitiesGrid=" + facilitiesGrid
		+ ", facilityAddrGrid=" + facilityAddrGrid + ", usersGrid=" + usersGrid + ", fieldFacilitiesFilter="
		+ fieldFacilitiesFilter + ", facilitiesService="
		+ facilitiesService + ", facilityAdrrService=" + facilityAdrrService
		+ ", usersService=" + usersService + ", toString()=" + super.toString() + "]";
    }

    /**
     * Gets the page title.
     *
     * @return the page title
     */
    @Override
    public String getPageTitle() {
	return getTranslation(AppTitleMsg.APP_TITLE_ALLFACILITIES, UI.getCurrent().getLocale());
    }
}
