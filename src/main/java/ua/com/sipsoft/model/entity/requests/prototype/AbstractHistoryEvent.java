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
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import ua.com.sipsoft.model.entity.user.User;

/**
 * Simple JavaBeen object that represents standard system history event.
 *
 * @author Pavlo Degtyaryev
 */
@ToString(of = { "id", "creationDate", "version", "author", "description" })
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractHistoryEvent implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 9047035523222681687L;

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The version. */
    @Version
    @Column(columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version = 0L;

    /** The creation date. */
    @Column(nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    /** The description. */
    @Column(nullable = false, length = 1000)
    private String description = "";

    /** The author. */
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.PERSIST)
    private User author;

    /**
     * Instantiates a new abstract history event.
     *
     * @param description  the {@link String}
     * @param author       the {@link User}
     * @param creationDate the creation {@link LocalDateTime}
     */
    public AbstractHistoryEvent(@NonNull String description, @NonNull User author, LocalDateTime creationDate) {
	super();
	this.description = description;
	this.author = author;
	if (creationDate == null) {
	    creationDate = LocalDateTime.now();
	}
	this.creationDate = creationDate;
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
	if (!(obj instanceof AbstractHistoryEvent)) {
	    return false;
	}
	AbstractHistoryEvent other = (AbstractHistoryEvent) obj;
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
