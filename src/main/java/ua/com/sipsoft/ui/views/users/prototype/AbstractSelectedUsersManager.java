package ua.com.sipsoft.ui.views.users.prototype;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.ui.commons.AppNotificator;
import ua.com.sipsoft.ui.commons.forms.Modality;
import ua.com.sipsoft.ui.commons.forms.viewform.ViewForm;
import ua.com.sipsoft.ui.views.users.components.UserEditor;
import ua.com.sipsoft.ui.views.users.components.UsersGridViewer;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.messages.AppNotifyMsg;

/**
 * The Class AbstractSelectedUsersManager.
 *
 * @author Pavlo Degtyaryev
 */
@Slf4j
@SpringComponent
public abstract class AbstractSelectedUsersManager extends VerticalLayout {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3120440143379685008L;

    /** The user editor. */
    private final UserEditor<User> userEditor;

    /** The view form. */
    private final ViewForm viewForm;

    /**
     * Sets the change handler.
     *
     */
    private ChangeHandler<User> changeHandler;

    /**
     * Instantiates a new abstract selected users manager.
     *
     * @param usersGridViewer the selected users grid viewer
     * @param viewForm        the view form
     * @param userEditor      the user editor
     */
    public AbstractSelectedUsersManager(UsersGridViewer usersGridViewer, ViewForm viewForm,
	    UserEditor<User> userEditor) {
	this.userEditor = userEditor;
	this.viewForm = viewForm;

	viewForm.setDataEditor(userEditor);
	viewForm.addModality(Modality.MR_SAVE, event -> {
	    if (userEditor.isValidOperationData()) {
		User operationData = usersGridViewer.getUsersService().saveUser(userEditor.getOperationData());
		if (changeHandler != null) {
		    changeHandler.onChange(operationData);
		}
		AppNotificator.notify(getTranslation(AppNotifyMsg.USER_SAVED));

	    } else {
		AppNotificator.notifyError(getTranslation(AppNotifyMsg.USER_NOT_SAVED));

	    }
	});
	viewForm.addModality(Modality.MR_REFRESH, event -> {
	    userEditor.resetOperationData();
	});

	viewForm.setWidth(Props.EM_30);
	viewForm.setMinWidth(Props.EM_22);
	viewForm.setMaxWidth(Props.EM_40);

	SplitLayout background = new SplitLayout();

	usersGridViewer.getUsersGrid().addSelectionListener(
		event -> this.showDetails(event));
	changeHandler = usersGridViewer.getRefreshChangeHandler();
	viewForm.setVisible(false);

	background.addToPrimary(usersGridViewer);
	background.addToSecondary(viewForm);

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
	    viewForm.setVisible(true);
	} else {
	    viewForm.setVisible(false);
	}
    }
}
