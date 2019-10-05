package ua.com.sipsoft.services.common;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;

import ua.com.sipsoft.model.entity.common.Facility;
import ua.com.sipsoft.model.entity.common.FacilityAddress;
import ua.com.sipsoft.model.entity.user.User;

/**
 * The Interface FacilitiesService.
 *
 * @author Pavlo Degtyaryev
 */
@Service
public interface FacilitiesService {

    /**
     * Gets the by name.
     *
     * @param name the name
     * @return the by name
     */
    List<Facility> getByName(String name);

    /**
     * Drop links to users.
     *
     * @param facility the facility
     * @param users    the users
     * @return the optional
     */
    Optional<Facility> dropLinksToUsers(Facility facility, Collection<User> users);

    /**
     * Save.
     *
     * @param facility the facility
     * @return the optional
     */
    Optional<Facility> save(Facility facility);

    /**
     * Delete.
     *
     * @param facility the facility
     */
    void delete(Facility facility);

    /**
     * Adds the addr to facility.
     *
     * @param facility the facility
     * @param address  the address
     * @return the optional
     */
    Optional<Facility> addAddrToFacility(Facility facility, FacilityAddress address);

    /**
     * Del addr from facility.
     *
     * @param facility the facility
     * @param address  the address
     * @return the optional
     */
    Optional<Facility> delAddrFromFacility(Facility facility, FacilityAddress address);

    /**
     * Adds the links to users.
     *
     * @param facility the facility
     * @param users    the users
     * @return the optional
     */
    Optional<Facility> addLinksToUsers(Facility facility, Collection<User> users);

    /**
     * Gets the queried facilities.
     *
     * @param query the query
     * @param value the value
     * @return the queried facilities
     */
    Stream<Facility> getQueriedFacilities(Query<Facility, Void> query, String value);

    /**
     * Gets the queried facilities count.
     *
     * @param query the query
     * @param value the value
     * @return the queried facilities count
     */
    int getQueriedFacilitiesCount(Query<Facility, Void> query, String value);

    /**
     * Gets the ordered filtered facilities.
     *
     * @param filter the filter
     * @param offset the offset
     * @param limit  the limit
     * @return the ordered filtered facilities
     */
    Stream<Facility> getOrderedFilteredFacilities(String filter, int offset, int limit);

    /**
     * Gets the ordered filtered facilities count.
     *
     * @param filter the filter
     * @return the ordered filtered facilities count
     */
    int getOrderedFilteredFacilitiesCount(String filter);

    /**
     * Fetch by id.
     *
     * @param id the id
     * @return the optional
     */
    Optional<Facility> fetchById(Long id);

    /**
     * Gets the queried facilities.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried facilities
     */
    Stream<Facility> getQueriedFacilities(Query<Facility, FacilitiesFilter> query,
	    FacilitiesFilter filter);

    /**
     * Gets the queried facilities count.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried facilities count
     */
    int getQueriedFacilitiesCount(Query<Facility, FacilitiesFilter> query,
	    FacilitiesFilter filter);

}
