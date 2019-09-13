package ua.com.sipsoft.model.repository.requests.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.requests.archive.ArchivedRouteSheetEvent;

/**
 * The Interface ArchivedRouteSheetEventRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface ArchivedRouteSheetEventRepository extends JpaRepository<ArchivedRouteSheetEvent, Long> {

}
