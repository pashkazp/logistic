package ua.com.sipsoft.services.requests.issued;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.data.provider.Query;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedRouteSheet;
import ua.com.sipsoft.model.entity.requests.issued.CourierVisit;
import ua.com.sipsoft.model.entity.requests.issued.IssuedRouteSheet;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.model.repository.requests.issued.IssuedRouteSheetRepository;
import ua.com.sipsoft.services.requests.arcive.ArchivedSheetsService;
import ua.com.sipsoft.services.utils.EntityFilter;
import ua.com.sipsoft.services.utils.HasQueryToSortConvertor;
import ua.com.sipsoft.services.utils.OffsetBasedPageRequest;

/**
 * The Interface IssuedRouteSheetServiceImpl.
 * 
 * @author Pavlo Degtyaryev
 */
@Slf4j
@Service
@Transactional
public class IssuedRouteSheetServiceImpl implements IssuedRouteSheetService, HasQueryToSortConvertor {

    /** The dao. */
    @Autowired
    private IssuedRouteSheetRepository dao;

    /** The archive route sheet service. */
    @Autowired
    private ArchivedSheetsService archiveRouteSheetService;

    /**
     * Gets the queried issued route sheets.
     *
     * @param query the query
     * @param value the value
     * @return the queried issued route sheets
     */
    @Override
    public Stream<IssuedRouteSheet> getQueriedIssuedRouteSheets(Query<IssuedRouteSheet, Void> query, String value) {
	log.debug("Get requested page Route Sheet with offset {} and limit {}", query.getOffset(),
		query.getLimit());
	try {
	    Pageable pageable = new OffsetBasedPageRequest(query.getOffset(), query.getLimit(), queryToSort(query));
	    return dao
		    .findAll(pageable).stream();
	} catch (Exception e) {
	    log.error("The Route Sheet list was not received for a reason: {}", e.getMessage());
	}
	return Stream.empty();
    }

    /**
     * Gets the queried issued route sheets count.
     *
     * @param query the query
     * @param value the value
     * @return the queried issued route sheets count
     */
    @Override
    public int getQueriedIssuedRouteSheetsCount(Query<IssuedRouteSheet, Void> query, String value) {
	// todo paramcheck and logging
	return dao.findAll().size();
    }

    /**
     * Recreate draft route sheet.
     *
     * @param emptyList the empty list
     * @param value     the value
     * @param author    the author
     * @return the optional
     */
    @Override
    public Optional<IssuedRouteSheet> recreateDraftRouteSheet(List<Object> emptyList, String value, User author) {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * Move to archive.
     *
     * @param issuedRouteSheet the issued route sheet
     * @param author           the author
     */
    @Override
    public void moveToArchive(IssuedRouteSheet issuedRouteSheet, User author) {
	log.info("Move to Archive Issued Route Sheet");
	if (issuedRouteSheet == null || author == null) {
	    log.warn("Missing some data. Move is impossible.");
	    return;
	}
	if (issuedRouteSheet.getRequests()
		.stream()
		.anyMatch(CourierVisit::isActive)) {
	    log.info("Move to Archive Issued Route Sheet is impossible. Has active Visits.");
	    return;
	}
	ArchivedRouteSheet archivedRouteSheet = new ArchivedRouteSheet(issuedRouteSheet);
	archivedRouteSheet.addHistoryEvent("Створено архівний запис", LocalDateTime.now(), author);
	archivedRouteSheet.getRequests()
		.forEach(v -> v.addHistoryEvent("Створено архівний запис", LocalDateTime.now(), author));
	dao.delete(issuedRouteSheet);
	archiveRouteSheetService.save(archivedRouteSheet);
    }

    /**
     * Adds the courier visits and save.
     *
     * @param issuedRouteSheet the issued route sheet
     * @param author           the author
     * @param courierVisit     the courier visit
     * @return the optional
     */
    @Override
    public Optional<IssuedRouteSheet> addCourierVisitsAndSave(IssuedRouteSheet issuedRouteSheet, User author,
	    CourierVisit courierVisit) {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * Removes the visits and save.
     *
     * @param issuedRouteSheet the issued route sheet
     * @param requests         the requests
     * @param author           the author
     * @return the optional
     */
    @Override
    public Optional<IssuedRouteSheet> removeVisitsAndSave(IssuedRouteSheet issuedRouteSheet,
	    List<CourierVisit> requests, User author) {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * Save.
     *
     * @param issuedRouteSheet the issued route sheet
     * @return the optional
     */
    @Override
    @Transactional
    public Optional<IssuedRouteSheet> save(IssuedRouteSheet issuedRouteSheet) {
	log.info("Save Issued Route Sheet: " + issuedRouteSheet);
	if (issuedRouteSheet == null) {
	    log.warn("Missing some data. Save is impossible.");
	    return Optional.of(null);
	}
	return Optional.of(dao.saveAndFlush(issuedRouteSheet));
    }

    /**
     * Gets the queried issued route sheets by filter.
     *
     * @param query the query
     * @return the queried issued route sheets by filter
     */
    @Override
    public Stream<IssuedRouteSheet> getQueriedIssuedRouteSheetsByFilter(
	    Query<IssuedRouteSheet, EntityFilter<IssuedRouteSheet>> query) {
	log.debug("Get requested page Issued Route Sheets with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
		query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter());
	if (query == null || query.getFilter().isEmpty()) {
	    log.debug("Get Issued Route Sheets is impossible. Miss some data.");
	    return Stream.empty();
	}
	try {
	    return dao.findAll(queryToSort(query))
		    .stream()
		    .filter(entity -> query.getFilter().get().isPass(entity))
		    .skip(query.getOffset())
		    .limit(query.getLimit());
	} catch (Exception e) {
	    log.error("The Issued Route Sheets list was not received for a reason: {}", e.getMessage());
	}
	return Stream.empty();
    }

    /**
     * Gets the queried issued route sheets by filter count.
     *
     * @param query the query
     * @return the queried issued route sheets by filter count
     */
    @Override
    public int getQueriedIssuedRouteSheetsByFilterCount(Query<IssuedRouteSheet, EntityFilter<IssuedRouteSheet>> query) {
	log.debug("Get requested size Issued Route Sheets  with filter '{}'", query.getFilter().get().toString());
	return (int) getQueriedIssuedRouteSheetsByFilter(query).count();
    }

    /**
     * Fetch by id.
     *
     * @param id the id
     * @return the optional
     */
    @Override
    public Optional<IssuedRouteSheet> fetchById(Long id) {
	log.debug("Get Issued Sheet id: '{}'", id);
	if (id == null) {
	    log.debug("Get  Issued Sheet by id is impossible. id is null.");
	    return Optional.of(null);
	}
	try {
	    return dao.findById(id);
	} catch (Exception e) {
	    log.error("The Issued Sheet bi id is not received for a reason: {}", e.getMessage());
	    return Optional.of(null);
	}
    }
}
