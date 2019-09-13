package ua.com.sipsoft.utils.history;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.sipsoft.model.entity.requests.issued.CourierVisit;
import ua.com.sipsoft.model.entity.requests.issued.CourierVisitEvent;
import ua.com.sipsoft.model.entity.requests.prototype.AbstractCourierRequest;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.utils.CourierVisitState;
import ua.com.sipsoft.utils.security.SecurityUtils;

/**
 * Class to take a snapshot of the current status of {@link CourierVisit} entity.
 *
 * @author Pavlo Degtyaryev
 */

/**
 * Instantiates a new courier visit snapshot.
 */
@NoArgsConstructor

/**
 * Instantiates a new courier visit snapshot.
 *
 * @param state the state
 */
@AllArgsConstructor

/**
 * Gets the state.
 *
 * @return the state
 */
@Getter

/**
 * Sets the state.
 *
 * @param state the new state
 */
@Setter

/**
 * To string.
 *
 * @return the java.lang. string
 */
@ToString
public class CourierVisitSnapshot extends CourierRequestSnapshot {

	/** The state. */
	private CourierVisitState state;

	/**
	 * Make snapshot from {@link CourierVisit}.
	 *
	 * @param <T>          the generic type
	 * @param courierVisit the courier visit
	 */
	public <T extends AbstractCourierRequest> CourierVisitSnapshot(T courierVisit) {
		super(courierVisit);
		if (courierVisit instanceof CourierVisit) {
			this.state = ((CourierVisit) courierVisit).getState();
		}
	}

	/**
	 * Compare "Old state" of current snapshot and "New state" of newCourierVisit
	 * and register changes as events in {@link CourierVisitEvent}.
	 *
	 * @param <T>             the generic type
	 * @param newCourierVisit the new courier visit
	 * @param author          the author
	 */
	@Override
	public <T extends AbstractCourierRequest> void fillHistoryChangesTo(T newCourierVisit, User author) {
		super.fillHistoryChangesTo(newCourierVisit, author);
		if (newCourierVisit instanceof CourierVisit && this.state != ((CourierVisit) newCourierVisit).getState()) {
			newCourierVisit.addHistoryEvent(
					"Стан віклику був змінений з: " + this.state.name() + " на: "
							+ ((CourierVisit) newCourierVisit).getState().name(),
					SecurityUtils.getUser(), LocalDateTime.now());
		}

	}
}
