package ua.com.sipsoft.ui.commons.dialogform;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

/**
 * The Class DialogFormCloseEvent.
 *
 * @author Pavlo Degtyaryev
 * @param <S> the generic type
 * @param <T> the generic type
 */
@SpringComponent
@VaadinSessionScope
public class DialogFormCloseEvent<S extends DialogForm> extends ComponentEvent<S> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7582104424418842442L;

    /** The close mode. */
    private Modality closeMode;

    /**
     * Instantiates a new dialog form close event.
     *
     * @param source     the source
     * @param fromClient the from client
     */
    public DialogFormCloseEvent(S source, boolean fromClient) {
	super(source, fromClient);
    }

    /**
     * Gets the close mode.
     *
     * @return the closeMode
     */
    public Modality getCloseMode() {
	return closeMode;
    }

    /**
     * Sets the close mode.
     *
     * @param closeMode the closeMode to set
     */
    public void setCloseMode(Modality closeMode) {
	this.closeMode = closeMode;
    }

}