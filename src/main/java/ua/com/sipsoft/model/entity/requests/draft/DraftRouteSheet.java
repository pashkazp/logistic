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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class DraftRouteSheet extends AbstractRouteSheet implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6970131851588007663L;

	/** The requests. */
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "draft_route_sheets_requests", //
			joinColumns = @JoinColumn(name = "draft_route_sheets_id"), //
			inverseJoinColumns = @JoinColumn(name = "courier_request_id"))
	private Set<CourierRequest> requests = new HashSet<>();

	/** The history events. */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "fk_sheet_id")
	private Set<DraftRouteSheetEvent> historyEvents = new HashSet<>();

	/**
	 * Adds the event {@link DraftRouteSheetEvent}.
	 *
	 * @param description      the {@link String}
	 * @param creationDateTime the creation {@link LocalDateTime}
	 * @param author           the {@link User}
	 */
	public void addHistoryEvent(String description, LocalDateTime creationDateTime, User author) {
		historyEvents.add(new DraftRouteSheetEvent(description, creationDateTime, author));
	}
}
