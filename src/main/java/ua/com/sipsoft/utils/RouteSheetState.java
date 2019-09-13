package ua.com.sipsoft.utils;

import lombok.Getter;

/**
 * The Enum RouteSheetState.
 *
 * @author Pavlo Degtyaryev
 */
public enum RouteSheetState {
	RUNNING("RUNNING"),
	COMPLETED("COMPLETED"),
	CANCELLED("CANCELLED"),
	REREQUESTED("REREQUESTED");

	/**
	 * Gets the state name.
	 *
	 * @return the state name
	 */
	@Getter
	private String stateName;

	/**
	 * Instantiates a new route sheet state.
	 *
	 * @param stateName the state name
	 */
	private RouteSheetState(String stateName) {
		this.stateName = stateName;
	}

}