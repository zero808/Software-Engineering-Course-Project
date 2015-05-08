package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertNull;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.component.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.service.integration.RenewPasswordIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

/**
 * Class that contains the test suite for the
 * RenewPasswordIntegrator.
 */

public class RenewPasswordIntegratorTest extends BubbleDocsServiceTest {
	
	@Mocked
	private IDRemoteServices idRemote;
	
	private String userToken;
	private String notInSessionToken = "antonio6";
	
	/**
	 * Method that populates the DB with all
	 * the objects the test suite needs to execute.
	 */

	public void populate4Test() {
		getBubbleDocs();
		
		createUser("lff", "woot", "Luis");
		this.userToken = addUserToSession("lff");
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
		BubbleDocs bd = getBubbleDocs();
		String username = bd.getUsernameByToken(userToken);
		
		RenewPasswordIntegrator service = new RenewPasswordIntegrator(userToken);
		
		new Expectations() {
			{
				idRemote.renewPassword(username);
			}
		};
		service.execute();
		
		assertNull("Password was not invalidated", bd.getUserByUsername(bd.getUsernameByToken(userToken)).getPassword());
	}
	
	/**
	 * Test Case #2 - UserNotInSession
	 * 
	 * Tests what happens when the user calling the service
	 * doesn't have a valid session.
	 * 
	 * Result - FAILURE - UserNotInSessionException
	 */
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		RenewPasswordIntegrator service = new RenewPasswordIntegrator(notInSessionToken);
		service.execute();
	}
	
	/**
	 * Test Case #3 - InvalidToken
	 * 
	 * Tests what happens when the user gives an invalid token.
	 * 
	 * Result - FAILURE - InvalidTokenException
	 */
	
	@Test(expected = InvalidTokenException.class)
	public void invalidToken() {
		RenewPasswordIntegrator service = new RenewPasswordIntegrator("");
		service.execute();
	}
	
	/**
	 * Test Case #4 - RemoteInvocationFailure
	 * 
	 * Tests what happens when the remote service is down.
	 * 
	 * Result - FAILURE - UnavailableServiceException
	 */
	
	@Test(expected = UnavailableServiceException.class)
	public void remoteInvocationFailure() {
		RenewPasswordIntegrator service = new RenewPasswordIntegrator(userToken);
		
		new Expectations() {
			{
				idRemote.renewPassword(withNotNull());
				result = new RemoteInvocationException();
			}
		};
		service.execute();
	}
	
	/**
	 * Test Case #5 - LoginFailure
	 * 
	 * Tests what happens when the user that wants his password
	 * renewed fails to login properly.
	 * 
	 * Result - FAILURE - LoginBubbleDocsException
	 */
	
	@Test(expected = LoginBubbleDocsException.class)
	public void loginFailure() {
		RenewPasswordIntegrator service = new RenewPasswordIntegrator(userToken);
		
		new Expectations() {
			{
				idRemote.renewPassword(withNotNull());
				result = new LoginBubbleDocsException("username");
			}
		};
		service.execute();
	}
}// End RenewPasswordIntegratorTest class
