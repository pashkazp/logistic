package ua.com.sipsoft.model.entity.requests.issued;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
@Table(name = "issued_route_sheet_events")
@Slf4j
public class IssuedRouteSheetEvent extends AbstractHistoryEvent implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7944618171896372529L;

    /**
     * Instantiates a new issued route sheet event.
     *
     * @param description      the description
     * @param creationDateTime the creation date time
     * @param author           the author
     */
    public IssuedRouteSheetEvent(@NonNull String description, LocalDateTime creationDateTime, @NonNull User author) {
	super(description, author, creationDateTime);
	log.info("Instantiates a new issued route sheet event");
    }
}
