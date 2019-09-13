package ua.com.sipsoft.utils.messages;

import ua.com.sipsoft.model.entity.common.Facility;

/**
 * Set of standard messages for entity {@link Facility}.
 *
 * @author Pavlo Degtyaryev
 */
public class FacilityEntityMsg {
	public static final String FACILITYID = "entity.facility.facilityid";
	public static final String NAME = "entity.facility.name";
	public static final String USERS = "entity.facility.users";
	public static final String ADDRESSES = "entity.facility.addresses";
	public static final String ADD = "entity.facility.add";
	public static final String EDIT = "entity.facility.edit";
	public static final String DEL = "entity.facility.del";

	public static final String CHK_NAME_SHORT = "entity.facility.name.short";
	public static final String CHK_NAME_LONG = "entity.facility.name.long";

	/**
	 * Instantiates a new {@link Facility} entity msg.
	 */
	private FacilityEntityMsg() {
	}

}
