package ua.com.sipsoft.model.entity.requests.issued;

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
@NoArgsConstructor
@Entity
@Table(name = "courier_visits")
@Slf4j
public class CourierVisit extends AbstractCourierRequest<CourierVisitEvent> implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2179992968848667614L;

    /**
     * Adds the history event.
     *
     * @param description      the description
     * @param creationDateTime the creation date time
     * @param author           the author
     */
    @Override
    public void addHistoryEvent(String description, LocalDateTime creationDateTime, User author) {
	log.info(" Adds the history event by user '{}' {} '{}'", author.getUsername(), creationDateTime, description);
	getHistoryEvents().add(new CourierVisitEvent(description, author,
		creationDateTime));
    }

    /**
     * Instantiates a new courier visit.
     *
     * @param request the request
     */
    public CourierVisit(CourierRequest request) {
	super(request.getAuthor(), request.getFromPoint(), request.getToPoint());
	createHistoryEvents();
	setState(CourierVisitState.RUNNING);
	setCreationDate(request.getCreationDate());
	setDescription(request.getDescription());
	request.getHistoryEvents()
		.forEach(event -> addHistoryEvent(event.getDescription(), event.getCreationDate(), event.getAuthor()));
    }

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    public boolean isActive() {
	return CourierVisitState.ACTIVESET.contains(getState());
    }

    /**
     * Creates the history events.
     */
    @Override
    protected void createHistoryEvents() {
	setHistoryEvents(new HashSet<CourierVisitEvent>());
    }
}
