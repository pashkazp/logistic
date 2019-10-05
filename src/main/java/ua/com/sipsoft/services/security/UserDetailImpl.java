package ua.com.sipsoft.services.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Class UserDetailImpl.
 *
 * @author Pavlo Degtyaryev
 */

public class UserDetailImpl implements UserDetails {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -477179629478050549L;

    /** The user. */
    private User user;

    /**
     * Instantiates a new user detail impl.
     *
     * @param user the user
     */
    public UserDetailImpl(User user) {

	super();
	this.user = user;

    }

    /**
     * Gets the authorities.
     *
     * @return the authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

	List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

	for (Role role : user.getRoles()) {
	    grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
	}
	return grantedAuthorities;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
	return user.getPassword();
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    @Override
    public String getUsername() {

	return user.getUsername();
    }

    /**
     * Checks if is account non expired.
     *
     * @return true, if is account non expired
     */
    @Override
    public boolean isAccountNonExpired() {
	return true;
    }

    /**
     * Checks if is account non locked.
     *
     * @return true, if is account non locked
     */
    @Override
    public boolean isAccountNonLocked() {
	return true;
    }

    /**
     * Checks if is credentials non expired.
     *
     * @return true, if is credentials non expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
	return true;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    @Override
    public boolean isEnabled() {
	return (user.getEnabled() && user.getVerified());
    }

    /**
     * Gets the roles.
     *
     * @return the roles
     */
    public Collection<Role> getRoles() {
	return Collections.unmodifiableSet(user.getRoles());
    }

    /**
     * Gets the highest role. Where Admin - highest and Registered - lowest. If Role
     * is absent return Registered
     *
     * @return the highest {@link Role}
     */
    public Role getHighestRole() {
	return user.getHighesRole();
    }

    /**
     * Gets the user copy.
     *
     * @return the user copy
     */
    public User getUserCopy() {
	return new User(this.user);
    }
}
