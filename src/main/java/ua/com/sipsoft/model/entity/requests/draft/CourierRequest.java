package ua.com.sipsoft.model.entity.requests.draft;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisit;
import ua.com.sipsoft.model.entity.requests.issued.CourierVisit;
import ua.com.sipsoft.model.entity.requests.prototype.AbstractCourierRequest;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.utils.CourierVisitState;

/**
 * Simple JavaBeen object that represents Courier Request.
 *
 * @author Pavlo Degtyaryev
 * @version 1.0
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "courier_requests")
@Slf4j
public class CourierRequest extends AbstractCourierRequest<CourierRequestEvent> implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5842400198817694887L;

    /**
     * Adds the history event.
     *
     * @param description      the {@link String}
     * @param author           the {@link User}
     * @param creationDateTime the creation {@link LocalDateTime}
     */
    public void addHistoryEvent(String description, LocalDateTime creationDateTime, User author) {
	log.info(" Adds the history event by user '{}' {} '{}'", author.getUsername(), creationDateTime, description);
	getHistoryEvents().add(new CourierRequestEvent(description, creationDateTime, author));
    }

    /**
     * Instantiates a new courier request from the courier visit.
     *
     * @param courierVisit the {@link CourierVisit}
     * @param author       the author
     */
    public CourierRequest(CourierVisit courierVisit, User author) {
	super(author, courierVisit.getFromPoint(), courierVisit.getToPoint());
	log.info("Instantiates a new courier request from the courier visit.");
	createHistoryEvents();
	setState(CourierVisitState.NEW);
	setCreationDate(courierVisit.getCreationDate());
	setDescription(courierVisit.getDescription());

	courierVisit.getHistoryEvents()
		.forEach(event -> addHistoryEvent(event.getDescription(), event.getCreationDate(), event.getAuthor()));
    }

    /**
     * Instantiates a new courier request.
     *
     * @param archivedCourierVisit the archived courier visit
     * @param author               the author
     */
    public CourierRequest(ArchivedCourierVisit archivedCourierVisit, User author) {
	super(author, archivedCourierVisit.getFromPoint(), archivedCourierVisit.getToPoint());
	log.info("Instantiates a new courier request from the courier visit.");
	setCreationDate(archivedCourierVisit.getCreationDate());
	setDescription(archivedCourierVisit.getDescription());
	setState(CourierVisitState.NEW);

	archivedCourierVisit.getHistoryEvents()
		.forEach(event -> addHistoryEvent(event.getDescription(), event.getCreationDate(), event.getAuthor()));
    }

    /**
     * Creates the history events.
     */
    @Override
    protected void createHistoryEvents() {
	setHistoryEvents(new HashSet<CourierRequestEvent>());
    }
}
