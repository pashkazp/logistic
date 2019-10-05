package ua.com.sipsoft.model.repository.requests.draft;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;
import ua.com.sipsoft.model.entity.requests.draft.DraftRouteSheet;

/**
 * The Interface DraftRouteSheetRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface DraftRouteSheetRepository extends JpaRepository<DraftRouteSheet, Long> {

    /**
     * Gets the by requests.
     *
     * @param requests the requests
     * @return the by requests
     */
    @Query("select distinct rs from DraftRouteSheet rs left join rs.requests as r where r in (:requests) ")
    List<DraftRouteSheet> getByRequests(@Param("requests") Collection<CourierRequest> requests);

}
