package ua.com.sipsoft.services.common;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;

import ua.com.sipsoft.model.entity.common.Facility;
import ua.com.sipsoft.model.entity.common.FacilityAddress;

/**
 * The Interface FacilityAddrService.
 *
 * @author Pavlo Degtyaryev
 */
@Service
public interface FacilityAddrService {

	/**
	 * Gets the facility addresses.
	 *
	 * @param facility the facility
	 * @return the facility addresses
	 */
	public List<FacilityAddress> getFacilityAddresses(Facility facility);

	/**
	 * Save.
	 *
	 * @param operationData the operation data
	 * @return the optional
	 */
	Optional<FacilityAddress> save(FacilityAddress operationData);

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	public Optional<FacilityAddress> fetchById(Long id);

	/**
	 * Gets the queried facility addr by filter.
	 *
	 * @param query  the query
	 * @param filter the filter
	 * @return the queried facility addr by filter
	 */
	Stream<FacilityAddress> getQueriedFacilityAddrByFilter(Query<FacilityAddress, FacilityAddressFilter> query,
			FacilityAddressFilter filter);

	/**
	 * Gets the queried facility addr by filter count.
	 *
	 * @param query  the query
	 * @param filter the filter
	 * @return the queried facility addr by filter count
	 */
	int getQueriedFacilityAddrByFilterCount(Query<FacilityAddress, FacilityAddressFilter> query,
			FacilityAddressFilter filter);

}
