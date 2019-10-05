package ua.com.sipsoft.ui.views.request.common;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.Collections;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.prototype.AbstractHistoryEvent;
import ua.com.sipsoft.utils.Props;
import ua.com.sipsoft.utils.messages.HistoryEventMsg;

/**
 * The Class FacilityUsersEditor.
 *
 * @author Pavlo Degtyaryev
 * @param <T> the generic type
 */
@Scope("prototype")
//@Tag("facility-users-editor-form")

/** The Constant log. */
@Slf4j
@SpringComponent
public class HistoryEventViever<T extends AbstractHistoryEvent> extends FormLayout {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -246574475427870320L;

    /**
     * Instantiates a new history viewer
     *
     * @param usersService the users service
     */
    public HistoryEventViever(Grid<T> grid, Collection<T> events) {
	super();
	log.info("Instantiates a new history viewer");
	if (grid == null) {
	    return;
	}
	VerticalLayout panelFields = new VerticalLayout();

	grid.setColumnReorderingAllowed(true);
	grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);
	grid.setWidthFull();

	grid.removeAllColumns();

	grid.addColumn("description")
		.setHeader(getTranslation(HistoryEventMsg.EVENT))
		.setFlexGrow(1)
		.setSortProperty("description");

	grid.addColumn("author.username")
		.setHeader(getTranslation(HistoryEventMsg.AUTHOR))
		.setWidth(Props.EM_05)
		.setFlexGrow(0)
		.setSortProperty("author.username");

	grid.addColumn(new LocalDateTimeRenderer<>(T::getCreationDate,
		DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)))
		.setHeader(getTranslation(HistoryEventMsg.DATA))
		.setWidth(Props.EM_11)
		.setFlexGrow(0)
		.setKey("creationDate")
		.setComparator((event1, event2) -> event1.getCreationDate().compareTo(event2.getCreationDate()));

	grid.addColumn("id")
		.setHeader(getTranslation(HistoryEventMsg.ID))
		.setSortProperty("id")
		.setWidth(Props.EM_05)
		.setFlexGrow(0);

	grid.focus();
	grid.getColumns().forEach(column -> {
	    column.setResizable(true);
	    column.setSortable(true);
	});

	grid.setColumnReorderingAllowed(true);
	grid.setMultiSort(true);

	grid.setWidth(Props.EM_56);

	panelFields.add(grid);
	panelFields.setAlignItems(Alignment.STRETCH);
	panelFields.setFlexGrow(1, grid);
	panelFields.setMargin(false);
	panelFields.setPadding(false);
	panelFields.setSpacing(false);
	panelFields.setSizeFull();
	panelFields.getStyle().set(Props.MARGIN, Props.EM_0_5);
	panelFields.getStyle().set(Props.PADDING, null);

	add(panelFields);
	setSizeFull();

	if (events == null) {
	    grid.setItems(Collections.emptyList());
	} else {
	    grid.setItems(events.stream().sorted((e1, e2) -> e1.getCreationDate().compareTo(e2.getCreationDate())));
	}
    }

}
