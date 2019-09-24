package ua.com.sipsoft.model.entity.requests.draft;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisit;
import ua.com.sipsoft.model.entity.requests.issued.CourierVisit;
import ua.com.sipsoft.model.entity.requests.prototype.AbstractCourierRequest;
import ua.com.sipsoft.model.entity.user.User;

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
@Table(name = "courier_requests")
@Slf4j
public class CourierRequest extends AbstractCourierRequest implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5842400198817694887L;

    /** The history events. */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_courier_request_id")
//	@Fetch(FetchMode.SELECT)
    private Set<CourierRequestEvent> historyEvents = new HashSet<>();

    /**
     * Adds the history event.
     *
     * @param description      the {@link String}
     * @param author           the {@link User}
     * @param creationDateTime the creation {@link LocalDateTime}
     */
    public void addHistoryEvent(String description, User author, LocalDateTime creationDateTime) {
	log.info(" Adds the history event by user '{}' {} '{}'", author.getUsername(), creationDateTime, description);
	historyEvents.add(new CourierRequestEvent(description, creationDateTime, author));
    }

    /**
     * Instantiates a new courier request from the courier visit.
     *
     * @param courierVisit the {@link CourierVisit}
     */
    public CourierRequest(CourierVisit courierVisit, User author) {
	super(author, courierVisit.getFromPoint(), courierVisit.getToPoint());
	log.info("Instantiates a new courier request from the courier visit.");
	setCreationDate(courierVisit.getCreationDate());
	setDescription(courierVisit.getDescription());

	courierVisit.getHistoryEvents()
		.forEach(event -> addHistoryEvent(event.getDescription(), event.getAuthor(), event.getCreationDate()));
    }

    public CourierRequest(ArchivedCourierVisit archivedCourierVisit, User author) {
	super(author, archivedCourierVisit.getFromPoint(), archivedCourierVisit.getToPoint());
	log.info("Instantiates a new courier request from the courier visit.");
	setCreationDate(archivedCourierVisit.getCreationDate());
	setDescription(archivedCourierVisit.getDescription());

	archivedCourierVisit.getHistoryEvents()
		.forEach(event -> addHistoryEvent(event.getDescription(), event.getAuthor(), event.getCreationDate()));
    }
}
