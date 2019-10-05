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
@Table(name = "archived_route_sheet_events")
@Slf4j
public class ArchivedRouteSheetEvent extends AbstractHistoryEvent implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3401875132999941972L;

    /**
     * Instantiates a new archived route sheet event.
     *
     * @param description      the description
     * @param creationDateTime the creation date time
     * @param author           the author
     */
    public ArchivedRouteSheetEvent(String description, LocalDateTime creationDateTime, User author) {
	super(description, author, creationDateTime);
	log.info("Instantiates a new archived route sheet event by author '{}' {} '{}'", author.getUsername(),
		creationDateTime, description);
    }
}
