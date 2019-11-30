package ua.com.sipsoft.services.requests.issued;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.defaultString;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.model.entity.requests.issued.IssuedRouteSheet;
import ua.com.sipsoft.services.utils.EntityFilter;

/**
 * The Interface IssuedRouteSheetFilter.
 * 
 * @author Pavlo Degtyaryev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class IssuedRouteSheetFilter implements EntityFilter<IssuedRouteSheet> {

    /** The description. */
    @Builder.Default
    private String description = null;

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
	return String.format("IssuedRouteSheetFilter [description=\"%s\"]", description);
    }

    /**
     * Checks if is pass.
     *
     * @param entity the entity
     * @return true, if is pass
     */
    @Override
    public boolean isPass(IssuedRouteSheet entity) {
	if (entity == null) {
	    return false;
	}
	if (!containsIgnoreCase(entity.getDescription(), defaultString(description))) {
	    return false;
	}
	return true;
    }

}
