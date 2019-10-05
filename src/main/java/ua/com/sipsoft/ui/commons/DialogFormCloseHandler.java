package ua.com.sipsoft.ui.commons;

/**
 * The Interface DialogFormCloseHandler.
 *
 * @param <S> the generic type
 * @param <T> the generic type
 */
public interface DialogFormCloseHandler<S extends DialogForm<T>, T> {

    /**
     * On close.
     *
     * @param event the event
     */
    void onClose(DialogFormCloseEvent<S, T> event);
}
