package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class DeleteUserTest extends BubbleDocsServiceTest {

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
		User smf = createUser(USERNAME_TO_DELETE, "smf", "Sérgio Fernandes");
		createSpreadSheet(smf, USERNAME_TO_DELETE, 20, 20);

		new Expectations() {
			{
				idRemoteService.removeUser(anyString);
			}
		};

		root = addUserToSession(ROOT_USERNAME);
	}
	
	@Test
	public void success() {
		BubbleDocs bd = getBubbleDocs();
		DeleteUser service = new DeleteUser(root, USERNAME_TO_DELETE);
		service.setIDRemoteService(idRemoteService);
		service.execute();
		
		boolean deleted = false;
		
		try {
			getUserFromUsername(USERNAME_TO_DELETE);
		} catch (LoginBubbleDocsException e) {
			deleted = true;
		}
		
		assertTrue("user was not deleted", deleted);
		assertNull("Spreadsheet was not deleted", bd.getSpreadsheetByNameNoException(SPREADSHEET_NAME));
	}

	/*
	 * accessUsername exists, is in session and is root toDeleteUsername exists
	 * and is not in session
	 */
	@Test
	public void successToDeleteIsNotInSession() {
		BubbleDocs bd = getBubbleDocs();
		DeleteUser service = new DeleteUser(root, USERNAME_TO_DELETE);
		service.setIDRemoteService(idRemoteService);
		service.execute();
		
		boolean deleted = false;
		
		try {
			getUserFromUsername(USERNAME_TO_DELETE);
		} catch (LoginBubbleDocsException e) {
			deleted = true;
		}
		
		assertTrue("user was not deleted", deleted);
		assertNull("Spreadsheet was not deleted", bd.getSpreadsheetByNameNoException(SPREADSHEET_NAME));
	}

	/*
	 * accessUsername exists, is in session and is root toDeleteUsername exists
	 * and is in session Test if user and session are both deleted
	 */
	@Test
	public void successToDeleteIsInSession() {
		BubbleDocs bd = getBubbleDocs();
		String token = addUserToSession(USERNAME_TO_DELETE);
		DeleteUser service = new DeleteUser(root, USERNAME_TO_DELETE);
		service.setIDRemoteService(idRemoteService);
		service.execute();
		
		boolean deleted = false;
		
		try {
			getUserFromUsername(USERNAME_TO_DELETE);
		} catch (LoginBubbleDocsException e) {
			deleted = true;
		}
		
		assertTrue("user was not deleted", deleted);
		assertNull("Spreadsheet was not deleted", bd.getSpreadsheetByNameNoException(SPREADSHEET_NAME));
		assertNull("Removed user but not removed from session", getUserFromSession(token));
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void userToDeleteDoesNotExist() {
		DeleteUser service = new DeleteUser(root, USERNAME_DOES_NOT_EXIST);
		service.setIDRemoteService(idRemoteService);
		service.execute();
	}

	@Test(expected = InvalidPermissionException.class)
	public void notRootUser() {
		String ars = addUserToSession(USERNAME);
		DeleteUser service = new DeleteUser(ars, USERNAME_TO_DELETE);
		service.setIDRemoteService(idRemoteService);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void rootNotInSession() {
		removeUserFromSession(root);
		DeleteUser service = new DeleteUser(root, USERNAME_TO_DELETE);
		service.setIDRemoteService(idRemoteService);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void notInSessionAndNotRoot() {
		String ars = addUserToSession(USERNAME);
		removeUserFromSession(ars);

		DeleteUser service = new DeleteUser(ars, USERNAME_TO_DELETE);
		service.setIDRemoteService(idRemoteService);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUserDoesNotExist() {
		DeleteUser service = new DeleteUser(USERNAME_DOES_NOT_EXIST, USERNAME_TO_DELETE);
		service.setIDRemoteService(idRemoteService);
		service.execute();
	}
	
	@Test(expected = LoginBubbleDocsException.class)
	public void invalidPairUserPassword() {
		new Expectations() {
			{
				idRemoteService.removeUser(anyString);
				result = new LoginBubbleDocsException("anyString");
			}
		};
		success();
	}
	
	@Test(expected = UnavailableServiceException.class)
	public void idServiceUnavailable() {
		new Expectations() {
			{
				idRemoteService.removeUser(anyString);
				result = new RemoteInvocationException();
			}
		};
		success();
	}	
}// End DeleteUserTest class