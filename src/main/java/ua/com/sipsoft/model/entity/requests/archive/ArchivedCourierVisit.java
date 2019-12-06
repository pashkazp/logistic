package ua.com.sipsoft.model.entity.requests.archive;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
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
@Table(name = "archived_courier_visits")
@Slf4j
public class ArchivedCourierVisit extends AbstractCourierRequest<ArchivedCourierVisitEvent> implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 933190931148752876L;

    /**
     * Instantiates a new archived courier visit.
     *
     * @param request the request
     */
    public ArchivedCourierVisit(CourierRequest request) {
	super(request.getAuthor(), request.getFromPoint(), request.getToPoint());
	log.info("Instantiates archived visit from courier request");
	setCreationDate(request.getCreationDate());
	setDescription(request.getDescription());
	request.getHistoryEvents()
		.forEach(event -> addHistoryEvent(event.getDescription(), event.getCreationDate(), event.getAuthor()));
    }

    /**
     * Instantiates a new archived courier visit.
     *
     * @param request the request
     */
    public ArchivedCourierVisit(CourierVisit request) {
	super(request.getAuthor(), request.getFromPoint(), request.getToPoint());
	log.info("Instantiates archived visit from courier visit '{}'", request);
	createHistoryEvents();
	setState(CourierVisitState.COMPLETED);
	setCreationDate(request.getCreationDate());
	setDescription(request.getDescription());
	setState(request.getState());
	request.getHistoryEvents()
		.forEach(event -> addHistoryEvent(event.getDescription(), event.getCreationDate(), event.getAuthor()));
    }

    /**
     * Adds the history event.
     *
     * @param description      the description
     * @param creationDateTime the creation date time
     * @param author           the author
     */
    @Override
    public void addHistoryEvent(String description, LocalDateTime creationDateTime, User author) {
	log.info("Add history messsage event. Author '{}' date {} description '{}'", author.getUsername(),
		creationDateTime, description);
	getHistoryEvents().add(new ArchivedCourierVisitEvent(description, author, creationDateTime));
    }

    /**
     * Creates the history events.
     */
    @Override
    protected void createHistoryEvents() {
	setHistoryEvents(new HashSet<ArchivedCourierVisitEvent>());
    }
}
