package ua.com.sipsoft.model.entity.requests.prototype;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import ua.com.sipsoft.model.entity.common.FacilityAddress;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.utils.CourierVisitState;

/**
 * Simple JavaBeen object that represents Courier Request.
 *
 * @author Pavlo Degtyaryev
 * @version 1.0
 */
@ToString(of = { "id", "creationDate", "version", "author", "fromPoint", "toPoint", "description" })

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractCourierRequest<T extends AbstractHistoryEvent> implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7414770099090952575L;

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The version. */
    @Version
    @Column(columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version = 0L;

    /** The author. */
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.PERSIST)
    private User author;

    /** The creation date. */
    @Column(nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    /** The from point. */
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.PERSIST)
    // @Column(nullable = false)
    private FacilityAddress fromPoint;

    /** The to point. */
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.PERSIST)
    // @Column(nullable = false)
    private FacilityAddress toPoint;

    /** The description. */
    @Column(nullable = false, length = 100)
    private String description = "";

    /** The history events. */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_courier_req_id")
    private Set<T> historyEvents;

    /** The state. */
    @Enumerated(EnumType.STRING)
    private CourierVisitState state = CourierVisitState.NEW;

    /**
     * Instantiates a new abstract courier request.
     *
     * @param author    the {@link User}
     * @param fromPoint the from {@link FacilityAddress}
     * @param toPoint   the to {@link FacilityAddress}
     */
    public AbstractCourierRequest(User author, FacilityAddress fromPoint, FacilityAddress toPoint) {
	super();
	this.author = author;
	this.fromPoint = fromPoint;
	this.toPoint = toPoint;
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
	if (!(obj instanceof AbstractCourierRequest)) {
	    return false;
	}
	AbstractCourierRequest<?> other = (AbstractCourierRequest<?>) obj;
	if (this.id == null && other.id == null) {
	    return false;
	}
	return Objects.equals(id, other.id);
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

}
