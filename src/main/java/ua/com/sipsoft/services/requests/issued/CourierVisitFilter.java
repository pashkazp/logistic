package ua.com.sipsoft.services.requests.issued;

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
import ua.com.sipsoft.model.entity.requests.issued.CourierVisit;
import ua.com.sipsoft.services.utils.EntityFilter;
import ua.com.sipsoft.utils.CourierVisitState;

/**
 * The Interface CourierVisitFilter.
 * 
 * @author Pavlo Degtyaryev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CourierVisitFilter implements EntityFilter<CourierVisit> {

    /** The description. */
    @Builder.Default
    private String description = null;

    /** The courier visit state. */
    @Builder.Default
    private Set<CourierVisitState> courierVisitStates = null;

    @Override
    public String toString() {
	return String.format("CourierVisitFilter [description=\"%s\", courierVisitStates=%s]", description,
		courierVisitStates);
    }

    /**
     * Checks if is pass.
     *
     * @param entity the entity
     * @return true, if is pass
     */
    @Override
    public boolean isPass(CourierVisit entity) {
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
