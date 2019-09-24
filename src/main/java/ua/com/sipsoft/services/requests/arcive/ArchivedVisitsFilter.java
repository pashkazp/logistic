package ua.com.sipsoft.services.requests.arcive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.utils.CourierVisitState;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode

public class ArchivedVisitsFilter {

	/** The description. */
	@Builder.Default
	private String description = null;

	/** The sheet id. */
	@Builder.Default
	private Long sheetId = null;

	/** The courier visit state. */
	@Builder.Default
	private CourierVisitState courierVisitState = null;

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "ArchivedVisitsFilter [description=\"" + description + "\", sheetId=" + sheetId + ", courierVisitState="
				+ courierVisitState + "]";
	}

}
