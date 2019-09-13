package ua.com.sipsoft.model.entity.requests.prototype;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.sipsoft.model.entity.common.FacilityAddress;
import ua.com.sipsoft.model.entity.user.User;

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
public abstract class AbstractCourierRequest implements Serializable {

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
	 * @param author      the {@link User}
	 * @param now         the {@link LocalDateTime}
	 */
	public abstract void addHistoryEvent(String description, User author, LocalDateTime now);

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
		AbstractCourierRequest other = (AbstractCourierRequest) obj;
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
