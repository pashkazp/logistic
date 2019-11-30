package ua.com.sipsoft.model.repository.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.model.entity.user.User;
import ua.com.sipsoft.utils.security.Role;

/**
 * The Interface UserRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find by username.
     *
     * @param username the username
     * @return the user
     */
    Optional<User> findByUsernameIgnoreCase(String username);

    /**
     * Find by email.
     *
     * @param email the email
     * @return the user
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Gets the all users.
     *
     * @return the all users
     */
    @Query("select u from User u")
    List<User> getAllUsers();

    /**
     * Gets the users by name.
     *
     * @param name the name
     * @return the users by name
     */
    @Query("from User u "
	    + "where LOWER ( concat (u.firstName, ' ', u.lastName, ' ', u.patronymic, ' ', u.username, ' ', u.email ) ) "
	    + " like concat('%',:name,'%') ")
    List<User> getUsersByName(@Param("name") String name);

    /**
     * Gets the by roles.
     *
     * @param roles the roles
     * @return the by roles
     */
    @Query("select distinct u from User u left join u.roles as r where r in (:roles) ")
    List<User> getByRoles(@Param("roles") Collection<Role> roles);

    /**
     * Gets the by roles and find by name.
     *
     * @param roles     the roles
     * @param firstName the first name
     * @return the by roles and find by name
     */
    @Query("select distinct u from User u left join u.roles as r "
	    + "where (LOWER ( concat (u.firstName, ' ', u.lastName, ' ', u.patronymic, ' ', u.username, ' ', u.email ) ) "
	    + " like concat('%',:name,'%')) and r in (:roles)  ")
    List<User> getByRolesAndFindByName(@Param("roles") Collection<Role> roles, @Param("name") String firstName);

    /**
     * Gets the courier request by drat rouet sheet id.
     *
     * @param id   the id
     * @param sort the sort
     * @return the courier request by drat rouet sheet id
     */
    @Query(" SELECT u "
	    + " FROM Facility f "
	    + " join f.users u "
	    + " WHERE f.id = :facilityid ")
    List<User> getUsersByFasilityId(@Param("facilityid") Long id, Sort sort);

}
