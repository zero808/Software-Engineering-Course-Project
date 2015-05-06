package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.service.GetUserInfoService;

public class GetUserInfoServiceTest extends BubbleDocsServiceTest {

	private static final String USERNAME = "MARCO";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";
	private static final String EMAIL = "marcocunha@tecnico.pt";
	private static final String NAME = "Marco Cunha";
	

	@Override
	public void populate4Test() {
		getBubbleDocs();
		createUser(USERNAME, EMAIL, NAME);
	}

	@Test
	public void success() {
		GetUserInfoService service = new GetUserInfoService(USERNAME);
		service.execute();

		assertEquals(USERNAME, service.getUserUsername());
		assertEquals(NAME, service.getUserName());
		assertEquals(EMAIL, service.getUserEmail());
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void usernameDoesNotExist() {
		GetUserInfoService service = new GetUserInfoService(USERNAME_DOES_NOT_EXIST);
		service.execute();
	}
}// End GetUserInfoServiceTest class
