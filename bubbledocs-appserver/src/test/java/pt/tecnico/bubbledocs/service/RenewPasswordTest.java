package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertNull;
import mockit.Expectations;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordTest extends BubbleDocsServiceTest {
	
	private String userToken;
	private String notInSessionToken = "antonio6";
	private String remoteFailureToken;
	private String invalidUserToken;

	public void populate4Test() {
		getBubbleDocs();
		
		createUser("lf", "woot", "Luis");
		this.userToken = addUserToSession("lf");
	}

	@Test
	public void success() {
		BubbleDocs bd = getBubbleDocs();
		String username = bd.getUsernameByToken(userToken);
		
		RenewPassword service = new RenewPassword(userToken);
		service.execute();
		
		new Expectations() {
			{
				IDRemoteServices idRemote = new IDRemoteServices();
				idRemote.renewPassword(username);
			}
		};
		
		assertNull("Password was not invalidated", bd.getUserByUsername(bd.getUsernameByToken(userToken)).getPassword());
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		RenewPassword service = new RenewPassword(notInSessionToken);
		service.execute();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void invalidToken() {
		RenewPassword service = new RenewPassword("");
		service.execute();
	}
	
	@Test(expected = UnavailableServiceException.class)
	public void remoteInvocationFailure() {
		createUser("Remote invocation failure", "woot", "Luis");
		this.remoteFailureToken = addUserToSession("Remote invocation failure");
		
		RenewPassword service = new RenewPassword(remoteFailureToken);
		service.execute();
		
		new Expectations() {
			{
				IDRemoteServices idRemote = new IDRemoteServices();
				idRemote.renewPassword(withNotNull());
				result = new RemoteInvocationException();
			}
		};
	}
	
	@Test(expected = LoginBubbleDocsException.class)
	public void loginFailure() {
		createUser("invalidUser", "woot", "Luis");
		this.invalidUserToken = addUserToSession("invalidUser");
		
		RenewPassword service = new RenewPassword(invalidUserToken);
		service.execute();
		
		new Expectations() {
			{
				IDRemoteServices idRemote = new IDRemoteServices();
				idRemote.renewPassword(withNotNull());
				result = new LoginBubbleDocsException("invalidUser");
			}
		};	
	}
}// End RenewPasswordTest class