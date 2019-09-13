package ua.com.sipsoft.ui.views.login;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * The Class Logout.
 * 
 * @author Pavlo Degtyaryev
 */
@UIScope
@SpringComponent
@Route("logout")
public class Logout extends VerticalLayout {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -291560613224713157L;

	/**
	 * Instantiates a new logout.
	 */
	public Logout() {
		Label label = new Label("Ви успішно вийшли.");
		add(label);
	}

}
