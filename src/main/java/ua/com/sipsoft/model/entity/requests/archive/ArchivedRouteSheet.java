package ua.com.sipsoft.model.entity.requests.archive;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.model.entity.requests.draft.DraftRouteSheet;
import ua.com.sipsoft.model.entity.requests.issued.CourierVisit;
import ua.com.sipsoft.model.entity.requests.issued.IssuedRouteSheet;
import ua.com.sipsoft.model.entity.requests.issued.IssuedRouteSheetEvent;
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
@Table(name = "archived_route_sheets")
@Slf4j
public class ArchivedRouteSheet extends AbstractRouteSheet<ArchivedRouteSheetEvent> implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8955016987705988470L;

    /** The requests. */
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ArchivedCourierVisit> requests = new HashSet<>();

    /**
     * Adds the history event.
     *
     * @param description      the description
     * @param creationDateTime the creation date time
     * @param author           the author
     */
    @Override
    public void addHistoryEvent(String description, LocalDateTime creationDateTime, User author) {
	log.info("Add history event by author '{}' {} '{}'", author.getUsername(), creationDateTime, description);
	getHistoryEvents().add(new ArchivedRouteSheetEvent(description, creationDateTime, author));
    }

    /**
     * Instantiates a new archived route sheet.
     *
     * @param draftRouteSheet the draft route sheet
     */
    public ArchivedRouteSheet(@NonNull DraftRouteSheet draftRouteSheet) {
	log.info("Instantiates a new archived route sheet from DraftRouteSheet");
	setAuthor(draftRouteSheet.getAuthor());
	setDescription(draftRouteSheet.getDescription());

	draftRouteSheet.getHistoryEvents()
		.forEach(event -> addHistoryEvent(event.getDescription(), event.getCreationDate(), event.getAuthor()));
    }

    /**
     * Instantiates a new archived route sheet.
     *
     * @param author the author
     */
    public ArchivedRouteSheet(@NonNull User author) {
	super(author);
	log.info("Instantiates Archived Sheet by author '{}'", author.getUsername());
    }

    /**
     * Instantiates a new archived route sheet.
     *
     * @param issuedRouteSheet the issued route sheet
     */
    public ArchivedRouteSheet(@NonNull IssuedRouteSheet issuedRouteSheet) {
	log.info("Instantiates Archived Sheet from Issued Sheet");
	setAuthor(issuedRouteSheet.getAuthor());
	setDescription(issuedRouteSheet.getDescription());
	for (IssuedRouteSheetEvent event : issuedRouteSheet.getHistoryEvents()) {
	    addHistoryEvent(event.getDescription(), event.getCreationDate(), event.getAuthor());
	}
	for (CourierVisit request : issuedRouteSheet.getRequests()) {
	    requests.add(new ArchivedCourierVisit(request));
	}

    }

    /**
     * Creates the history events.
     */
    @Override
    protected void createHistoryEvents() {
	setHistoryEvents(new HashSet<ArchivedRouteSheetEvent>());
    }

}
