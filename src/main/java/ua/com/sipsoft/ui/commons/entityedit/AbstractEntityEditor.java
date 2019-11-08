package ua.com.sipsoft.ui.commons.entityedit;

import com.vaadin.flow.component.formlayout.FormLayout;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class AbstractEntityEditor.
 *
 * @param <T> the generic type
 */
public abstract class AbstractEntityEditor<T> extends FormLayout {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -166516810158550418L;

    /** The operation data. */

    /**
     * Gets the operation data.
     *
     * @return the operation data
     */
    @Getter

    /**
     * Sets the operation data.
     *
     * @param operationData the new operation data
     */
    @Setter
    protected T operationData;

    /** The read only mode. */

    /**
     * Checks if is read only mode.
     *
     * @return true, if is read only mode
     */
    @Getter

    /**
     * Sets the read only mode.
     *
     * @param readOnlyMode the new read only mode
     */
    @Setter
    private boolean readOnlyMode;

    /**
     * Checks if is valid operation data.
     *
     * @return true, if is valid operation data
     */
    public abstract boolean isValidOperationData();

}
