package pt.tecnico.bubbledocs.integration.component;

import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Class that contains the test suite for the
 * GetUsername4TokenService.
 */

public class GetUsername4TokenServiceTest extends BubbleDocsServiceTest {
	
	private static final String USERNAME = "ABC";
	private static final String EMAIL = "ABC@tecnico.pt";
	private static final String NAME = "ABCNAME";
	private static final String INCORRECT_TOKEN = "INCORRECTTOKEN";
	private static final String EMPTY_TOKEN = "";
	
	private String userToken;
	
	/**
	 * Method that populates the DB with all
	 * the objects the test suite needs to execute.
	 */

	@Override
	public void populate4Test() {
		getBubbleDocs();
		
		createUser(USERNAME, EMAIL, NAME);
		userToken = addUserToSession(USERNAME);
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
		GetUsername4TokenService service = new GetUsername4TokenService(userToken);
		service.execute();
		
		assertEquals(USERNAME, service.getUserUsername());
	}
	
	/**
	 * Test Case #2 - EmptyToken
	 * 
	 * Tests what happens when an empty token is given.
	 * 
	 * Result - FAILURE - InvalidTokenException
	 */
	
	@Test(expected = InvalidTokenException.class)
	public void emptyToken() {
		GetUsername4TokenService service = new GetUsername4TokenService(EMPTY_TOKEN);
		service.execute();
	}
	
	/**
	 * Test Case #3 - IncorrectToken
	 * 
	 * Tests what happens when an incorrect token in given.
	 * 
	 * Result - FAILURE - UserNotInSessionException
	 */
	
	@Test(expected = UserNotInSessionException.class)
	public void incorrectToken() {
		GetUsername4TokenService service = new GetUsername4TokenService(INCORRECT_TOKEN);
		service.execute();
	}
	
	/**
	 * Test Case #4 - UserNotInSession
	 * 
	 * Tests what happens when the token is no longer valid.
	 * 
	 * Result - FAILURE - UserNotInSessionException
	 */
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		removeUserFromSession(userToken);
		GetUsername4TokenService service = new GetUsername4TokenService(userToken);
		service.execute();
	}
}// End GetUsername4TokenServiceTest class
