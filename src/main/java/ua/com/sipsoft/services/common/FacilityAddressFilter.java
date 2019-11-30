package ua.com.sipsoft.services.common;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.defaultString;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.model.entity.common.FacilityAddress;
import ua.com.sipsoft.services.utils.EntityFilter;

/**
 * The Interface FacilityAddressFilter.
 *
 * @author Pavlo Degtyaryev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class FacilityAddressFilter implements EntityFilter<FacilityAddress> {

    /** The address alias. */
    @Builder.Default
    private String addressAlias = null;

    /** The address. */
    @Builder.Default
    private String address = null;

    /** The facility id. */
    @Builder.Default
    private Long facilityId = null;

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
	return String.format("FacilityAddressFilter [facilityId=%s, addressAlias=\"%s\", address=\"%s\"]",
		facilityId, addressAlias, address);
    }

    /**
     * Checks if is pass.
     *
     * @param entity the entity
     * @return true, if is pass
     */
    @Override
    public boolean isPass(FacilityAddress entity) {
	if (entity == null) {
	    return false;
	}
	if (!containsIgnoreCase(entity.getAddress(), defaultString(address))) {
	    return false;
	}
	if (!containsIgnoreCase(entity.getAddressesAlias(), defaultString(addressAlias))) {
	    return false;
	}
	if (facilityId != null && entity.getFacility().getId() != facilityId) {
	    return false;
	}

	return true;
    }

}
