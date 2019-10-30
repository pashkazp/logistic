package ua.com.sipsoft.ui.commons.entityedit;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractBindedEntityEditor<T> extends AbstractEntityEditor<T> {

    private static final long serialVersionUID = -1715796619592562117L;

    @Getter

    private Binder<T> binder;

    @Override
    public boolean isValidOperationData() {
	if (binder.validate().isOk() & binder.isValid()) {
	    if (operationData == null) {
		log.warn("Property 'operationData' can not be null", this.operationData);
		return false;
	    }
	    BinderValidationStatus<T> status = binder.validate();
	    binder.writeBeanIfValid(operationData);
	    return status.isOk();
	}
	return false;
    }

    @Override
    public void setOperationData(T operationData) {
	super.setOperationData(operationData);
	binder.readBean(operationData);
    }

    @Override
    public void setReadOnlyMode(boolean readOnlyMode) {
	super.setReadOnlyMode(readOnlyMode);
	if (binder != null) {
	    binder.setReadOnly(readOnlyMode);
	}
    }

    public void setBinder(Binder<T> binder) {
	this.binder = binder;
	if (binder != null) {
	    binder.setReadOnly(isReadOnlyMode());
	}
    }

}
