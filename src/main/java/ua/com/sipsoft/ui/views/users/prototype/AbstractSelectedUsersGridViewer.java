package ua.com.sipsoft.ui.views.users.prototype;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.users.UserFilter;
import ua.com.sipsoft.services.users.UsersService;
import ua.com.sipsoft.ui.commons.ChangeHandler;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.TooltippedComponent;
import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.messages.ButtonMsg;
import ua.com.sipsoft.utils.messages.GridToolMsg;
import ua.com.sipsoft.utils.messages.UserEntityMsg;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Class AbstractSelectedUsersGridViewer.
 *
 * @author Pavlo Degtyaryev
 */

@Log4j2
public abstract class AbstractSelectedUsersGridViewer extends VerticalLayout implements TooltippedComponent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5662619402901561707L;

    /** The users service. */
    private transient UsersService usersService;

    /**
     * Gets the users grid.
     *
     * @return the users grid
     */
    @Getter
    private Grid<User> usersGrid;

    /** The field users filter. */
    private TextField fieldUsersFilter;

    /** The filter roles. */
    private Collection<Role> filterRoles = Collections.emptyList();

    /** The btn users grid reset. */
    private Button btnUsersGridReset;

    /**
     * Instantiates a new abstract selected users grid viewer.
     *
     * @param usersService the users service
     * @param filterRoles  the filter roles
     */
    public AbstractSelectedUsersGridViewer(UsersService usersService,
	    Collection<Role> filterRoles) {

	log.debug("Create Selected Users Grid Viewer for roles: " + filterRoles.toString());
	this.filterRoles = filterRoles;
	this.usersService = usersService;

	fieldUsersFilter = new TextField("", getTranslation(GridToolMsg.SEARCH_FIELD));
	fieldUsersFilter.setPrefixComponent(UIIcon.SEARCH.createIcon());

	btnUsersGridReset = new Button(UIIcon.BTN_REFRESH.createIcon());
	btnUsersGridReset.setSizeUndefined();
	btnUsersGridReset.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);

	Component divRefreshBtn = wrapWithTooltip(btnUsersGridReset, getTranslation(ButtonMsg.BTN_REFRESH));

	HorizontalLayout panelUsersToolbar = new HorizontalLayout(fieldUsersFilter, btnUsersGridReset);

	panelUsersToolbar.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	panelUsersToolbar.setFlexGrow(0, divRefreshBtn);
	panelUsersToolbar.setFlexGrow(1, fieldUsersFilter);
	panelUsersToolbar.setMargin(false);
	panelUsersToolbar.setPadding(false);
	panelUsersToolbar.setSpacing(false);
	panelUsersToolbar.setWidthFull();

	usersGrid = new Grid<>(User.class);
	usersGrid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);

	VerticalLayout panelUsersGrigPresenter = new VerticalLayout(panelUsersToolbar, usersGrid);
	panelUsersGrigPresenter.setWidthFull();
	panelUsersGrigPresenter.setHeightFull();

	panelUsersGrigPresenter.setMargin(false);
	panelUsersGrigPresenter.setPadding(false);
	panelUsersGrigPresenter.setSpacing(false);

	add(panelUsersGrigPresenter);
	setAlignItems(Alignment.STRETCH);
	setFlexGrow(1, panelUsersGrigPresenter);
	setSizeFull();

	setAlignItems(Alignment.STRETCH);
	setFlexGrow(1, panelUsersGrigPresenter);
	setSizeFull();

	prepare();
    }

    /**
     * Prepare behavior of the form.
     */
    private void prepare() {
	DataProvider<User, UserFilter> userDataProvider;

	fieldUsersFilter.setValueChangeMode(ValueChangeMode.EAGER);

	usersGrid.removeAllColumns();

	usersGrid.addColumn("username")
		.setFlexGrow(8)
		.setFrozen(true)
		.setSortProperty("username")
		.setHeader(getTranslation(UserEntityMsg.USERNAME));
	usersGrid.addColumn("firstName")
		.setFlexGrow(9)
		.setSortProperty("firstName")
		.setHeader(getTranslation(UserEntityMsg.FIRSNAME));
	usersGrid.addColumn("lastName")
		.setFlexGrow(9)
		.setSortProperty("lastName")
		.setHeader(getTranslation(UserEntityMsg.LASTNAME));
	usersGrid.addColumn("patronymic")
		.setFlexGrow(9)
		.setSortProperty("patronymic")
		.setHeader(getTranslation(UserEntityMsg.PATRONYMIC));
	usersGrid.addColumn("email")
		.setFlexGrow(9)
		.setSortProperty("email")
		.setHeader(getTranslation(UserEntityMsg.EMAIL));
	usersGrid.addColumn(buildUserVerifiedColumn(), "verified")
		.setFlexGrow(2)
		.setHeader(getTranslation(UserEntityMsg.VERIFIED))
		.setTextAlign(ColumnTextAlign.CENTER);
	usersGrid.addColumn(buildUsersRoleColumn(), "roles")
		.setFlexGrow(4)
		.setHeader(getTranslation(UserEntityMsg.ROLES))
		.setTextAlign(ColumnTextAlign.CENTER);
	usersGrid.addColumn(buildUserStatesColumn(), "enabled")
		.setFlexGrow(2)
		.setHeader(getTranslation(UserEntityMsg.ENABLED))
		.setTextAlign(ColumnTextAlign.CENTER);

	usersGrid
		.getColumns().forEach(column -> {
		    column.setResizable(true);
		    column.setSortable(true);
		});

	usersGrid.setColumnReorderingAllowed(true);
	usersGrid.setMultiSort(true);

	userDataProvider = DataProvider.fromFilteringCallbacks(
		query -> this.usersService.getQueriedUsersbyFilter(query,
			UserFilter.builder()
				.roles(filterRoles)
				.username(StringUtils.truncate(fieldUsersFilter.getValue(), 100)).build()),
		query -> this.usersService.getQueriedUsersByFilterCount(query,
			UserFilter.builder()
				.roles(filterRoles)
				.username(StringUtils.truncate(fieldUsersFilter.getValue(), 100)).build()));

	usersGrid.setDataProvider(userDataProvider.withConfigurableFilter());

	btnUsersGridReset.addClickListener(e -> usersGrid.getDataProvider().refreshAll());
	fieldUsersFilter.addValueChangeListener(e -> usersGrid.getDataProvider().refreshAll());
    }

    /**
     * Inits the.
     */
    @PostConstruct
    private void init() {
	usersGrid.getDataProvider().refreshAll();
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
     * Builds the user states column.
     *
     * @return the component renderer
     */
    private ComponentRenderer<Div, User> buildUserVerifiedColumn() {
	return new ComponentRenderer<>(user -> {
	    Icon icon = new Icon();
	    if (user.getVerified()) {
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
			icon.setColor("--lumo-primary-color");
			div.add(icon);
		    }
		}
		div.setTitle(sb.append("]").toString());
	    }
	    return div;
	});
    }

    /**
     * Gets the refresh change handler.
     *
     * @return the refresh change handler
     */
    public ChangeHandler<User> getRefreshChangeHandler() {
	return this::doRefreshChangeHandler;
    }

    /**
     * Do refresh change handler.
     *
     * @param user the user
     */
    private void doRefreshChangeHandler(User user) {
	if (user != null) {
	    usersGrid.deselectAll();
	    usersGrid.getDataProvider().refreshItem(user);
	    usersGrid.select(user);
	} else {
	    usersGrid.getDataProvider().refreshAll();
	}
    }

    /**
     * Gets the filter roles.
     *
     * @return the filterRoles
     */
    public Collection<Role> getFilterRoles() {
	return filterRoles;
    }

    /**
     * Sets the filter roles.
     *
     * @param filterRoles the filterRoles to set
     */
    public void setFilterRoles(Collection<Role> filterRoles) {
	this.filterRoles = filterRoles;
    }
}
