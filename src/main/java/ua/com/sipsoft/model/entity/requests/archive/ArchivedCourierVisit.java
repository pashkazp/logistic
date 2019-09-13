package ua.com.sipsoft.model.entity.requests.archive;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@NoArgsConstructor
@Entity
@Table(name = "archived_courier_visits")
@Slf4j
public class ArchivedCourierVisit extends AbstractCourierRequest implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 933190931148752876L;

	/** The state. */
	@Enumerated(EnumType.STRING)
	private CourierVisitState state = CourierVisitState.RUNNING;

	/** The history events. */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "fk_archive_courier_visit_id")
	private Set<ArchivedCourierVisitEvent> historyEvents = new HashSet<>();

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
				.forEach(event -> addHistoryEvent(event.getDescription(), event.getAuthor(), event.getCreationDate()));
	}

	/**
	 * Instantiates a new archived courier visit.
	 *
	 * @param request the request
	 */
	public ArchivedCourierVisit(CourierVisit request) {
		super(request.getAuthor(), request.getFromPoint(), request.getToPoint());
		log.info("Instantiates archived visit from courier visit");
		setCreationDate(request.getCreationDate());
		setDescription(request.getDescription());
		setState(request.getState());
		request.getHistoryEvents()
				.forEach(event -> addHistoryEvent(event.getDescription(), event.getAuthor(), event.getCreationDate()));
	}

	/**
	 * Adds the history event.
	 *
	 * @param description      the description
	 * @param author           the author
	 * @param creationDateTime the creation date time
	 */
	@Override
	public void addHistoryEvent(String description, User author, LocalDateTime creationDateTime) {
		log.info("Add history messsage event. Author '{}' date {} description '{}'", author.getUsername(),
				creationDateTime, description);
		historyEvents.add(new ArchivedCourierVisitEvent(description, author, creationDateTime));
	}
}
