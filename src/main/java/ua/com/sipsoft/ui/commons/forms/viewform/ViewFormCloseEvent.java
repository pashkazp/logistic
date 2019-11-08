package ua.com.sipsoft.ui.commons.forms.viewform;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import ua.com.sipsoft.ui.commons.forms.Modality;

/**
 * The Class DialogFormCloseEvent.
 *
 * @author Pavlo Degtyaryev
 * @param <S> the generic type
 * @param <T> the generic type
 */
@SpringComponent
@VaadinSessionScope
public class ViewFormCloseEvent<S extends ViewForm> extends ComponentEvent<S> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3375043661927209417L;
    /** The close mode. */
    private Modality closeMode;

    /**
     * Instantiates a new dialog form close event.
     *
     * @param source     the source
     * @param fromClient the from client
     */
    public ViewFormCloseEvent(S source, boolean fromClient) {
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