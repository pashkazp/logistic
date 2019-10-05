package ua.com.sipsoft.model.entity.requests.archive;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name = "archived_courier_visit_events")
@Slf4j
public class ArchivedCourierVisitEvent extends AbstractHistoryEvent implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7773164530475320823L;

    /**
     * Instantiates a new archived courier visit event.
     *
     * @param description      the description
     * @param author           the author
     * @param creationDateTime the creation date time
     */
    public ArchivedCourierVisitEvent(String description, User author, LocalDateTime creationDateTime) {
	super(description, author, creationDateTime);
	log.info("Instantiates history messsage event. Author '{}' date {} description '{}'", author.getUsername(),
		creationDateTime, description);
    }
}
