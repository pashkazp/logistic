package ua.com.sipsoft.model.entity.requests.draft;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.prototype.AbstractRouteSheet;
import ua.com.sipsoft.model.entity.user.User;

/**
 * Simple JavaBeen object that represents Draft Route sheet of
 * {@link CourierRequest}`s.
 *
 * @author Pavlo Degtyaryev
 * @version 1.0
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "draft_route_sheets")
@Slf4j
public class DraftRouteSheet extends AbstractRouteSheet<DraftRouteSheetEvent> implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6970131851588007663L;

    /** The requests. */
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "draft_route_sheets_requests", //
	    joinColumns = @JoinColumn(name = "draft_route_sheets_id"), //
	    inverseJoinColumns = @JoinColumn(name = "courier_request_id"))
    private Set<CourierRequest> requests = new HashSet<>();

    /**
     * Adds the history event.
     *
     * @param description the description
     * @param now         the now
     * @param author      the author
     */
    @Override
    public void addHistoryEvent(String description, LocalDateTime now, User author) {
	log.info(" Adds the history event by user '{}' {} '{}'", author.getUsername(), now, description);
	getHistoryEvents().add(new DraftRouteSheetEvent(description, now, author));
    }

    /**
     * Creates the history events.
     */
    @Override
    protected void createHistoryEvents() {
	setHistoryEvents(new HashSet<DraftRouteSheetEvent>());
    }
}
