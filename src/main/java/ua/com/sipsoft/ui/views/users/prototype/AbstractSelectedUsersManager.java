package ua.com.sipsoft.ui.views.users.prototype;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.selection.SelectionEvent;

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
public abstract class AbstractSelectedUsersManager extends VerticalLayout {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3120440143379685008L;

    /** The background. */
    private SplitLayout background;

    /** The user editor. */
    private final UserEditor<User> userEditor;

    /** The selected users grid viewer. */
    private final UsersGridViewer usersGridViewer;

    /**
     * Instantiates a new abstract selected users manager.
     *
     * @param usersGridViewer the selected users grid viewer
     * @param userEditor      the user editor
     */
    public AbstractSelectedUsersManager(UsersGridViewer usersGridViewer, UserEditor<User> userEditor) {
	this.userEditor = userEditor;
	this.usersGridViewer = usersGridViewer;
	background = new SplitLayout();

	this.usersGridViewer.getUsersGrid().addSelectionListener(
		event -> this.showDetails(event));
	userEditor.setChangeHandler(this.usersGridViewer.getRefreshChangeHandler());
	userEditor.setVisible(false);

	background.addToPrimary(this.usersGridViewer);
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
     * @param event the user
     */
    public void showDetails(SelectionEvent<Grid<User>, User> event) {
	log.debug("showDetail swithch visibility of UserEditor by user: {}", event);
	User user = event.getFirstSelectedItem().stream().findFirst().orElse(null);
	if (user != null) {
	    userEditor.setOperationData(user);
	    userEditor.setVisible(true);
	} else {
	    userEditor.setVisible(false);
	}
    }
}
