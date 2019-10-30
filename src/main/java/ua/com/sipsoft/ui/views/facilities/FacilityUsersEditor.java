package ua.com.sipsoft.ui.views.facilities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.users.UserFilter;
import ua.com.sipsoft.services.users.UsersService;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.messages.ButtonMsg;
import ua.com.sipsoft.utils.messages.GridToolMsg;
import ua.com.sipsoft.utils.messages.UserEntityMsg;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Class FacilityUsersEditor.
 *
 * @author Pavlo Degtyaryev
 * @param <T> the generic type
 */
@Scope("prototype")
@Tag("facility-users-editor-form")

/** The Constant log. */
@Slf4j
@SpringComponent
public class FacilityUsersEditor<T extends Set<User>> extends FormLayout {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3291395147592860970L;

    /**
     * Sets the users service.
     *
     * @param usersService the new users service
     */
    @Setter
    private transient UsersService usersService;

    /** The user data provider. */
    private DataProvider<User, UserFilter> userDataProvider;

    /**
     * Gets the grid.
     *
     * @return the grid
     */
    @Getter
    private Grid<User> grid;

    /** The filter. */
    private TextField filter;

    /** The filter roles. */
    private Collection<Role> filterRoles = Arrays.asList(Role.values());

    /** The toolbar. */
    private HorizontalLayout toolbar;

    /** The refresh btn. */
    private Button refreshBtn;

    private T operationData;

    /** The read only mode. */
    private boolean readOnlyMode;

    /**
     * Instantiates a new facility users editor.
     *
     * @param usersService the users service
     */
    public FacilityUsersEditor(UsersService usersService) {
	super();
	log.info("Instantiates a new facility users editor");
	this.usersService = usersService;
	VerticalLayout panelFields = new VerticalLayout();

	filter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	filter.setValueChangeMode(ValueChangeMode.EAGER);
	filter.addValueChangeListener(e -> showUsers(e.getValue()));

	refreshBtn = new Button(UIIcon.BTN_REFRESH.createIcon());
	refreshBtn.addClickListener(e -> showFilteredUsers());
	Div rb = new Div(refreshBtn);
	rb.setTitle(getTranslation(ButtonMsg.BTN_REFRESH));
	rb.setSizeUndefined();

	grid = new Grid<>(User.class);
	grid.setColumnReorderingAllowed(true);
	grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);

	grid.setColumns(/* "id", */ "username", "firstName", "lastName", "patronymic", "email");

	grid.addColumn(buildUsersRoleColumn()).setSortable(true).setKey("roles");
	grid.addColumn(buildUserStatesColumn()).setSortable(true).setKey("enabled");

	grid.getColumns().forEach(column -> column.setResizable(true));

	grid.getColumnByKey("username").setFlexGrow(8).setFrozen(true)
		.setHeader(getTranslation(UserEntityMsg.USERNAME));
	grid.getColumnByKey("firstName").setFlexGrow(9).setHeader(getTranslation(UserEntityMsg.FIRSNAME));
	grid.getColumnByKey("lastName").setFlexGrow(9).setHeader(getTranslation(UserEntityMsg.LASTNAME));
	grid.getColumnByKey("patronymic").setFlexGrow(9)
		.setHeader(getTranslation(UserEntityMsg.PATRONYMIC));
	grid.getColumnByKey("email").setFlexGrow(9).setHeader(getTranslation(UserEntityMsg.EMAIL));
	grid.getColumnByKey("roles").setFlexGrow(4).setHeader(getTranslation(UserEntityMsg.ROLES))
		.setTextAlign(ColumnTextAlign.CENTER);
	grid.getColumnByKey("enabled").setFlexGrow(2).setHeader(getTranslation(UserEntityMsg.ENABLED))
		.setTextAlign(ColumnTextAlign.CENTER);

	GridMultiSelectionModel<User> multiSelection = (GridMultiSelectionModel<User>) grid
		.setSelectionMode(Grid.SelectionMode.MULTI);
	multiSelection.setSelectionColumnFrozen(true);
//		multiSelection.addMultiSelectionListener(this::usersGridSelectionListener);

	toolbar = new HorizontalLayout(filter, rb);
	toolbar.setMargin(false);
	toolbar.setPadding(false);
	toolbar.setSpacing(true);
	toolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	toolbar.setFlexGrow(1, filter);
	toolbar.setFlexGrow(0, rb);

	grid.setWidthFull();
	grid.setMultiSort(true);
	grid.focus();

	panelFields.add(toolbar, grid);
	panelFields.setAlignItems(Alignment.STRETCH);
	panelFields.setFlexGrow(1, grid);
	panelFields.setMargin(true);
	panelFields.setPadding(false);
	panelFields.setSpacing(true);
	panelFields.setSizeFull();
	panelFields.getStyle().set(Props.MARGIN, Props.EM_0_5);
	panelFields.getStyle().set(Props.PADDING, null);
	add(panelFields);
	setSizeFull();
	setMaxWidth(getWidth());
	setMinWidth(getWidth());

	userDataProvider = DataProvider.fromFilteringCallbacks(
		query -> this.usersService.getQueriedUsersbyFilter(query,
			UserFilter.builder()
				.roles(filterRoles)
				.username(filter.getValue()).build()),
		query -> this.usersService.getQueriedUsersByFilterCount(query,
			UserFilter.builder()
				.roles(filterRoles)
				.username(filter.getValue()).build()));

	grid.setDataProvider(userDataProvider.withConfigurableFilter());

	filter.addValueChangeListener(e -> grid.getDataProvider().refreshAll());
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
			sb.append(getTranslation(r.getRoleName())).append(" ");
			icon = r.getIcon().createIcon();
			icon.setSize(Props.EM_0_85);
			icon.getStyle().set(Props.MARGIN, Props.EM_0_1);
			div.add(icon);
		    }
		}
		div.setTitle(sb.append("]").toString());
	    }
	    return div;
	});
    }

    /**
     * Show users.
     *
     * @param name the name
     */
    private void showUsers(String name) {
	if (name != null) {
	    if (name.isEmpty()) {
		grid.setItems(usersService.getByRoles(filterRoles));
	    } else {
		grid.setItems(usersService.getByRolesAndFindByName(filterRoles, name));
	    }
	}
    }

    /**
     * Show filtered users.
     */
    private void showFilteredUsers() {
	grid.getDataProvider().refreshAll();
    }

    /**
     * Inits the.
     */
    @PostConstruct
    private void init() {
	showFilteredUsers();
    }

    /**
     * Checks if is read only mode.
     *
     * @return the readOnlyMode
     */
    public boolean isReadOnlyMode() {
	return readOnlyMode;
    }

    /**
     * Sets the read only mode.
     *
     * @param readOnlyMode the readOnlyMode to set
     */
    public void setReadOnlyMode(boolean readOnlyMode) {
	this.readOnlyMode = readOnlyMode;
    }

    @SuppressWarnings("unchecked")
    public boolean isValidOperationData() {
	operationData = (T) grid.getSelectedItems();
	if (operationData != null) {
	    return true;
	}
	return false;
    }

    public void setOperationData(T operationData) {
	this.operationData = operationData;
    }

    public T getOperationData() {
	return operationData;
    }

}
