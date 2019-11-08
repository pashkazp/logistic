package ua.com.sipsoft.ui.commons.forms.dialogform;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.ui.commons.forms.Modality;
import ua.com.sipsoft.utils.ButtonPreparer;

/**
 * Class that represent the data editor. Class is a conjunction of the
 * {@link Dialog}
 *
 * @author Pavlo Degtyaryev
 */
@Scope("prototype")
@Slf4j
@SpringComponent
public class DialogForm extends Dialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4108520790106630150L;

    /** The btn panel. */
    private HorizontalLayout btnPanel;

    /** The completion mode. */
    private Modality completionMode;

    /** The data editor. */
    private transient Component dataEditor;

    /** The modalities. */
    private Set<Modality> modalities;

    /** The listeners. */
    private Map<Modality, ComponentEventListener<ClickEvent<Button>>> listeners;

    /**
     * Instantiates a new dialog form.
     */
    public DialogForm() {
	modalities = new HashSet<>(Arrays.asList(Modality.MR_CANCEL));
	listeners = new HashMap<>() {
	    private static final long serialVersionUID = 5482617395448455112L;
	    {
		put(Modality.MR_CANCEL, event -> {
		    closeWithResult(Modality.MR_CANCEL);
		});
	    }
	};

	btnPanel = new HorizontalLayout();
	btnPanel.setSizeUndefined();
	btnPanel.setDefaultVerticalComponentAlignment(Alignment.STRETCH);

	btnPanel.setMargin(false);
	btnPanel.setPadding(false);
	btnPanel.setSpacing(true);

	btnPanel.setWidthFull();

	addOpenedChangeListener(this::openedChangeListener);
    }

    /**
     * Adds the modality.
     *
     * @param modality the modality
     */
    public void addModality(Modality... modality) {
	if (modality != null) {
	    for (Modality m : modality) {
		this.modalities.add(m);
	    }
	}
    }

    /**
     * Adds the modality.
     *
     * @param modality the modality
     * @param listener the listener
     */
    public void addModality(Modality modality, ComponentEventListener<ClickEvent<Button>> listener) {
	if (modality != null) {
	    this.modalities.add(modality);
	    if (listener != null) {
		listeners.put(modality, listener);
	    }
	}
    }

    /**
     * Gets the completion mode.
     *
     * @return the completion mode
     */
    public Modality getCompletionMode() {
	return completionMode;
    }

    /**
     * Sets the completion mode.
     *
     * @param mode the new completion mode
     */
    public void setCompletionMode(Modality mode) {
	completionMode = mode;
    }

    /**
     * Gets the editor.
     *
     * @return the editor
     */
    public Component getDataEditor() {
	return dataEditor;
    }

    /** The on close handler. */

    /**
     * Gets the on close handler.
     *
     * @return the on close handler
     */

    /**
     * Gets the on close handler.
     *
     * @return the on close handler
     */
    @Getter

    /**
     * Sets the on close handler.
     *
     * @param onCloseHandler the new on close handler
     */

    /**
     * Sets the on close handler.
     *
     * @param onCloseHandler the new on close handler
     */
    @Setter
    private DialogFormCloseHandler<DialogForm> onCloseHandler;

    /**
     * Open.
     */
    @Override
    public void open() {
	for (Modality modality : Modality.values()) {
	    if (modalities.contains(modality)) {
		ComponentEventListener<ClickEvent<Button>> listener;
		listener = listeners.getOrDefault(modality, event -> {
		    completionMode = modality;
		    close();
		});
		Button button = ButtonPreparer
			.prepare(new Button(getTranslation(modality.getBtCaption()), modality.getBtIcon().createIcon(),
				listener));
		btnPanel.add(button);
		btnPanel.setFlexGrow(1, button);
	    }
	}
	this.add(btnPanel);
	super.open();
    }

    /**
     * Opened change listener.
     *
     * @param event the event
     */
    private void openedChangeListener(OpenedChangeEvent<Dialog> event) {
	if (event != null && !event.isOpened() && getOnCloseHandler() != null) {
	    DialogFormCloseEvent<DialogForm> closeEvent = new DialogFormCloseEvent<>(this,
		    event.isFromClient());
	    closeEvent.setCloseMode(getCompletionMode());
	    getOnCloseHandler().onClose(closeEvent);
	}
    }

    /**
     * Sets the data editor.
     *
     * @param editor the new data editor
     */
    public void setDataEditor(Component editor) {
	this.dataEditor = editor;
	if (editor != null) {
	    // this.dataEditor.setWidth(getWidth());
	    // TODO specify whether the editor should be resized
	}
	add(editor);
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
	return "DialogForm [btnPanel=" + btnPanel + ", completionMode=" + completionMode
		+ ", [dataEditor=" + dataEditor + "], modalities=" + modalities + ", onCloseHandler=" + onCloseHandler
		+ ", toString()=" + super.toString() + "]";
    }

    /**
     * With close on esc.
     *
     * @param value the value
     * @return the dialog form
     */
    public DialogForm withCloseOnEsc(Boolean value) {
	setCloseOnEsc(value);
	return this;
    }

    /**
     * Set close on outside click.
     *
     * @param value the value
     * @return the dialog form
     */
    public DialogForm withCloseOnOutsideClick(Boolean value) {
	setCloseOnOutsideClick(value);
	return this;
    }

    /**
     * Set data editor.
     *
     * @param editor the editor
     * @return the dialog form
     */
    public DialogForm withDataEditor(Component editor) {
	setDataEditor(editor);
	return this;
    }

    /**
     * Set height.
     *
     * @param value the value
     * @return the dialog form
     */
    public DialogForm withHeight(String value) {
	setHeight(value);
	return this;
    }

    /**
     * Set modalitys.
     *
     * @param modality the modality
     * @return the dialog form
     */
    public DialogForm withModality(Modality... modality) {
	addModality(modality);
	return this;
    }

    /**
     * Set modality and its event listener
     *
     * @param modality the modality
     * @param listener the listener
     * @return the dialog form
     */
    public DialogForm withModality(Modality modality, ComponentEventListener<ClickEvent<Button>> listener) {
	addModality(modality, listener);
	return this;
    }

    /**
     * Set dialog close handler.
     *
     * @param onCloseHandler the on close handler
     * @return the dialog form
     */
    public DialogForm withOnCloseHandler(
	    DialogFormCloseHandler<DialogForm> onCloseHandler) {
	setOnCloseHandler(onCloseHandler);
	return this;
    }

    /**
     * With dialog header.
     *
     * @param header the header
     * @return the dialog form
     */
    public DialogForm withHeader(String header) {
	addComponentAsFirst(new H4(header));
	return this;
    }

    /**
     * Set dialog width.
     *
     * @param value the value
     * @return the dialog form
     */
    public DialogForm withWidth(String value) {
	setWidth(value);
	return this;
    }

    /**
     * Set modality result and close.
     *
     * @param modality the modality
     */
    public void closeWithResult(Modality modality) {
	setCompletionMode(modality);
	close();
    }

}
