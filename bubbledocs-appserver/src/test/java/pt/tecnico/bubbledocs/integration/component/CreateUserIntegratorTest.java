package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import mockit.Expectations;
import mockit.Mocked;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

/**
 * Class that contains the test suite for the
 * CreateUserIntegrator.
 */

public class CreateUserIntegratorTest extends BubbleDocsServiceTest {
	
	@Mocked
	private IDRemoteServices idRemoteService;

	private String root;
	private String ars;

	private static final String USERNAME = "ars";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";
	private static final String EMAIL = "cenas@cenas.pt";
	
	/**
	 * Method that populates the DB with all
	 * the objects the test suite needs to execute.
	 */
	
	@Override
	public void populate4Test() {
		getBubbleDocs();
		createUser(USERNAME, EMAIL, "António Rito Silva");
		root = addUserToSession("root");
		ars = addUserToSession("ars");
	}
	
	/**
	 * Test Case #1 - Success
	 * 
	 * Tests a normal invocation of the service
	 * where nothing goes wrong.
	 * 
	 * Result - SUCCESS
	 */

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
	
	/**
	 * Test Case #2 - UsernameExists
	 * 
	 * Tests what happens when the username already
	 * exists in the application.
	 * 
	 * Result - FAILURE - DuplicateUsernameException
	 */

	@Test(expected = DuplicateUsernameException.class)
	public void usernameExists() {
		CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME, EMAIL, "José Ferreira");
		service.execute();
	}
	
	/**
	 * Test Case #3 - EmptyUsername
	 * 
	 * Tests what happens when the username is invalid.
	 * 
	 * Result - FAILURE - InvalidUsernameException
	 */

	@Test(expected = InvalidUsernameException.class)
	public void emptyUsername() {
		CreateUserIntegrator service = new CreateUserIntegrator(root, "", EMAIL, "José Ferreira");
		service.execute();
	}
	
	/**
	 * Test Case #4 - UnauthorizedUserCreation
	 * 
	 * Tests what happens when the user invoking the service isn't root.
	 * 
	 * Result - FAILURE - InvalidPermissionException
	 */

	@Test(expected = InvalidPermissionException.class)
	public void unauthorizedUserCreation() {
		CreateUserIntegrator service = new CreateUserIntegrator(ars, USERNAME_DOES_NOT_EXIST, EMAIL, "José Ferreira");
		
		service.execute();
	}
	
	/**
	 * Test Case #5 - RootNotInSession
	 * 
	 * Tests what happens when root doesn't have an active session.
	 * 
	 * Result - FAILURE - UserNotInSessionException
	 */

	@Test(expected = UserNotInSessionException.class)
	public void rootNotInSession() {
		removeUserFromSession(root);
		
		CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, EMAIL, "José Ferreira");
		service.execute();
	}
	
	/**
	 * Test Case #6 - IdServiceUnavailable
	 * 
	 * Tests what happens when the remote service is down.
	 * 
	 * Result - FAILURE - UnavailableServiceException
	 */
	
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
	
	/**
	 * Test Case #7 - UserDeletedLocalSuccessfuly
	 * 
	 * Tests what happens when the remote service is down, the local copy of the user is deleted.
	 * 
	 * Result - SUCCESS
	 */
	
	@Test
	public void userDeletedLocalSuccessfuly() {
		CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, EMAIL, "José Ferreira");
		
		new Expectations() {
			{
				idRemoteService.createUser(anyString, anyString, anyString);
				result = new RemoteInvocationException();
			}
		};
		try{ 
			service.execute();
		}catch (UnavailableServiceException ex){
			//Because service failed, try and see if user is not created locally.
			try{
				getUserFromUsername(USERNAME_DOES_NOT_EXIST);
				fail("user not deleted");
			}catch (LoginBubbleDocsException ex2){
				assertThat(ex2.toString(), CoreMatchers.containsString("The username given is invalid."));
			}
		}
	}	
}// End CreateUserIntegratorTest class
