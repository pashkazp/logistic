package ua.com.sipsoft.ui.commons.forms;

import ua.com.sipsoft.utils.UIIcon;
import ua.com.sipsoft.utils.messages.ButtonMsg;

/**
 * Class represents standard buttons for modality
 *
 * @author Pavlo Degtyaryev
 */
public enum Modality {
    MR_YES(ButtonMsg.BTN_YES, UIIcon.BTN_YES),
    MR_OK(ButtonMsg.BTN_OK, UIIcon.BTN_OK),
    MR_SAVE(ButtonMsg.BTN_SAVE, UIIcon.BTN_PUT),
    MR_REFRESH(ButtonMsg.BTN_REFRESH, UIIcon.BTN_REFRESH),
    MR_DELETE(ButtonMsg.BTN_DELETE, UIIcon.BTN_DEL),
    MR_NO(ButtonMsg.BTN_NO, UIIcon.BTN_NO),
    MR_CANCEL(ButtonMsg.BTN_CANCEL, UIIcon.BTN_CANCEL);

    /** The button caption. */
    private final String btCaption;

    /** The button icon. */
    private final UIIcon btIcon;

    /**
     * Instantiates a new modality.
     *
     * @param btCaption the button caption
     * @param btIcon    the button icon
     */
    private Modality(String btCaption, UIIcon btIcon) {
	this.btCaption = btCaption;
	this.btIcon = btIcon;
    }

    /**
     * Gets the button caption.
     *
     * @return the button caption
     */
    public String getBtCaption() {
	return btCaption;
    }

    /**
     * Gets the button icon.
     *
     * @return the button icon
     */
    public UIIcon getBtIcon() {
	return btIcon;
    }
}
