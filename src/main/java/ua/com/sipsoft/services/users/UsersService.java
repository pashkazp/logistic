package ua.com.sipsoft.services.users;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;

import ua.com.sipsoft.model.entity.common.VerificationToken;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.utils.security.Role;
import ua.com.sipsoft.utils.security.VerificationTokenType;

/**
 * The Interface UsersService.
 *
 * @author Pavlo Degtyaryev
 */
@Service
public interface UsersService {

    /**
     * Save user.
     *
     * @param user the user
     * @return the user
     */
    public User saveUser(User user);

    /**
     * Save users.
     *
     * @param users the users
     * @return the collection
     */
    public Collection<User> saveUsers(Collection<User> users);

    /**
     * Fetch by id.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<User> fetchById(Long id);

    /**
     * Gets the by roles.
     *
     * @param roles the roles
     * @return the by roles
     */
    public List<User> getByRoles(Collection<Role> roles);

    /**
     * Gets the by roles and find by name.
     *
     * @param roles the roles
     * @param name  the name
     * @return the by roles and find by name
     */
    public List<User> getByRolesAndFindByName(Collection<Role> roles, String name);

    /**
     * Gets the queried usersby filter.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried usersby filter
     */
    public Stream<User> getQueriedUsersbyFilter(Query<User, UserFilter> query,
	    UserFilter filter);

    /**
     * Gets the queried users by filter count.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried users by filter count
     */
    public int getQueriedUsersByFilterCount(Query<User, UserFilter> query,
	    UserFilter filter);

    public Optional<User> fetchByEmail(String email);

    public Optional<User> fetchByUsername(String username);

    public Optional<User> registerNewUser(User user);

    public VerificationToken createVerificationToken(User user, String token, VerificationTokenType tokenType);

}
