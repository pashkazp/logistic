package ua.com.sipsoft.services.requests.arcive;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;

import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisit;
import ua.com.sipsoft.model.entity.user.User;

/**
 * The Interface ArchivedVisitsService.
 */
@Service
public interface ArchivedVisitsService {

    /**
     * Save.
     *
     * @param acv the acv
     * @return the optional
     */
    Optional<ArchivedCourierVisit> save(ArchivedCourierVisit acv);

    /**
     * @param query
     * @param filter
     * @return
     */
    Stream<ArchivedCourierVisit> getQueriedCourierVisitsByFilter(
	    Query<ArchivedCourierVisit, ArchivedVisitsFilter> query, ArchivedVisitsFilter filter);

    /**
     * @param query
     * @param filter
     * @return
     */
    int getQueriedCourierVisitsByFilterCount(Query<ArchivedCourierVisit, ArchivedVisitsFilter> query,
	    ArchivedVisitsFilter filter);

    /**
     * @param visits
     * @param description
     * @param user
     */
    void redraftVisits(Collection<ArchivedCourierVisit> visits, String description, User author);

}
