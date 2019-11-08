package ua.com.sipsoft.ui.commons.forms.viewform;

/**
 * The Interface DialogFormCloseHandler.
 *
 * @param <S> the generic type
 * @param <T> the generic type
 */
public interface ViewFormCloseHandler<S extends ViewForm> {

    /**
     * On close.
     *
     * @param event the event
     */
    void onClose(ViewFormCloseEvent<S> event);
}
