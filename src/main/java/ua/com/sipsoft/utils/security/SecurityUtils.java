package ua.com.sipsoft.utils.security;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.security.UserDetailImpl;

/**
 * The Class SecurityUtils.
 *
 * @author Pavlo Degtyaryev
 */
@Slf4j
public final class SecurityUtils {

    /** The Constant walker. */
    static final StackWalker stackWalker = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);

    /**
     * Instantiates a new security utils.
     */
    private SecurityUtils() {
	// Util methods only
    }

    /**
     * Gets the user name of the currently signed in user.
     *
     * @return the user name of the current user or <code>null</code> if the user
     *         has not signed in
     */
    public static String getUsername() {
	SecurityContext context = SecurityContextHolder.getContext();
	Object principal = context.getAuthentication().getPrincipal();
	if (principal instanceof UserDetailImpl) {
	    UserDetailImpl userDetails = (UserDetailImpl) context.getAuthentication().getPrincipal();
	    return userDetails.getUsername();
	}
	// Anonymous or no authentication.
	return null;
    }

    /**
     * Checks if the user is logged in.
     *
     * @return true if the user is logged in. False otherwise.
     */
    public static boolean isUserLoggedIn() {
	return isUserLoggedIn(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Checks if is user logged in.
     *
     * @param authentication the authentication
     * @return true, if is user logged in
     */
    private static boolean isUserLoggedIn(Authentication authentication) {
	return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public static User getUser() {
	SecurityContext context = SecurityContextHolder.getContext();
	Object principal = context.getAuthentication().getPrincipal();
	if (principal instanceof UserDetailImpl) {
	    UserDetailImpl userDetails = (UserDetailImpl) context.getAuthentication().getPrincipal();
	    return userDetails.getUserCopy();
	}
	// Anonymous or no authentication.
	return null;
    }

    /**
     * Gets the user roles.
     *
     * @return the user roles
     */
    public static Collection<Role> getUserRoles() {
	SecurityContext context = SecurityContextHolder.getContext();
	Object principal = context.getAuthentication().getPrincipal();
	if (principal instanceof UserDetailImpl) {
	    UserDetailImpl userDetails = (UserDetailImpl) context.getAuthentication().getPrincipal();
	    return userDetails.getRoles();
	}
	// Anonymous or no authentication.
	return Collections.emptySet();
    }

    /**
     * Gets the highest role. Where Admin - highest and Registered - lowest. If Role
     * is absent return Registered
     *
     * @return the highest {@link Role}
     */
    public static Role getHighestUserRole() {
	SecurityContext context = SecurityContextHolder.getContext();
	Object principal = context.getAuthentication().getPrincipal();
	if (principal instanceof UserDetailImpl) {
	    UserDetailImpl userDetails = (UserDetailImpl) context.getAuthentication().getPrincipal();
	    return userDetails.getHighestRole();
	}
	// Anonymous or no authentication.
	return Role.ROLE_REGISTERED;
    }

    /**
     * Check annotation {@link AllowedFor} for Class. If annotations exist and have
     * any ROLES, then return true for the user who has one of these roles
     *
     * @param securedClass the secured class
     * @return true, if is access granted
     */
    public static boolean isAccessGranted(Class<?> securedClass) {
	log.info("Check access for Class '{}'", securedClass);
	if (securedClass == null) {
	    return true;
	}
	AllowedFor allowedFor = AnnotationUtils.findAnnotation(securedClass, AllowedFor.class);
	if (allowedFor == null) {
	    return true; //
	}

	// lookup needed role in user roles
	List<String> allowedRoles = Arrays.asList(allowedFor.value());
	log.debug("Found @AllowedFor annotation '{}'", allowedFor.toString());
	Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
	log.debug("Check access for User '{}'", userAuthentication.getName());
	return userAuthentication.getAuthorities().stream() //
		.map(GrantedAuthority::getAuthority)
		.peek(a -> log.debug("Found authorityes '{}'", a))
		.anyMatch(allowedRoles::contains);
    }

    /**
     * Check annotation {@link AllowedFor} for method. If annotations exist and have
     * any ROLES, then return true for the user who has one of these roles
     *
     * @param method the method
     * @return true, if is access granted
     */
    public static boolean isAccessGranted(Method method) {
	log.info("Check access for Method '{}'", method);
	if (method == null) {
	    return true;
	}
	AllowedFor allowedFor = AnnotationUtils.findAnnotation(method, AllowedFor.class);
	if (allowedFor == null) {
	    return true;
	}
	List<String> allowedRoles = Arrays.asList(allowedFor.value());
	log.debug("Found @AllowedFor annotation '{}'", allowedFor.toString());
	Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
	log.debug("Check access for User '{}'", userAuthentication.getName());
	return userAuthentication.getAuthorities().stream()
		.map(GrantedAuthority::getAuthority)
		.peek(a -> log.debug("Found authorityes '{}'", a))
		.anyMatch(allowedRoles::contains);
    }

    /**
     * Find enclosing Class and checks that is access granted.
     *
     * @return true, if is access granted
     */
    public static boolean isClassAccessGranted() {
	Class<?> callerClass = stackWalker.getCallerClass();
	return isAccessGranted(callerClass);
    }

    /**
     * Find enclosing method and checks that the found method is access granted.
     *
     * @return true, if is access granted
     */
    public static boolean isMethodAccessGranted() {
	StackTraceElement[] stackTrace = Thread.currentThread()
		.getStackTrace();
	StackTraceElement e = stackTrace[2];
	log.info("Check access for current method '{}'", e.getMethodName());
	try {
	    return isAccessGranted(Class.forName(e.getClassName())
		    .getDeclaredMethod(e.getMethodName()));
	} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e1) {
	    return true;
	}
    }

    /**
     * Gets the user authorities.
     *
     * @return the user authorities
     */
    public static Collection<String> getUserAuthorities() {
	Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
	log.info("Gets authorities for user '{}'", userAuthentication.getName());
	return userAuthentication.getAuthorities().stream()
		.map(GrantedAuthority::getAuthority)
		.collect(Collectors.toList());

    }
}
