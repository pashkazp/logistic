package ua.com.sipsoft.services.users;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.utils.security.Role;

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Builder

/**
 * Instantiates a new user filter.
 */
@NoArgsConstructor

/**
 * Instantiates a new user filter.
 *
 * @param id         the id
 * @param username   the username
 * @param firstName  the first name
 * @param lastName   the last name
 * @param patronymic the patronymic
 * @param email      the email
 * @param facilityId the facility id
 * @param roles      the roles
 */
@AllArgsConstructor

/**
 * Gets the roles.
 *
 * @return the roles
 */
@Getter

/**
 * Sets the roles.
 *
 * @param roles the new roles
 */
@Setter

/**
 * Hash code.
 *
 * @return the int
 */
@EqualsAndHashCode
public class UserFilter {

	/** The id. */
	@Builder.Default
	private Long id = null;

	/** The username. */
	@Builder.Default
	private String username = null;

	/** The first name. */
	@Builder.Default
	private String firstName = null;

	/** The last name. */
	@Builder.Default
	private String lastName = null;

	/** The patronymic. */
	@Builder.Default
	private String patronymic = null;

	/** The email. */
	@Builder.Default
	private String email = null;

	/** The facility id. */
	@Builder.Default
	private Long facilityId = null;

	/** The roles. */
	@Builder.Default
	private Collection<Role> roles = null;

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "UserFilter [id=" + id + ", username=\"" + username + "\", firstName=\"" + firstName + "\", lastName=\""
				+ lastName + "\", patronymic=\"" + patronymic + "\", email=\"" + email + "\", facilityId=\""
				+ facilityId + "\", roles=" + roles + "]";
	}

}
