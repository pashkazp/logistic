package ua.com.sipsoft.services.utils;

import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;

/**
 * The Interface HasQueryToSortConvertor.
 */
public interface HasQueryToSortConvertor {

	/**
	 * Query to sort.
	 *
	 * @param query the query
	 * @return the sort
	 */
	default Sort queryToSort(Query<?, ?> query) {
		return Sort.by(query.getSortOrders().stream()
				.map(order -> new Order(
						order.getDirection() == SortDirection.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC,
						order.getSorted()))
				.collect(Collectors.toList()));
	}
}
