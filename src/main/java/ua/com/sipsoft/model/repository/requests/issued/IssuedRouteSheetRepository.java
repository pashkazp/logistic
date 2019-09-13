package ua.com.sipsoft.model.repository.requests.issued;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.requests.issued.IssuedRouteSheet;

/**
 * The Interface IssuedRouteSheetRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface IssuedRouteSheetRepository extends JpaRepository<IssuedRouteSheet, Long> {

}
