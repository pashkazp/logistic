package ua.com.sipsoft.ui.commons.forms.viewform;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.ui.commons.forms.Modality;
import ua.com.sipsoft.utils.ButtonPreparer;

/**
 * The Class ViewForm.
 */
@Scope(value = "prototype")
@SpringComponent
@Slf4j
public class ViewForm extends VerticalLayout {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5273546062734713335L;

    /** The data editor. */

    /**
     * Gets the data editor.
     *
     * @return the data editor
     */
    @Getter
    private transient Component dataEditor;

    /** The btn panel. */
    private HorizontalLayout btnPanel;

    /** The comletition mode. */

    /**
     * Gets the completition mode.
     *
     * @return the completition mode
     */
    @Getter

    /**
     * Sets the completition mode.
     *
     * @param completitionMode the new completition mode
     */
    @Setter
    private Modality completitionMode;

    /** The modalities. */
    private Set<Modality> modalities;

    /** The listeners. */
    private Map<Modality, ComponentEventListener<ClickEvent<Button>>> listeners;

    /**
     * Inits the.
     */
    @PostConstruct
    private void init() {
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

	setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	setMargin(false);
	setPadding(false);
	setSpacing(false);

    }

    /**
     * Gets the editor.
     *
     * @return the editor
     */
    public Component getEditor() {
	return dataEditor;
    }

    /**
     * Sets the data editor.
     *
     * @param editor the new data editor
     */
    public void setDataEditor(Component editor) {
	this.dataEditor = editor;
	add(editor);
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
     * Open.
     */
    public void open() {
	btnPanel.removeAll();
	for (Modality modality : Modality.values()) {
	    if (modalities.contains(modality)) {
		ComponentEventListener<ClickEvent<Button>> listener;
		listener = listeners.getOrDefault(modality, event -> {
		    closeWithResult(Modality.MR_CANCEL);
		});
		Button button = ButtonPreparer
			.prepare(new Button(getTranslation(modality.getBtCaption()), modality.getBtIcon().createIcon(),
				listener));
		btnPanel.add(button);
		btnPanel.setFlexGrow(1, button);
	    }
	}
	add(btnPanel);
    }

    /**
     * Close with result.
     *
     * @param modality the modality
     */
    public void closeWithResult(Modality modality) {
	setCompletitionMode(modality);
	setVisible(false);
    }

    /**
     * With header.
     *
     * @param header the header
     * @return the dialog form
     */
    public ViewForm withHeader(String header) {
	addComponentAsFirst(new H4(header));
	return this;
    }

    /**
     * Sets the visible.
     *
     * @param mode the new visible
     */
    @Override
    public void setVisible(boolean mode) {
	if (mode) {
	    open();
	}
	super.setVisible(mode);
    }

    /**
     * With modality.
     *
     * @param modality the modality
     * @return the dialog form
     */
    public ViewForm withModality(Modality... modality) {
	addModality(modality);
	return this;
    }

    /**
     * With modality.
     *
     * @param modality the modality
     * @param listener the listener
     * @return the view form
     */
    public ViewForm withModality(Modality modality, ComponentEventListener<ClickEvent<Button>> listener) {
	addModality(modality, listener);
	return this;
    }

    /**
     * With height.
     *
     * @param value the value
     * @return the dialog form
     */
    public ViewForm withHeight(String value) {
	setHeight(value);
	return this;
    }
}
