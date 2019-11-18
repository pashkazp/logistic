package ua.com.sipsoft.ui.views.request.draft;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridMultiSelectionModel.SelectAllCheckboxVisibility;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.common.FacilitiesService;
import ua.com.sipsoft.services.requests.draft.CourierRequestFilter;
import ua.com.sipsoft.services.requests.draft.CourierRequestService;
import ua.com.sipsoft.services.requests.draft.DraftRouteSheetService;
import ua.com.sipsoft.ui.commons.AppNotificator;
import ua.com.sipsoft.ui.commons.forms.Modality;
import ua.com.sipsoft.ui.commons.forms.dialogform.DialogForm;
import ua.com.sipsoft.ui.views.common.AccessDenied;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.history.CourierRequestSnapshot;
import ua.com.sipsoft.utils.messages.AppNotifyMsg;
import ua.com.sipsoft.utils.messages.AppTitleMsg;
import ua.com.sipsoft.utils.messages.ButtonMsg;
import ua.com.sipsoft.utils.messages.CourierRequestsMsg;
import ua.com.sipsoft.utils.messages.DraftRouteSheetMsg;
import ua.com.sipsoft.utils.messages.GridToolMsg;
import ua.com.sipsoft.utils.security.AllowedFor;
import ua.com.sipsoft.utils.security.Role;
import ua.com.sipsoft.utils.security.RoleName;
import ua.com.sipsoft.utils.security.SecurityUtils;

/** The Constant log. */
@Slf4j
@UIScope
@SpringComponent
//@Route(value = AppURL.REQUESTS_ALL, layout = MainView.class)
@AllowedFor(value = { RoleName.ROLE_ADMIN, RoleName.ROLE_CLIENT, RoleName.ROLE_COURIER, RoleName.ROLE_DISPATCHER,
	RoleName.ROLE_MANAGER, RoleName.ROLE_PRODUCTOPER })
public class CourierRequestsView extends VerticalLayout implements HasDynamicTitle, BeforeEnterObserver {

    /**
     * Check access. Enter if access granted or reroute otherwise
     *
     * @param event the event
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
	log.info("Perform check for grant access to enter.");
	if (event.getNavigationTarget() != AccessDenied.class
		&& !SecurityUtils.isClassAccessGranted()) {
	    event.rerouteTo(AccessDenied.class);
	}
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 809709679426905366L;

    /** The field requests filter. */
    private TextField fieldRequestsFilter;

    /** The btn requests add. */
    private Button btnRequestsAdd;

    /** The btn requests edt. */
    private Button btnRequestsEdt;

    /** The btn requests del. */
    private Button btnRequestsDel;

    /** The btn requests grid reset. */
    private Button btnRequestsGridReset;

    /** The btn draft sheet make. */
    private Button btnDraftSheetMake;

    /** The requests grid. */
    private Grid<CourierRequest> requestsGrid;

    /** The courier request service. */
    private transient CourierRequestService courierRequestService;

    /** The draft route sheet service. */
    private transient DraftRouteSheetService draftRouteSheetService;

    /** The facilities service. */
    private final transient FacilitiesService facilitiesService;

    /** The courier request data provider. */
    private DataProvider<CourierRequest, CourierRequestFilter> courierRequestDataProvider;

    /** The courier reques configurable filtert DP. */
    private ConfigurableFilterDataProvider<CourierRequest, Void, CourierRequestFilter> courierRequesConfigurableFiltertDP;

    /**
     * Instantiates a new courier requests view.
     *
     * @param courierRequestService  the courier request service
     * @param draftRouteSheetService the draft route sheet service
     */
    @Autowired
    public CourierRequestsView(CourierRequestService courierRequestService,
	    DraftRouteSheetService draftRouteSheetService, FacilitiesService facilitiesService) {
	super();

	log.info("Instantiates a new courier requests view.");
	this.courierRequestService = courierRequestService;
	this.draftRouteSheetService = draftRouteSheetService;
	this.facilitiesService = facilitiesService;

	btnRequestsAdd = new Button(UIIcon.BTN_ADD.createIcon());
	btnRequestsDel = new Button(UIIcon.BTN_NO.createIcon());
	btnRequestsEdt = new Button(UIIcon.BTN_EDIT.createIcon());
	btnRequestsGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());
	btnDraftSheetMake = new Button(UIIcon.SHEET_DRAFT.createIcon());

	fieldRequestsFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldRequestsFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnRequestsAdd.setSizeUndefined();
	btnRequestsEdt.setSizeUndefined();
	btnRequestsDel.setSizeUndefined();
	btnRequestsGridReset.setSizeUndefined();
	btnDraftSheetMake.setSizeUndefined();

	btnRequestsAdd.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnRequestsEdt.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnRequestsDel.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnRequestsGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	btnDraftSheetMake.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);

	HorizontalLayout panelRequestsToolbar = new HorizontalLayout(fieldRequestsFilter, btnRequestsEdt,
		btnRequestsAdd, btnRequestsDel, btnRequestsGridReset, btnDraftSheetMake);

	panelRequestsToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelRequestsToolbar.setFlexGrow(0, btnRequestsAdd);
	panelRequestsToolbar.setFlexGrow(0, btnRequestsEdt);
	panelRequestsToolbar.setFlexGrow(0, btnRequestsDel);
	panelRequestsToolbar.setFlexGrow(0, btnRequestsGridReset);
	panelRequestsToolbar.setFlexGrow(0, btnDraftSheetMake);
	panelRequestsToolbar.setFlexGrow(1, fieldRequestsFilter);
	panelRequestsToolbar.setMargin(false);
	panelRequestsToolbar.setPadding(false);
	panelRequestsToolbar.setSpacing(true);

	requestsGrid = new Grid<>(CourierRequest.class);
	requestsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);
	requestsGrid.setSizeFull();

	VerticalLayout panelRequestsGrigPresenter = new VerticalLayout(panelRequestsToolbar, requestsGrid);
	panelRequestsGrigPresenter.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	panelRequestsGrigPresenter.setWidthFull();
	panelRequestsGrigPresenter.setHeightFull();

	panelRequestsGrigPresenter.setMargin(false);
	panelRequestsGrigPresenter.setPadding(false);
	panelRequestsGrigPresenter.setSpacing(false);
	panelRequestsGrigPresenter.getStyle().set(Props.MARGIN, Props.EM_0_5);
	panelRequestsGrigPresenter.getStyle().set(Props.PADDING, null);

	add(panelRequestsGrigPresenter);

	setAlignItems(Alignment.STRETCH);
	setFlexGrow(1, panelRequestsGrigPresenter);
	setSizeFull();

	// behavior section

	btnRequestsAdd
		.addClickListener(e -> requestAdd());
	btnRequestsAdd
		.addClickShortcut(Key.NUMPAD_ADD, KeyModifier.ALT);
	btnRequestsEdt
		.addClickListener(e -> requestEdit());
	btnRequestsDel
		.addClickListener(e -> requestDel());
	btnDraftSheetMake
		.addClickListener(e -> draftSheetMake());

	fieldRequestsFilter.setValueChangeMode(ValueChangeMode.LAZY);
	fieldRequestsFilter.addValueChangeListener(e -> {
	    requestsGrid.getSelectionModel().deselectAll();
	    requestsGrid.getDataProvider().refreshAll();
	});

	btnRequestsEdt.setEnabled(false);
	btnRequestsDel.setEnabled(false);
	btnDraftSheetMake.setEnabled(false);

	prepareRequestGrid(courierRequestService);
    }

    /**
     * Gets the filtered courier request query.
     *
     * @param courierRequestService the courier request service
     * @param query                 the query
     * @return the filtered courier request query
     */
    private Stream<CourierRequest> getFilteredCourierRequestQuery(CourierRequestService courierRequestService,
	    Query<CourierRequest, CourierRequestFilter> query) {
	log.info("Get filtered courier request query.");
	return courierRequestService
		.getQueriedCourierRequestsByFilter(query, getCourierRequestFilter());
    }

    /**
     * Gets the filtered courier request query count.
     *
     * @param courierRequestService the courier request service
     * @param query                 the query
     * @return the filtered courier request query count
     */
    private int getFilteredCourierRequestQueryCount(CourierRequestService courierRequestService,
	    Query<CourierRequest, CourierRequestFilter> query) {
	log.info("Get filtered courier request query count.");
	return courierRequestService
		.getQueriedCourierRequestsByFilterCount(query, getCourierRequestFilter());
    }

    /**
     * Gets the courier request filter.
     *
     * @return the courier request filter
     */
    private CourierRequestFilter getCourierRequestFilter() {
	log.info("Create CourierRequestFilter");
	User author = null;
	if (CollectionUtils.containsAny(Set.of(Role.ROLE_CLIENT, Role.ROLE_PRODUCTOPER),
		SecurityUtils.getUser().getHighesRole())) {
	    author = SecurityUtils.getUser();
	}
	return CourierRequestFilter.builder()
		.description(getSanitizedFieldRequestFilter())
		.author(author)
		.build();
    }

    /**
     * Gets the sanitized field request filter.
     *
     * @return the sanitized field request filter
     */
    private String getSanitizedFieldRequestFilter() {
	log.info("Create sanitized filter from {}", fieldRequestsFilter.getValue());
	return StringUtils.truncate(fieldRequestsFilter.getValue(), 100);
    }

    /**
     * Prepare request grid.
     *
     * @param courierRequestService the courier request service
     */
    private void prepareRequestGrid(CourierRequestService courierRequestService) {
	log.info("Prepare request grid using courierRequestService");
	courierRequestDataProvider = DataProvider.fromFilteringCallbacks(
		query -> getFilteredCourierRequestQuery(courierRequestService, query),
		query -> getFilteredCourierRequestQueryCount(courierRequestService, query));
	courierRequesConfigurableFiltertDP = courierRequestDataProvider.withConfigurableFilter();

	requestsGrid.setDataProvider(courierRequesConfigurableFiltertDP);

	requestsGrid.removeAllColumns();

	requestsGrid.addColumn("id")
		.setHeader(getTranslation(CourierRequestsMsg.REQUESTID))
		.setSortProperty("id")
		.setWidth(Props.EM_05)
		.setFlexGrow(0);

	requestsGrid
		.addColumn(new ComponentRenderer<>(CourierRequestPresenter::new))
		.setHeader(getTranslation(CourierRequestsMsg.REQUEST))
		.setFlexGrow(1);

	requestsGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});

	GridMultiSelectionModel<CourierRequest> multiSelection = (GridMultiSelectionModel<CourierRequest>) requestsGrid
		.setSelectionMode(Grid.SelectionMode.MULTI);
	multiSelection.setSelectAllCheckboxVisibility(SelectAllCheckboxVisibility.VISIBLE);
	multiSelection.setSelectionColumnFrozen(true);
	multiSelection.addMultiSelectionListener(event -> {
	    boolean enabled = !event.getFirstSelectedItem().isEmpty();
	    btnRequestsEdt.setEnabled(enabled && event.getAllSelectedItems().size() == 1);
	    btnRequestsDel.setEnabled(enabled);
	    if (CollectionUtils.containsAny(SecurityUtils.getUserRoles(),
		    Role.ROLE_ADMIN, Role.ROLE_DISPATCHER,
		    Role.ROLE_MANAGER, Role.ROLE_PRODUCTOPER)) {
		btnDraftSheetMake.setEnabled(enabled);
	    }
	});

	btnRequestsGridReset.addClickListener(e -> requestsGrid.getDataProvider().refreshAll());
    }

    /**
     * Creates and get courier request editor.
     *
     * @return the courier request editor
     */
    @Lookup
    CourierRequestEditor<CourierRequest> getCourierRequestEditor() {
	log.info("Create courier request editor");
	return null;
    }

    @Lookup
    DialogForm getDialogForm() {
	return null;
    }

    /**
     * Request add.
     */
    private void requestAdd() {
	log.info("Try to add Curier Request");
	CourierRequest courierRequest = new CourierRequest();
	courierRequest.setAuthor(SecurityUtils.getUser());

	CourierRequestEditor<CourierRequest> editor = getCourierRequestEditor();
	editor.setOperationData(courierRequest);

	DialogForm dialogForm = getDialogForm();

	dialogForm
		.withDataEditor(editor)
		.withHeader(getTranslation(CourierRequestsMsg.ADD))
		.withWidth(Props.EM_28)
		.withModality(Modality.MR_SAVE, event -> {
		    if (!editor.isValidOperationData()) {
			AppNotificator.notifyError(getTranslation(AppNotifyMsg.COURIER_REQ_CHK_FAIL));
		    } else {
			try {
			    log.info("Perform to add Courier Request");
			    CourierRequest requestIn = editor.getOperationData();
			    courierRequestService.addRequest(requestIn,
				    SecurityUtils.getUser());
			    requestsGrid.getDataProvider().refreshAll();
			    dialogForm.closeWithResult(Modality.MR_SAVE);
			} catch (Exception e) {
			    log.error(e.getMessage() + " " + e);
			    AppNotificator.notifyError(5000, e.getMessage());
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
     * Request edit.
     */
    private void requestEdit() {
	log.info("Try to edit selected Curier Request");
	Optional<CourierRequest> courierRequestInO = requestsGrid.getSelectionModel().getFirstSelectedItem();
	if (!courierRequestInO.isPresent()) {
	    log.info("Nothing to edit. Selection is empty");
	    return;
	}
	courierRequestInO = courierRequestService.fetchById(courierRequestInO.get().getId());
	if (!courierRequestInO.isPresent()) {
	    AppNotificator.notifyError(getTranslation(AppNotifyMsg.REQUEST_NOT_FOUND));
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
			AppNotificator.notifyError(getTranslation(AppNotifyMsg.COURIER_REQ_CHK_FAIL));
		    } else {
			try {
			    log.info("Perform to save Courier Request");
			    Optional<CourierRequest> courierRequestOut = courierRequestService
				    .registerChangesAndSave(editor.getOperationData(), requestSnapshot,
					    SecurityUtils.getUser());
			    if (courierRequestOut.isPresent()) {
				requestsGrid.getDataProvider().refreshItem(courierRequestOut.get());
			    }
			    dialogForm.closeWithResult(Modality.MR_SAVE);
			} catch (Exception e) {
			    AppNotificator.notifyError(5000, e.getMessage());
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
     * Request del.
     */
    private void requestDel() {
	log.info("Try to delete Curier Request");
	List<CourierRequest> requestsIn = requestsGrid.getSelectionModel()
		.getSelectedItems().stream()
		.map(req -> courierRequestService.fetchById(req.getId()))
		.filter(Optional<CourierRequest>::isPresent)
		.map(Optional<CourierRequest>::get)
		.collect(Collectors.toList());
	if (requestsIn.isEmpty()) {
	    log.info("Nothing to delete. Selection is empty");
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
		new Label(getTranslation(CourierRequestsMsg.CANCEL_REQUEST_MESSAGE)),
		description);
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	ConfirmDialog.create()
		.withCaption(getTranslation(CourierRequestsMsg.CANCEL_REQUEST_HEADER))
		.withMessage(panel)
		.withSaveButton(() -> {
		    if (description.isInvalid()) {
			AppNotificator.notifyError(5000, getTranslation(AppNotifyMsg.COURIER_REQ_SMALL_REASON));
			return;
		    }
		    try {
			requestsIn.forEach(req -> requestsGrid.deselect(req));
			courierRequestService.moveCourierRequestsToArchive(requestsIn, description.getValue(),
				SecurityUtils.getUser());
			requestsGrid.getDataProvider().refreshAll();
			AppNotificator.notify(getTranslation(AppNotifyMsg.COURIER_REQ_DELETED));
		    } catch (Exception e) {
			AppNotificator.notifyError(5000, e.getMessage());
		    }

		}, ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_DELETE)),
			ButtonOption.icon(UIIcon.BTN_DEL.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * Draft sheet make.
     */
    @AllowedFor(value = { RoleName.ROLE_ADMIN, RoleName.ROLE_DISPATCHER, RoleName.ROLE_PRODUCTOPER })
    private void draftSheetMake() {
	log.info("Try to create Draft Route Sheet using selected Requests");
	if (!SecurityUtils.isMethodAccessGranted()) {
	    AppNotificator.notifyError(getTranslation(AppNotifyMsg.NOT_ENOUGH_RIGHT));
	    log.info("Insufficent right for operation for User '{}'", SecurityUtils.getUser().getUsername());
	    return;
	}
	List<CourierRequest> requestsIn = requestsGrid.getSelectionModel()
		.getSelectedItems().stream()
		.map(req -> courierRequestService.fetchById(req.getId()))
		.filter(Optional<CourierRequest>::isPresent)
		.map(Optional<CourierRequest>::get)
		.collect(Collectors.toList());
	if (requestsIn.isEmpty()) {
	    log.info("Nothing to add to sheet. Selection is empty.");
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
		    if (description.isInvalid()) {
			AppNotificator.notifyError(getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION));
			return;
		    }
		    try {
			draftRouteSheetService.createAndSaveDraftRouteSheet(requestsIn, description.getValue(),
				SecurityUtils.getUser());
			AppNotificator.notify(getTranslation(AppNotifyMsg.DRAFT_ROUTE_SHEET_CREATED));
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
     * Gets the page title.
     *
     * @return the page title
     */
    @Override
    public String getPageTitle() {
	log.info("Get localized Page Title");
	return getTranslation(AppTitleMsg.APP_TITLE_REQUESTS, UI.getCurrent().getLocale());
    }

}
