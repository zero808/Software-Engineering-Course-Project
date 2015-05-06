package pt.tecnico.bubbledocs.integration.component;

import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class GetUsername4TokenServiceTest extends BubbleDocsServiceTest {
	
	private static final String USERNAME = "ABC";
	private static final String EMAIL = "ABC@tecnico.pt";
	private static final String NAME = "ABCNAME";
	private static final String INCORRECT_TOKEN = "INCORRECTTOKEN";
	private static final String EMPTY_TOKEN = "";
	
	private String userToken;

	@Override
	public void populate4Test() {
		getBubbleDocs();
		
		createUser(USERNAME, EMAIL, NAME);
		userToken = addUserToSession(USERNAME);
	}
	
	@Test
	public void success() {
		GetUsername4TokenService service = new GetUsername4TokenService(userToken);
		service.execute();
		
		assertEquals(USERNAME, service.getUserUsername());
	}
	
	@Test(expected = InvalidTokenException.class)
	public void emptyToken() {
		GetUsername4TokenService service = new GetUsername4TokenService(EMPTY_TOKEN);
		service.execute();
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void incorrectToken() {
		GetUsername4TokenService service = new GetUsername4TokenService(INCORRECT_TOKEN);
		service.execute();
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		removeUserFromSession(userToken);
		GetUsername4TokenService service = new GetUsername4TokenService(userToken);
		service.execute();
	}
}// End GetUsername4TokenServiceTest class
