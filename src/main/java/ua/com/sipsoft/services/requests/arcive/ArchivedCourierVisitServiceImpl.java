package ua.com.sipsoft.services.requests.arcive;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisit;
import ua.com.sipsoft.model.repository.requests.archive.ArchivedCourierVisitRepository;

/** The Constant log. */
@Slf4j
@Service
@Transactional
public class ArchivedCourierVisitServiceImpl implements ArchivedCourierVisitService {

	/** The dao. */
	@Autowired
	private ArchivedCourierVisitRepository dao;

	/**
	 * Save.
	 *
	 * @param acv the acv
	 * @return the optional
	 */
	@Override
	public Optional<ArchivedCourierVisit> save(ArchivedCourierVisit acv) {
		log.info("Save courier Archived Courier Visit: " + acv);
		if (acv == null) {
			log.warn("Save Archive Courier Visit impossible. Some data missing");
			return Optional.of(null);
		}
		return Optional.of(dao.saveAndFlush(acv));
	}

}
