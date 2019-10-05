package ua.com.sipsoft.services.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.model.repository.user.UserRepository;

/**
 * The Class MyUserDetailService.
 *
 * @author Pavlo Degtyaryev
 */
@Service
public class MyUserDetailService implements UserDetailsService {

    /** The user dao. */
    @Autowired
    private UserRepository userDao;

    /**
     * Load user by username.
     *
     * @param username the username
     * @return the user details
     * @throws UsernameNotFoundException the username not found exception
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	Optional<User> user = userDao.findByUsernameIgnoreCase(username);
	if (!user.isPresent()) {
	    throw new UsernameNotFoundException("User 404");
	}

	return new UserDetailImpl(user.get());
    }

}
