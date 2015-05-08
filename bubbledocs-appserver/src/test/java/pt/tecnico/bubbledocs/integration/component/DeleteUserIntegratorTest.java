package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.integration.DeleteUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

/**
 * Class that contains the test suite for the
 * DeleteUserIntegrator.
 */

public class DeleteUserIntegratorTest extends BubbleDocsServiceTest {

	private static final String USERNAME_TO_DELETE = "smf";
	private static final String USERNAME = "ars";
	private static final String PASSWORD = "ars";
	private static final String ROOT_USERNAME = "root";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";
	private static final String SPREADSHEET_NAME = "spread";

	private String root;

	@Mocked
	private IDRemoteServices idRemoteService;
	
	/**
	 * Method that populates the DB with all
	 * the objects the test suite needs to execute.
	 */

	@Override
	public void populate4Test() {
		getBubbleDocs();
		createUser(USERNAME, PASSWORD, "António Rito Silva");
		User smf = createUser(USERNAME_TO_DELETE, "email", "Sérgio Fernandes");
		createSpreadSheet(smf, USERNAME_TO_DELETE, 20, 20);

		root = addUserToSession(ROOT_USERNAME);
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
		DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_TO_DELETE);

		new Expectations() {
			{
				idRemoteService.removeUser(USERNAME_TO_DELETE);
			}
		};
		service.execute();

		boolean deleted = false;
		boolean deletedSpreadsheet = false;

		try {
			getUserFromUsername(USERNAME_TO_DELETE);
		} catch (LoginBubbleDocsException e) {
			deleted = true;
		}
		
		try {
			getSpreadSheet(SPREADSHEET_NAME);
		} catch (SpreadsheetDoesNotExistException e) {
			deletedSpreadsheet = true;
		}

		assertTrue("User was not deleted", deleted);
		assertTrue("Spreadsheet was not deleted", deletedSpreadsheet);
	}
	
	/**
	 * Test Case #2 - SuccessToDeleteIsNotInSession
	 * 
	 * Tests a normal invocation of the service
	 * where nothing goes wrong and the user to remove doesn't have
	 * an active session.
	 * 
	 * Result - SUCCESS
	 */

	@Test
	public void successToDeleteIsNotInSession() {
		DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_TO_DELETE);

		new Expectations() {
			{
				idRemoteService.removeUser(USERNAME_TO_DELETE);
			}
		};
		service.execute();

		boolean deleted = false;
		boolean deletedSpreadsheet = false;

		try {
			getUserFromUsername(USERNAME_TO_DELETE);
		} catch (LoginBubbleDocsException e) {
			deleted = true;
		}
		
		try {
			getSpreadSheet(SPREADSHEET_NAME);
		} catch (SpreadsheetDoesNotExistException e) {
			deletedSpreadsheet = true;
		}

		assertTrue("User was not deleted", deleted);
		assertTrue("Spreadsheet was not deleted", deletedSpreadsheet);
	}
	
	/**
	 * Test Case #3 - SuccessToDeleteIsInSession
	 * 
	 * Tests a normal invocation of the service
	 * where nothing goes wrong and the user to remove has
	 * an active session.
	 * 
	 * Result - SUCCESS
	 */

	@Test
	public void successToDeleteIsInSession() {
		String token = addUserToSession(USERNAME_TO_DELETE);

		DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_TO_DELETE);

		new Expectations() {
			{
				idRemoteService.removeUser(USERNAME_TO_DELETE);
			}
		};
		service.execute();

		boolean deleted = false;
		boolean deletedSpreadsheet = false;

		try {
			getUserFromUsername(USERNAME_TO_DELETE);
		} catch (LoginBubbleDocsException e) {
			deleted = true;
		}
		
		try {
			getSpreadSheet(SPREADSHEET_NAME);
		} catch (SpreadsheetDoesNotExistException e) {
			deletedSpreadsheet = true;
		}

		assertTrue("User was not deleted", deleted);
		assertTrue("Spreadsheet was not deleted", deletedSpreadsheet);
		assertNull("Removed user but not removed from session", getUserFromSession(token));
	}
	
	/**
	 * Test Case #4 - UserToDeleteDoesNotExist
	 * 
	 * Tests what happens when the user to delete doesn't exist
	 * in the first place.
	 * 
	 * Result - FAILURE - LoginBubbleDocsException
	 */

	@Test(expected = LoginBubbleDocsException.class)
	public void userToDeleteDoesNotExist() {
		DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_DOES_NOT_EXIST);
		service.execute();
	}
	
	/**
	 * Test Case #5 - NotRootUser
	 * 
	 * Tests what happens when the user calling the service isn't root.
	 * 
	 * Result - FAILURE - InvalidPermissionException
	 */

	@Test(expected = InvalidPermissionException.class)
	public void notRootUser() {
		String ars = addUserToSession(USERNAME);
		DeleteUserIntegrator service = new DeleteUserIntegrator(ars, USERNAME_TO_DELETE);
		service.execute();
	}
	
	/**
	 * Test Case #6 - RootNotInSession
	 * 
	 * Tests what happens when root doesn't have an active session.
	 * 
	 * Result - FAILURE - UserNotInSessionException
	 */

	@Test(expected = UserNotInSessionException.class)
	public void rootNotInSession() {
		removeUserFromSession(root);
		DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_TO_DELETE);
		service.execute();
	}
	
	/**
	 * Test Case #7 - NotInSessionAndNotRoot
	 * 
	 * Tests what happens when the user calling the service isn't root
	 * and the user to delete doesn't have an active session.
	 * 
	 * Result - FAILURE - UserNotInSessionException
	 */

	@Test(expected = UserNotInSessionException.class)
	public void notInSessionAndNotRoot() {
		String ars = addUserToSession(USERNAME);
		removeUserFromSession(ars);

		DeleteUserIntegrator service = new DeleteUserIntegrator(ars, USERNAME_TO_DELETE);
		service.execute();
	}
	
	/**
	 * Test Case #8 - AccessUserDoesNotExist
	 * 
	 * Tests what happens when the user calling the service doesn't exist.
	 * 
	 * Result - FAILURE - UserNotInSessionException
	 */

	@Test(expected = UserNotInSessionException.class)
	public void accessUserDoesNotExist() {
		DeleteUserIntegrator service = new DeleteUserIntegrator(USERNAME_DOES_NOT_EXIST, USERNAME_TO_DELETE);
		service.execute();
	}
	
	/**
	 * Test Case #9 - InvalidPairUserPassword
	 * 
	 * Tests what happens when root has an invalid pair username/password.
	 * 
	 * Result - FAILURE - LoginBubbleDocsException
	 */

	@Test(expected = LoginBubbleDocsException.class)
	public void invalidPairUserPassword() {

		DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_TO_DELETE);

		new Expectations() {
			{
				idRemoteService.removeUser(USERNAME_TO_DELETE);
				result = new LoginBubbleDocsException("username");
			}
		};
		service.execute();
	}
	
	/**
	 * Test Case #10 - IdServiceUnavailable
	 * 
	 * Tests what happens when the remote service is down.
	 * 
	 * Result - FAILURE - UnavailableServiceException
	 */

	@Test(expected = UnavailableServiceException.class)
	public void idServiceUnavailable() {

		DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_TO_DELETE);

		new Expectations() {
			{
				idRemoteService.removeUser(USERNAME_TO_DELETE);
				result = new RemoteInvocationException();
			}
		};
		service.execute();
	}
	
	/**
	 * Test Case #11 - IdServiceUnavailableCompensation
	 * 
	 * Tests what happens when if remote service is down
	 * the user is created again.
	 * 
	 * Result - SUCCESS
	 */
	
	@Test
	public void idServiceUnavailableCompensation() {

		DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_TO_DELETE);

		new Expectations() {
			{
				idRemoteService.removeUser(USERNAME_TO_DELETE);
				result = new RemoteInvocationException();
			}
		};
		try{
			service.execute();
		}catch(UnavailableServiceException ex){
			boolean userExists = true;

			try {
				getUserFromUsername(USERNAME_TO_DELETE);
			} catch (LoginBubbleDocsException e) {
				userExists = false;
			}

			assertTrue("User was deleted", userExists);
		}		
	}
}// End DeleteUserIntegratorTest class
