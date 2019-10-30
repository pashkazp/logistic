package ua.com.sipsoft.ui.commons.dialogform;

/**
 * The Interface DialogFormCloseHandler.
 *
 * @param <S> the generic type
 * @param <T> the generic type
 */
public interface DialogFormCloseHandler<S extends DialogForm> {

    /**
     * On close.
     *
     * @param event the event
     */
    void onClose(DialogFormCloseEvent<S> event);
}
