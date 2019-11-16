package ua.com.sipsoft.ui;

import static com.github.appreciated.app.layout.entity.Section.FOOTER;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.appreciated.app.layout.addons.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
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
public class MainView extends AppLayoutRouterLayout<LeftLayouts.LeftResponsiveHybridNoAppBar> {

    private static final long serialVersionUID = 1112777237411381510L;

    private DefaultNotificationHolder notifications;

    private transient DefaultBadgeHolder badge;

    /**
     * Construct MainView and add Main menu.
     */
    public MainView() {
	log.info("Create Main View");

	notifications = new DefaultNotificationHolder(newStatus -> {
	});
	badge = new DefaultBadgeHolder(5);
//	for (int i = 1; i < 6; i++) {
//	    notifications.addNotification(new DefaultNotification("Тестовий заголовок" + i,
//		    "Досить довгий тестовий опис ..............." + i));
//	}
//		LeftNavigationItem menuEntry = new LeftNavigationItem("Меню", VaadinIcon.MENU.create(), View6.class);
//		badge.bind(menuEntry.getBadge());

	init(AppLayoutBuilder.get(LeftLayouts.LeftResponsiveHybridNoAppBar.class)

		.withIcon("images/logo_b.png")
		.withTitle(getTranslation(MainMenuMsg.APP_BAR_TITLE))
//				.withAppBar(
//						AppBarBuilder.get()
//						.add(new AppBarNotificationButton<>(VaadinIcon.BELL, notifications))
//						.add(new AppBarNotificationButton<>(VaadinIcon.BELL, notifications))
//								.build())
		.withAppMenu(getLeftAppMenu().build())

		.build());
    }

    private LeftAppMenuBuilder getLeftAppMenu() {
	LeftAppMenuBuilder menu = LeftAppMenuBuilder.get();
	log.info("Build Main menu that is granted for the user \"{}\"", SecurityUtils.getUsername(),
		SecurityUtils.getUsername());

	if (isGrantedFor()) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    HomeView.class.getName());
	    menu.add((new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_HOME),
		    UIIcon.HOME.createIcon(), HomeView.class)));
	}

	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_CLIENT, Role.ROLE_COURIER, Role.ROLE_DISPATCHER,
		Role.ROLE_MANAGER, Role.ROLE_PRODUCTOPER)) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    CourierRequestsView.class.getName());
	    menu.add(new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_COURIER_REQ),
		    UIIcon.PHONE.createIcon(), CourierRequestsView.class));
	}

	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_COURIER, Role.ROLE_DISPATCHER,
		Role.ROLE_MANAGER, Role.ROLE_PRODUCTOPER)) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    CourierRequestsManager.class.getName());
	    menu.add(new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_COURIER_DRAFT),
		    UIIcon.SHEET_DRAFT.createIcon(), CourierRequestsManager.class));
	}

	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_COURIER, Role.ROLE_DISPATCHER,
		Role.ROLE_MANAGER, Role.ROLE_PRODUCTOPER)) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    CourierVisitsManager.class.getName());
	    menu.add(new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_COURIER_ISSUED),
		    UIIcon.SHEET_ISSUED.createIcon(), CourierVisitsManager.class));
	}

	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_COURIER, Role.ROLE_DISPATCHER,
		Role.ROLE_MANAGER, Role.ROLE_PRODUCTOPER)) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    ArchvedVisitsManager.class.getName());
	    menu.add(new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_COURIER_ARCHIVED),
		    UIIcon.SHEET_ARCHIVE.createIcon(), ArchvedVisitsManager.class));
	}

	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_COURIER, Role.ROLE_DISPATCHER,
		Role.ROLE_MANAGER, Role.ROLE_PRODUCTOPER)) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    FacilitiesManager.class.getName());
	    menu.add(new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_FACILITIES),
		    VaadinIcon.OFFICE.create(), FacilitiesManager.class));
	}

	Optional<LeftSubMenuBuilder> subMenu = getLeftUsersSubmenu();
	if (subMenu.isPresent()) {
	    log.info("Build User submenu is granted for the user \"{}\"", SecurityUtils.getUsername(),
		    SecurityUtils.getUsername());
	    menu.add(subMenu.get().build());
	} else {
	    log.info("Build User submenu is not granted for the user \"{}\"", SecurityUtils.getUsername(),
		    SecurityUtils.getUsername());
	}

	log.info("Added Logout menu item for the user \"{}\"", SecurityUtils.getUsername());
	menu.addToSection(FOOTER,
		new LeftClickableItem(getTranslation(MainMenuMsg.MENU_LOGOUT),
			UIIcon.SIGN_OUT.createIcon(),
			clickEvent -> confirmLogout()));

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

    private Optional<LeftSubMenuBuilder> getLeftUsersSubmenu() {
	boolean isEmpty = true;
	LeftSubMenuBuilder menu = LeftSubMenuBuilder.get(getTranslation(MainMenuMsg.MENU_USERS),
		UIIcon.USERS.createIcon());

	log.info("Build Users submenu for the user \"{}\"", SecurityUtils.getUsername(),
		SecurityUtils.getUsername());

	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER)) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllRegisteredManager.class.getName());
	    isEmpty = false;
	    menu.add(new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_REGISTERED),
		    RoleIcon.USER.createIcon(), AllRegisteredManager.class));
	}

	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER)) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllClientsManager.class.getName());
	    isEmpty = false;
	    menu.add(new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_CLIENTS),
		    RoleIcon.CLIENT.createIcon(), AllClientsManager.class));
	}

	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
		Role.ROLE_PRODUCTOPER)) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllCouriersManager.class.getName());
	    isEmpty = false;
	    menu.add(new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_COURIERS),
		    RoleIcon.COURIER.createIcon(), AllCouriersManager.class));
	}

	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
		Role.ROLE_PRODUCTOPER)) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllManagersManager.class.getName());
	    isEmpty = false;
	    menu.add(new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_MANAGERS),
		    RoleIcon.MANAGER.createIcon(), AllManagersManager.class));
	}

	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
		Role.ROLE_PRODUCTOPER)) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllProductOpersManager.class.getName());
	    isEmpty = false;
	    menu.add(new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_PRODUCTOPERS),
		    RoleIcon.PRODUCTOPER.createIcon(), AllProductOpersManager.class));
	}

	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
		Role.ROLE_PRODUCTOPER)) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllDispatchersManager.class.getName());
	    isEmpty = false;
	    menu.add(new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_DISPATCHERS),
		    RoleIcon.DISPATCHER.createIcon(), AllDispatchersManager.class));
	}

	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
		Role.ROLE_PRODUCTOPER)) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllAdminsManager.class.getName());
	    isEmpty = false;
	    menu.add(new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_ADMINS),
		    RoleIcon.ADMIN.createIcon(), AllAdminsManager.class));
	}

	if (isGrantedFor(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
		Role.ROLE_PRODUCTOPER)) {
	    log.info("Access for the user \"{}\" is granted for view: {}", SecurityUtils.getUsername(),
		    AllUsersManager.class.getName());
	    isEmpty = false;
	    menu.add(new LeftNavigationItem(getTranslation(MainMenuMsg.MENU_ALLUSERS),
		    UIIcon.GROUP.createIcon(), AllUsersManager.class));
//	    RouteConfiguration.forSessionScope().setAnnotatedRoute(AllUsersManager.class);
	}

	if (isEmpty) {
	    return Optional.ofNullable(null);
	} else {
	    return Optional.of(menu);
	}
    }

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

}
