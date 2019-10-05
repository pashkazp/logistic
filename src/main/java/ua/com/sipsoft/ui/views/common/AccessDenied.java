package ua.com.sipsoft.ui.views.common;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import ua.com.sipsoft.ui.MainView;
import ua.com.sipsoft.utils.AppURL;
import ua.com.sipsoft.utils.messages.AppTitleMsg;

/**
 * The Class AccessDenied.
 * 
 * @author Pavlo Degtyaryev
 */
@UIScope
@SpringComponent
@Route(value = AppURL.ACCESS_DENIED_URL, layout = MainView.class)
public class AccessDenied extends VerticalLayout implements HasDynamicTitle {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4446519681131361208L;

    /**
     * Instantiates a new access denied.
     */
    public AccessDenied() {
	H1 h1 = new H1("ACCESS DENIED");
	add(h1);
	setAlignItems(Alignment.CENTER);
	Image titleComponent = new Image("frontend/images/access-denied.png", "access denied image");
	add(titleComponent);

    }

    /**
     * Gets the page title.
     *
     * @return the page title
     */
    @Override
    public String getPageTitle() {
	return getTranslation(AppTitleMsg.APP_TITLE_ACCSDENIED, UI.getCurrent().getLocale());
    }

}
