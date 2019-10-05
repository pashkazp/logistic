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
@Table(name = "draft_route_sheet_events")
public class DraftRouteSheetEvent extends AbstractHistoryEvent implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6690214330269697310L;

    /**
     * Instantiates a new draft route sheet event.
     *
     * @param description      the description
     * @param creationDateTime the creation date time
     * @param author           the author
     */
    public DraftRouteSheetEvent(@NonNull String description, LocalDateTime creationDateTime, @NonNull User author) {
	super(description, author, creationDateTime);
    }
}
