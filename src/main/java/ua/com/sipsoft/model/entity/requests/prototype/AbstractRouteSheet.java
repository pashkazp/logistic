
package ua.com.sipsoft.model.entity.requests.prototype;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.model.entity.user.User;

/**
 * Simple JavaBeen object that represents Draft Route sheet of
 * {@link CourierRequest}`s.
 *
 * @author Pavlo Degtyaryev
 * @version 1.0
 * @param <T> the generic type
 */

@ToString(of = { "id", "creationDate", "version", "author", "description" })
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractRouteSheet<T extends AbstractHistoryEvent> implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3976627474731979476L;

    /** The id. */
    @Id
    @Column(name = "sheet_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The version. */
    @Version
    @Column(columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version = 0L;

    /** The author. */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User author;

    /** The creation date. */
    @Column(nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    /** The description. */
    @Column(nullable = false, length = 100)
    private String description = "";

    /** The history events. */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_sheet_id")
    private Set<T> historyEvents;

    /**
     * Instantiates a new abstract route sheet.
     *
     * @param author the {@link User}
     */
    public AbstractRouteSheet(User author) {
	super();
	this.author = author;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
	return Objects.hash(id);
    }

    /**
     * Equals.
     *
     * @param obj the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof AbstractRouteSheet)) {
	    return false;
	}
	AbstractRouteSheet<?> other = (AbstractRouteSheet<?>) obj;
	if (this.id == null && other.id == null) {
	    return false;
	}
	return Objects.equals(id, other.id);
    }

    /**
     * Adds the history event.
     *
     * @param description the {@link String}
     * @param now         the {@link LocalDateTime}
     * @param author      the {@link User}
     */
    public abstract void addHistoryEvent(String description, LocalDateTime now, User author);

    /**
     * Creates the history events.
     */
    protected abstract void createHistoryEvents();

    /**
     * Gets the history events.
     *
     * @return the history events
     */
    public Set<T> getHistoryEvents() {
	if (historyEvents == null) {
	    createHistoryEvents();
	}
	return historyEvents;
    }
}
