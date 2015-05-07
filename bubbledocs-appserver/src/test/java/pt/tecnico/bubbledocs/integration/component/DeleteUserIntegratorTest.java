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

	@Override
	public void populate4Test() {
		getBubbleDocs();
		createUser(USERNAME, PASSWORD, "António Rito Silva");
		User smf = createUser(USERNAME_TO_DELETE, "email", "Sérgio Fernandes");
		createSpreadSheet(smf, USERNAME_TO_DELETE, 20, 20);

		root = addUserToSession(ROOT_USERNAME);
	}

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

	/*
	 * accessUsername exists, is in session and is root 
	 * toDeleteUsername exists and is not in session
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

	/*
	 * accessUsername exists, is in session and is root toDeleteUsername exists
	 * and is in session 
	 * Test if user and session are both deleted
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

	@Test(expected = LoginBubbleDocsException.class)
	public void userToDeleteDoesNotExist() {
		DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_DOES_NOT_EXIST);
		service.execute();
	}

	@Test(expected = InvalidPermissionException.class)
	public void notRootUser() {
		String ars = addUserToSession(USERNAME);
		DeleteUserIntegrator service = new DeleteUserIntegrator(ars, USERNAME_TO_DELETE);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void rootNotInSession() {
		removeUserFromSession(root);
		DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_TO_DELETE);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void notInSessionAndNotRoot() {
		String ars = addUserToSession(USERNAME);
		removeUserFromSession(ars);

		DeleteUserIntegrator service = new DeleteUserIntegrator(ars, USERNAME_TO_DELETE);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUserDoesNotExist() {
		DeleteUserIntegrator service = new DeleteUserIntegrator(USERNAME_DOES_NOT_EXIST, USERNAME_TO_DELETE);
		service.execute();
	}

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
