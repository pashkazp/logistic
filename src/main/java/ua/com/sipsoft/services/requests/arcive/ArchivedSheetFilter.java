package ua.com.sipsoft.services.requests.arcive;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.defaultString;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedRouteSheet;
import ua.com.sipsoft.services.utils.EntityFilter;

/**
 * The Class ArchivedSheetFilter.
 *
 * @author Pavlo Degtyaryev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ArchivedSheetFilter implements EntityFilter<ArchivedRouteSheet> {

    /** The description. */
    @Builder.Default
    private String description = null;

    /** The sheet id. */
    @Builder.Default
    private Long sheetId = null;

    @Override
    public String toString() {
	return String.format("ArchivedSheetFilter [description=\"%s\", sheetId=%s]", description, sheetId);
    }

    /**
     * Checks if is pass.
     *
     * @param entity the entity
     * @return true, if is pass
     */
    @Override
    public boolean isPass(ArchivedRouteSheet entity) {
	if (entity == null) {
	    return false;
	}
	if (!containsIgnoreCase(entity.getDescription(), defaultString(description))) {
	    return false;
	}
	return true;
    }

}
