package ua.com.sipsoft.services.requests.draft;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.defaultString;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.utils.EntityFilter;

/**
 * The Class CourierRequestFilter.
 *
 * @author Pavlo Degtyaryev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CourierRequestFilter implements EntityFilter<CourierRequest> {

    /** The description. */
    @Builder.Default
    private String description = null;

    /** The author. */
    @Builder.Default
    private User author = null;

    /**
     * Checks if is pass.
     *
     * @param entity the entity
     * @return true, if is pass
     */
    @Override
    public boolean isPass(CourierRequest entity) {
	if (entity == null) {
	    return false;
	}
	if (!containsIgnoreCase(entity.getDescription(), defaultString(description))) {
	    return false;
	}
	if (author != null && entity.getAuthor().getId() != this.author.getId()) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return String.format("CourierRequestFilter [description=\"%s\", author=%s]", description, author);
    }

}
