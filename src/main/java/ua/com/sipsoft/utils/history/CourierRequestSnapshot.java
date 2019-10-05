package ua.com.sipsoft.utils.history;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.sipsoft.model.entity.common.Facility;
import ua.com.sipsoft.model.entity.common.FacilityAddress;
import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.model.entity.requests.draft.CourierRequestEvent;
import ua.com.sipsoft.model.entity.requests.prototype.AbstractCourierRequest;
import ua.com.sipsoft.model.entity.user.User;

/**
 * Class to take a snapshot of the current status of {@link CourierRequest}
 * entity.
 *
 * @author Pavlo Degtyaryev
 */

/**
 * Instantiates a new courier request snapshot.
 */
@NoArgsConstructor

/**
 * Instantiates a new courier request snapshot.
 *
 * @param fromFacility the from facility
 * @param fromPoint    the from point
 * @param toFacility   the to facility
 * @param toPoint      the to point
 * @param description  the description
 */
@AllArgsConstructor

/**
 * Gets the description.
 *
 * @return the description
 */
@Getter

/**
 * Sets the description.
 *
 * @param description the new description
 */
@Setter

/**
 * To string.
 *
 * @return the java.lang. string
 */
@ToString

/**
 * Hash code.
 *
 * @return the int
 */
@EqualsAndHashCode
public class CourierRequestSnapshot {

    /** The from facility. */
    private Facility fromFacility;

    /** The from point. */
    private FacilityAddress fromPoint;

    /** The to facility. */
    private Facility toFacility;

    /** The to point. */
    private FacilityAddress toPoint;

    /** The description. */
    private String description;

    /**
     * Make snapshot from {@link CourierRequest}.
     *
     * @param <T>            the generic type
     * @param courierRequest the courier request
     */
    public <T extends AbstractCourierRequest> CourierRequestSnapshot(T courierRequest) {
	if (courierRequest == null)
	    return;
	this.description = courierRequest.getDescription();
	this.fromPoint = courierRequest.getFromPoint();
	if (this.fromPoint != null) {
	    this.fromFacility = this.fromPoint.getFacility();
	}
	this.toPoint = courierRequest.getToPoint();
	if (this.toPoint != null) {
	    this.toFacility = this.toPoint.getFacility();
	}
    }

    /**
     * Compare "Old state" of current snapshot and "New state" of newCourierVisit
     * and register changes as events in {@link CourierRequestEvent}.
     *
     * @param <T>        the generic type
     * @param requestNew the request new
     * @param author     the author
     */
    public <T extends AbstractCourierRequest> void fillHistoryChangesTo(T requestNew, User author) {
	if (requestNew == null
		|| this.getToPoint() == null
		|| this.getFromPoint() == null
		|| this.getToPoint().getFacility() == null
		|| this.getFromPoint().getFacility() == null
		|| this.getDescription() == null
		|| requestNew.getToPoint() == null
		|| requestNew.getFromPoint() == null
		|| requestNew.getToPoint().getFacility() == null
		|| requestNew.getFromPoint().getFacility() == null
		|| requestNew.getDescription() == null)
	    return;
	StringBuilder stringBuilder = new StringBuilder();
	String stringTo = "\" на: \"";
	String stringEnd = "\"";
	if (this.getFromPoint().getFacility().getId() != requestNew.getFromPoint().getFacility().getId()) {
	    stringBuilder.append("Установа відправник була змінена з: \"");
	    stringBuilder.append(this.getFromPoint().getFacility().getName());
	    stringBuilder.append(stringTo);
	    stringBuilder.append(requestNew.getFromPoint().getFacility().getName());
	    stringBuilder.append(stringEnd);
	    requestNew.addHistoryEvent(stringBuilder.toString(), author, LocalDateTime.now());
	}
	if (this.getToPoint().getFacility().getId() != requestNew.getToPoint().getFacility().getId()) {
	    stringBuilder.setLength(0);
	    stringBuilder.append("Установа утримувіч була змінена з: \"");
	    stringBuilder.append(this.getToPoint().getFacility().getName());
	    stringBuilder.append(stringTo);
	    stringBuilder.append(requestNew.getToPoint().getFacility().getName());
	    stringBuilder.append(stringEnd);
	    requestNew.addHistoryEvent(stringBuilder.toString(), author, LocalDateTime.now());
	}
	if (this.getFromPoint().getId() != requestNew.getFromPoint().getId()) {
	    stringBuilder.setLength(0);
	    stringBuilder.append("Адреса відправника була змінена з: \"");
	    stringBuilder.append(this.getFromPoint().getAddress());
	    stringBuilder.append(stringTo);
	    stringBuilder.append(requestNew.getFromPoint().getAddress());
	    stringBuilder.append(stringEnd);
	    requestNew.addHistoryEvent(stringBuilder.toString(), author, LocalDateTime.now());
	}
	if (this.getToPoint().getId() != requestNew.getToPoint().getId()) {
	    stringBuilder.setLength(0);
	    stringBuilder.append("Адреса утримувача була змінена з: \"");
	    stringBuilder.append(this.getToPoint().getAddress());
	    stringBuilder.append(stringTo);
	    stringBuilder.append(requestNew.getToPoint().getAddress());
	    stringBuilder.append(stringEnd);
	    requestNew.addHistoryEvent(stringBuilder.toString(), author, LocalDateTime.now());
	}
	if (!this.getDescription().equals(requestNew.getDescription())) {
	    stringBuilder.setLength(0);
	    stringBuilder.append("Припис було змінено з \"");
	    stringBuilder.append(this.getDescription());
	    stringBuilder.append(stringTo);
	    stringBuilder.append(requestNew.getDescription());
	    stringBuilder.append(stringEnd);
	    requestNew.addHistoryEvent(stringBuilder.toString(), author, LocalDateTime.now());
	}
    }

}
