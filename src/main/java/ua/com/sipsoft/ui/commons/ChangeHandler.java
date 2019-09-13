package ua.com.sipsoft.ui.commons;

/**
 * The Interface ChangeHandler.
 *
 * @author Pavlo Degtyaryev
 * @param <T> the generic type
 */
public interface ChangeHandler<T> {

	/**
	 * On change.
	 *
	 * @param value the value
	 */
	void onChange(T value);
}
