/**
 * 
 */
package ua.com.sipsoft.model.repository.requests.issued;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.requests.issued.CourierVisitEvent;

/**
 * The Interface CourierVisitEventRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface CourierVisitEventRepository extends JpaRepository<CourierVisitEvent, Long> {

}
