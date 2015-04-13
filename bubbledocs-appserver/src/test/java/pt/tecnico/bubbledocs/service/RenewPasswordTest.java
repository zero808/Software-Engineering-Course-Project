package pt.tecnico.bubbledocs.service;

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
import pt.tecnico.bubbledocs.service.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordTest extends BubbleDocsServiceTest {
	
	@Mocked
	private IDRemoteServices idRemote;
	
	private String userToken;
	private String notInSessionToken = "antonio6";

	public void populate4Test() {
		getBubbleDocs();
		
		createUser("lff", "woot", "Luis");
		this.userToken = addUserToSession("lff");
	}

	@Test
	public void success() {
		BubbleDocs bd = getBubbleDocs();
		String username = bd.getUsernameByToken(userToken);
		
		RenewPassword service = new RenewPassword(userToken);
		
		new Expectations() {
			{
				idRemote.renewPassword(username);
			}
		};
		service.setIDRemoteService(idRemote);
		service.execute();
		
		assertNull("Password was not invalidated", bd.getUserByUsername(bd.getUsernameByToken(userToken)).getPassword());
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		RenewPassword service = new RenewPassword(notInSessionToken);
		service.setIDRemoteService(idRemote);
		service.execute();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void invalidToken() {
		RenewPassword service = new RenewPassword("");
		service.setIDRemoteService(idRemote);
		service.execute();
	}
	
	@Test(expected = UnavailableServiceException.class)
	public void remoteInvocationFailure() {
		RenewPassword service = new RenewPassword(userToken);
		
		new Expectations() {
			{
				idRemote.renewPassword(withNotNull());
				result = new RemoteInvocationException();
			}
		};
		service.setIDRemoteService(idRemote);
		service.execute();
	}
	
	@Test(expected = LoginBubbleDocsException.class)
	public void loginFailure() {
		RenewPassword service = new RenewPassword(userToken);
		
		new Expectations() {
			{
				idRemote.renewPassword(withNotNull());
				result = new LoginBubbleDocsException("invalidUser");
			}
		};
		service.setIDRemoteService(idRemote);
		service.execute();
	}
}// End RenewPasswordTest class