package ua.com.sipsoft.model.repository.requests.draft;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.requests.draft.DraftRouteSheetEvent;

/**
 * The Interface DraftRouteSheeEventRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface DraftRouteSheeEventRepository extends JpaRepository<DraftRouteSheetEvent, Long> {

}
