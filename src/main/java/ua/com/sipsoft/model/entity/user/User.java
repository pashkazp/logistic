package ua.com.sipsoft.model.entity.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.sipsoft.utils.security.Role;

/**
 * Simple JavaBeen domain object that represents User.
 *
 * @author Pavlo Degtyaryev
 * @version 1.0
 */
@EqualsAndHashCode(of = { "id" })
@ToString(exclude = { "password" })
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8186778697067301448L;

    /** The id. */
    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    // @GeneratedValue(strategy = GenerationType.SEQUENCE)
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    /** The version. */
    @Version
    @Column(columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version = 0L;

    /** The username. */
    @Column(name = "username", unique = true, length = 32, nullable = false)
    private String username = "";

    /** The first name. */
    @Column(name = "firstname", length = 75, nullable = false)
    private String firstName = "";

    /** The last name. */
    @Column(name = "lastname", length = 75, nullable = false)
    private String lastName = "";

    /** The patronymic. */
    @Column(name = "patronymic", length = 75, nullable = false)
    private String patronymic = "";

    /** The password. */
    @Column(name = "password", length = 255, nullable = false)
    private String password = "";

    /** The email. */
    @Column(name = "email", unique = true, length = 100, nullable = false)
    private String email = "";

    /** The enabled. */
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    /** The verified. */
    @Column(name = "verified", nullable = false)
    private Boolean verified = false;

    /** The roles. */
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "user_role", nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    @Fetch(FetchMode.JOIN)
    private Set<Role> roles = new HashSet<>();

    /**
     * Sets the roles.
     *
     * @param roles the new roles
     */
    public void setRoles(Iterable<Role> roles) {
	this.roles.clear();
	for (Role role : roles) {
	    this.roles.add(role);
	}
    }

    /**
     * Contains any role.
     *
     * @param roles the roles
     * @return true, if successful
     */
    public boolean containsAnyRole(Collection<Role> roles) {
	if (roles != null && this.roles != null) {
	    for (Role role : roles) {
		if (this.roles.contains(role))
		    return true;
	    }
	}
	return false;
    }

    /**
     * Instantiates a new user.
     *
     * @param that the that
     */
    public User(User that) {
	this.id = that.id;
	this.version = that.version;
	this.username = that.username;
	this.firstName = that.firstName;
	this.lastName = that.lastName;
	this.patronymic = that.patronymic;
	this.email = that.email;
	this.enabled = that.enabled;
	this.verified = that.verified;
	this.password = that.password;
	this.roles = new HashSet<>(that.roles);
    }

    /**
     * Gets the highest role. Where Admin - highest and Registered - lowest. If Role
     * is absent return Registered
     *
     * @return the highest {@link Role}
     */
    public Role getHighesRole() {
	for (Role role : Role.values()) {
	    if (roles.contains(role)) {
		return role;
	    }
	}
	return Role.ROLE_REGISTERED;
    }

}
