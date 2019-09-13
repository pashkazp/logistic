package ua.com.sipsoft.model.repository.requests.issued;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.requests.issued.IssuedRouteSheetEvent;

/**
 * The Interface IssuedRouteSheetEventRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface IssuedRouteSheetEventRepository extends JpaRepository<IssuedRouteSheetEvent, Long> {

}
