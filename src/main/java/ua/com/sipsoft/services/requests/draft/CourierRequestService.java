package ua.com.sipsoft.services.requests.draft;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;

import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.utils.history.CourierRequestSnapshot;

/**
 * The Interface CourierRequestService.
 */
@Service
public interface CourierRequestService {

    /**
     * Register changes and save.
     *
     * @param request         the request
     * @param requestSnapshot the request snapshot
     * @param author          the author
     * @return the optional
     */
    Optional<CourierRequest> registerChangesAndSave(CourierRequest request, CourierRequestSnapshot requestSnapshot,
	    User author);

    /**
     * Save.
     *
     * @param request the request
     * @return the optional
     */
    Optional<CourierRequest> save(CourierRequest request);

    /**
     * Move courier requests to archive.
     *
     * @param requests    the requests
     * @param description the description
     * @param author      the author
     */
    void moveCourierRequestsToArchive(List<CourierRequest> requests, String description, User author);

    /**
     * Adds the request.
     *
     * @param request the request
     * @param author  the author
     * @return the optional
     */
    Optional<CourierRequest> addRequest(CourierRequest request, User author);

    /**
     * Delete.
     *
     * @param courierRequest the courier request
     */
    void delete(Collection<CourierRequest> courierRequest);

    /**
     * Fetch by id.
     *
     * @param id the id
     * @return the optional
     */
    Optional<CourierRequest> fetchById(Long id);

    /**
     * Gets the queried courier requests by filter.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried courier requests by filter
     */
    Stream<CourierRequest> getQueriedCourierRequestsByFilter(Query<CourierRequest, CourierRequestFilter> query,
	    CourierRequestFilter filter);

    /**
     * Gets the queried courier requests by filter count.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried courier requests by filter count
     */
    int getQueriedCourierRequestsByFilterCount(Query<CourierRequest, CourierRequestFilter> query,
	    CourierRequestFilter filter);

}
