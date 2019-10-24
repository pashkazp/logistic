package ua.com.sipsoft.model.entity.requests.issued;

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
public class CourierVisit extends AbstractCourierRequest implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2179992968848667614L;

    /** The state. */
    @Enumerated(EnumType.STRING)
    private CourierVisitState state = CourierVisitState.RUNNING;

    /** The history events. */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_courier_visit_id")
    private Set<CourierVisitEvent> historyEvents = new HashSet<>();

    /**
     * Adds the history event.
     *
     * @param description      the description
     * @param author           the author
     * @param creationDateTime the creation date time
     */
    @Override
    public void addHistoryEvent(String description, User author, LocalDateTime creationDateTime) {
	historyEvents.add(new CourierVisitEvent(description, author, creationDateTime));
    }

    /**
     * Instantiates a new courier visit.
     *
     * @param request the request
     */
    public CourierVisit(CourierRequest request) {
	super(request.getAuthor(), request.getFromPoint(), request.getToPoint());
	setCreationDate(request.getCreationDate());
	setDescription(request.getDescription());
	request.getHistoryEvents()
		.forEach(event -> addHistoryEvent(event.getDescription(), event.getAuthor(), event.getCreationDate()));
    }

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    public boolean isActive() {
	return CourierVisitState.ACTIVESET.contains(state);
    }
}
