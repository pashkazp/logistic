package ua.com.sipsoft.model.entity.requests.issued;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.model.entity.requests.prototype.AbstractHistoryEvent;
import ua.com.sipsoft.model.entity.user.User;

/**
 * Simple JavaBeen object that represents standard system history event.
 *
 * @author Pavlo Degtyaryev
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "courier_visit_events")
public class CourierVisitEvent extends AbstractHistoryEvent implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5659649729080279447L;

    /**
     * Instantiates a new courier visit event.
     *
     * @param description      the description
     * @param author           the author
     * @param creationDateTime the creation date time
     */
    public CourierVisitEvent(String description, User author, LocalDateTime creationDateTime) {
	super(description, author, creationDateTime);
    }
}
