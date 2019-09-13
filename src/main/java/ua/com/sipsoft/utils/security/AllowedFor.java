package ua.com.sipsoft.utils.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for describing service layer security attributes.
 *
 * <p>
 * The <code>AllowedFor</code> annotation is used to define a list of security
 * configuration attributes for business methods. This annotation can be used as
 * a Java 5 alternative to XML configuration.
 * <p>
 * For example:
 *
 * <pre>
 * &#064;AllowedFor({ &quot;ROLE_USER&quot; })
 * public void create(Contact contact);
 *
 * &#064;AllowedFor({ &quot;ROLE_USER&quot;, &quot;ROLE_ADMIN&quot; })
 * public void update(Contact contact);
 *
 * &#064;AllowedFor({ &quot;ROLE_ADMIN&quot; })
 * public void delete(Contact contact);
 * </pre>
 * 
 * @author Pavlo Degtyaryev
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AllowedFor {
	/**
	 * Returns the list of security configuration attributes (e.g.&nbsp;ROLE_USER,
	 * ROLE_ADMIN).
	 *
	 * @return String[] The secure method attributes
	 */
	public String[] value();
}
