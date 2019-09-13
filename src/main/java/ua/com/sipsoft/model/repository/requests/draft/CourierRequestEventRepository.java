package ua.com.sipsoft.model.repository.requests.draft;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.requests.draft.CourierRequestEvent;

/**
 * The Interface CourierRequestEventRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface CourierRequestEventRepository extends JpaRepository<CourierRequestEvent, Long> {

}
