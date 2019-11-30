package ua.com.sipsoft.services.users;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vaadin.flow.data.provider.Query;

import ua.com.sipsoft.model.entity.common.VerificationToken;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.services.utils.EntityFilter;
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
     * @param query the query
     * @return the queried usersby filter
     */
    public Stream<User> getQueriedUsersbyFilter(Query<User, EntityFilter<User>> query);

    /**
     * Gets the queried users by filter count.
     *
     * @param query the query
     * @return the queried users by filter count
     */
    public int getQueriedUsersByFilterCount(Query<User, EntityFilter<User>> query);

    /**
     * Gets the queried usersby filter.
     *
     * @param query      the query
     * @param facilityId the facility id
     * @return the queried usersby filter
     */
    public Stream<User> getQueriedUsersByFacilityIdByFilter(Query<User, EntityFilter<User>> query, Long facilityId);

    /**
     * Gets the queried users by filter count.
     *
     * @param query      the query
     * @param facilityId the facility id
     * @return the queried users by filter count
     */
    public int getQueriedUsersByFacilityIdByFilterCount(Query<User, EntityFilter<User>> query, Long facilityId);

    /**
     * Fetch by email.
     *
     * @param email the email
     * @return the optional
     */
    public Optional<User> fetchByEmail(String email);

    /**
     * Fetch by username.
     *
     * @param username the username
     * @return the optional
     */
    public Optional<User> fetchByUsername(String username);

    /**
     * Register new user.
     *
     * @param user the user
     * @return the optional
     */
    public Optional<User> registerNewUser(User user);

    /**
     * Creates the verification token.
     *
     * @param user      the user
     * @param token     the token
     * @param tokenType the token type
     * @return the verification token
     */
    public VerificationToken createVerificationToken(User user, String token, VerificationTokenType tokenType);

}
