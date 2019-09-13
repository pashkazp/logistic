package ua.com.sipsoft.services.common;

import java.util.Collection;

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
public class FacilitiesFilter {

	/** The name. */
	@Builder.Default
	private String name = null;

	/** The users. */
	@Builder.Default
	private Collection<User> users = null;

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return String.format("FacilitiesFilter [name=\"%s\"]", name);
	}

}
