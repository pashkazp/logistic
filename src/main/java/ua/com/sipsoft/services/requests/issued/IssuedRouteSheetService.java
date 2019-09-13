package ua.com.sipsoft.services.requests.issued;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;

import ua.com.sipsoft.model.entity.requests.issued.CourierVisit;
import ua.com.sipsoft.model.entity.requests.issued.IssuedRouteSheet;
import ua.com.sipsoft.model.entity.user.User;

/**
 * The Interface IssuedRouteSheetService.
 * @author Pavlo Degtyaryev
 */
@Service
public interface IssuedRouteSheetService {
	
	/**
	 * Gets the queried issued route sheets.
	 *
	 * @param query the query
	 * @param value the value
	 * @return the queried issued route sheets
	 */
	Stream<IssuedRouteSheet> getQueriedIssuedRouteSheets(Query<IssuedRouteSheet, Void> query, String value);

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	Optional<IssuedRouteSheet> fetchById(Long id);

	/**
	 * Gets the queried issued route sheets count.
	 *
	 * @param query the query
	 * @param value the value
	 * @return the queried issued route sheets count
	 */
	int getQueriedIssuedRouteSheetsCount(Query<IssuedRouteSheet, Void> query, String value);

	/**
	 * Recreate draft route sheet.
	 *
	 * @param emptyList the empty list
	 * @param value the value
	 * @param author the author
	 * @return the optional
	 */
	Optional<IssuedRouteSheet> recreateDraftRouteSheet(List<Object> emptyList, String value, User author);

	/**
	 * Save.
	 *
	 * @param issuedRouteSheet the issued route sheet
	 * @return the optional
	 */
	Optional<IssuedRouteSheet> save(IssuedRouteSheet issuedRouteSheet);

	/**
	 * Move to archive.
	 *
	 * @param issuedRouteSheet the issued route sheet
	 * @param author the author
	 */
	void moveToArchive(IssuedRouteSheet issuedRouteSheet, User author);

	/**
	 * Adds the courier visits and save.
	 *
	 * @param issuedRouteSheet the issued route sheet
	 * @param author the author
	 * @param courierVisit the courier visit
	 * @return the optional
	 */
	Optional<IssuedRouteSheet> addCourierVisitsAndSave(IssuedRouteSheet issuedRouteSheet, User author,
			CourierVisit courierVisit);

	/**
	 * Removes the visits and save.
	 *
	 * @param issuedRouteSheet the issued route sheet
	 * @param requests the requests
	 * @param author the author
	 * @return the optional
	 */
	Optional<IssuedRouteSheet> removeVisitsAndSave(IssuedRouteSheet issuedRouteSheet, List<CourierVisit> requests,
			User author);

	/**
	 * Gets the queried issued route sheets by filter.
	 *
	 * @param query the query
	 * @param filter the filter
	 * @return the queried issued route sheets by filter
	 */
	Stream<IssuedRouteSheet> getQueriedIssuedRouteSheetsByFilter(Query<IssuedRouteSheet, IssuedRouteSheetFilter> query,
			IssuedRouteSheetFilter filter);

	/**
	 * Gets the queried issued route sheets by filter count.
	 *
	 * @param query the query
	 * @param filter the filter
	 * @return the queried issued route sheets by filter count
	 */
	int getQueriedIssuedRouteSheetsByFilterCount(Query<IssuedRouteSheet, IssuedRouteSheetFilter> query,
			IssuedRouteSheetFilter filter);

}
