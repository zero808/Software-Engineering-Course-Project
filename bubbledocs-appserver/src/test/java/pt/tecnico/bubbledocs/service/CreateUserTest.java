package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

//TODO Exceptions might need to change or even create new ones.

public class CreateUserTest extends BubbleDocsServiceTest {

	// the tokens
	private String root;
	private String ars;

	private static final String USERNAME = "ars";
	private static final String PASSWORD = "ars";
	//private static final String ROOT_USERNAME = "root";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";

	@Override
	public void populate4Test() {
		createUser(USERNAME, PASSWORD, "António Rito Silva");
		root = addUserToSession("root");
		ars = addUserToSession("ars");
	}

	@Test
	public void success() {
		CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST, "jose", "José Ferreira");
		service.execute();

		// User is the domain class that represents a User
		User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

		assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
		assertEquals("jose", user.getPassword());
		assertEquals("José Ferreira", user.getName());
	}

	@Test(expected = UserAlreadyExistsException.class)
	public void usernameExists() {
		CreateUser service = new CreateUser(root, USERNAME, "jose", "José Ferreira");
		service.execute();
	}

	@Test(expected = InvalidUsernameException.class)
	public void emptyUsername() {
		CreateUser service = new CreateUser(root, "", "jose", "José Ferreira");
		service.execute();
	}

	@Test(expected = InvalidPermissionException.class)
	public void unauthorizedUserCreation() {
		CreateUser service = new CreateUser(ars, USERNAME_DOES_NOT_EXIST, "jose", "José Ferreira");
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUsernameNotExist() {
		removeUserFromSession(root);
		CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST, "jose", "José Ferreira");
		service.execute();
	}
}// End CreateUserTest class.