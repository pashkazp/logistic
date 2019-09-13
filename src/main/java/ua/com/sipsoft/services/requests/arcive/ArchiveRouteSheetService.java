package ua.com.sipsoft.services.requests.arcive;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.sipsoft.model.entity.requests.archive.ArchivedRouteSheet;

/**
 * The Interface ArchiveRouteSheetService.
 */
@Service
public interface ArchiveRouteSheetService {

	/**
	 * Save.
	 *
	 * @param archivedRouteSheet the archived route sheet
	 * @return the optional
	 */
	Optional<ArchivedRouteSheet> save(ArchivedRouteSheet archivedRouteSheet);
}
