package ua.com.sipsoft.model.repository.requests.archive;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisit;

/**
 * The Interface ArchivedCourierVisitRepository.
 *
 * @author Pavlo Degtyaryev
 * @version 1.0
 */
@Repository
public interface ArchivedCourierVisitRepository extends JpaRepository<ArchivedCourierVisit, Long> {

    /**
     * Gets the courier visit by issued route sheet id.
     *
     * @param id   the id
     * @param sort the sort
     * @return the courier visit by issued route sheet id
     */
    @Query(" SELECT request "
	    + " FROM ArchivedRouteSheet archived "
	    + " join archived.requests request "
	    + " WHERE archived.id = :sheetid ")
    List<ArchivedCourierVisit> getCourierVisitByArchivedSheetId(@Param("sheetid") Long id, Sort sort);

}
