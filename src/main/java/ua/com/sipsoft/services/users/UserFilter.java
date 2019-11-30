package ua.com.sipsoft.services.users;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.Collection;

import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.utils.EntityFilter;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Interface UserFilter.
 * 
 * @author Pavlo Degtyaryev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserFilter implements EntityFilter<User> {

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

    /** The roles. */
    @Builder.Default
    private Collection<Role> roles = null;

    @Override
    public String toString() {
	return String.format(
		"UserFilter [id=%s, username=\"%s\", firstName=\"%s\", lastName=\"%s\", patronymic=\"%s\", email=\"%s\", roles=%s]",
		id, username, firstName, lastName, patronymic, email, roles);
    }

    /**
     * Checks if is pass.
     *
     * @param entity the entity
     * @return true, if is pass
     */
    @Override
    public boolean isPass(User entity) {
	if (entity == null) {
	    return false;
	}
	if (id != null && !id.equals(entity.getId())) {
	    return false;
	}
	if (!containsIgnoreCase(entity.getUsername(), defaultString(username))) {
	    return false;
	}
	if (!containsIgnoreCase(entity.getFirstName(), defaultString(firstName))) {
	    return false;
	}
	if (!containsIgnoreCase(entity.getLastName(), defaultString(lastName))) {
	    return false;
	}
	if (!containsIgnoreCase(entity.getPatronymic(), defaultString(patronymic))) {
	    return false;
	}
	if (!containsIgnoreCase(entity.getEmail(), defaultString(email))) {
	    return false;
	}
	if (roles != null && !CollectionUtils.containsAny(roles, entity.getRoles())) {
	    return false;
	}
	return true;
    }

}
