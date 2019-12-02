package ua.com.sipsoft.services.common;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.common.Facility;
import ua.com.sipsoft.model.entity.common.FacilityAddress;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.model.repository.common.FacilityRepository;
import ua.com.sipsoft.services.utils.EntityFilter;
import ua.com.sipsoft.services.utils.HasQueryToSortConvertor;
import ua.com.sipsoft.services.utils.OffsetBasedPageRequest;
import ua.com.sipsoft.ui.commons.AppNotificator;

/**
 * The Class FacilitiesServiceImpl.
 *
 * @author Pavlo Degtyaryev
 */

@Slf4j
@Service
@Transactional
public class FacilitiesServiceImpl implements FacilitiesService, HasQueryToSortConvertor {

    /** The dao. */
    @Autowired
    private FacilityRepository dao;

    /**
     * Gets the by name.
     *
     * @param name the name
     * @return the by name
     */
    @Override
    public List<Facility> getByName(String name) {
	return dao.getByName(name.toLowerCase(), null);
    }

    /**
     * Drop links to users.
     *
     * @param facility the facility
     * @param users    the users
     * @return the optional
     */
    @Override
    public Optional<Facility> dropLinksToUsers(Facility facility, Collection<User> users) {
	log.info("Drop Links To Users from Facility: {}", facility);
	if (facility == null || users == null || users.isEmpty()) {
	    log.warn("Drop impossible. Missing some data. ");
	    return Optional.of(null);
	}
	for (User user : users) {
	    facility.removeUser(user);
	}
	try {
	    return save(facility);
	} catch (ObjectOptimisticLockingFailureException e) {
	    AppNotificator.notifyError("ObjectOptimisticLockingFailureException");
	    return Optional.of(null);
	}
    }

    /**
     * Adds the links to users.
     *
     * @param facility the facility
     * @param users    the users
     * @return the optional
     */
    @Override
    public Optional<Facility> addLinksToUsers(Facility facility, Collection<User> users) {
	log.info("Add Links from Users to Facility: {}", facility);
	if (facility == null || users == null || users.isEmpty()) {
	    log.warn("Add is impossible. Missing some data. ");
	    return Optional.of(null);
	}
	for (User user : users) {
	    facility.addUser(user);
	}
	try {
	    return save(facility);
	} catch (Exception e) {
	    AppNotificator.notifyError(e.toString());
	    return Optional.of(null);
	}
    }

    /**
     * Save.
     *
     * @param facility the facility
     * @return the optional
     */
    @Override
    public Optional<Facility> save(Facility facility) {
	log.info("Save Facility: {}", facility);
	if (facility == null) {
	    log.warn("Save impossible. Missing some data. ");
	    return Optional.of(null);
	}
	return Optional.of(dao.saveAndFlush(facility));
    }

    /**
     * Delete.
     *
     * @param facility the facility
     */
    @Override
    public void delete(Facility facility) {
	log.info("Delete Facility: {}", facility);
	if (facility == null) {
	    log.warn("Delete impossible. Missing some data. ");
	}
	try {
	    dao.delete(facility);
	} catch (Exception e) {
	    AppNotificator.notifyError(e.toString());
	}
    }

    /**
     * Adds the addr to facility.
     *
     * @param facility the facility
     * @param address  the address
     * @return the optional
     */
    @Override
    public Optional<Facility> addAddrToFacility(Facility facility, FacilityAddress address) {
	log.info("Add Addr [{}] to Facility: [{}]", address, facility);
	if (facility == null || address == null) {
	    log.warn("Addition impossible. Missing some data. ");
	    return Optional.of(null);
	}
	address.setFacility(facility);
	facility.addFacilityAddress(address);
	try {
	    return save(facility);
	} catch (Exception e) {
	    AppNotificator.notifyError(e.toString());
	    return Optional.of(null);
	}
    }

    /**
     * Del addr from facility.
     *
     * @param facility the facility
     * @param address  the address
     * @return the optional
     */
    @Override
    public Optional<Facility> delAddrFromFacility(Facility facility, FacilityAddress address) {
	log.info("Remove Addr [{}] from Facility: [{}]", address, facility);
	if (facility == null || address == null) {
	    log.warn("Remove is impossible. Missing some data. ");
	    return Optional.of(null);
	}
	facility.delFacilityAddress(address);
	try {
	    return save(facility);
	} catch (Exception e) {
	    AppNotificator.notifyError(e.toString());
	    return Optional.of(null);
	}
    }

    /**
     * Gets the queried facilities stream.
     *
     * @param query the query
     * @param value the value
     * @return the queried facilities stream
     */
    public Stream<Facility> getQueriedFacilitiesStream(Query<Facility, Void> query, String value) {
	log.debug("Get requested page Facilitys by Name '{}' with offset {} and limit {}", value, query.getOffset(),
		query.getLimit());
	try {
	    Pageable pageable = new OffsetBasedPageRequest(query.getOffset(), query.getLimit(), queryToSort(query));
	    return dao
		    .getByName(value, pageable).stream();
	} catch (Exception e) {
	    log.error("The Facilitys list was not received for a reason: {}", e.getMessage());
	}
	return Stream.empty();
    }

    /**
     * Gets the queried facilities.
     *
     * @param query the query
     * @param value the value
     * @return the queried facilities
     */
    @Override
    public Stream<Facility> getQueriedFacilities(Query<Facility, Void> query, String value) {
	return getQueriedFacilitiesStream(query, value);

    }

    /**
     * Gets the queried facilities count.
     *
     * @param query the query
     * @param value the value
     * @return the queried facilities count
     */
    @Override
    public int getQueriedFacilitiesCount(Query<Facility, Void> query, String value) {
	return ((Long) getQueriedFacilitiesStream(query, value).count()).intValue();
    }

    /**
     * Gets the ordered filtered facilities.
     *
     * @param filter the filter
     * @param offset the offset
     * @param limit  the limit
     * @return the ordered filtered facilities
     */
    @Override
    public Stream<Facility> getOrderedFilteredFacilities(String filter, int offset, int limit) {
	return getQueriedFacilitiesStream(new Query<>(offset, limit, QuerySortOrder.asc("name").build(), null, null),
		filter);
    }

    /**
     * Gets the ordered filtered facilities count.
     *
     * @param filter the filter
     * @return the ordered filtered facilities count
     */
    @Override
    public int getOrderedFilteredFacilitiesCount(String filter) {
	return dao.getByName(filter, null).size();
    }

    /**
     * Fetch by id.
     *
     * @param id the id
     * @return the optional
     */
    @Override
    public Optional<Facility> fetchById(Long id) {
	log.debug("Get Facility by id: '{}'", id);
	if (id == null) {
	    log.debug("Get Facility by id is impossible. id is null.");
	    return Optional.of(null);
	}
	try {
	    return dao.findById(id);
	} catch (Exception e) {
	    log.error("The Facility by id is not received for a reason: {}", e.getMessage());
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
    private boolean isEntityPassFilter(Facility entity, FacilitiesFilter filter) {
	if ((filter.getUsers() != null) && !(CollectionUtils.containsAny(filter.getUsers(), entity.getUsers()))) {
	    return false;
	}
	return entity.getName().toLowerCase()
		.contains(filter.getName() == null ? "" : filter.getName().toLowerCase());
    }

    /**
     * Gets the queried facilities.
     *
     * @param query the query
     * @return the queried facilities
     */
    @Override
    public Stream<Facility> getQueriedFacilities(Query<Facility, EntityFilter<Facility>> query) {
	log.debug(
		"Get requested page Facilities with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
		query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
	if (query == null || query.getFilter().isEmpty()) {
	    log.debug("Get Facilities is impossible. Miss some data.");
	    return Stream.empty();
	}
	try {
	    return dao.findAll(queryToSort(query))
		    .stream()
		    .filter(entity -> query.getFilter().get().isPass(entity))
		    .skip(query.getOffset())
		    .limit(query.getLimit());
	} catch (Exception e) {
	    log.error("The Facilities list was not received for a reason: {}", e.getMessage());
	}
	return Stream.empty();
    }

    /**
     * Gets the queried facilities count.
     *
     * @param query the query
     * @return the queried facilities count
     */
    @Override
    public int getQueriedFacilitiesCount(Query<Facility, EntityFilter<Facility>> query) {
	log.debug("Get requested size Facilities with filter '{}'", query.getFilter().get().toString());
	return (int) getQueriedFacilities(query).count();
    }

}
