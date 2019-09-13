package ua.com.sipsoft.services.requests.draft;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;

import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.model.entity.requests.draft.DraftRouteSheet;
import ua.com.sipsoft.model.entity.user.User;

/**
 * The Interface DraftRouteSheetService.
 */
@Service
public interface DraftRouteSheetService {

	/**
	 * Clean draft sheets from courier requests.
	 *
	 * @param requests the requests
	 */
	void cleanDraftSheetsFromCourierRequests(Collection<CourierRequest> requests);

	/**
	 * Removes the requests and save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param requests        the requests
	 * @return the optional
	 */
	Optional<DraftRouteSheet> removeRequestsAndSave(DraftRouteSheet draftRouteSheet,
			Collection<CourierRequest> requests);

	/**
	 * Creates the and save draft route sheet.
	 *
	 * @param requests    the requests
	 * @param description the description
	 * @param author      the author
	 * @return the optional
	 */
	Optional<DraftRouteSheet> createAndSaveDraftRouteSheet(Collection<CourierRequest> requests, String description,
			User author);

	/**
	 * Save.
	 *
	 * @param request the request
	 * @return the optional
	 */
	Optional<DraftRouteSheet> save(DraftRouteSheet request);

	/**
	 * Move to archive.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param movingReason    the moving reason
	 * @param author          the author
	 */
	void moveToArchive(DraftRouteSheet draftRouteSheet, String movingReason, User author);

	/**
	 * Adds the courier requests and save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param author          the author
	 * @param request         the request
	 * @return the optional
	 */
	Optional<DraftRouteSheet> addCourierRequestsAndSave(DraftRouteSheet draftRouteSheet, User author,
			CourierRequest... request);

	/**
	 * Adds the requests and save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param requests        the requests
	 * @param author          the author
	 * @return the optional
	 */
	Optional<DraftRouteSheet> addRequestsAndSave(DraftRouteSheet draftRouteSheet, Collection<CourierRequest> requests,
			User author);

	/**
	 * Issue draft route sheet.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param description     the description
	 * @param author          the author
	 */
	void issueDraftRouteSheet(DraftRouteSheet draftRouteSheet, String description, User author);

	/**
	 * Gets the queried draft route sheets.
	 *
	 * @param query  the query
	 * @param filter the filter
	 * @return the queried draft route sheets
	 */
	Stream<DraftRouteSheet> getQueriedDraftRouteSheets(Query<DraftRouteSheet, DraftRouteSheetFilter> query,
			DraftRouteSheetFilter filter);

	/**
	 * Gets the queried draft route sheets count.
	 *
	 * @param query  the query
	 * @param filter the filter
	 * @return the queried draft route sheets count
	 */
	int getQueriedDraftRouteSheetsCount(Query<DraftRouteSheet, DraftRouteSheetFilter> query,
			DraftRouteSheetFilter filter);

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	Optional<DraftRouteSheet> fetchById(Long id);

}
