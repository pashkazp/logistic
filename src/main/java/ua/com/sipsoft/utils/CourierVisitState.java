package ua.com.sipsoft.utils;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import lombok.Getter;
import ua.com.sipsoft.model.entity.requests.archive.ArchivedCourierVisit;
import ua.com.sipsoft.model.entity.requests.issued.CourierVisit;

/**
 * Class that represents states for {@link CourierVisit} and
 * {@link ArchivedCourierVisit}.
 *
 * @author Pavlo Degtyaryev
 */
public enum CourierVisitState {
    RUNNING("RUNNING", VaadinIcon.TIME_FORWARD),
    COMPLETED("COMPLETED", VaadinIcon.CHECK_SQUARE_O),
    CANCELLED("CANCELLED", VaadinIcon.TRASH);

    /**
     * Gets the state name.
     *
     * @return the state name
     */
    @Getter
    private String stateName;

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    @Getter
    private final VaadinIcon icon;

    /**
     * Instantiates a new courier visit state.
     *
     * @param stateName the state name
     * @param icon      the icon
     */
    private CourierVisitState(String stateName, VaadinIcon icon) {
	this.stateName = stateName;
	this.icon = icon;
    }

    /**
     * Creates the icon.
     *
     * @return the icon
     */
    public Icon createIcon() {
	return icon.create();
    }

    /** The Constant INACTIVESET. */
    public static final Set<CourierVisitState> INACTIVESET = ImmutableSet.of(COMPLETED, CANCELLED);

    /** The Constant ACTIVESET. */
    public static final Set<CourierVisitState> ACTIVESET = ImmutableSet.of(RUNNING);
}
