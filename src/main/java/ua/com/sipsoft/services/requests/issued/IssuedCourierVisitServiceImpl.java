package ua.com.sipsoft.services.requests.issued;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.data.provider.Query;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.model.entity.requests.issued.CourierVisit;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.model.repository.requests.issued.CourierVisitRepository;
import ua.com.sipsoft.services.requests.draft.CourierRequestService;
import ua.com.sipsoft.services.utils.HasQueryToSortConvertor;
import ua.com.sipsoft.services.utils.OffsetBasedPageRequest;
import ua.com.sipsoft.utils.CourierVisitState;
import ua.com.sipsoft.utils.history.CourierVisitSnapshot;
import ua.com.sipsoft.utils.security.SecurityUtils;

@Slf4j
@Service
@Transactional
public class IssuedCourierVisitServiceImpl implements IssuedCourierVisitService, HasQueryToSortConvertor {

    /** The dao. */
    @Autowired
    private CourierVisitRepository dao;

    /** The courier request service. */
    @Autowired
    CourierRequestService courierRequestService;

    /**
     * Gets the queried courier visits.
     *
     * @param query the query
     * @param value the value
     * @return the queried courier visits
     */
    @Override
    public Stream<CourierVisit> getQueriedCourierVisits(Query<CourierVisit, Void> query, String value) {
	log.debug("Get requested page Courier Visits with offset {} and limit {}", query.getOffset(),
		query.getLimit());
	try {
	    Pageable pageable = new OffsetBasedPageRequest(query.getOffset(), query.getLimit(), queryToSort(query));
	    return dao
		    .findAll(pageable).stream();
	} catch (Exception e) {
	    log.error("The Courier Visits list was not received for a reason: {}", e.getMessage());
	}
	return Stream.empty();
    }

    /**
     * Gets the queried courier visits count.
     *
     * @param query the query
     * @param value the value
     * @return the queried courier visits count
     */
    @Override
    public int getQueriedCourierVisitsCount(Query<CourierVisit, Void> query, String value) {
	return dao.findAll().size();
    }

    /**
     * Move to archive.
     *
     * @param requests the requests
     * @param value    the value
     * @param user     the user
     */
    @Override
    public void moveToArchive(List<CourierVisit> requests, String value, User user) {
	// TODO Auto-generated method stub

    }

    /**
     * Mark visits as completed.
     *
     * @param courierVisits the courier visits
     * @param author        the author
     * @return the collection
     */
    @Override
    public Collection<CourierVisit> markVisitsAsCompleted(Collection<CourierVisit> courierVisits, User author) {
	log.info("Mark Courier Visit(s) as completed");
	if (courierVisits == null || courierVisits.isEmpty() || author == null) {
	    log.warn("Mark Courier Visits(s) is impossible. Some data missing or nothing to do");
	    return Collections.emptyList();
	}
	return courierVisits.stream()
		.filter(CourierVisit::isActive)
		.map(courierVisit -> {
		    courierVisit.addHistoryEvent("Візит кур'єра маркований як виконаний", author, LocalDateTime.now());
		    courierVisit.setState(CourierVisitState.COMPLETED);
		    return dao.saveAndFlush(courierVisit);
		})
		.collect(Collectors.toList());

    }

    /**
     * Mark visits as cancelled.
     *
     * @param courierVisits the courier visits
     * @param description   the description
     * @param author        the author
     * @return the collection
     */
    @Override
    public Collection<CourierVisit> markVisitsAsCancelled(Collection<CourierVisit> courierVisits, String description,
	    User author) {
	log.info("Mark Courier Visit(s) as cancelled");
	if (courierVisits == null || courierVisits.isEmpty() || author == null) {
	    log.warn("Mark Courier Visits(s) is impossible. Some data missing or nothing to do");
	    return Collections.emptyList();
	}
	return courierVisits.stream()
		.filter(CourierVisit::isActive)
		.map(courierVisit -> {
		    courierVisit.addHistoryEvent(new StringBuilder()
			    .append("Скасовано з причини \"")
			    .append(description)
			    .append("\".")
			    .toString(), author, LocalDateTime.now());
		    courierVisit.setState(CourierVisitState.CANCELLED);
		    return (dao.saveAndFlush(courierVisit));
		})
		.collect(Collectors.toList());
    }

    /**
     * Fetch by id.
     *
     * @param id the id
     * @return the optional
     */
    @Override
    public Optional<CourierVisit> fetchById(Long id) {
	log.debug("Get Courier Visit by id: '{}'", id);
	if (id == null) {
	    log.debug("Get Courier Visit by id is impossible. id is null.");
	    return Optional.of(null);
	}
	try {
	    return dao.findById(id);
	} catch (Exception e) {
	    log.error("The Courier Visit by id is not received for a reason: {}", e.getMessage());
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
    private boolean isEntityPassFilter(CourierVisit entity, CourierVisitFilter filter) {
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
    public Stream<CourierVisit> getQueriedCourierVisitsByFilter(Query<CourierVisit, CourierVisitFilter> query,
	    CourierVisitFilter filter) {
	log.debug(
		"Get requested page Courier Visits By Sheet ID with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
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
		return dao.getCourierVisitByIssuedRouteSheetId(filter.getSheetId(), queryToSort(query))
			.stream()
			.filter(entity -> isEntityPassFilter(entity, filter))
			.skip(query.getOffset())
			.limit(query.getLimit());
	    }
	} catch (Exception e) {
	    log.error("The Courier Visits list was not received for a reason: {}", e.getMessage());
	}
	return Stream.empty();
    }

    /**
     * Gets the queried courier visits by filter count.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried courier visits by filter count
     */
    @Override
    public int getQueriedCourierVisitsByFilterCount(Query<CourierVisit, CourierVisitFilter> query,
	    CourierVisitFilter filter) {
	log.debug("Get requested size Courier Visits with filter '{}'", filter.toString());
	return (int) getQueriedCourierVisitsByFilter(query, filter).count();
    }

    /**
     * Redraft visits.
     *
     * @param visits      the visits
     * @param description the description
     * @param author      the author
     */
    @Override
    public void redraftVisits(List<CourierVisit> visits, String description, User author) {
	log.info("Redraft Courier Visits");
	if (visits == null || description == null || author == null) {
	    log.warn("Redraft Courier Visits is impossible. Some data missing");
	    return;
	}

	for (CourierVisit courierVisit : visits) {
	    Optional<CourierVisit> persistedVisit = dao.findById(courierVisit.getId());
	    if (persistedVisit.isPresent() && !(persistedVisit.get().isActive())) {
		CourierRequest courierRequest = new CourierRequest(persistedVisit.get(), author);
		courierRequest.addHistoryEvent(
			new StringBuilder()
				.append("Цей виклик було перестворено з виданого виклику з причини: \"")
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

    /**
     * Register changes and save.
     *
     * @param visit         the visit
     * @param visitSnapshot the visit snapshot
     * @param author        the author
     * @return the optional
     */
    @Override
    public Optional<CourierVisit> registerChangesAndSave(CourierVisit visit, CourierVisitSnapshot visitSnapshot,
	    User author) {
	log.info("Save courier Request: " + visit);
	if (visit == null || visitSnapshot == null) {
	    log.warn("Save Courier Request impossible. Some data missing: {}", visit);
	    return Optional.of(null);
	}
	visitSnapshot.fillHistoryChangesTo(visit, SecurityUtils.getUser());
	return Optional.of(dao.saveAndFlush(visit));
    }

}
