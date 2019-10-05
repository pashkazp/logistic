package ua.com.sipsoft.ui.views.homeview;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.utils.security.SecurityUtils;

/**
 * The Class HomeViewPresenterRegistered.
 *
 * @author Pavlo Degtyaryev
 */

/** The Constant log. */
@Slf4j
@Scope("prototype")
@SpringComponent
public class HomeViewPresenterRegistered extends HorizontalLayout {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7900942361709596270L;

    /**
     * Instantiates a new home view presenter for User Registered.
     */
    public HomeViewPresenterRegistered() {
	add(new Paragraph(SecurityUtils.getUser().getUsername()
		+ ", у вас недостатньо прав для здійснення будь яких операцій. Зверніться до менеджера щоб вирішити це питанння."));
    }

}
