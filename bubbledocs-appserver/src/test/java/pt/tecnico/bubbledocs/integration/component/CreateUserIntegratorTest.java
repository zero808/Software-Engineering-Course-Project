package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserTest extends BubbleDocsServiceTest {
	
	@Mocked
	private IDRemoteServices idRemoteService;

	private String root;
	private String ars;

	private static final String USERNAME = "ars";
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
		CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, EMAIL, "José Ferreira");
		
		new Expectations() {
			{
				idRemoteService.createUser(anyString, anyString, anyString);
			}
		};
		service.execute();

		User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);
		
		assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
		assertNull("Password is not null", user.getPassword());
		assertEquals(EMAIL, user.getEmail());
		assertEquals("José Ferreira", user.getName());
	}

	@Test(expected = DuplicateUsernameException.class)
	public void usernameExists() {
		CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME, EMAIL, "José Ferreira");
		service.execute();
	}

	@Test(expected = InvalidUsernameException.class)
	public void emptyUsername() {
		CreateUserIntegrator service = new CreateUserIntegrator(root, "", EMAIL, "José Ferreira");
		service.execute();
	}

	@Test(expected = InvalidPermissionException.class)
	public void unauthorizedUserCreation() {
		CreateUserIntegrator service = new CreateUserIntegrator(ars, USERNAME_DOES_NOT_EXIST, EMAIL, "José Ferreira");
		
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUsernameNotExist() {
		removeUserFromSession(root);
		
		CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, EMAIL, "José Ferreira");
		service.execute();
	}
	
	@Test(expected = UnavailableServiceException.class)
	public void idServiceUnavailable() {
		CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, EMAIL, "José Ferreira");
		
		new Expectations() {
			{
				idRemoteService.createUser(anyString, anyString, anyString);
				result = new RemoteInvocationException();
			}
		};
		service.execute();
	}	
}// End CreateUserTest class