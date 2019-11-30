package ua.com.sipsoft.services.requests.arcive;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;

import ua.com.sipsoft.model.entity.requests.archive.ArchivedRouteSheet;
import ua.com.sipsoft.services.utils.EntityFilter;

/**
 * The Interface ArchivedSheetsService.
 *
 * @author Pavlo Degtyaryev
 */
@Service
public interface ArchivedSheetsService {

    /**
     * Save.
     *
     * @param archivedRouteSheet the archived route sheet
     * @return the optional
     */
    Optional<ArchivedRouteSheet> save(ArchivedRouteSheet archivedRouteSheet);

    /**
     * Gets the queried archived sheets by filter.
     *
     * @param query the query
     * @return the queried archived sheets by filter
     */
    Stream<ArchivedRouteSheet> getQueriedArchivedSheetsByFilter(
	    Query<ArchivedRouteSheet, EntityFilter<ArchivedRouteSheet>> query);

    /**
     * Gets the queried archived sheets by filter count.
     *
     * @param query the query
     * @return the queried archived sheets by filter count
     */
    int getQueriedArchivedSheetsByFilterCount(Query<ArchivedRouteSheet, EntityFilter<ArchivedRouteSheet>> query);
}
