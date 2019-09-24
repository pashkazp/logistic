package ua.com.sipsoft.services.requests.arcive;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.data.provider.Query;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisit;
import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.model.repository.requests.archive.ArchivedCourierVisitRepository;
import ua.com.sipsoft.services.requests.draft.CourierRequestService;
import ua.com.sipsoft.services.utils.HasQueryToSortConvertor;

/** The Constant log. */
@Slf4j
@Service
@Transactional
public class ArchivedVisitsServiceImpl implements ArchivedVisitsService, HasQueryToSortConvertor {

    /** The dao. */
    @Autowired
    private ArchivedCourierVisitRepository dao;

    @Autowired
    CourierRequestService courierRequestService;

    /**
     * Save.
     *
     * @param acv the acv
     * @return the optional
     */
    @Override
    public Optional<ArchivedCourierVisit> save(ArchivedCourierVisit acv) {
	log.info("Save courier Archived Courier Visit: " + acv);
	if (acv == null) {
	    log.warn("Save Archive Courier Visit impossible. Some data missing");
	    return Optional.of(null);
	}
	return Optional.of(dao.saveAndFlush(acv));
    }

    /**
     * Checks if is entity pass filter.
     *
     * @param entity the entity
     * @param filter the filter
     * @return true, if is entity pass filter
     */
    private boolean isEntityPassFilter(ArchivedCourierVisit entity, ArchivedVisitsFilter filter) {
	return entity.getDescription()
		.toLowerCase()
		.contains(filter.getDescription() == null ? "" : filter.getDescription().toLowerCase());
    }

    /**
     * Gets the queried courier visits by filter.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried courier visits by filter
     */
    @Override
    public Stream<ArchivedCourierVisit> getQueriedCourierVisitsByFilter(
	    Query<ArchivedCourierVisit, ArchivedVisitsFilter> query, ArchivedVisitsFilter filter) {
	log.debug(
		"Get requested page Archived Courier Visits By Sheet ID with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
		query.getOffset(), query.getLimit(), query.getSortOrders(), filter);
	if (query == null || filter == null) {
	    log.debug("Get ourier Visits is impossible. Miss some data.");
	    return Stream.empty();
	}
	try {
	    if (filter.getSheetId() == null) {
		return dao.findAll(queryToSort(query))
			.stream()
			.filter(entity -> isEntityPassFilter(entity, filter))
			.skip(query.getOffset())
			.limit(query.getLimit());
	    } else {
		return dao.getCourierVisitByArchivedSheetId(filter.getSheetId(), queryToSort(query))
			.stream()
			.filter(entity -> isEntityPassFilter(entity, filter))
			.skip(query.getOffset())
			.limit(query.getLimit());
	    }
	} catch (Exception e) {
	    log.error("The Archived Visits list was not received for a reason: {}", e.getMessage());
	}
	return Stream.empty();
    }

    /**
     *
     */
    @Override
    public int getQueriedCourierVisitsByFilterCount(Query<ArchivedCourierVisit, ArchivedVisitsFilter> query,
	    ArchivedVisitsFilter filter) {
	log.debug("Get requested size Archived Courier Visits with filter '{}'", filter.toString());
	return (int) getQueriedCourierVisitsByFilter(query, filter).count();
    }

    @Override
    public void redraftVisits(Collection<ArchivedCourierVisit> visits, String description, User author) {
	log.info("Redraft Archived Courier Visits");
	if (visits == null || description == null || author == null) {
	    log.warn("Redraft Archived Courier Visits is impossible. Some data missing");
	    return;
	}

	for (ArchivedCourierVisit courierVisit : visits) {
	    Optional<ArchivedCourierVisit> persistedVisit = dao.findById(courierVisit.getId());
	    if (persistedVisit.isPresent()) {
		CourierRequest courierRequest = new CourierRequest(persistedVisit.get(), author);
		courierRequest.addHistoryEvent(
			new StringBuilder()
				.append("Цей виклик було перестворено з архівного виклику з причини: \"")
				.append(description)
				.append("\". Прототип має #id ")
				.append(persistedVisit.get().getId().toString())
				.toString(),
			author, LocalDateTime.now());
		Optional<CourierRequest> newCourierRequest = courierRequestService.addRequest(courierRequest, author);
		if (newCourierRequest.isPresent()) {
		    persistedVisit.get().addHistoryEvent(
			    new StringBuilder()
				    .append("Цей виклик було взято за основу для повторного виклику з причини: \"")
				    .append(description)
				    .append("\". Нова чернетка зареєстрована з #id: ")
				    .append(newCourierRequest.get().getId().toString())
				    .toString(),
			    author, LocalDateTime.now());
		    dao.saveAndFlush(persistedVisit.get());
		}
	    }
	}
    }

}
