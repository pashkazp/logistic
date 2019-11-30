package ua.com.sipsoft.services.requests.arcive;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;

import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisit;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.utils.EntityFilter;

/**
 * The Interface ArchivedVisitsService.
 *
 * @author Pavlo Degtyaryev
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
     * Gets the queried courier visits by filter.
     *
     * @param query the query
     * @return the queried courier visits by filter
     */
    Stream<ArchivedCourierVisit> getQueriedCourierVisitsByFilter(
	    Query<ArchivedCourierVisit, EntityFilter<ArchivedCourierVisit>> query);

    /**
     * Gets the queried courier visits by filter count.
     *
     * @param query the query
     * @return the queried courier visits by filter count
     */
    int getQueriedCourierVisitsByFilterCount(Query<ArchivedCourierVisit, EntityFilter<ArchivedCourierVisit>> query);

    /**
     * Gets the queried courier visits by filter by sheet id.
     *
     * @param query   the query
     * @param sheetId the sheet id
     * @return the queried courier visits by filter by sheet id
     */
    Stream<ArchivedCourierVisit> getQueriedCourierVisitsByFilterBySheetId(
	    Query<ArchivedCourierVisit, EntityFilter<ArchivedCourierVisit>> query, Long sheetId);

    /**
     * Gets the queried courier visits by filter by sheet id count.
     *
     * @param query   the query
     * @param sheetId the sheet id
     * @return the queried courier visits by filter by sheet id count
     */
    int getQueriedCourierVisitsByFilterBySheetIdCount(
	    Query<ArchivedCourierVisit, EntityFilter<ArchivedCourierVisit>> query, Long sheetId);

    /**
     * Redraft visits.
     *
     * @param visits      the visits
     * @param description the description
     * @param author      the author
     */
    void redraftVisits(Collection<ArchivedCourierVisit> visits, String description, User author);

}
