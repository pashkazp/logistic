package ua.com.sipsoft.ui.views.users.prototype;

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
