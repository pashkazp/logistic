package ua.com.sipsoft.ui.views.users.prototype;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.ui.views.users.components.UserEditor;
import ua.com.sipsoft.ui.views.users.components.UsersGridViewer;
import ua.com.sipsoft.utils.Props;

/**
 * The Class AbstractSelectedUsersManager.
 *
 * @author Pavlo Degtyaryev
 * @param <T> the generic type
 */

/** The Constant log. */
@Slf4j
public class AbstractSelectedUsersManager<T extends UsersGridViewer> extends VerticalLayout {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3120440143379685008L;

    /** The background. */
    private SplitLayout background;

    /** The user editor. */
    private UserEditor<User> userEditor;

    /** The selected users grid viewer. */
    private T usersGridViewer;

    /**
     * Instantiates a new abstract selected users manager.
     *
     * @param usersGridViewer the selected users grid viewer
     * @param userEditor              the user editor
     */
    public AbstractSelectedUsersManager(T usersGridViewer, UserEditor<User> userEditor) {
	this.userEditor = userEditor;
	this.setUsersGridViewer(usersGridViewer);
	background = new SplitLayout();

	usersGridViewer.getUsersGrid().addSelectionListener(
		event -> this.showDetails(event.getFirstSelectedItem().stream().findFirst().orElse(null)));
	userEditor.setChangeHandler(usersGridViewer.getRefreshChangeHandler());
	userEditor.setVisible(false);

	background.addToPrimary(usersGridViewer);
	background.addToSecondary(userEditor);

	background.getStyle().set(Props.MARGIN, Props.EM_0_5);
	background.getStyle().set("padding", null);

	add(background);
	setAlignItems(Alignment.STRETCH);
	setFlexGrow(1, background);
	setSizeFull();
	setMargin(false);
	setPadding(false);
	setSpacing(true);
    }

    /**
     * Show details.
     *
     * @param user the user
     */
    public void showDetails(User user) {
	log.debug("showDetail swithch visibility of UserEditor by user: {}", user);
	if (user != null) {
	    userEditor.setVisible(true);
	    userEditor.editUser(user);
	} else {
	    userEditor.setVisible(false);
	}
    }

    /**
     * @return the usersGridViewer
     */
    public T getUsersGridViewer() {
	return usersGridViewer;
    }

    /**
     * @param usersGridViewer the usersGridViewer to set
     */
    public void setUsersGridViewer(T usersGridViewer) {
	this.usersGridViewer = usersGridViewer;
    }
}
