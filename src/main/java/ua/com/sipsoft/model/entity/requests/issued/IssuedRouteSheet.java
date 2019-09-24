package ua.com.sipsoft.model.entity.requests.issued;

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

import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.model.entity.requests.draft.DraftRouteSheet;
import ua.com.sipsoft.model.entity.requests.prototype.AbstractRouteSheet;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.utils.CourierVisitState;

/**
 * Simple JavaBeen object that represents Draft Route sheet of
 * {@link CourierRequest}`s.
 *
 * @author Pavlo Degtyaryev
 * @version 1.0
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "issued_route_sheets")
public class IssuedRouteSheet extends AbstractRouteSheet implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6613770484943554708L;

    /** The requests. */
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<CourierVisit> requests = new HashSet<>();

    /** The history events. */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_sheet_id")
    private Set<IssuedRouteSheetEvent> historyEvents = new HashSet<>();

    /**
     * Adds the history event.
     *
     * @param description      the description
     * @param creationDateTime the creation date time
     * @param author           the author
     */
    public void addHistoryEvent(String description, LocalDateTime creationDateTime, User author) {
	historyEvents.add(new IssuedRouteSheetEvent(description, creationDateTime, author));
    }

    /**
     * Adds the courier visit.
     *
     * @param courierRequest the courier request
     * @param author         the author
     * @return true, if successful
     */
    private boolean addCourierVisit(CourierRequest courierRequest, User author) {
	if (courierRequest == null || author == null) {
	    return false;
	}
	CourierVisit courierVisit = new CourierVisit();
	courierVisit.setAuthor(courierRequest.getAuthor());
	courierVisit.setCreationDate(courierRequest.getCreationDate());
	courierVisit.setDescription(courierRequest.getDescription());
	courierVisit.setFromPoint(courierRequest.getFromPoint());
	courierVisit.setToPoint(courierRequest.getToPoint());

	courierRequest.getHistoryEvents()
		.forEach(event -> courierVisit.addHistoryEvent(event.getDescription(), event.getAuthor(),
			event.getCreationDate()));

	courierVisit.setState(CourierVisitState.RUNNING);
	courierVisit.addHistoryEvent("Виклик кур'єра було видано.", author, LocalDateTime.now());
	return requests.add(courierVisit);
    }

    /**
     * Instantiates a new issued route sheet.
     *
     * @param draftRouteSheet the draft route sheet
     * @param author          the author
     */
    public IssuedRouteSheet(@NonNull DraftRouteSheet draftRouteSheet, @NonNull User author) {
	super(author);
	log.info("Issued Route sheet was created from Draft Route sheet");
	setCreationDate(draftRouteSheet.getCreationDate());
	setDescription(draftRouteSheet.getDescription());

	draftRouteSheet.getRequests().forEach(request -> addCourierVisit(request, author));

	draftRouteSheet.getHistoryEvents()
		.forEach(event -> addHistoryEvent(event.getDescription(), event.getCreationDate(), event.getAuthor()));
    }

    /**
     * Checks for active visits.
     *
     * @return true, if successful
     */
    public boolean hasActiveVisits() {
	return requests
		.stream()
		.anyMatch(CourierVisit::isActive);
    }
}
