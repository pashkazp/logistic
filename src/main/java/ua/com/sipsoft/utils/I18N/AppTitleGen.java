package ua.com.sipsoft.utils.I18N;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//not implemented yet

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface AppTitleGen {
	String getPageTitle() default "application.title";
	// TODO Make AppTitleGen annotation
}
