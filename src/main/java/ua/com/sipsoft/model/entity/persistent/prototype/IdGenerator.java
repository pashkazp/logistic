package ua.com.sipsoft.model.entity.persistent.prototype;

import java.util.UUID;

/**
 *
 * @author Pavlo Degtyaryev
 */
public class IdGenerator {

	public static String createId() {
		UUID uuid = java.util.UUID.randomUUID();
		return uuid.toString();
	}
}