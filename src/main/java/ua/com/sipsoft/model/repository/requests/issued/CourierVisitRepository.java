package ua.com.sipsoft.model.repository.requests.issued;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.requests.issued.CourierVisit;

/**
 * The Interface CourierVisitRepository.
 *
 * @author Pavlo Degtyaryev
 * @version 1.0
 */
@Repository
public interface CourierVisitRepository extends JpaRepository<CourierVisit, Long> {

    /**
     * Gets the courier visit by issued route sheet id.
     *
     * @param id   the id
     * @param sort the sort
     * @return the courier visit by issued route sheet id
     */
    @Query(" SELECT request "
	    + " FROM IssuedRouteSheet issued "
	    + " join issued.requests request "
	    + " WHERE issued.id = :sheetid ")
    List<CourierVisit> getCourierVisitByIssuedRouteSheetId(@Param("sheetid") Long id, Sort sort);
}
