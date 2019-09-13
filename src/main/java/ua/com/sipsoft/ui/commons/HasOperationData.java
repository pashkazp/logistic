package ua.com.sipsoft.ui.commons;

import com.vaadin.flow.data.binder.Binder;

/**
 * The Interface HasOperationData.
 *
 * @author Pavlo Degtyaryev
 * @param <T> the generic type
 */
public interface HasOperationData<T> {

	/**
	 * Gets the binder.
	 *
	 * @return the binder
	 */
	Binder<T> getBinder();

}
