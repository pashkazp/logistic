package ua.com.sipsoft.ui.commons;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
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
	    for (String string : note) {
		Notification n = new Notification(new HorizontalLayout(new Span(string)));
		n.setDuration(duration);
		n.setPosition(Position.BOTTOM_STRETCH);

		log.info(callerClass.getName() + ": " + string);
		n.open();
	    }
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
	    for (String string : note) {
		Notification n = new Notification(new HorizontalLayout(new Span(string)));
		n.setDuration(3000);
		n.setPosition(Position.BOTTOM_STRETCH);
		log.info(callerClass.getName() + ": " + string);
		n.open();
	    }
	}
    }
}
