package ua.com.sipsoft.model.entity.requests.draft;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
@Table(name = "courier_request_events")
public class CourierRequestEvent extends AbstractHistoryEvent implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6036932028289544332L;

    /**
     * Instantiates a new courier request event.
     *
     * @param description      the {@linkplain String}
     * @param creationDateTime the {@linkplain LocalDateTime}
     * @param author           the {@linkplain User}
     */
    public CourierRequestEvent(@NonNull String description, LocalDateTime creationDateTime, @NonNull User author) {
	super(description, author, creationDateTime);
    }
}
