package ua.com.sipsoft.ui;

import static com.github.appreciated.app.layout.entity.Section.FOOTER;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.ui.views.facilities.FacilitiesManager;
import ua.com.sipsoft.ui.views.homeview.HomeView;
import ua.com.sipsoft.ui.views.request.archive.ArchvedVisitsManager;
import ua.com.sipsoft.ui.views.request.draft.CourierRequestsManager;
import ua.com.sipsoft.ui.views.request.draft.CourierRequestsView;
import ua.com.sipsoft.ui.views.request.issued.CourierVisitsManager;
import ua.com.sipsoft.ui.views.users.AllAdminsManager;
import ua.com.sipsoft.ui.views.users.AllClientsManager;
import ua.com.sipsoft.ui.views.users.AllCouriersManager;
import ua.com.sipsoft.ui.views.users.AllDispatchersManager;
import ua.com.sipsoft.ui.views.users.AllManagersManager;
import ua.com.sipsoft.ui.views.users.AllProductOpersManager;
import ua.com.sipsoft.ui.views.users.AllRegisteredManager;
import ua.com.sipsoft.ui.views.users.AllUsersManager;
import ua.com.sipsoft.utils.AppURL;
import ua.com.sipsoft.utils.RoleIcon;
import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.messages.ButtonMsg;
import ua.com.sipsoft.utils.messages.MainMenuMsg;
import ua.com.sipsoft.utils.security.Role;
import ua.com.sipsoft.utils.security.SecurityUtils;

/**
 * Main View of logistic Application.
 *
 * @author Pavlo Degtyaryev
 */
@Push
@Route("")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@PWA(name = "SIPSoft Логістика", shortName = "SIP Логістика", startPath = "login", iconPath = "images/logo_b.png", backgroundColor = "#233348", themeColor = "#233348")
//@HtmlImport("frontend://styles/custom.html") // You can use HTML Imports to manipulate f.e. the accent color
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Slf4j
@UIScope
@SpringComponent
public class MainView extends AppLayoutRouterLayout<LeftLayouts.LeftResponsiveHybridNoAppBar> implements RouterLayout {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1112777237411381510L;

    /**
     * Construct MainView and add Main menu.
     */
    public MainView() {
	log.info("Create Main View");

	init(AppLayoutBuilder.get(LeftLayouts.LeftResponsiveHybridNoAppBar.class)

		.withIcon("images/logo_b.png")
		.withTitle(getTranslation(MainMenuMsg.APP_BAR_TITLE))
		.withAppMenu(getLeftAppMenu().build())

		.build());
    }

    /**
     * Gets the left application menu.
     *
     * @return the left application menu
     */
    private LeftAppMenuBuilder getLeftAppMenu() {

	LeftAppMenuBuilder menu = LeftAppMenuBuilder.get();

	log.info("Build Main menu that is granted for the user \"{}\"", SecurityUtils.getUsername(),
		SecurityUtils.getUsername());

	// Home View
	if (isGrantedFor()) {

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    HomeView.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_HOME))
		    .withMenuIcon(UIIcon.HOME.createIcon())
		    .withRouteTarget(HomeView.class)
		    .build());
	}

	// Courier Requests View
	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_CLIENT, Role.ROLE_COURIER, Role.ROLE_DISPATCHER,
		Role.ROLE_MANAGER, Role.ROLE_PRODUCTOPER)) {

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    CourierRequestsView.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_COURIER_REQ))
		    .withMenuIcon(UIIcon.PHONE.createIcon())
		    .withRouteTarget(CourierRequestsView.class)
		    .withRoute().withRoutePath(AppURL.REQUESTS_ALL).withRouteLayout(MainView.class).store()
		    .build());
	}

	// Courier Requests Manager
	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_COURIER, Role.ROLE_DISPATCHER,
		Role.ROLE_MANAGER, Role.ROLE_PRODUCTOPER)) {

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    CourierRequestsManager.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_COURIER_DRAFT))
		    .withMenuIcon(UIIcon.SHEET_DRAFT.createIcon())
		    .withRouteTarget(CourierRequestsManager.class)
		    .withRoute().withRoutePath(AppURL.DRAFT_SHEETS).withRouteLayout(MainView.class).store()
		    .build());
	}

	// Courier Visits Manager
	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_COURIER, Role.ROLE_DISPATCHER,
		Role.ROLE_MANAGER, Role.ROLE_PRODUCTOPER)) {

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    CourierVisitsManager.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_COURIER_ISSUED))
		    .withMenuIcon(UIIcon.SHEET_ISSUED.createIcon())
		    .withRouteTarget(CourierVisitsManager.class)
		    .withRoute().withRoutePath(AppURL.ISSUED).withRouteLayout(MainView.class).store()
		    .build());
	}

	// Archived Visits Manager
	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_COURIER, Role.ROLE_DISPATCHER,
		Role.ROLE_MANAGER, Role.ROLE_PRODUCTOPER)) {

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    ArchvedVisitsManager.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_COURIER_ARCHIVED))
		    .withMenuIcon(UIIcon.SHEET_ARCHIVE.createIcon())
		    .withRouteTarget(ArchvedVisitsManager.class)
		    .withRoute().withRoutePath(AppURL.ARCHIVE).withRouteLayout(MainView.class).store()
		    .build());
	}

	// Facilities Manager
	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_COURIER, Role.ROLE_DISPATCHER,
		Role.ROLE_MANAGER, Role.ROLE_PRODUCTOPER)) {

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    FacilitiesManager.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_FACILITIES))
		    .withMenuIcon(UIIcon.OFFICE.createIcon())
		    .withRouteTarget(FacilitiesManager.class)
		    .withRoute().withRoutePath(AppURL.FACILITIES_ALL).withRouteLayout(MainView.class).store()
		    .build());

	}

	// Users Submenu
	Optional<LeftSubMenuBuilder> subMenu = getLeftUsersSubmenu();
	if (subMenu.isPresent()) {
	    log.info("Build User submenu is granted for the user \"{}\"", SecurityUtils.getUsername(),
		    SecurityUtils.getUsername());
	    menu.add(subMenu.get().build());
	} else {
	    log.info("Build User submenu is not granted for the user \"{}\"", SecurityUtils.getUsername(),
		    SecurityUtils.getUsername());
	}

	// Logout
	log.info("Added Logout menu item for the user \"{}\"", SecurityUtils.getUsername());
	menu.addToSection(FOOTER,
		new LeftClickableItem(getTranslation(MainMenuMsg.MENU_LOGOUT),
			UIIcon.SIGN_OUT.createIcon(),
			clickEvent -> confirmLogout()));

	// Light Dark theme change
	log.info("Added Theme Repaint menu item for the user \"{}\"", SecurityUtils.getUsername());
	menu.addToSection(FOOTER,
		new LeftClickableItem("",
			UIIcon.THEME_PAINTER.createIcon(),
			clickEvent -> {
			    ThemeList themeList = UI.getCurrent().getElement().getThemeList();
			    if (themeList.contains(Lumo.DARK)) { //
				themeList.remove(Lumo.DARK);
			    } else {
				themeList.add(Lumo.DARK);
			    }
			}));
	return menu;
    }

    /**
     * Gets the left users submenu.
     *
     * @return the left users submenu
     */
    private Optional<LeftSubMenuBuilder> getLeftUsersSubmenu() {

	boolean isUserSubmenuEmpty = true;

	LeftSubMenuBuilder menu = LeftSubMenuBuilder.get(getTranslation(MainMenuMsg.MENU_USERS),
		UIIcon.USERS.createIcon());

	log.info("Build Users submenu for the user \"{}\"", SecurityUtils.getUsername(),
		SecurityUtils.getUsername());

	// All Registered Manager
	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER)) {

	    isUserSubmenuEmpty = false;

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllRegisteredManager.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_REGISTERED))
		    .withMenuIcon(RoleIcon.USER.createIcon())
		    .withRouteTarget(AllRegisteredManager.class)
		    .withRoute().withRoutePath(AppURL.REGISTERED_ALL).withRouteLayout(MainView.class).store()
		    .build());
	}

	// All Clients Manager
	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER)) {

	    isUserSubmenuEmpty = false;

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllClientsManager.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_CLIENTS))
		    .withMenuIcon(RoleIcon.CLIENT.createIcon())
		    .withRouteTarget(AllClientsManager.class)
		    .withRoute().withRoutePath(AppURL.CLIENTS_ALL).withRouteLayout(MainView.class).store()
		    .build());
	}

	// All Couriers Manager
	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
		Role.ROLE_PRODUCTOPER)) {

	    isUserSubmenuEmpty = false;

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllCouriersManager.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_COURIERS))
		    .withMenuIcon(RoleIcon.COURIER.createIcon())
		    .withRouteTarget(AllCouriersManager.class)
		    .withRoute().withRoutePath(AppURL.COURIERS_ALL).withRouteLayout(MainView.class).store()
		    .build());
	}

	// All Managers Manager
	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
		Role.ROLE_PRODUCTOPER)) {

	    isUserSubmenuEmpty = false;

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllManagersManager.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_MANAGERS))
		    .withMenuIcon(RoleIcon.MANAGER.createIcon())
		    .withRouteTarget(AllManagersManager.class)
		    .withRoute().withRoutePath(AppURL.MANAGERS_ALL).withRouteLayout(MainView.class).store()
		    .build());
	}

	// All Product Operators Manager
	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
		Role.ROLE_PRODUCTOPER)) {

	    isUserSubmenuEmpty = false;

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllProductOpersManager.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_PRODUCTOPERS))
		    .withMenuIcon(RoleIcon.PRODUCTOPER.createIcon())
		    .withRouteTarget(AllProductOpersManager.class)
		    .withRoute().withRoutePath(AppURL.PRODUCTOPERS_ALL).withRouteLayout(MainView.class).store()
		    .build());
	}

	// All Dispatchers Manager
	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
		Role.ROLE_PRODUCTOPER)) {

	    isUserSubmenuEmpty = false;

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllDispatchersManager.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_DISPATCHERS))
		    .withMenuIcon(RoleIcon.DISPATCHER.createIcon())
		    .withRouteTarget(AllDispatchersManager.class)
		    .withRoute().withRoutePath(AppURL.DISPATCHERS_ALL).withRouteLayout(MainView.class).store()
		    .build());
	}

	// All Admins Manager
	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
		Role.ROLE_PRODUCTOPER)) {

	    isUserSubmenuEmpty = false;

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllAdminsManager.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_ADMINS))
		    .withMenuIcon(RoleIcon.ADMIN.createIcon())
		    .withRouteTarget(AllAdminsManager.class)
		    .withRoute().withRoutePath(AppURL.ADMINS_ALL).withRouteLayout(MainView.class).store()
		    .build());
	}

	// All Users Manager
	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
		Role.ROLE_PRODUCTOPER)) {

	    isUserSubmenuEmpty = false;

	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllUsersManager.class.getName());

	    menu.add(new LeftNavigationItemBuilder()
		    .withMenuItem(getTranslation(MainMenuMsg.MENU_ALLUSERS))
		    .withMenuIcon(UIIcon.GROUP.createIcon())
		    .withRouteTarget(AllUsersManager.class)
		    .withRoute().withRoutePath(AppURL.USERS_ALL).withRouteLayout(MainView.class).store()
		    .build());
	}
	if (isUserSubmenuEmpty) {
	    return Optional.ofNullable(null);
	} else {
	    return Optional.of(menu);
	}
    }

    /**
     * Checks if is granted for.
     *
     * @param roles the set of acceptred roles
     * @return true, if is granted for current User
     */
    private boolean isGrantedFor(Role... roles) {
	if (roles == null || roles.length == 0) {
	    log.info("Restriction roles set is absent. Acces granted.");
	    return true;
	}
	Collection<Role> sRoles = SecurityUtils.getUserRoles();
	String user = SecurityUtils.getUsername();
	if (CollectionUtils.containsAny(sRoles, roles)) {
	    log.info("\"{}\"`s roles intersect with Restriction roles set in \"{}\"", user,
		    CollectionUtils.intersection(sRoles, Arrays.asList(roles)),
		    Arrays.deepToString(roles));
	    return true;
	}
	log.info("\"{}\"`s roles is not intersect with restriction roles set. Access denied.", user);
	return false;
    }

    /**
     * Confirm logout by {@link ConfirmDialog} and redirect to standard logout URL.
     */
    public void confirmLogout() {
	log.info("Create confirm logout dialog");
	ConfirmDialog
		.createQuestion()
		.withCaption(getTranslation(MainMenuMsg.MENU_EXIT_TITLE))
		.withMessage(getTranslation(MainMenuMsg.MENU_EXIT_QUESTION))
		.withOkButton(() -> {
		    SecurityContextHolder.clearContext();
		    UI.getCurrent().getSession().close();
		    UI.getCurrent().getPage().reload();// to redirect user to the login page
		},
			ButtonOption.focus(), ButtonOption.caption(getTranslation(ButtonMsg.BTN_SIGN_OUT)),
			ButtonOption.icon(UIIcon.BTN_SIGN_OUT.getIcon()))
		.withCancelButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CANCEL)),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

    /**
     * The Class LeftNavigationItemBuilder.
     */
    private static final class LeftNavigationItemBuilder {

	/** The menu item. */
	private String menuItem;

	/** The menu icon. */
	private Icon menuIcon;

	/** The target Class */
	private Class<? extends Component> clazz;

	/** The route builder. */
	private NavigationItemRouteBuilder routeBuilder;

	/**
	 * With menu item.
	 *
	 * @param menuItem the menu item
	 * @return the left navigation item builder
	 */
	public LeftNavigationItemBuilder withMenuItem(String menuItem) {
	    this.menuItem = menuItem;
	    return this;
	}

	/**
	 * With menu icon.
	 *
	 * @param menuIcon the menu icon
	 * @return the left navigation item builder
	 */
	public LeftNavigationItemBuilder withMenuIcon(Icon menuIcon) {
	    this.menuIcon = menuIcon;
	    return this;
	}

	/**
	 * With route target.
	 *
	 * @param clazz the Class
	 * @return the left navigation item builder
	 */
	public LeftNavigationItemBuilder withRouteTarget(Class<? extends Component> clazz) {
	    this.clazz = clazz;
	    return this;
	}

	/**
	 * With route.
	 *
	 * @return the navigation item route builder
	 */
	public NavigationItemRouteBuilder withRoute() {
	    this.routeBuilder = new NavigationItemRouteBuilder(this);
	    return this.routeBuilder;
	}

	/**
	 * Builds the.
	 *
	 * @return the left navigation item
	 */
	public LeftNavigationItem build() {
	    if (menuIcon == null) {
		throw new IllegalArgumentException("Menu item has to have icon.");
	    }
	    if (menuItem == null) {
		throw new IllegalArgumentException("Menu item has to have menu item.");
	    }
	    if (clazz == null) {
		throw new IllegalArgumentException("Menu item has to have target view.");
	    }
	    if (routeBuilder != null && (routeBuilder.routePath == null | routeBuilder.routeLayout == null)) {
		if (routeBuilder.routePath == null) {
		    throw new IllegalArgumentException("Route has to have route path.");
		} else {
		    throw new IllegalArgumentException("Route has to have route layout class.");
		}
	    }
	    if (routeBuilder != null) {
		createRoute(routeBuilder.routePath, clazz, routeBuilder.routeLayout);
	    }
	    return new LeftNavigationItem(menuItem, menuIcon, clazz);
	}

	/**
	 * Creates the route.
	 *
	 * @param routePath   the route path
	 * @param clazz       the clazz
	 * @param routeLayout the route layout
	 */
	@SuppressWarnings("unchecked")
	private void createRoute(String routePath, Class<? extends Component> clazz,
		Class<? extends RouterLayout> routeLayout) {
	    RouteConfiguration.forSessionScope().setRoute(routePath, clazz, routeLayout);
	}
    }

    /**
     * The Class NavigationItemRouteBuilder.
     */
    private static class NavigationItemRouteBuilder {

	/** The route path. */
	private String routePath;

	/** The route layout. */
	private Class<? extends RouterLayout> routeLayout;

	/** The item builder. */
	private LeftNavigationItemBuilder itemBuilder;

	/**
	 * Instantiates a new navigation item route builder.
	 *
	 * @param itemBuilder the item builder
	 */
	public NavigationItemRouteBuilder(LeftNavigationItemBuilder itemBuilder) {
	    this.itemBuilder = itemBuilder;
	}

	/**
	 * With route path.
	 *
	 * @param routePath the route path
	 * @return the navigation item route builder
	 */
	public NavigationItemRouteBuilder withRoutePath(String routePath) {
	    this.routePath = routePath;
	    return this;
	}

	/**
	 * With route layout.
	 *
	 * @param routeLayout the route layout
	 * @return the navigation item route builder
	 */
	public NavigationItemRouteBuilder withRouteLayout(Class<? extends RouterLayout> routeLayout) {
	    this.routeLayout = routeLayout;
	    return this;
	}

	/**
	 * Store.
	 *
	 * @return the left navigation item builder
	 */
	public LeftNavigationItemBuilder store() {
	    return this.itemBuilder;
	}
    }

}
