package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.service.GetUserInfoService;

/**
 * Class that contains the test suite for the
 * GetUserInfoService.
 */

public class GetUserInfoServiceTest extends BubbleDocsServiceTest {

	private static final String USERNAME = "MARCO";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";
	private static final String EMAIL = "marcocunha@tecnico.pt";
	private static final String NAME = "Marco Cunha";
	
	/**
	 * Method that populates the DB with all
	 * the objects the test suite needs to execute.
	 */

	@Override
	public void populate4Test() {
		getBubbleDocs();
		createUser(USERNAME, EMAIL, NAME);
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
		GetUserInfoService service = new GetUserInfoService(USERNAME);
		service.execute();

		assertEquals(USERNAME, service.getUserUsername());
		assertEquals(NAME, service.getUserName());
		assertEquals(EMAIL, service.getUserEmail());
	}
	
	/**
	 * Test Case #2 - UsernameDoesNotExist
	 * 
	 * Tests what happens when the username doesn't exist.
	 * 
	 * Result - FAILURE - LoginBubbleDocsException
	 */

	@Test(expected = LoginBubbleDocsException.class)
	public void usernameDoesNotExist() {
		GetUserInfoService service = new GetUserInfoService(USERNAME_DOES_NOT_EXIST);
		service.execute();
	}
}// End GetUserInfoServiceTest class
