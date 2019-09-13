package ua.com.sipsoft.ui.commons;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.utils.ButtonPreparer;
import ua.com.sipsoft.utils.Modality;

/**
 * Class that represent the data editor. Class is a conjunction of the
 * {@link Dialog} and {@link FormLayout}.
 *
 * @author Pavlo Degtyaryev
 * @param <T> the generic type
 */
@Scope("prototype")

/** The Constant log. */
@Slf4j
@SpringComponent
public class DialogForm<T> extends Dialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4108520790106630150L;

	/** The binder. */
	private Binder<T> binder;

	/** The btn panel. */
	private HorizontalLayout btnPanel;

	/** The completition mode. */
	private Modality completitionMode;

	/** The data editor. */
	private transient FormLayout dataEditor;

	/** The modalities. */
	private Set<Modality> modalities;

	/** The operation data. */
	private transient T operationData;

	/**
	 * Instantiates a new dialog form.
	 */
	public DialogForm() {
		modalities = new HashSet<>(Arrays.asList(Modality.MR_CANCEL));

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
	 * Btn cancel click listener.
	 *
	 * @param event the event
	 */
	public void btnCancelClickListener(ComponentEvent<Button> event) {
		completitionMode = Modality.MR_CANCEL;
		close();
	}

	/**
	 * Btn delete click listener.
	 *
	 * @param event the event
	 */
	public void btnDeleteClickListener(ComponentEvent<Button> event) {
		completitionMode = Modality.MR_DELETE;
		close();
	}

	/**
	 * Btn no click listener.
	 *
	 * @param event the event
	 */
	public void btnNoClickListener(ComponentEvent<Button> event) {
		completitionMode = Modality.MR_NO;
		close();
	}

	/**
	 * Btn ok click listener.
	 *
	 * @param event the event
	 */
	public void btnOkClickListener(ComponentEvent<Button> event) {
		completitionMode = Modality.MR_OK;
		close();
	}

	/**
	 * Btn save click listener.
	 *
	 * @param event the event
	 */
	public void btnSaveClickListener(ComponentEvent<Button> event) {
		completitionMode = Modality.MR_SAVE;
		close();
	}

	/**
	 * Btn yes click listener.
	 *
	 * @param event the event
	 */
	public void btnYesClickListener(ComponentEvent<Button> event) {
		completitionMode = Modality.MR_YES;
		close();
	}

	/**
	 * Fill modality.
	 *
	 * @param modalities the modalities
	 */
	public void fillModality(Modality... modalities) {
		if (modalities != null) {
			this.modalities.clear();
			for (Modality modality : modalities) {
				this.modalities.add(modality);
			}
			if (this.modalities.isEmpty()) {
				this.modalities.add(Modality.MR_CANCEL);
			}
		}
	}

	/**
	 * Gets the binder.
	 *
	 * @return the binder
	 */
	public Binder<T> getBinder() {
		return binder;
	}

	/**
	 * Gets the completition mode.
	 *
	 * @return the completition mode
	 */
	public Modality getCompletitionMode() {
		return completitionMode;
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
	private DialogFormCloseHandler<DialogForm<T>, T> onCloseHandler;

	/**
	 * Gets the on close handler.
	 *
	 * @return the on close handler
	 */
	public DialogFormCloseHandler<DialogForm<T>, T> getOnCloseHandler() {
		return onCloseHandler;
	}

	/**
	 * Sets the on close handler.
	 *
	 * @param onCloseHandler the on close handler
	 */
	public void setOnCloseHandler(DialogFormCloseHandler<DialogForm<T>, T> onCloseHandler) {
		this.onCloseHandler = onCloseHandler;
	}

	/**
	 * Gets the operation data.
	 *
	 * @return the operation data
	 */
	public T getOperationData() {
		return operationData;
	}

	/**
	 * Open.
	 */
	@Override
	public void open() {
		for (Modality modality : Modality.values()) {
			if (modalities != null && modalities.contains(modality)) {
				ComponentEventListener<ClickEvent<Button>> listener;
				switch (modality) {
				case MR_CANCEL:
					listener = this::btnCancelClickListener;
					break;
				case MR_DELETE:
					listener = this::btnDeleteClickListener;
					break;
				case MR_NO:
					listener = this::btnNoClickListener;
					break;
				case MR_OK:
					listener = this::btnOkClickListener;
					break;
				case MR_SAVE:
					listener = this::btnSaveClickListener;
					break;
				case MR_YES:
					listener = this::btnYesClickListener;
					break;
				default:
					listener = this::btnCancelClickListener;
				}
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
			DialogFormCloseEvent<DialogForm<T>, T> closeEvent = new DialogFormCloseEvent<>(this,
					event.isFromClient());
			closeEvent.setValue(getOperationData());
			closeEvent.setCloseMode(getCompletitionMode());
			getOnCloseHandler().onClose(closeEvent);
		}
	}

	/**
	 * Checks if is valid operation data.
	 *
	 * @return true, if is valid operation data
	 */
	public boolean isValidOperationData() {
		if (dataEditor instanceof HasDialogFormDataCollector) {
			Object data = ((HasDialogFormDataCollector) dataEditor).collectOperationData();
			if (data != null) {
				operationData = (T) data;
				return true;
			}
		}
		if (binder != null && (binder.validate().isOk() & binder.isValid())) {
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

	/**
	 * Sets the data editor.
	 *
	 * @param <F>    the generic type
	 * @param editor the new data editor
	 */
	public <F extends FormLayout & HasOperationData<T>> void setDataEditor(F editor) {
		this.dataEditor = editor;
		if (editor != null) {
			binder = editor.getBinder();
			this.dataEditor.setWidth(getWidth());
			if (binder != null) {
				binder.readBean(operationData);
			}
		}
		add(editor);
	}

	/**
	 * Sets the operation data.
	 *
	 * @param operationData the new operation data
	 */
	public void setOperationData(T operationData) {
		this.operationData = operationData;
		if (binder != null) {
			binder.readBean(operationData);
		}
		if ((dataEditor != null) && (dataEditor instanceof HasDialogFormDataCollector)) {
			((HasDialogFormDataCollector) dataEditor).distributeOperationData(operationData);
		}
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "DialogForm [binder=" + binder + ", btnPanel=" + btnPanel + ", completitionMode=" + completitionMode
				+ ", dataEditor=" + dataEditor + ", modalities=" + modalities + ", onCloseHandler=" + onCloseHandler
				+ ", operationData=" + operationData + ", toString()=" + super.toString() + "]";
	}

	/**
	 * With close on esc.
	 *
	 * @param value the value
	 * @return the dialog form
	 */
	public DialogForm<T> withCloseOnEsc(Boolean value) {
		setCloseOnEsc(value);
		return this;
	}

	/**
	 * With close on outside click.
	 *
	 * @param value the value
	 * @return the dialog form
	 */
	public DialogForm<T> withCloseOnOutsideClick(Boolean value) {
		setCloseOnOutsideClick(value);
		return this;
	}

	/**
	 * With completition mode.
	 *
	 * @param completitionMode the completition mode
	 * @return the dialog form
	 */
	public DialogForm<T> withCompletitionMode(Modality completitionMode) {
		this.completitionMode = completitionMode;
		return this;
	}

	/**
	 * With components.
	 *
	 * @param components the components
	 * @return the dialog form
	 */
	public DialogForm<T> withComponents(Component... components) {
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
	public <S extends FormLayout & HasOperationData<T>> DialogForm<T> withDataEditor(
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
	public <S extends FormLayout & HasOperationData<T>> DialogForm<T> withDataEditor(
			S editor,
			boolean readOnlyMode) {
		setDataEditor(editor);
		if (editor != null) {
			editor.getBinder().setReadOnly(readOnlyMode);
		}
		return this;
	}

	/**
	 * With height.
	 *
	 * @param value the value
	 * @return the dialog form
	 */
	public DialogForm<T> withHeight(String value) {
		setHeight(value);
		return this;
	}

	/**
	 * With modality.
	 *
	 * @param modality the modality
	 * @return the dialog form
	 */
	public DialogForm<T> withModality(Modality... modality) {
		modalities.clear();
		addModality(modality);
		return this;
	}

	/**
	 * With on close handler.
	 *
	 * @param onCloseHandler the on close handler
	 * @return the dialog form
	 */
	public DialogForm<T> withOnCloseHandler(
			DialogFormCloseHandler<DialogForm<T>, T> onCloseHandler) {
		setOnCloseHandler(onCloseHandler);
		return this;
	}

	/**
	 * With operation data.
	 *
	 * @param operationData the operation data
	 * @return the dialog form
	 */
	public DialogForm<T> withOperationData(T operationData) {
		setOperationData(operationData);
		return this;
	}

	/**
	 * With header.
	 *
	 * @param header the header
	 * @return the dialog form
	 */
	public DialogForm<T> withHeader(String header) {
		if (dataEditor != null) {
			dataEditor.addComponentAsFirst(new H4(header));
		}
		setOperationData(operationData);
		return this;
	}

	/**
	 * With width.
	 *
	 * @param value the value
	 * @return the dialog form
	 */
	public DialogForm<T> withWidth(String value) {
		setWidth(value);
		if (dataEditor != null)
			dataEditor.setWidth(value);
		return this;
	}
}
