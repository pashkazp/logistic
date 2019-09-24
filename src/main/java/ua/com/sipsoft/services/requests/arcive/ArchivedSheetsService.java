package ua.com.sipsoft.services.requests.arcive;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;

import ua.com.sipsoft.model.entity.requests.archive.ArchivedRouteSheet;

/**
 * The Interface ArchivedSheetsService.
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
     * @param query
     * @param filter
     * @return
     */
    Stream<ArchivedRouteSheet> getQueriedArchivedSheetsByFilter(
	    Query<ArchivedRouteSheet, ArchivedSheetFilter> query, ArchivedSheetFilter filter);

    /**
     * @param query
     * @param filter
     * @return
     */
    int ggetQueriedArchivedSheetsByFilterCount(Query<ArchivedRouteSheet, ArchivedSheetFilter> query,
	    ArchivedSheetFilter filter);
}
