package ua.com.sipsoft.ui.commons.dialogform;

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
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.utils.ButtonPreparer;

/**
 * Class that represent the data editor. Class is a conjunction of the
 * {@link Dialog}
 *
 * @author Pavlo Degtyaryev
 * @param <T> the generic type
 */
@Scope("prototype")

/** The Constant log. */
@Slf4j
@SpringComponent
public class DialogForm extends Dialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4108520790106630150L;

    /** The btn panel. */
    private HorizontalLayout btnPanel;

    /** The completition mode. */

    private Modality completitionMode;

    /** The data editor. */
    private transient Component dataEditor;

    /** The modalities. */
    private Set<Modality> modalities;
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
		    completitionMode = Modality.MR_CANCEL;
		    close();
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

    public void addModality(Modality modality, ComponentEventListener<ClickEvent<Button>> listener) {
	if (modality != null) {
	    this.modalities.add(modality);
	    if (listener != null) {
		listeners.put(modality, listener);
	    }
	}
    }

    /**
     * Gets the completition mode.
     *
     * @return the completition mode
     */
    public Modality getCompletitionMode() {
	return completitionMode;
    }

    public void setCompletitionMode(Modality mode) {
	completitionMode = mode;
    }

    /**
     * Gets the editor.
     *
     * @return the editor
     */
    public FormLayout getEditor() {
	return getEditor();
    }

    /** The on close handler. */
    private DialogFormCloseHandler<DialogForm> onCloseHandler;

    /**
     * Gets the on close handler.
     *
     * @return the on close handler
     */
    public DialogFormCloseHandler<DialogForm> getOnCloseHandler() {
	return onCloseHandler;
    }

    /**
     * Sets the on close handler.
     *
     * @param onCloseHandler the on close handler
     */
    public void setOnCloseHandler(DialogFormCloseHandler<DialogForm> onCloseHandler) {
	this.onCloseHandler = onCloseHandler;
    }

    /**
     * Open.
     */
    @Override
    public void open() {
	for (Modality modality : Modality.values()) {
	    if (modalities.contains(modality)) {
		ComponentEventListener<ClickEvent<Button>> listener;
		listener = listeners.getOrDefault(modality, event -> {
		    completitionMode = modality;
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
	    closeEvent.setCloseMode(getCompletitionMode());
	    getOnCloseHandler().onClose(closeEvent);
	}
    }

    /**
     * Sets the data editor.
     *
     * @param <F>    the generic type
     * @param editor the new data editor
     */
    public <F extends Component> void setDataEditor(F editor) {
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
	return "DialogForm [btnPanel=" + btnPanel + ", completitionMode=" + completitionMode
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
     * With close on outside click.
     *
     * @param value the value
     * @return the dialog form
     */
    public DialogForm withCloseOnOutsideClick(Boolean value) {
	setCloseOnOutsideClick(value);
	return this;
    }

    /**
     * With completition mode.
     *
     * @param completitionMode the completition mode
     * @return the dialog form
     */
    public DialogForm withCompletitionMode(Modality completitionMode) {
	this.completitionMode = completitionMode;
	return this;
    }

    /**
     * With components.
     *
     * @param components the components
     * @return the dialog form
     */
    public DialogForm withComponents(Component... components) {
	if (components != null) {
	    for (Component component : components) {
		add(component);
	    }
	}
	return this;
    }

    /**
     * With data editor.
     *
     * @param <S>    the generic type
     * @param editor the editor
     * @return the dialog form
     */
    public <S extends Component> DialogForm withDataEditor(
	    S editor) {
	setDataEditor(editor);
	return this;
    }

    /**
     * With data editor.
     *
     * @param <S>          the generic type
     * @param editor       the editor
     * @param readOnlyMode the read only mode
     * @return the dialog form
     */
    public <S extends Component> DialogForm withDataEditor(
	    S editor,
	    boolean readOnlyMode) {
	setDataEditor(editor);
	return this;
    }

    /**
     * With height.
     *
     * @param value the value
     * @return the dialog form
     */
    public DialogForm withHeight(String value) {
	setHeight(value);
	return this;
    }

    /**
     * With modality.
     *
     * @param modality the modality
     * @return the dialog form
     */
    public DialogForm withModality(Modality... modality) {
	addModality(modality);
	return this;
    }

    public DialogForm withModality(Modality modality, ComponentEventListener<ClickEvent<Button>> listener) {
	addModality(modality, listener);
	return this;
    }

    /**
     * With on close handler.
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
     * With header.
     *
     * @param header the header
     * @return the dialog form
     */
    public DialogForm withHeader(String header) {
	addComponentAsFirst(new H4(header));
//	if (dataEditor != null) {
//	    dataEditor.addComponentAsFirst(new H4(header));
//	}
	return this;
    }

    /**
     * With width.
     *
     * @param value the value
     * @return the dialog form
     */
    public DialogForm withWidth(String value) {
	setWidth(value);
	if (dataEditor != null) {
	    // dataEditor.setWidth(value);
	    // TODO specify whether the editor should be resized
	}
	return this;
    }

    public void closeWithResult(Modality modality) {
	setCompletitionMode(modality);
	close();
    }
}
