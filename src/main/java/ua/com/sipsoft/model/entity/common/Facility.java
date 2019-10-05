package ua.com.sipsoft.model.entity.common;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.user.User;

/**
 * Simple JavaBean domain object that represents {@link User}'s facility.
 *
 * @author Pavlo Degtyaryev
 * @version 1.0
 */

@ToString(of = { "id", "name", "version", "facilityAddresses" })
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "facilities")
@Slf4j
public class Facility implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1776449468120725349L;

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "facility_id")
    private Long id;

    /** The version. */
    @Version
    @Column(columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version = 0L;

    /** The name. */
    @Column(name = "name", nullable = false, length = 200, unique = true)
    private String name = "";

    /** The users. */
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "users_facilities", //
	    joinColumns = @JoinColumn(name = "facility_id"), //
	    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    /** The facility addresses. */
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<FacilityAddress> facilityAddresses = new HashSet<>();

    /**
     * Adds the {@link User}.
     *
     * @param user the user
     */
    public void addUser(User user) {
	log.info("Add User '{}'", user);
	if (user != null) {
	    users.add(user);
	}
    }

    /**
     * Removes the {@link User}.
     *
     * @param user the user
     */
    public void removeUser(User user) {
	log.info("Remove User '{}'", user);
	if (user != null) {
	    users.remove(user);
	}
    }

    /**
     * Adds the facility address.
     *
     * @param facilityAddress the facility address
     */
    public void addFacilityAddress(FacilityAddress facilityAddress) {
	log.info("Try to add facility addresses");
	if (facilityAddress != null && facilityAddresses != null) {
	    facilityAddresses.add(facilityAddress);
	    facilityAddress.setFacility(this);
	}
    }

    /**
     * Del facility address.
     *
     * @param facilityAddress the facility address
     */
    public void delFacilityAddress(FacilityAddress facilityAddress) {
	log.info("Try to remove fasility addresses");
	if (facilityAddress != null && facilityAddresses != null) {
	    facilityAddresses.remove(facilityAddress);
	    facilityAddress.setFacility(null);
	}
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
	if (!(obj instanceof Facility)) {
	    return false;
	}
	Facility other = (Facility) obj;
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
