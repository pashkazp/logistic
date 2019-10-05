package ua.com.sipsoft.services.requests.issued;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;

import ua.com.sipsoft.model.entity.requests.issued.CourierVisit;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.utils.history.CourierVisitSnapshot;

/**
 * The Interface IssuedCourierVisitService.
 */
@Service
public interface IssuedCourierVisitService {

    /**
     * Gets the queried courier visits.
     *
     * @param query the query
     * @param value the value
     * @return the queried courier visits
     */
    Stream<CourierVisit> getQueriedCourierVisits(Query<CourierVisit, Void> query, String value);

    /**
     * Gets the queried courier visits count.
     *
     * @param query the query
     * @param value the value
     * @return the queried courier visits count
     */
    int getQueriedCourierVisitsCount(Query<CourierVisit, Void> query, String value);

    /**
     * Register changes and save.
     *
     * @param visit         the visit
     * @param visitSnapshot the visit snapshot
     * @param author        the author
     * @return the optional
     */
    Optional<CourierVisit> registerChangesAndSave(CourierVisit visit, CourierVisitSnapshot visitSnapshot,
	    User author);

    /**
     * Move to archive.
     *
     * @param visits the visits
     * @param value  the value
     * @param author the author
     */
    void moveToArchive(List<CourierVisit> visits, String value, User author);

    /**
     * Redraft visits.
     *
     * @param visits the visits
     * @param value  the value
     * @param author the author
     */
    void redraftVisits(List<CourierVisit> visits, String value, User author);

    /**
     * Mark visits as completed.
     *
     * @param courierVisits the courier visits
     * @param author        the author
     * @return the collection
     */
    Collection<CourierVisit> markVisitsAsCompleted(Collection<CourierVisit> courierVisits, User author);

    /**
     * Mark visits as cancelled.
     *
     * @param courierVisits the courier visits
     * @param description   the description
     * @param author        the author
     * @return the collection
     */
    Collection<CourierVisit> markVisitsAsCancelled(Collection<CourierVisit> courierVisits, String description,
	    User author);

    /**
     * Fetch by id.
     *
     * @param id the id
     * @return the optional
     */
    Optional<CourierVisit> fetchById(Long id);

    /**
     * Gets the queried courier visits by filter.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried courier visits by filter
     */
    Stream<CourierVisit> getQueriedCourierVisitsByFilter(Query<CourierVisit, CourierVisitFilter> query,
	    CourierVisitFilter filter);

    /**
     * Gets the queried courier visits by filter count.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried courier visits by filter count
     */
    int getQueriedCourierVisitsByFilterCount(Query<CourierVisit, CourierVisitFilter> query,
	    CourierVisitFilter filter);

}
