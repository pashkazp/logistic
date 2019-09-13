package ua.com.sipsoft.model.repository.requests.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisitEvent;

/**
 * The Interface ArchivedCourierVisitEventRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface ArchivedCourierVisitEventRepository extends JpaRepository<ArchivedCourierVisitEvent, Long> {

}
