package ua.com.sipsoft.services.common;

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
import ua.com.sipsoft.model.entity.common.Facility;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.utils.EntityFilter;

/**
 * The Class FacilitiesFilter.
 *
 * @author Pavlo Degtyaryev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class FacilitiesFilter implements EntityFilter<Facility> {

    /** The name. */
    @Builder.Default
    private String name = null;

    /** The users. */
    @Builder.Default
    private Collection<User> users = null;

    @Override
    public String toString() {
	return String.format("FacilitiesFilter [name=\"%s\", users=%s]", name, users);
    }

    /**
     * Checks if is pass.
     *
     * @param entity the entity
     * @return true, if is pass
     */
    @Override
    public boolean isPass(Facility entity) {
	if (entity == null) {
	    return false;
	}
	if (users != null && !CollectionUtils.containsAny(users, entity.getUsers())) {
	    return false;
	}
	if (!containsIgnoreCase(entity.getName(), defaultString(name))) {
	    return false;
	}
	return true;
    }

}
