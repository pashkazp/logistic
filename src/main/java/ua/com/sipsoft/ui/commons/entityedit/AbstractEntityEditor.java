package ua.com.sipsoft.ui.commons.entityedit;

import com.vaadin.flow.component.formlayout.FormLayout;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractEntityEditor<T> extends FormLayout {

    private static final long serialVersionUID = -166516810158550418L;

    /** The operation data. */
    @Getter
    @Setter
    protected T operationData;

    /** The read only mode. */
    @Getter
    @Setter
    private boolean readOnlyMode;

    public abstract boolean isValidOperationData();

}
