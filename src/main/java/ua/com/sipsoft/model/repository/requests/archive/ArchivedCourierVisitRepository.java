package ua.com.sipsoft.model.repository.requests.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisit;

/**
 * The Interface ArchivedCourierVisitRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface ArchivedCourierVisitRepository extends JpaRepository<ArchivedCourierVisit, Long> {

}
