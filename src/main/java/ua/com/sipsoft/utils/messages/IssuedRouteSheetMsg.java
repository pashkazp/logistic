package ua.com.sipsoft.utils.messages;

import ua.com.sipsoft.model.entity.requests.issued.IssuedRouteSheet;

/**
 * Set of standard messages for entity {@link IssuedRouteSheet}.
 *
 * @author Pavlo Degtyaryev
 */
public class IssuedRouteSheetMsg {
    public static final String ISSUEDID = "entity.issued.route.id";
    public static final String AUTHOR = "entity.issued.route.author";
    public static final String CREATION_DATE = "entity.issued.route.creation.date";
    public static final String DESCRIPTION = "entity.issued.route.description";
    public static final String STATES = "entity.issued.route.states";

    public static final String ISSUED_ROUTE_SHEET_DONE_ALL_HEADER = "issued.route.sheet.done.all.header";
    public static final String ISSUED_ROUTE_SHEET_DONE_ALL_MSG = "issued.route.sheet.done.all.msg";
    public static final String ISSUED_ROUTE_SHEET_CANCEL_ALL_HEADER = "issued.route.sheet.cancel.all.header";
    public static final String ISSUED_ROUTE_SHEET_CANCEL_ALL_MSG = "issued.route.sheet.cancel.all.msg";
    public static final String ISSUED_ROUTE_SHEET_ARCH_HEADER = "issued.route.sheet.arch.header";
    public static final String ISSUED_ROUTE_SHEET_ARCH_MSG = "issued.route.sheet.arch.msg";
    public static final String ISSUED_ROUTE_SHEET_REDRAFT_HEADER = "issued.route.sheet.redraft.header";
    public static final String ISSUED_ROUTE_SHEET_REDRAFT_MSG = "issued.route.sheet.redraft.msg";

    public static final String ISSUED = "entity.issued.route";

    // TODO divide on two files

//	public static final String DRAFT_ROUTE_SHEET_REM_REQ_HEADER = "draft.route.sheet.remove.request.header";
//	public static final String DRAFT_ROUTE_SHEET_REM_REQ_MSG = "draft.route.sheet.remove.request.msg";
//	public static final String DRAFT_ROUTE_SHEET_ADD_REQ_HEADER = "draft.route.sheet.add.requests.header";
//	public static final String DRAFT_ROUTE_SHEET_ADD_REQ_MSG = "draft.route.sheet.add.requests.msg";

    /**
     * Instantiates a new {@link IssuedRouteSheet} msg.
     */
    private IssuedRouteSheetMsg() {
    }
}
