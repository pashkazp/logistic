package ua.com.sipsoft.services.requests.draft;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.model.entity.user.User;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CourierRequestFilter {

	/** The description. */
	@Builder.Default
	private String description = null;

	/** The sheet id. */
	@Builder.Default
	private Long sheetId = null;

	/** The author. */
	@Builder.Default
	private User author = null;

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return String.format("FacilitiesFilter [description=\"%s\", sheetId=%s]", description, sheetId);
	}

}
