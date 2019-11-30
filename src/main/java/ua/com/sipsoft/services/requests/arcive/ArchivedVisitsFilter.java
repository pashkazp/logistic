package ua.com.sipsoft.services.requests.arcive;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.Set;

import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisit;
import ua.com.sipsoft.services.utils.EntityFilter;
import ua.com.sipsoft.utils.CourierVisitState;

/**
 * The Interface ArchivedVisitsFilter.
 *
 * @author Pavlo Degtyaryev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode

public class ArchivedVisitsFilter implements EntityFilter<ArchivedCourierVisit> {

    /** The description. */
    @Builder.Default
    private String description = null;

    /** The courier visit state. */
    @Builder.Default
    private Set<CourierVisitState> courierVisitStates = null;

    @Override
    public String toString() {
	return String.format("ArchivedVisitsFilter [description=\"%s\", courierVisitStates=%s]", description,
		courierVisitStates);
    }

    /**
     * Checks if is pass.
     *
     * @param entity the entity
     * @return true, if is pass
     */
    @Override
    public boolean isPass(ArchivedCourierVisit entity) {
	if (entity == null) {
	    return false;
	}
	if (!containsIgnoreCase(entity.getDescription(), defaultString(description))) {
	    return false;
	}
	if (courierVisitStates != null && !CollectionUtils.containsInstance(courierVisitStates, entity.getState())) {
	    return false;
	}

	return true;
    }

}
