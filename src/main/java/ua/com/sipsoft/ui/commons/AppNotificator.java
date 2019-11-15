package ua.com.sipsoft.ui.commons;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class AppNotificator.
 *
 * @author Pavlo Degtyaryev
 */

/** The Constant log. */
@Slf4j
public class AppNotificator {

    /** The Constant walker. */
    static final StackWalker walker = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);

    /**
     * Instantiates a new app notificator.
     */
    private AppNotificator() {

    }

    /**
     * Notify.
     *
     * @param <T>      the generic type
     * @param duration the duration
     * @param note     the note
     */
    public static <T extends Object> void notify(int duration, String... note) {
	if (note != null && duration >= 0) {
	    Class<?> callerClass = walker.getCallerClass();
	    doNotify(duration, callerClass, note);
	}
    }

    public static <T extends Object> void notify(int duration, NotificationVariant variant, String... note) {
	if (note != null && duration >= 0) {
	    Class<?> callerClass = walker.getCallerClass();
	    doNotify(duration, callerClass, variant, note);
	}
    }

    public static <T extends Object> void notifyPrimary(int duration, String... note) {
	if (note != null && duration >= 0) {
	    Class<?> callerClass = walker.getCallerClass();
	    doNotify(duration, callerClass, NotificationVariant.LUMO_PRIMARY, note);
	}
    }

    public static <T extends Object> void notifyContrast(int duration, String... note) {
	if (note != null && duration >= 0) {
	    Class<?> callerClass = walker.getCallerClass();
	    doNotify(duration, callerClass, NotificationVariant.LUMO_CONTRAST, note);
	}
    }

    public static <T extends Object> void notifySuccess(int duration, String... note) {
	if (note != null && duration >= 0) {
	    Class<?> callerClass = walker.getCallerClass();
	    doNotify(duration, callerClass, NotificationVariant.LUMO_SUCCESS, note);
	}
    }

    public static <T extends Object> void notifyError(int duration, String... note) {
	if (note != null && duration >= 0) {
	    Class<?> callerClass = walker.getCallerClass();
	    doNotify(duration, callerClass, NotificationVariant.LUMO_ERROR, note);
	}
    }

    /**
     * Notify.
     *
     * @param <T>  the generic type
     * @param note the note
     */
    public static <T extends Object> void notify(String... note) {
	if (note != null) {
	    Class<?> callerClass = walker.getCallerClass();
	    doNotify(3000, callerClass, note);
	}
    }

    public static <T extends Object> void notify(NotificationVariant variant, String... note) {
	if (note != null) {
	    Class<?> callerClass = walker.getCallerClass();
	    doNotify(3000, callerClass, variant, note);
	}
    }

    public static <T extends Object> void notifyPrimary(String... note) {
	if (note != null) {
	    Class<?> callerClass = walker.getCallerClass();
	    doNotify(3000, callerClass, NotificationVariant.LUMO_PRIMARY, note);
	}
    }

    public static <T extends Object> void notifyContrast(String... note) {
	if (note != null) {
	    Class<?> callerClass = walker.getCallerClass();
	    doNotify(3000, callerClass, NotificationVariant.LUMO_CONTRAST, note);
	}
    }

    public static <T extends Object> void notifySuccess(String... note) {
	if (note != null) {
	    Class<?> callerClass = walker.getCallerClass();
	    doNotify(3000, callerClass, NotificationVariant.LUMO_SUCCESS, note);
	}
    }

    public static <T extends Object> void notifyError(String... note) {
	if (note != null) {
	    Class<?> callerClass = walker.getCallerClass();
	    doNotify(3000, callerClass, NotificationVariant.LUMO_ERROR, note);
	}
    }

    private static void doNotify(int duration, Class<?> callerClass, NotificationVariant variant, String... note) {
	for (String string : note) {
	    Notification n = new Notification(new HorizontalLayout(new Span(string)));
	    n.addThemeVariants(variant);
	    openNotify(duration, callerClass, string, n);
	}
    }

    private static void doNotify(int duration, Class<?> callerClass, String... note) {
	for (String string : note) {
	    Notification n = new Notification(new HorizontalLayout(new Span(string)));
	    openNotify(duration, callerClass, string, n);
	}
    }

    private static void openNotify(int duration, Class<?> callerClass, String string, Notification notiffcation) {
	notiffcation.setDuration(duration);
	notiffcation.setPosition(Position.BOTTOM_STRETCH);

	log.info(callerClass.getName() + ": " + string);
	notiffcation.open();
    }

}
