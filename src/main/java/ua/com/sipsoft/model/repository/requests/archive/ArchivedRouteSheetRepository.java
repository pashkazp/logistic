package ua.com.sipsoft.model.repository.requests.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.requests.archive.ArchivedRouteSheet;

/**
 * The Interface ArchivedRouteSheetRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface ArchivedRouteSheetRepository extends JpaRepository<ArchivedRouteSheet, Long> {

}
