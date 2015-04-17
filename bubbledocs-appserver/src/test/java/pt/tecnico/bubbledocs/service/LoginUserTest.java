package pt.tecnico.bubbledocs.service;

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
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LoginUserTest extends BubbleDocsServiceTest {
	
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

	@Override
	public void populate4Test() {
		getBubbleDocs();
		createUser(USERNAME, EMAIL, "João Pereira");
		createUser(USERNAME2, EMAIL2, "XPTO");
	}

	// returns the time of the last access for the user with token userToken.
	// It must get this data from the session object of the application
	private LocalTime getLastAccessTimeInSession(String userToken) {
		BubbleDocs bd = getBubbleDocs();
		return bd.getLastAccessTimeInSession(userToken);
	}
	
	private void changeUserTokenExpirationDate(String userToken, LocalTime newExpirationDate) {
		BubbleDocs bd = getBubbleDocs();
		bd.changeUserTokenExpirationDate(userToken, newExpirationDate);
	}
	
	private boolean isInSession(String userToken) {
		BubbleDocs bd = getBubbleDocs();
		return bd.isInSession(userToken);
	}

	@Test
	public void success() {
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, PASSWORD);
			}
		};
		service.setIDRemoteService(idRemote);
		service.execute();
		
		String token = service.getUserToken();
		LocalTime currentTime = new LocalTime();

		User user = getUserFromSession(token);
		assertTrue("User not in session", isInSession(token));
		assertEquals(USERNAME, user.getUsername());
		assertEquals(EMAIL, user.getEmail());
		assertEquals(PASSWORD, user.getPassword());

		int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();
		assertTrue("Access time in session not correctly set", difference >= 0);
		assertTrue("diference in seconds greater than expected", difference < 2);
	}

	@Test
	public void successLoginTwice() {
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, PASSWORD);
				idRemote.loginUser(USERNAME, PASSWORD);
			}
		};
		service.setIDRemoteService(idRemote);
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
		assertEquals(EMAIL, user.getEmail());
		assertEquals(PASSWORD, user.getPassword());
	}
	
	@Test
	public void successExpiredDateUserRemoved() {
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, PASSWORD);
			}
		};
		service.setIDRemoteService(idRemote);
		service.execute();
		
		String token = service.getUserToken();
		
		LocalTime newTime = new LocalTime().minusSeconds(1);
		changeUserTokenExpirationDate(token, newTime);
		assertFalse("User with expired date not removed from session", isInSession(token));
		
		User user = getUserFromSession(token);
		assertNull(user);
	}
	
	@Test
	public void successOtherExpiredDateUserRemoved() {
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, PASSWORD);
				idRemote.loginUser(USERNAME2, PASSWORD2);
			}
		};
		service.setIDRemoteService(idRemote);
		service.execute();
		
		String token = service.getUserToken();
		
		LocalTime newTime = new LocalTime().minusSeconds(1);
		changeUserTokenExpirationDate(token, newTime);
		
		LoginUser service2 = new LoginUser(USERNAME2, PASSWORD2);
		service2.execute();
		
		String token2 = service2.getUserToken();
		
		User user = getUserFromSession(token);
		assertFalse("User with expired date not removed from session", isInSession(token));
		assertNull(user);
		
		User user2 = getUserFromSession(token2);
		assertTrue("User not in session", isInSession(token2));
		assertEquals(USERNAME2, user2.getUsername());
		assertEquals(PASSWORD2, user2.getPassword());
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void loginUnknownUser() {
		LoginUser service = new LoginUser(USERNAME_NONEXISTENT, PASSWORD);
		service.execute();
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void loginUserWithWrongPassword() {
		LoginUser service = new LoginUser(USERNAME, INCORRECT_PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, INCORRECT_PASSWORD);
				result = new LoginBubbleDocsException("password");
			}
		};
		service.setIDRemoteService(idRemote);
		service.execute();
	}
	
	@Test(expected = UnavailableServiceException.class)
	public void remoteIDServerFailure(){
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		
		new Expectations() {
			{
				idRemote.loginUser(USERNAME, PASSWORD);
				result = new RemoteInvocationException();
			}
		};
		service.setIDRemoteService(idRemote);
		service.execute();
	}	
}// End LoginUserTest class