
package ua.com.sipsoft.model.repository.common;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.common.Facility;

/**
 * The Interface FacilityRepository.
 */
@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

	/**
	 * Gets the by name.
	 *
	 * @param name the name
	 * @param pageable the pageable
	 * @return the by name
	 */
	@Query("from Facility f " + "where LOWER ( f.name ) " + " like concat('%',:name,'%') ")
	List<Facility> getByName(String name, Pageable pageable);

}
