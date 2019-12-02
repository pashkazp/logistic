package ua.com.sipsoft.services.users;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.data.provider.Query;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.common.VerificationToken;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.model.repository.user.UserRepository;
import ua.com.sipsoft.services.security.VerificationTokenService;
import ua.com.sipsoft.services.utils.EntityFilter;
import ua.com.sipsoft.services.utils.HasQueryToSortConvertor;
import ua.com.sipsoft.utils.security.Role;
import ua.com.sipsoft.utils.security.VerificationTokenType;

/**
 * The Class UsersServiceImpl.
 *
 * @author Pavlo Degtyaryev
 */

@Slf4j
@Service
@Transactional
public class UsersServiceImpl implements UsersService, HasQueryToSortConvertor {

    /** The dao. */
    @Autowired
    private UserRepository dao;

    /** The verification token service. */
    @Autowired
    private VerificationTokenService verificationTokenService;

    /**
     * Save user.
     *
     * @param user the user
     * @return the user
     */
    @Override
    public User saveUser(User user) {
	log.info("Perform Save user: \"{}\"", user);
	if (user == null) {
	    log.warn("User saving impossible. Missing some data. ");
	    return null;
	}
	try {
	    return dao.save(user);
	} catch (Exception e) {
	    log.warn(e.getMessage());
	    return null;
	}
    }

    /**
     * Fetch by id.
     *
     * @param id the id
     * @return the optional
     */
    @Override
    public Optional<User> fetchById(Long id) {
	log.info("Perform get user by Id: \"{}\"", id);
	if (id == null) {
	    log.warn("User fetching impossible. Missing some data. ");
	}
	try {
	    return dao.findById(id);
	} catch (Exception e) {
	    log.warn(e.getMessage());
	}
	return Optional.ofNullable(null);
    }

    /**
     * Gets the by roles.
     *
     * @param roles the roles
     * @return the by roles
     */
    @Override
    public List<User> getByRoles(Collection<Role> roles) {
	log.info("Get users by Roles collection: \"{}\"", roles);
	if (roles == null) {
	    log.warn("Users fetching impossible. Missing some data. ");
	}
	try {
	    return dao.getByRoles(roles);
	} catch (Exception e) {
	    log.warn(e.getMessage());
	}
	return Collections.emptyList();
    }

    /**
     * Gets the by roles and find by name.
     *
     * @param roles the roles
     * @param name  the name
     * @return the by roles and find by name
     */
    public List<User> getByRolesAndFindByName(Collection<Role> roles, String name) {
	log.info("Get Users by roles \"{}\" and by name: \"{}\"", roles, name);
	if (roles == null || name == null) {
	    log.warn("Users fetching impossible. Missing some data. ");
	}
	try {
	    return dao.getByRolesAndFindByName(roles, name.toLowerCase());
	} catch (Exception e) {
	    log.warn(e.getMessage());
	}
	return Collections.emptyList();
    }

    /**
     * Save users.
     *
     * @param users the users
     * @return the collection
     */
    @Override
    public Collection<User> saveUsers(Collection<User> users) {
	// TODO checkeing
	users = dao.saveAll(users);
	dao.flush();
	return users;
    }

    /**
     * Gets the queried usersby filter.
     *
     * @param query the query
     * @return the queried usersby filter
     */
    @Override
    public Stream<User> getQueriedUsersbyFilter(Query<User, EntityFilter<User>> query) {
	log.debug(
		"Get requested page Users with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
		query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
	try {
	    return dao.findAll(queryToSort(query))
		    .stream()
		    .filter(entity -> query.getFilter().get().isPass(entity))
		    .skip(query.getOffset())
		    .limit(query.getLimit());
	} catch (Exception e) {
	    log.error("The Courier Requests list was not received for a reason: {}", e.getMessage());
	}
	return Stream.empty();
    }

    /**
     * Gets the queried users by filter count.
     *
     * @param query the query
     * @return the queried users by filter count
     */
    @Override
    public int getQueriedUsersByFilterCount(Query<User, EntityFilter<User>> query) {
	log.debug("Get requested size Users with filter '{}'", query.getFilter().get().toString());
	return (int) getQueriedUsersbyFilter(query).count();
    }

    /**
     * Gets the queried usersby filter.
     *
     * @param query      the query
     * @param facilityId the facility id
     * @return the queried usersby filter
     */
    @Override
    public Stream<User> getQueriedUsersByFacilityIdByFilter(Query<User, EntityFilter<User>> query, Long facilityId) {
	log.debug(
		"Get requested page Users with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
		query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
	try {
	    return dao.getUsersByFasilityId(facilityId, queryToSort(query))
		    .stream()
		    .filter(entity -> query.getFilter().get().isPass(entity))
		    .skip(query.getOffset())
		    .limit(query.getLimit());
	} catch (Exception e) {
	    log.error("The Courier Requests list was not received for a reason: {}", e.getMessage());
	}
	return Stream.empty();
    }

    /**
     * Gets the queried users by filter count.
     *
     * @param query      the query
     * @param facilityId the facility id
     * @return the queried users by filter count
     */
    @Override
    public int getQueriedUsersByFacilityIdByFilterCount(Query<User, EntityFilter<User>> query, Long facilityId) {
	log.debug("Get requested size Users with filter '{}'", query.getFilter().get().toString());
	return (int) getQueriedUsersByFacilityIdByFilter(query, facilityId).count();
    }

    /**
     * Fetch by email.
     *
     * @param email the email
     * @return the optional
     */
    @Override
    public Optional<User> fetchByEmail(String email) {
	if (email == null) {
	    return Optional.ofNullable(null);
	}
	try {
	    return dao.findByEmailIgnoreCase(email);
	} catch (Exception e) {
	    return Optional.ofNullable(null);
	}
    }

    /**
     * Fetch by username.
     *
     * @param username the username
     * @return the optional
     */
    @Override
    public Optional<User> fetchByUsername(String username) {
	if (username == null) {
	    return Optional.ofNullable(null);
	}
	try {
	    return dao.findByUsernameIgnoreCase(username);
	} catch (Exception e) {
	    return Optional.ofNullable(null);
	}
    }

    /**
     * Register new user.
     *
     * @param userDTO the user DTO
     * @return the optional
     */
    @Override
    public Optional<User> registerNewUser(User userDTO) {
	log.info("Register new User \"{}\"", userDTO);
	if (userDTO == null) {
	    log.warn("New User Creation is impossible. Missing some data. ");
	    return Optional.ofNullable(null);
	}
	try {
	    User user = new User(userDTO);
//	    user.setPassword((new BCryptPasswordEncoder(10)).encode(userDTO.getPassword()));
	    return Optional.of(dao.save(user));
	} catch (Exception e) {
	    log.warn(e.getMessage());
	    return Optional.ofNullable(null);
	}
    }

    /**
     * Creates the verification token.
     *
     * @param user      the user
     * @param token     the token
     * @param tokenType the token type
     * @return the verification token
     */
    @Override
    public VerificationToken createVerificationToken(User user, String token, VerificationTokenType tokenType) {
	log.info("Creation verification token \"{}\" for new user: \"{}\"", token, user);
	if (user == null || token == null || token.isEmpty()) {
	    log.warn("Creation verification token impossible. Missing some data. ");
	    return null;
	}
	try {
	    VerificationToken vToken = new VerificationToken(user, token, tokenType);
	    return verificationTokenService.saveVerificationToken(vToken);
	} catch (Exception e) {
	    log.warn(e.getMessage());
	}
	return null;

    }

}
