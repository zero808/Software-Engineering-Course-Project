package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;
import org.joda.time.LocalTime;
import org.joda.time.Seconds;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

/**
 * Class that contains the test suite for the
 * LoginUserIntegrator.
 */

public class LoginUserIntegratorTest extends BubbleDocsServiceTest {
	
	@Mocked
	private IDRemoteServices idRemote;
	
	private static final String USERNAME = "jpp";
	private static final String EMAIL = "jpp@tecnico.pt";
	private static final String PASSWORD = "jpppass";
	private static final String USERNAME2 = "xpp";
	private static final String EMAIL2 = "xpp@tecnico.pt";
	private static final String PASSWORD2 = "xpppass";
	private static final String USERNAME_NONEXISTENT = "ABC";
	private static final String INCORRECT_PASSWORD = "ABC#";
	
	/**
	 * Method that populates the DB with all
	 * the objects the test suite needs to execute.
	 */

	@Override
	public void populate4Test() {
		BubbleDocs bd = getBubbleDocs();
		
		createUser(USERNAME, EMAIL, "João Pereira");
		createUser(USERNAME2, EMAIL2, "XPTO");
		
		bd.getUserByUsername(USERNAME).setPassword(PASSWORD);
		bd.getUserByUsername(USERNAME2).setPassword(PASSWORD2);
	}
	
	/**
	 * Auxiliary method that returns the last access time of the user with the
	 * token given.
	 * 
	 * @param {String} userToken The user's token.
	 * @return {LocalTime} The last time the token was used.
	 */

	private LocalTime getLastAccessTimeInSession(String userToken) {
		BubbleDocs bd = getBubbleDocs();
		return bd.getLastAccessTimeInSession(userToken);
	}
	
	/**
	 * Auxiliary method that changed the expiration time of the 
	 * given token.
	 * 
	 * @param {String} userToken The user's token.
	 * @param {LocalTime} newExpirationDate The new expiration time.
	 */
	
	private void changeUserTokenExpirationDate(String userToken, LocalTime newExpirationDate) {
		BubbleDocs bd = getBubbleDocs();
		bd.changeUserTokenExpirationDate(userToken, newExpirationDate);
	}
	
	/**
	 * Auxiliary method that checks if a user is currently in session.
	 * 
	 * @param {String} userToken The user's token.
	 * @return {Boolean} True if yes, false otherwise.
	 */
	
	private boolean isInSession(String userToken) {
		BubbleDocs bd = getBubbleDocs();
		return bd.isInSession(userToken);
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
		LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, PASSWORD);
			}
		};
		service.execute();
		
		String token = service.getUserToken();
		LocalTime currentTime = new LocalTime();

		User user = getUserFromSession(token);
		assertTrue("User not in session", isInSession(token));
		assertEquals(USERNAME, user.getUsername());

		int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();
		assertTrue("Access time in session not correctly set", difference >= 0);
		assertTrue("diference in seconds greater than expected", difference < 2);
	}
	
	/**
	 * Test Case #2 - SuccessCorrectPassword
	 * 
	 * Tests what happens when the remote service is down.
	 * If the password is correct the login proceeds.
	 * 
	 * Result - SUCCESS
	 */
	
	@Test
	public void successCorrectPassword() {
		LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, PASSWORD);
				result = new RemoteInvocationException();
			}
		};
		service.execute();
		
		String token = service.getUserToken();
		LocalTime currentTime = new LocalTime();

		User user = getUserFromSession(token);
		assertTrue("User not in session", isInSession(token));
		assertEquals(USERNAME, user.getUsername());

		int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();
		assertTrue("Access time in session not correctly set", difference >= 0);
		assertTrue("diference in seconds greater than expected", difference < 2);
	}
	
	/**
	 * Test Case #3 - SuccessIncorrectPassword
	 * 
	 * Tests what happens when the remote service updates the password remotely,
	 * the local copy is then updated as well and the login proceeds.
	 * 
	 * Result - SUCCESS
	 */
	
	@Test
	public void successIncorrectPassword() {	
		LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, INCORRECT_PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, INCORRECT_PASSWORD);
			}
		};
		service.execute();
		
		String token = service.getUserToken();
		LocalTime currentTime = new LocalTime();

		User user = getUserFromSession(token);
		assertTrue("User not in session", isInSession(token));
		assertEquals(USERNAME, user.getUsername());

		int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();
		assertTrue("Access time in session not correctly set", difference >= 0);
		assertTrue("diference in seconds greater than expected", difference < 2);
	}
	
	/**
	 * Test Case #4 - SuccessLoginTwice
	 * 
	 * Tests what happens when the same user logs in twice.
	 * 
	 * Result - SUCCESS
	 */

	@Test
	public void successLoginTwice() {
		LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, PASSWORD);
				idRemote.loginUser(USERNAME, PASSWORD);
			}
		};
		service.execute();
		
		String token1 = service.getUserToken();

		service.execute();
		String token2 = service.getUserToken();
		
		assertFalse("Token not renewed", token1.equals(token2));

		User user = getUserFromSession(token1);
		assertFalse("Token not renewed", isInSession(token1));
		assertNull(user);
		
		user = getUserFromSession(token2);
		assertTrue("User not in session", isInSession(token2));
		assertEquals(USERNAME, user.getUsername());
	}
	
	/**
	 * Test Case #5 - SuccessExpiredDateUserRemoved
	 * 
	 * Tests what happens when the user that just logged in has his session terminated.
	 * 
	 * Result - SUCCESS
	 */
	
	@Test
	public void successExpiredDateUserRemoved() {
		LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, PASSWORD);
			}
		};
		service.execute();
		
		String token = service.getUserToken();
		
		LocalTime newTime = new LocalTime().minusSeconds(1);
		changeUserTokenExpirationDate(token, newTime);
		assertFalse("User with expired date not removed from session", isInSession(token));
		
		User user = getUserFromSession(token);
		assertNull(user);
	}
	
	/**
	 * Test Case #6 - SuccessOtherExpiredDateUserRemoved
	 *
	 * Tests what happens when a user's session terminates.
	 * 
	 * Result - SUCCESS
	 */
	
	@Test
	public void successOtherExpiredDateUserRemoved() {
		LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, PASSWORD);
				idRemote.loginUser(USERNAME2, PASSWORD2);
			}
		};
		service.execute();
		
		String token = service.getUserToken();
		
		LocalTime newTime = new LocalTime().minusSeconds(1);
		changeUserTokenExpirationDate(token, newTime);
		
		LoginUserIntegrator service2 = new LoginUserIntegrator(USERNAME2, PASSWORD2);
		service2.execute();
		
		String token2 = service2.getUserToken();
		
		User user = getUserFromSession(token);
		assertFalse("User with expired date not removed from session", isInSession(token));
		assertNull(user);
		
		User user2 = getUserFromSession(token2);
		assertTrue("User not in session", isInSession(token2));
		assertEquals(USERNAME2, user2.getUsername());
	}
	
	/**
	 * Test Case #7 - LoginUnknownUser
	 *
	 * Tests what happens when the user trying to login doesn't exist.
	 * 
	 * Result - FAILURE - LoginBubbleDocsException
	 */

	@Test(expected = LoginBubbleDocsException.class)
	public void loginUnknownUser() {
		LoginUserIntegrator service = new LoginUserIntegrator(USERNAME_NONEXISTENT, PASSWORD);
		service.execute();
	}
	
	/**
	 * Test Case #8 - LoginUserWithWrongPassword
	 *
	 * Tests what happens when the user trying to login gives the wrong password.
	 * 
	 * Result - FAILURE - LoginBubbleDocsException
	 */

	@Test(expected = LoginBubbleDocsException.class)
	public void loginUserWithWrongPassword() {
		LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, INCORRECT_PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, INCORRECT_PASSWORD);
				result = new LoginBubbleDocsException("password");
			}
		};
		service.execute();
	}
	
	/**
	 * Test Case #9 - RemoteIDServerFailure
	 *
	 * Tests what happens when the remote service is down and
	 * the user gives the wrong password.
	 * 
	 * Result - FAILURE - UnavailableServiceException
	 */
	
	@Test(expected = UnavailableServiceException.class)
	public void remoteIDServerFailure(){
		LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, INCORRECT_PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, INCORRECT_PASSWORD);
				result = new RemoteInvocationException();
			}
		};
		service.execute();
	}	
}// End LoginUserIntegratorTest class
