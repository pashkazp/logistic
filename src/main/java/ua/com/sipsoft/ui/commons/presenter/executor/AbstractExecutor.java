package ua.com.sipsoft.ui.commons.presenter.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import com.vaadin.flow.component.grid.GridSelectionModel;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import lombok.Setter;
import ua.com.sipsoft.utils.I18N.VaadinI18NProvider;

@UIScope
@SpringComponent
public abstract class AbstractExecutor<T> {
    @Autowired
    @Setter
    protected VaadinI18NProvider i18n;

    @Autowired
    @Setter
    protected ApplicationEventPublisher publisher;

    @Setter
    protected GridSelectionModel<T> selectionModel;

    public abstract void execute();

}
