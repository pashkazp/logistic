package ua.com.sipsoft.model.repository.requests.draft;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.requests.draft.CourierRequest;

/**
 * The Interface CourierRequestRepository.
 *
 * @author Pavlo Degtyaryev
 * @version 1.0
 */
@Repository
public interface CourierRequestRepository extends JpaRepository<CourierRequest, Long> {

    /**
     * Gets the courier request by drat rouet sheet id.
     *
     * @param id   the id
     * @param sort the sort
     * @return the courier request by drat rouet sheet id
     */
    @Query(" SELECT request "
	    + " FROM DraftRouteSheet draft "
	    + " join draft.requests request "
	    + " WHERE draft.id = :sheetid ")
    List<CourierRequest> getCourierRequestByDratRequestSheetId(@Param("sheetid") Long id, Sort sort);
}
