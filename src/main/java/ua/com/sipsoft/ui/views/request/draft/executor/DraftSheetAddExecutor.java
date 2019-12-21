package ua.com.sipsoft.ui.views.request.draft.executor;

import java.util.Optional;

import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.draft.DraftRouteSheet;
import ua.com.sipsoft.services.requests.draft.DraftRouteSheetService;
import ua.com.sipsoft.services.utils.DraftRouteSheetEntityEvent;
import ua.com.sipsoft.services.utils.EntityOperationType;
import ua.com.sipsoft.ui.commons.AppNotificator;
import ua.com.sipsoft.ui.commons.presenter.executor.AbstractExecutor;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.messages.AppNotifyMsg;
import ua.com.sipsoft.utils.messages.ButtonMsg;
import ua.com.sipsoft.utils.messages.DraftRouteSheetMsg;
import ua.com.sipsoft.utils.security.SecurityUtils;

@Slf4j
@UIScope
@SpringComponent
public class DraftSheetAddExecutor<T extends DraftRouteSheet> extends AbstractExecutor<T> {

    @Autowired
    @Setter
    private transient DraftRouteSheetService draftRouteSheetService;

    @Override
    public void execute() {
	log.info("Try to add Draft Route Sheet");
	if (selectionModel == null) {
	    log.error("Selection model is not installed");
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
	description.setInvalid(description.getValue().length() < 10);
	description
		.setErrorMessage(i18n.getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION, UI.getCurrent().getLocale()));

	VerticalLayout panel = new VerticalLayout(
		new Label(i18n.getTranslation(DraftRouteSheetMsg.DRAFT_ROUTE_SHEET_CREATE_MSG,
			UI.getCurrent().getLocale())),
		description);
	panel.setMargin(false);
	panel.setPadding(false);
	panel.setSpacing(false);
	panel.getStyle().set(Props.MARGIN, Props.EM_0_5);
	ConfirmDialog
		.create()
		.withCaption(
			i18n.getTranslation(DraftRouteSheetMsg.DRAFT_ROUTE_SHEET_CREATE_HEADER,
				UI.getCurrent().getLocale()))
		.withMessage(panel)
		.withSaveButton(() -> {
		    try {
			if (description.isInvalid()) {
			    AppNotificator.notifyError(i18n.getTranslation(AppNotifyMsg.WARN_SHORT_DESCRIPTION,
				    UI.getCurrent().getLocale()));
			    return;
			}
			log.info("Perform to add Draft Route Sheet");
			DraftRouteSheet draftRouteSheetIn = new DraftRouteSheet();
			draftRouteSheetIn.setAuthor(SecurityUtils.getUser());
			draftRouteSheetIn.setDescription(description.getValue());
			draftRouteSheetIn.addHistoryEvent(
				new StringBuilder()
					.append("Чернетку маршрутного листа з описом \"")
					.append(description.getValue())
					.append("\" було створено.")
					.toString(),
				draftRouteSheetIn.getCreationDate(), SecurityUtils.getUser());
			Optional<DraftRouteSheet> draftRouteSheetOut = draftRouteSheetService
				.save(draftRouteSheetIn);
			if (draftRouteSheetOut.isPresent()) {
			    final DraftRouteSheetEntityEvent event = new DraftRouteSheetEntityEvent(
				    EntityOperationType.CREATE, draftRouteSheetOut.get());
			    publisher.publishEvent(event);
			    AppNotificator.notify(i18n.getTranslation(AppNotifyMsg.DRAFT_ROUTE_SHEET_CREATED,
				    UI.getCurrent().getLocale()));
			}
		    } catch (Exception e) {
			AppNotificator.notifyError(5000, e.getMessage());
		    }

		}, ButtonOption.focus(),
			ButtonOption.caption(i18n.getTranslation(ButtonMsg.BTN_SAVE, UI.getCurrent().getLocale())),
			ButtonOption.icon(UIIcon.BTN_PUT.getIcon()))
		.withCancelButton(
			ButtonOption.caption(i18n.getTranslation(ButtonMsg.BTN_CANCEL, UI.getCurrent().getLocale())),
			ButtonOption.icon(UIIcon.BTN_NO.getIcon()))
		.open();
    }

}
