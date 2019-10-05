package ua.com.sipsoft.model.repository.common;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.common.Facility;
import ua.com.sipsoft.model.entity.common.FacilityAddress;

/**
 * The Interface FacilityAddressRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface FacilityAddressRepository extends JpaRepository<FacilityAddress, Long> {

    /**
     * Find by facility.
     *
     * @param facility the facility
     * @return the list
     */
    List<FacilityAddress> findByFacility(Facility facility);

    /**
     * Gets the by facility id.
     *
     * @param id   the id
     * @param sort the sort
     * @return the by facility id
     */
    @Query(" FROM FacilityAddress fa "
	    + " WHERE fa.facility.id = :facilityid ")
    List<FacilityAddress> getByFacilityId(@Param("facilityid") Long id, Sort sort);
}
