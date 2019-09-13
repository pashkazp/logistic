package ua.com.sipsoft.utils.messages;

import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;

/**
 * Set of standard messages for entity {@link CourierRequest}.
 *
 * @author Pavlo Degtyaryev
 */
public class CourierRequestsMsg {
	public static final String REQUEST = "entity.courier.request";
	public static final String REQUESTID = "entity.courier.request.id";
	public static final String AUTHOR = "entity.courier.request.author";
	public static final String FACILITY_FROM = "entity.courier.request.facilityaddr.from";
	public static final String FACILITY_TO = "entity.courier.request.facilityaddr.to";
	public static final String FACILITYADDR_FROM = "entity.courier.request.address.from";
	public static final String FACILITYADDR_TO = "entity.courier.request.address.to";
	public static final String HISTORY_EVENTS = "entity.courier.request.history.event";
	public static final String CREATION_DATE = "entity.courier.request.creation.date";
	public static final String DESCRIPTION = "entity.courier.request.description";
	public static final String ADD = "entity.courier.request.add";
	public static final String EDIT = "entity.courier.request.edit";
	public static final String DEL = "entity.courier.request.del";

	public static final String CANCEL_REQUEST_HEADER = "courier.request.cancel.header";
	public static final String CANCEL_REQUEST_MESSAGE = "courier.request.cancel.message";

	public static final String SELECT_FACILITY_FROM = "courier.request.select.facility.from";
	public static final String SELECT_FACILITY_ADDR_FROM = "courier.request.select.facility.addr.from";
	public static final String SELECT_FACILITY_TO = "courier.request.select.facility.to";
	public static final String SELECT_FACILITY_ADDR_TO = "courier.request.select.facility.addr.to";
	public static final String SELECT_FACILITY_SENDER_CHECK = "courier.request.select.sender.check";
	public static final String SELECT_FACILITY_RECIEVER_CHECK = "courier.request.select.reciever.check";
	public static final String SELECT_REQUEST_DESCR_CHECK = "courier.request.description.check";
	public static final String SELECT_REQUEST_DESCR_CHECK_LONG = "courier.request.description.check.long";

	/**
	 * Instantiates a new {@link CourierRequest} msg.
	 */
	private CourierRequestsMsg() {
	}
}
