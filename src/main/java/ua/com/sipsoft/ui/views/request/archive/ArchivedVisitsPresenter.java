
package ua.com.sipsoft.ui.views.request.archive;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisit;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisitEvent;
import ua.com.sipsoft.ui.views.request.common.HistoryEventViever;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.messages.ButtonMsg;
import ua.com.sipsoft.utils.messages.HistoryEventMsg;

/**
 * The Class ArchivedVisitsPresenter.
 * 
 * @author Pavlo Degtyaryev
 */
@Slf4j
public class ArchivedVisitsPresenter extends VerticalLayout {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8337502175083498173L;

    /** The Constant SEMICOLON. */
    private static final String SEMICOLON = " : ";

    /** The formatter. */
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    /**
     * Instantiates a new courier visits presenter.
     *
     * @param visit the visit
     */
    public ArchivedVisitsPresenter(ArchivedCourierVisit visit) {
	log.debug("Instantiates a new courier visits presenter");
	Label fromFacility = new Label(visit.getFromPoint().getFacility().getName() + SEMICOLON);
	Label fromPoint = new Label(visit.getFromPoint().getAddress());
	Label author = new Label(visit.getAuthor().getUsername());
	Label toFacility = new Label(visit.getToPoint().getFacility().getName() + SEMICOLON);
	Label toPoint = new Label(visit.getToPoint().getAddress());
	Label creationDate = new Label(visit.getCreationDate()
		.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)));
//		Label creationDate = new Label(visit.getCreationDate().format(formatter));
	Label description = new Label(visit.getDescription());

	fromFacility.setHeight(Props.EM_01_2);
	Icon iconMapFrom = new Icon(UIIcon.ADDRESS.getIcon());
	fromPoint.setHeight(Props.EM_01_2);
	Icon iconCalendar = new Icon(UIIcon.CALENDAR_CLOCK.getIcon());
	creationDate.setHeight(Props.EM_01_2);
	toFacility.setHeight(Props.EM_01_2);
	Icon iconMapTo = new Icon(UIIcon.ADDRESS.getIcon());
	toPoint.setHeight(Props.EM_01_2);
	Icon iconAuthor = new Icon(UIIcon.CHILD.getIcon());
	author.setHeight(Props.EM_01_2);
	description.setHeight(Props.EM_01_2);
	Icon iconState = visit.getState().createIcon();
	Icon iconInfo = UIIcon.INFO.createIcon();

	HorizontalLayout firstLine = new HorizontalLayout();
	HorizontalLayout secondLine = new HorizontalLayout();
	HorizontalLayout thirdLine = new HorizontalLayout();

	iconMapFrom.setSize(Props.EM_01_2);
	iconCalendar.setSize(Props.EM_01_2);
	iconMapTo.setSize(Props.EM_01_2);
	iconAuthor.setSize(Props.EM_01_2);
	iconState.setSize(Props.EM_01_2);
	iconInfo.setSize(Props.EM_01_2);

	// first line
	firstLine.add(fromFacility, iconMapFrom, fromPoint, iconCalendar, creationDate);
	creationDate.setWidth(Props.EM_08);
	firstLine.setFlexGrow(0, fromFacility);
	firstLine.setFlexGrow(0, iconMapFrom);
	firstLine.setFlexGrow(1, fromPoint);
	firstLine.setFlexGrow(0, iconCalendar);
	firstLine.setFlexGrow(0, creationDate);
	firstLine.setMargin(false);
	firstLine.setPadding(false);
	firstLine.setSpacing(false);
	firstLine.setWidthFull();
	firstLine.getStyle().set(Props.PADDING, null);

	fromPoint.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	creationDate.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);

	// second line
	secondLine.add(toFacility, iconMapTo, toPoint, iconAuthor, author);
	author.setWidth(Props.EM_08);
	secondLine.setFlexGrow(0, toFacility);
	secondLine.setFlexGrow(0, iconMapTo);
	secondLine.setFlexGrow(1, toPoint);
	secondLine.setFlexGrow(0, iconAuthor);
	secondLine.setFlexGrow(0, author);
	secondLine.setMargin(false);
	secondLine.setPadding(false);
	secondLine.setSpacing(false);
	secondLine.setWidthFull();
	secondLine.getStyle().set(Props.PADDING, null);

	toPoint.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);
	author.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);

	// third line
	thirdLine.add(description, iconState, iconInfo);
	thirdLine.setFlexGrow(0, iconState);
	thirdLine.setFlexGrow(0, iconInfo);
	thirdLine.setFlexGrow(1, description);
	thirdLine.setMargin(false);
	thirdLine.setPadding(false);
	thirdLine.setSpacing(false);
	thirdLine.setWidthFull();

	iconInfo.getStyle().set(Props.MARGIN_LEFT, Props.EM_0_2);

	thirdLine.getStyle().set(Props.PADDING, null);
	add(firstLine, secondLine, thirdLine);
	setMargin(false);
	setPadding(false);
	setSpacing(false);

	iconInfo.addClickListener(event -> ConfirmDialog
		.create()
		.withCaption(getTranslation(HistoryEventMsg.HEADER_VEIW))
		.withMessage(new HistoryEventViever<>(new Grid<>(ArchivedCourierVisitEvent.class),
			visit.getHistoryEvents()))
		.withCloseButton(ButtonOption.caption(getTranslation(ButtonMsg.BTN_CLOSE)),
			ButtonOption.icon(UIIcon.BTN_OK.getIcon()))
		.open());
    }

}
