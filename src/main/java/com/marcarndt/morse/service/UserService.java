package com.marcarndt.morse.service;

import com.marcarndt.morse.MorseBotException;
import com.marcarndt.morse.command.BaseCommand;
import com.marcarndt.morse.data.User;
import com.marcarndt.morse.data.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/04/13.
 */
@Stateless
public class UserService {

  public static final String UNAUTHENTICATED = "unauthenticated";
  public static final String USER = "user";
  public static final String ADMIN = "admin";
  private static final Logger LOG = Logger.getLogger(UserService.class.getName());
  @Inject
  MongoService mongoService;
  @Any
  @Inject
  Instance<BaseCommand> baseCommands;

  /**
   * Validates a user against a role.
   *
   * @param id id
   * @param role role
   * @return true if the user is valid
   * @throws MorseBotException on exception
   */
  public boolean validateUser(Integer id, String role) throws MorseBotException {
    if (role.equals(UNAUTHENTICATED)) {
      return true;
    }

    User user = getUser(id);
    if (user == null) {
      throw new MorseBotException(
          "You are not known to me. Use /register to make me aware of your presence. ");
    }
    if (role.equals(USER)) {
      return true;
    }

    UserRole userRole = getUserRole(role);
    if (userRole == null) {
      return false;
    }
    return userRole.getUsers().contains(user);
  }

  private UserRole getUserRole(String role) {
    return mongoService.getDatastore().createQuery(UserRole.class).field("role")
        .equal(role).get();
  }

  /**
   * Adds a user, with optional roles.
   *
   * @param id ID for the user
   * @param name Name for the user
   * @param lastname last name
   * @param roles List of roes for the user
   */
  public void addUser(int id, String name, String lastname, String... roles) {
    User user = getUser(id);
    if (user == null) {
      user = new User();
    }
    if (lastname != null) {
      name = name + " " + lastname;
    }
    user.setName(name);
    user.setUserId(id);
    mongoService.getDatastore().save(user);

    for (String role : roles) {
      addUserToRole(user, role);
    }
  }

  private User getUser(int id) {
    return mongoService.getDatastore().createQuery(User.class).field("userId").equal(id).get();
  }

  private void addUserToRole(User user, String role) {
    UserRole userRole = getUserRole(role);
    if (userRole == null) {
      userRole = new UserRole();
    }
    List<User> users = userRole.getUsers();
    if (users == null) {
      users = new ArrayList<>();
    }
    if (!users.contains(user)) {
      users.add(user);
    }
    userRole.setUsers(users);
    userRole.setRole(role.toString());
    mongoService.getDatastore().save(userRole);
  }

  public void addUserToRole(String name, String roleName) throws MorseBotException {
    User user = getUserByName(name);
    addUserToRole(user, roleName);
  }

  /**
   * Gets roles for a user.
   *
   * @param id Chat ID For the user
   * @return List of Roles the user has
   */
  public List<String> getUserRoles(Integer id) {
    return mongoService.getDatastore().createQuery(UserRole.class).asList().stream()
        .filter(userRole ->
            userRole.getUsers().stream().filter(user -> user.getUserId().equals(id)).count() > 0)
        .filter(userRole -> userRole.getRole() != null)
        .map(userRole -> userRole.getRole()).collect(
            Collectors.toList());
  }

  /**
   * Gets all users.
   *
   * @return List of users
   */
  public List<User> getAllUsers() {
    return mongoService.getDatastore().createQuery(User.class).asList();
  }

  /**
   * Finds a user by the name.
   *
   * @param name Name of the user
   * @return User object
   * @throws MorseBotException when the user is not found
   */
  public User getUserByName(String name) throws MorseBotException {
    User user = mongoService.getDatastore().createQuery(User.class).field("name").equal(name).get();
    if (user == null) {
      throw new MorseBotException("Unable to find user " + name);
    }
    return user;
  }

  /**
   * Remove a user from a role.
   *
   * @param name First name of the user
   * @param roleName role name
   * @throws MorseBotException on exception
   */
  public void removeUserFromRole(String name, String roleName) throws MorseBotException {
    User user = getUserByName(name);

    UserRole userRole = getUserRole(roleName);
    userRole.getUsers().remove(user);

    mongoService.getDatastore().save(userRole);
  }

  /**
   * Returns a list of all the roles.
   *
   * @return list of all roles identified
   */
  public List<String> getAllRoles() {
    List<String> result = new ArrayList<>();

    for (BaseCommand baseCommand : baseCommands) {
      String role = baseCommand.getRole();
      if (role != null) {
        if (!result.contains(role)) {
          result.add(role);
        }
      }
    }
    return result;
  }

  /**
   * Check if the system as a user admin in the database.
   *
   * @return true if a user admin exists
   */
  public boolean adminUserExists() {
    return
        mongoService.getDatastore().createQuery(UserRole.class).field("role").equal(ADMIN).count()
            > 0;
  }
}
