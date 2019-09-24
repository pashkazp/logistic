package ua.com.sipsoft.utils.messages;

import ua.com.sipsoft.model.entity.requests.archive.ArchivedRouteSheet;
import ua.com.sipsoft.model.entity.requests.issued.IssuedRouteSheet;

/**
 * Set of standard messages for entity {@link IssuedRouteSheet}.
 *
 * @author Pavlo Degtyaryev
 */
public class ArchivedRouteSheetMsg {
    public static final String ARCHIVEDID = "entity.archived.route.id";
    public static final String AUTHOR = "entity.archived.route.author";
    public static final String CREATION_DATE = "entity.archived.route.creation.date";
    public static final String DESCRIPTION = "entity.archived.route.description";
    public static final String STATES = "entity.archived.route.states";

    public static final String ARCHIVED_REDRAFT_HEADER = "archived.redraft.header";
    public static final String ARCHIVED_REDRAFT_MSG = "archived.redraft.msg";

    public static final String ARCHIVED = "entity.archived.route";

    // TODO divide on two files

//	public static final String DRAFT_ROUTE_SHEET_REM_REQ_HEADER = "draft.route.sheet.remove.request.header";
//	public static final String DRAFT_ROUTE_SHEET_REM_REQ_MSG = "draft.route.sheet.remove.request.msg";
//	public static final String DRAFT_ROUTE_SHEET_ADD_REQ_HEADER = "draft.route.sheet.add.requests.header";
//	public static final String DRAFT_ROUTE_SHEET_ADD_REQ_MSG = "draft.route.sheet.add.requests.msg";

    /**
     * Instantiates a new {@link ArchivedRouteSheet} msg.
     */
    private ArchivedRouteSheetMsg() {
    }
}
