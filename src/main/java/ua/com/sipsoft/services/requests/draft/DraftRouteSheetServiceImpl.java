package ua.com.sipsoft.services.requests.draft;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.data.provider.Query;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedRouteSheet;
import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.model.entity.requests.draft.DraftRouteSheet;
import ua.com.sipsoft.model.entity.requests.issued.IssuedRouteSheet;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.model.repository.requests.draft.DraftRouteSheetRepository;
import ua.com.sipsoft.services.requests.arcive.ArchiveRouteSheetService;
import ua.com.sipsoft.services.requests.issued.IssuedRouteSheetService;
import ua.com.sipsoft.services.utils.HasQueryToSortConvertor;

/** The Constant log. */
@Slf4j
@Service
@Transactional
public class DraftRouteSheetServiceImpl implements DraftRouteSheetService, HasQueryToSortConvertor {

	/** The dao. */
	@Autowired
	private DraftRouteSheetRepository dao;

	/** The archive route sheet service. */
	@Autowired
	private ArchiveRouteSheetService archiveRouteSheetService;

	/** The issued route sheet service. */
	@Autowired
	private IssuedRouteSheetService issuedRouteSheetService;

	/** The courier request service. */
	@Autowired
	private CourierRequestService courierRequestService;

	/**
	 * Clean draft sheets from courier requests.
	 *
	 * @param requests the requests
	 */
	@Override
	public void cleanDraftSheetsFromCourierRequests(Collection<CourierRequest> requests) {
		log.info("Clean Draft Route Sheet(s) from Courier Request(s)");
		if (requests == null || requests.isEmpty()) {
			log.warn("Missing some data or nothing delete. Cleaning impossible.");
			return;
		}
		List<DraftRouteSheet> draftRouteSheetList = dao.getByRequests(requests);
		draftRouteSheetList
				.forEach(sheet -> removeRequestsAndSave(sheet, requests));
	}

	/**
	 * Removes the requests and save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param requests        the requests
	 * @return the optional
	 */
	@Override
	public Optional<DraftRouteSheet> removeRequestsAndSave(DraftRouteSheet draftRouteSheet,
			Collection<CourierRequest> requests) {
		log.info("Remove Courier Request(s) from Draft Route Sheet");
		if (draftRouteSheet == null || requests == null) {
			log.warn("Missing some data. Remove impossible.");
			return Optional.of(null);
		}
		boolean someRequestcCleaned = draftRouteSheet.getRequests().removeAll(requests);
		if (someRequestcCleaned) {
			return save(draftRouteSheet);
		} else {
			return Optional.of(null);
		}
	}

	/**
	 * Creates the and save draft route sheet.
	 *
	 * @param requests    the requests
	 * @param description the description
	 * @param author      the author
	 * @return the optional
	 */
	@Override
	public Optional<DraftRouteSheet> createAndSaveDraftRouteSheet(Collection<CourierRequest> requests,
			String description, User author) {
		log.info("Create and save Draft Route Sheet");
		if (requests == null || author == null) {
			log.warn("Missing some data. Save impossible.");
			Optional.of(null);
		}
		DraftRouteSheet draftRouteSheet = new DraftRouteSheet();
		draftRouteSheet.setAuthor(author);
		draftRouteSheet.setDescription(description);
		if (!requests.isEmpty()) {
			draftRouteSheet.getRequests().addAll(requests);
		}
		// todo possible to make registration of added requests?
		draftRouteSheet.addHistoryEvent("Чернетку маршрутного листа було створено.", draftRouteSheet.getCreationDate(),
				author);
		return save(draftRouteSheet);
	}

	/**
	 * Save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @return the optional
	 */
	@Override
	public Optional<DraftRouteSheet> save(DraftRouteSheet draftRouteSheet) {
		log.info("Save Draft Route Sheet: " + draftRouteSheet);
		if (draftRouteSheet == null) {
			log.warn("Missing some data. Save impossible.");
			return Optional.of(null);
		}
		return Optional.of(dao.saveAndFlush(draftRouteSheet));
	}

	/**
	 * Move to archive.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param movingReason    the moving reason
	 * @param author          the author
	 */
	@Override
	public void moveToArchive(DraftRouteSheet draftRouteSheet, String movingReason, User author) {
		log.info("Move to Archive Draft Route Sheet");
		if (draftRouteSheet == null || movingReason == null || author == null) {
			log.warn("Missing some data. Move is impossible.");
			return;
		}
		draftRouteSheet.getRequests().clear();
		ArchivedRouteSheet archivedRouteSheet = new ArchivedRouteSheet(draftRouteSheet);
		dao.delete(draftRouteSheet);
		archivedRouteSheet.addHistoryEvent(
				new StringBuilder()
						.append("Скасовано з причини '")
						.append(movingReason)
						.append("'. Переміщено до архіву.")
						.toString(),
				LocalDateTime.now(), author);
		archiveRouteSheetService.save(archivedRouteSheet);
	}

	/**
	 * Issue draft route sheet.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param description     the description
	 * @param author          the author
	 */
	@Override
	public void issueDraftRouteSheet(DraftRouteSheet draftRouteSheet, String description, User author) {
		log.info("Do Issue Draft Route Sheet");
		if (draftRouteSheet == null || author == null || description == null) {
			log.warn("Missing some data. Issue is impossible.");
			return;
		}
		IssuedRouteSheet issuedRouteSheet = new IssuedRouteSheet(draftRouteSheet, author);
		issuedRouteSheet.addHistoryEvent(
				new StringBuilder()
						.append("Чернетку маршрутного листа було видано")
						.append(issuedRouteSheet.getDescription().equals(description) ? "."
								: (" з новим приписом \"").concat(description).concat("\"."))
						.toString(),
				LocalDateTime.now(), author);
		issuedRouteSheet.setDescription(description);
		issuedRouteSheet.setCreationDate(LocalDateTime.now());
		issuedRouteSheetService.save(issuedRouteSheet);

		cleanDraftSheetsFromCourierRequests(draftRouteSheet.getRequests());
		courierRequestService.delete(draftRouteSheet.getRequests());

		// draftRouteSheet.getRequests().clear();
		dao.deleteById(draftRouteSheet.getId());
		dao.flush();
		// dao.delete(draftRouteSheet);

	}

	/**
	 * Adds the courier requests and save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param author          the author
	 * @param request         the request
	 * @return the optional
	 */
	@Override
	public Optional<DraftRouteSheet> addCourierRequestsAndSave(DraftRouteSheet draftRouteSheet, User author,
			CourierRequest... request) {
		log.info("Add Courier Request(s) to Draft Route Sheet");
		if (draftRouteSheet == null || request == null || author == null || (request != null && request.length < 1)) {
			log.warn("Missing some data. Save impossible.");
			return Optional.of(null);
		}
		for (CourierRequest courierRequest : request) {
			draftRouteSheet.getRequests().add(courierRequest);
		}
		try {
			draftRouteSheet.addHistoryEvent(request.length + " request(s) was added.", LocalDateTime.now(), author);
		} catch (Exception e) {
			log.error("Error on add Corier Request(s) to the Draft Route Sheet for a reason: {}", e.getMessage());
		}
		return save(draftRouteSheet);
	}

	/**
	 * Adds the requests and save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param requests        the requests
	 * @param author          the author
	 * @return the optional
	 */
	@Override
	public Optional<DraftRouteSheet> addRequestsAndSave(DraftRouteSheet draftRouteSheet,
			Collection<CourierRequest> requests,
			User author) {
		log.info("Add Courier Request(s) to Draft Route Sheet");
		if (draftRouteSheet == null || requests == null || author == null) {
			log.warn("Missing some data. Add impossible.");
			return Optional.of(null);
		}
		draftRouteSheet.getRequests().addAll(requests);
		return save(draftRouteSheet);
	}

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	@Override
	public Optional<DraftRouteSheet> fetchById(Long id) {
		log.debug("Get Route Sheet id: '{}'", id);
		if (id == null) {
			log.debug("Get  Route Sheet by id is impossible. id is null.");
			return Optional.of(null);
		}
		try {
			return dao.findById(id);
		} catch (Exception e) {
			log.error("The Route Sheet bi id is not received for a reason: {}", e.getMessage());
			return Optional.of(null);
		}
	}

	/**
	 * Checks if is entity pass filter.
	 *
	 * @param entity the entity
	 * @param filter the filter
	 * @return true, if is entity pass filter
	 */
	private boolean isEntityPassFilter(DraftRouteSheet entity, DraftRouteSheetFilter filter) {
		return entity.getDescription()
				.toLowerCase()
				.contains(filter.getDescription() == null ? "" : filter.getDescription().toLowerCase());
	}

	/**
	 * Gets the queried draft route sheets.
	 *
	 * @param query  the query
	 * @param filter the filter
	 * @return the queried draft route sheets
	 */
	@Override
	public Stream<DraftRouteSheet> getQueriedDraftRouteSheets(Query<DraftRouteSheet, DraftRouteSheetFilter> query,
			DraftRouteSheetFilter filter) {
		log.debug("Get requested page Drafr Route Sheets with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
				query.getOffset(), query.getLimit(), query.getSortOrders(), filter);
		if (query == null || filter == null) {
			log.debug("Get Drafr Route Sheets is impossible. Miss some data.");
			return Stream.empty();
		}
		try {
			return dao.findAll(queryToSort(query))
					.stream()
					.filter(entity -> isEntityPassFilter(entity, filter))
					.skip(query.getOffset())
					.limit(query.getLimit());
		} catch (Exception e) {
			log.error("The Drafr Route Sheets list was not received for a reason: {}", e.getMessage());
		}
		return Stream.empty();
	}

	/**
	 * Gets the queried draft route sheets count.
	 *
	 * @param query  the query
	 * @param filter the filter
	 * @return the queried draft route sheets count
	 */
	@Override
	public int getQueriedDraftRouteSheetsCount(Query<DraftRouteSheet, DraftRouteSheetFilter> query,
			DraftRouteSheetFilter filter) {
		log.debug("Get requested size Drafr Route Sheets  with filter '{}'", filter);
		return (int) getQueriedDraftRouteSheets(query, filter).count();
	}
}
