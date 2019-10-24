package ua.com.sipsoft.services.requests.arcive;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.data.provider.Query;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedRouteSheet;
import ua.com.sipsoft.model.repository.requests.archive.ArchivedRouteSheetRepository;
import ua.com.sipsoft.services.utils.HasQueryToSortConvertor;

/** The Constant log. */

/** The Constant log. */
@Slf4j
@Service
@Transactional
public class ArchivedSheetsServiceImpl implements ArchivedSheetsService, HasQueryToSortConvertor {

    /** The dao. */
    @Autowired
    private ArchivedRouteSheetRepository dao;

    /**
     * Save.
     *
     * @param archivedRouteSheet the archived route sheet
     * @return the optional
     */
    @Override
    public Optional<ArchivedRouteSheet> save(ArchivedRouteSheet archivedRouteSheet) {
	log.info("Save Archived Route Sheet");
	if (archivedRouteSheet == null) {
	    log.warn("Save Archived Route Sheet impossible. Some data missing");
	    return Optional.of(null);
	}
	return Optional.of(dao.saveAndFlush(archivedRouteSheet));
    }

    /**
     * Checks if is entity pass filter.
     *
     * @param entity the entity
     * @param filter the filter
     * @return true, if is entity pass filter
     */
    private boolean isEntityPassFilter(ArchivedRouteSheet entity, ArchivedSheetFilter filter) {
	return entity.getDescription()
		.toLowerCase()
		.contains(filter.getDescription() == null ? "" : filter.getDescription().toLowerCase());
    }

    /**
     * Gets the queried archived sheets by filter.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried archived sheets by filter
     */
    @Override
    public Stream<ArchivedRouteSheet> getQueriedArchivedSheetsByFilter(
	    Query<ArchivedRouteSheet, ArchivedSheetFilter> query, ArchivedSheetFilter filter) {
	log.debug("Get requested page Archived Route Sheets with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
		query.getOffset(), query.getLimit(), query.getSortOrders(), filter);
	if (query == null || filter == null) {
	    log.debug("Get Фксршмув Route Sheets is impossible. Miss some data.");
	    return Stream.empty();
	}
	try {
	    return dao.findAll(queryToSort(query))
		    .stream()
		    .filter(entity -> isEntityPassFilter(entity, filter))
		    .skip(query.getOffset())
		    .limit(query.getLimit());
	} catch (Exception e) {
	    log.error("The Archived Route Sheets list was not received for a reason: {}", e.getMessage());
	}
	return Stream.empty();
    }

    /**
     * Gget queried archived sheets by filter count.
     *
     * @param query  the query
     * @param filter the filter
     * @return the int
     */
    @Override
    public int ggetQueriedArchivedSheetsByFilterCount(Query<ArchivedRouteSheet, ArchivedSheetFilter> query,
	    ArchivedSheetFilter filter) {
	return (int) getQueriedArchivedSheetsByFilter(query, filter).count();
    }
}
