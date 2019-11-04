package ua.com.sipsoft.utils.messages;

import ua.com.sipsoft.ui.commons.AppNotificator;

/**
 * Set of standard Applications messages for {@link AppNotificator}.
 *
 * @author Pavlo Degtyaryev
 */
public class AppNotifyMsg {
    public static final String USER_SAVED = "app.note.user.saved";
    public static final String USER_NOT_SAVED = "app.note.user.not.saved";
    public static final String FACILITY_CHK_FAIL = "app.note.facility.check.fail";
    public static final String FACILITY_DEL_FAIL = "app.note.facility.delete.fail";
    public static final String FACILITY_SAVED = "app.note.facility.saved";
    public static final String FACILITY_DELETED = "app.note.facility.deleted";
    public static final String FACILITYADDR_CHK_FAIL = "app.note.facility.addr.check.fail";
    public static final String FACILITYADDR_LASTDEL_FAIL = "app.note.facility.addr.last.del.fail";
    public static final String FACILITYADDR_DEL_FAIL = "app.note.facility.addr.del.fail";
    public static final String FACILITYADDR_SAVED = "app.note.facility.addr.saved";
    public static final String FACILITYADDR_DELETED = "app.note.facility.addr.deleted";
    public static final String FACILITYADDR_NOT_FOUND = "app.note.facility.addr.not.found";
    public static final String FACILITY_USR_ADD_CHK_FAIL = "app.note.facility.user.add.fail";
    public static final String FACILITY_USR_ADDED = "app.note.facility.user.added";
    public static final String FACILITY_USR_DELETED = "app.note.facility.user.deleted";
    public static final String FACILITY_NOT_FOUND = "app.note.facility.not.found";

    public static final String COURIER_REQ_CHK_FAIL = "app.note.courier.request.check.fail";
    public static final String COURIER_REQ_SAVED = "app.note.courier.request.saved";
    public static final String COURIER_REQ_DELETED = "app.note.courier.request.deleted";
    public static final String COURIER_REQ_SMALL_REASON = "app.note.courier.request.small.reason";

    public static final String DRAFT_ROUTE_SHEET_CREATED = "app.note.draft.route.sheet.created";
    public static final String DRAFT_ROUTE_SHEET_SAVED = "app.note.draft.route.sheet.saved";
    public static final String DRAFT_ROUTE_SHEET_DELETED = "app.note.draft.route.sheet.deleted";
    public static final String DRAFT_ROUTE_SHEET_ISSUED = "app.note.draft.route.sheet.issued";
    public static final String DRAFT_COURIER_REQ_REMOVED = "app.note.draft.route.sheet.req.rem";
    public static final String DRAFT_COURIER_REQ_ADDED = "app.note.draft.route.sheet.req.add";
    public static final String WARN_SHORT_DESCRIPTION = "app.note.warning.short.description";
    public static final String DRAFT_NOT_FOUND = "app.note.draft.not.found";
    public static final String REQUEST_NOT_FOUND = "app.note.request.not.found";

    public static final String MENU_ADMIN = "Admins";
    public static final String ISSUED_SHEET_NOT_FOUND = "app.note.issued.sheet.not.found";
    public static final String ISSUED_SHEET_HAVE_NO_UNFINISHED = "app.note.issued.sheet.no.unfinished";
    public static final String ISSUED_IS_UNFINISHED = "app.note.draft.is.unfinished";
    public static final String ISSUED_IS_ARCHIVED = "app.note.issued.is.archived";

    public static final String VISITS_MARKS_AS_COMPLETED = "app.note.visits.marks.as.completed";
    public static final String VISITS_MARKS_AS_CANCELLED = "app.note.visits.marks.as.cancelled";
    public static final String VISITS_WERE_REDRAFTS = "app.note.visits.redrafts";
    public static final String VISITS_NOTHING_REDRAFTS = "app.note.visits.nothing.redrafts";

    public static final String NOTHING_TO_EDIT = "app.note.nothing.to.edit";
    public static final String NOTHING_TO_CANCEL = "app.note.nothing.to.cancel";
    public static final String NOTHING_TO_COMPLETE = "app.note.nothing.to.complete";

    public static final String NOT_ENOUGH_RIGHT = "app.note.not.enough.right";

    public static final String EMAIL_SENDING = "app.note.email.sending";

    /**
     * Instantiates a new {@link AppNotificator} msg.
     */
    private AppNotifyMsg() {
    }

}
