package ua.com.sipsoft.services.requests.arcive;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisit;

/**
 * The Interface ArchivedCourierVisitService.
 */
@Service
public interface ArchivedCourierVisitService {

	/**
	 * Save.
	 *
	 * @param acv the acv
	 * @return the optional
	 */
	Optional<ArchivedCourierVisit> save(ArchivedCourierVisit acv);

}
