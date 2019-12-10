package ua.com.sipsoft.utils;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Standartized set of icons for application.
 *
 * @author Pavlo Degtyaryev
 */
public enum UIIcon {
    HOME(VaadinIcon.HOME),
    USERS(VaadinIcon.USERS),
    GROUP(VaadinIcon.GROUP),
    BELL(VaadinIcon.BELL),
    COURIER_RQUESTS(VaadinIcon.LIST_UL),
    SIGN_OUT(VaadinIcon.SIGN_OUT),
    CHILD(VaadinIcon.CHILD),
    CALENDAR_CLOCK(VaadinIcon.CALENDAR_CLOCK),
    MAP_MARKER(VaadinIcon.MAP_MARKER),
    ADDRESS(VaadinIcon.ROAD),
    THEME_PAINTER(VaadinIcon.PAINT_ROLL),
    PRINTER(VaadinIcon.PRINT),
    SHEET_REDRAFT(VaadinIcon.REPLY),
    SHEET_DRAFT(VaadinIcon.FILE_TEXT),
    SHEET_ISSUED(VaadinIcon.FILE_TEXT_O),
    SHEET_ARCHIVE(VaadinIcon.RECORDS),
    PHONE(VaadinIcon.PHONE),
    SEARCH(VaadinIcon.SEARCH),
    OFFICE(VaadinIcon.OFFICE),
    INFO(VaadinIcon.INFO_CIRCLE_O),
    MAIL(VaadinIcon.ENVELOPE_O),
    MORE(VaadinIcon.ELLIPSIS_DOTS_H),

    BTN_YES(VaadinIcon.CHECK),
    BTN_NO(VaadinIcon.CLOSE),
    BTN_CANCEL(VaadinIcon.EXIT),
    BTN_OK(VaadinIcon.THUMBS_UP),

    BTN_SIGN_IN(VaadinIcon.SIGN_IN),
    BTN_SIGN_OUT(VaadinIcon.SIGN_OUT),
    BTN_SIGN_UP(VaadinIcon.TOUCH),

    BTN_EDIT(VaadinIcon.PENCIL),
    BTN_ADD(VaadinIcon.PLUS),
    BTN_DEL(VaadinIcon.MINUS),
    BTN_PUT(VaadinIcon.DOWNLOAD),
    BTN_GET(VaadinIcon.UPLOAD),
    BTN_REFRESH(VaadinIcon.REFRESH),

    BTN_REM_FROM_DRAFT(VaadinIcon.FILE_REMOVE),
    BTN_ADD_TO_DRAFT(VaadinIcon.FILE_ADD);

    /** The icon. */
    private final VaadinIcon icon;

    /**
     * Instantiates a new UI icon.
     *
     * @param icon the icon
     */
    private UIIcon(VaadinIcon icon) {
	this.icon = icon;
    }

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    public VaadinIcon getIcon() {
	return icon;
    }

    /**
     * Creates the icon.
     *
     * @return the icon
     */
    public Icon createIcon() {
	return icon.create();
    }

}
