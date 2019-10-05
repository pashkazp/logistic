package ua.com.sipsoft.services.users;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.vaadin.flow.data.provider.Query;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.model.entity.common.VerificationToken;
import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.model.repository.user.UserRepository;
import ua.com.sipsoft.services.security.VerificationTokenService;
import ua.com.sipsoft.services.utils.HasQueryToSortConvertor;
import ua.com.sipsoft.utils.security.Role;
import ua.com.sipsoft.utils.security.VerificationTokenType;

/**
 * The Class UsersServiceImpl.
 *
 * @author Pavlo Degtyaryev
 */

/** The Constant log. */
@Slf4j
@Service
@Transactional
public class UsersServiceImpl implements UsersService, HasQueryToSortConvertor {

    /** The dao. */
    @Autowired
    private UserRepository dao;
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
	// TODO checkeing
	return dao.save(user);
    }

    /**
     * Fetch by id.
     *
     * @param id the id
     * @return the optional
     */
    @Override
    public Optional<User> fetchById(Long id) {
	// TODO checkeing
	return dao.findById(id);
    }

    /**
     * Gets the by roles.
     *
     * @param roles the roles
     * @return the by roles
     */
    @Override
    public List<User> getByRoles(Collection<Role> roles) {
	// TODO checkeing
	return dao.getByRoles(roles);
    }

    /**
     * Gets the by roles and find by name.
     *
     * @param roles the roles
     * @param name  the name
     * @return the by roles and find by name
     */
    public List<User> getByRolesAndFindByName(Collection<Role> roles, String name) {
	// TODO checkeing
	return dao.getByRolesAndFindByName(roles, name.toLowerCase());
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
     * Checks if is entity pass filter.
     *
     * @param entity the entity
     * @param filter the filter
     * @return true, if is entity pass filter
     */
    private boolean isEntityPassFilter(User entity, UserFilter filter) {
	if (!(filter.getRoles() == null || CollectionUtils.containsAny(entity.getRoles(), filter.getRoles()))) {
	    return false;
	}
	if (!StringUtils.containsIgnoreCase(entity
		.getUsername().concat(entity.getFirstName())
		.concat(entity.getLastName())
		.concat(entity.getPatronymic())
		.concat(entity.getEmail()), filter.getUsername())) {
	    return false;
	}
	return true;
    }

    /**
     * Gets the queried usersby filter.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried usersby filter
     */
    @Override
    public Stream<User> getQueriedUsersbyFilter(Query<User, UserFilter> query,
	    UserFilter filter) {
	log.debug(
		"Get requested page Users with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
		query.getOffset(), query.getLimit(), query.getSortOrders(), filter);
	try {
	    if (filter.getFacilityId() == null) {
		return dao.findAll(queryToSort(query))
			.stream()
			.filter(entity -> isEntityPassFilter(entity, filter))
			.skip(query.getOffset())
			.limit(query.getLimit());
	    } else {
		return dao.getCourierRequestByDratRouetSheetId(filter.getFacilityId(), queryToSort(query))
			.stream()
			.filter(entity -> isEntityPassFilter(entity, filter))
			.skip(query.getOffset())
			.limit(query.getLimit());
	    }
	} catch (Exception e) {
	    log.error("The Courier Requests list was not received for a reason: {}", e.getMessage());
	}
	return Stream.empty();
    }

    /**
     * Gets the queried users by filter count.
     *
     * @param query  the query
     * @param filter the filter
     * @return the queried users by filter count
     */
    @Override
    public int getQueriedUsersByFilterCount(Query<User, UserFilter> query,
	    UserFilter filter) {
	log.debug("Get requested size Users with filter '{}'", filter);
	return (int) getQueriedUsersbyFilter(query, filter).count();
    }

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

    @Override
    public VerificationToken createVerificationToken(User user, String token, VerificationTokenType tokenType) {
	log.info("Creation verification token \"{}\" for new user: \"{}\"", token, user);
	if (user == null || token == null || token.isEmpty()) {
	    log.warn("Creation verification token impossible. Missing some data. ");
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
