package ua.com.sipsoft.utils;

import ua.com.sipsoft.ui.commons.DialogForm;
import ua.com.sipsoft.utils.messages.ButtonMsg;

/**
 * Class represents standard buttons for modality {@link DialogForm}.
 *
 * @author Pavlo Degtyaryev
 */
public enum Modality {
    MR_YES(ButtonMsg.BTN_YES, UIIcon.BTN_YES),
    MR_OK(ButtonMsg.BTN_OK, UIIcon.BTN_OK),
    MR_SAVE(ButtonMsg.BTN_SAVE, UIIcon.BTN_PUT),
    MR_DELETE(ButtonMsg.BTN_DELETE, UIIcon.BTN_DEL),
    MR_NO(ButtonMsg.BTN_NO, UIIcon.BTN_NO),
    MR_CANCEL(ButtonMsg.BTN_CANCEL, UIIcon.BTN_CANCEL);

    /** The bt caption. */
    private final String btCaption;

    /** The bt icon. */
    private final UIIcon btIcon;

    /**
     * Instantiates a new modality.
     *
     * @param btCaption the bt caption
     * @param btIcon    the bt icon
     */
    private Modality(String btCaption, UIIcon btIcon) {
	this.btCaption = btCaption;
	this.btIcon = btIcon;
    }

    /**
     * Gets the bt caption.
     *
     * @return the bt caption
     */
    public String getBtCaption() {
	return btCaption;
    }

    /**
     * Gets the bt icon.
     *
     * @return the bt icon
     */
    public UIIcon getBtIcon() {
	return btIcon;
    }
}
