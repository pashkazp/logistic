package ua.com.sipsoft.services.common;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.common.Facility;
import ua.com.sipsoft.model.entity.common.FacilityAddress;
import ua.com.sipsoft.model.repository.common.FacilityAddressRepository;
import ua.com.sipsoft.services.utils.HasQueryToSortConvertor;

/**
 * The Class FacilityAddrServiceImpl.
 *
 * @author Pavlo Degtyaryev
 */
@Service

/** The Constant log. */
@Slf4j
public class FacilityAddrServiceImpl implements FacilityAddrService, HasQueryToSortConvertor {

    /** The dao. */
    @Autowired
    private FacilityAddressRepository dao;

    /**
     * Gets the facility addresses.
     *
     * @param facility the facility
     * @return the facility addresses
     */
    @Override
    public List<FacilityAddress> getFacilityAddresses(Facility facility) {
	return dao.findByFacility(facility);
    }

    /**
     * Save.
     *
     * @param facilityAddress the facility address
     * @return the optional
     */
    @Override
    public Optional<FacilityAddress> save(FacilityAddress facilityAddress) {
	log.info("Save Facility address: {}", facilityAddress);
	if (facilityAddress == null) {
	    log.warn("Save impossible. Missing some data. ");
	    return Optional.of(null);
	}
	return Optional.of(dao.saveAndFlush(facilityAddress));
    }

    /**
     * Fetch by id.
     *
     * @param id the id
     * @return the optional
     */
    @Override
    public Optional<FacilityAddress> fetchById(Long id) {
	log.debug("Get Facility Address by id: '{}'", id);
	if (id == null) {
	    log.debug("Get Facility Address by id is impossible. id is null.");
	    return Optional.of(null);
	}
	try {
	    return dao.findById(id);
	} catch (Exception e) {
	    log.error("The Facility Address by id is not received for a reason: {}", e.getMessage());
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
    private boolean isEntityPassFilter(FacilityAddress entity, FacilityAddressFilter filter) {
	return entity.getAddress().concat(entity.getAddressesAlias()).toLowerCase()
		.contains(filter.getAddress() == null ? "" : filter.getAddress().toLowerCase());
    }

    /**
     * Gets the queried facility addr by filter.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried facility addr by filter
     */
    @Override
    public Stream<FacilityAddress> getQueriedFacilityAddrByFilter(Query<FacilityAddress, FacilityAddressFilter> query,
	    FacilityAddressFilter filter) {
	log.debug("Get requested page Facility Addresses with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
		query.getOffset(), query.getLimit(), query.getSortOrders(), filter);
	if (query == null || filter == null) {
	    log.debug("Get Drafr Facility Addresses is impossible. Miss some data.");
	    return Stream.empty();
	}
	try {
	    if (filter.getFacilityId() == null) {
		return Stream.empty();
	    }
	    return dao.getByFacilityId(filter.getFacilityId(), queryToSort(query))
		    .stream()
		    .filter(entity -> isEntityPassFilter(entity, filter))
		    .skip(query.getOffset())
		    .limit(query.getLimit());
	} catch (Exception e) {
	    log.error("The Facility Addresses list was not received for a reason: {}", e.getMessage());
	}
	return Stream.empty();
    }

    /**
     * Gets the queried facility addr by filter count.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried facility addr by filter count
     */
    @Override
    public int getQueriedFacilityAddrByFilterCount(Query<FacilityAddress, FacilityAddressFilter> query,
	    FacilityAddressFilter filter) {
	log.debug("Get requested size Facility Addresses  with filter '{}'", filter);
	return (int) getQueriedFacilityAddrByFilter(query, filter).count();
    }

}
