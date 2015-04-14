package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserTest extends BubbleDocsServiceTest {
	
	@Mocked
	private IDRemoteServices idRemoteService;

	private String root;
	private String ars;

	private static final String USERNAME = "ars";
	//private static final String PASSWORD = "ars";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";
	private static final String EMAIL = "cenas@cenas.pt";
	
	@Override
	public void populate4Test() {
		getBubbleDocs();
		createUser(USERNAME, EMAIL, "António Rito Silva");
		root = addUserToSession("root");
		ars = addUserToSession("ars");
	}

	@Test
	public void success() {
	//	BubbleDocs bd = getBubbleDocs();
		CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST, EMAIL, "José Ferreira");
		
		new Expectations() {
			{
				idRemoteService.createUser(anyString, anyString, anyString);
			}
		};
		service.setIDRemoteService(idRemoteService);
		service.execute();

		User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);
		
		assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
		assertEquals("", user.getPassword());
		assertEquals(EMAIL, user.getEmail());
		assertEquals("José Ferreira", user.getName());
	}

	@Test(expected = UserAlreadyExistsException.class)
	public void usernameExists() {
		CreateUser service = new CreateUser(root, USERNAME, EMAIL, "José Ferreira");
		service.execute();
	}

	@Test(expected = InvalidUsernameException.class)
	public void emptyUsername() {
		CreateUser service = new CreateUser(root, "", EMAIL, "José Ferreira");
		service.execute();
	}

	@Test(expected = InvalidPermissionException.class)
	public void unauthorizedUserCreation() {
		CreateUser service = new CreateUser(ars, USERNAME_DOES_NOT_EXIST, EMAIL, "José Ferreira");
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUsernameNotExist() {
		removeUserFromSession(root);
		CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST, EMAIL, "José Ferreira");
		service.execute();
	}
	
	@Test(expected = UnavailableServiceException.class)
	public void idServiceUnavailable() {
		CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST, EMAIL, "José Ferreira");
		
		new Expectations() {
			{
				idRemoteService.createUser(anyString, anyString, anyString);
				result = new RemoteInvocationException();
			}
		};
		service.setIDRemoteService(idRemoteService);
		service.execute();
	}
	
	
}// End CreateUserTest class