package ua.com.sipsoft.services.requests.draft;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.defaultString;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.model.entity.requests.draft.DraftRouteSheet;
import ua.com.sipsoft.services.utils.EntityFilter;

/**
 * The Class CourierRequestServiceImpl.
 *
 * @author Pavlo Degtyaryev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DraftRouteSheetFilter implements EntityFilter<DraftRouteSheet> {

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
	return String.format("DraftRouteSheetFilter [description=\"%s\"]", description);
    }

    /**
     * Checks if is pass.
     *
     * @param entity the entity
     * @return true, if is pass
     */
    @Override
    public boolean isPass(DraftRouteSheet entity) {
	if (entity == null) {
	    return false;
	}
	if (!containsIgnoreCase(entity.getDescription(), defaultString(description))) {
	    return false;
	}
	return true;
    }

}
