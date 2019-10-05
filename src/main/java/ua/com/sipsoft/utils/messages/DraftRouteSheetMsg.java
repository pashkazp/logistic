package ua.com.sipsoft.utils.messages;

import ua.com.sipsoft.model.entity.requests.draft.DraftRouteSheet;

/**
 * Set of standard messages for entity {@link DraftRouteSheet}.
 *
 * @author Pavlo Degtyaryev
 */
public class DraftRouteSheetMsg {
    public static final String REQUEST = "entity.draft.route";
    public static final String REQUESTID = "entity.draft.route.id";
    public static final String AUTHOR = "entity.draft.route.author";
    public static final String CREATION_DATE = "entity.draft.route.creation.date";
    public static final String DESCRIPTION = "entity.draft.route.description";
    // TODO divide on two files
    public static final String DRAFT_ROUTE_SHEET_CREATE_HEADER = "draft.route.sheet.add.header";
    public static final String DRAFT_ROUTE_SHEET_CREATE_MSG = "draft.route.sheet.add.msg";
    public static final String DRAFT_ROUTE_SHEET_EDIT_HEADER = "draft.route.sheet.edit.header";
    public static final String DRAFT_ROUTE_SHEET_EDIT_MSG = "draft.route.sheet.edit.msg";
    public static final String DRAFT_ROUTE_SHEET_DEL_HEADER = "draft.route.sheet.del.header";
    public static final String DRAFT_ROUTE_SHEET_DEL_MSG = "draft.route.sheet.del.msg";
    public static final String DRAFT_ROUTE_SHEET_ISSUE_HEADER = "draft.route.sheet.issue.header";
    public static final String DRAFT_ROUTE_SHEET_ISSUE_MSG = "draft.route.sheet.issue.msg";

//	public static final String DRAFT_ROUTE_SHEET_REM_REQ_HEADER = "draft.route.sheet.remove.request.header";
//	public static final String DRAFT_ROUTE_SHEET_REM_REQ_MSG = "draft.route.sheet.remove.request.msg";
//	public static final String DRAFT_ROUTE_SHEET_ADD_REQ_HEADER = "draft.route.sheet.add.requests.header";
//	public static final String DRAFT_ROUTE_SHEET_ADD_REQ_MSG = "draft.route.sheet.add.requests.msg";

    /**
     * Instantiates a new {@link DraftRouteSheet} msg.
     */
    private DraftRouteSheetMsg() {
    }
}
