package ua.com.sipsoft.services.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class FacilityAddressFilter {

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

}
