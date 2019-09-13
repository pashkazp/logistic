package ua.com.sipsoft.services.requests.arcive;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedRouteSheet;
import ua.com.sipsoft.model.repository.requests.archive.ArchivedRouteSheetRepository;

/** The Constant log. */
@Slf4j
@Service
@Transactional
public class ArchiveRouteSheetServiceImpl implements ArchiveRouteSheetService {

	/** The dao. */
	@Autowired
	private ArchivedRouteSheetRepository dao;

	/**
	 * Save.
	 *
	 * @param archivedRouteSheet the archived route sheet
	 * @return the optional
	 */
	@Override
	public Optional<ArchivedRouteSheet> save(ArchivedRouteSheet archivedRouteSheet) {
		log.info("Save Archived Route Sheet");
		if (archivedRouteSheet == null) {
			log.warn("Save Archived Route Sheet impossible. Some data missing");
			return Optional.of(null);
		}
		return Optional.of(dao.saveAndFlush(archivedRouteSheet));
	}
}
