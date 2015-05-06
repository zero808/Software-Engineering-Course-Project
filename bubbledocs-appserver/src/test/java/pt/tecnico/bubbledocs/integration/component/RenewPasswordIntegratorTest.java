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

public class RenewPasswordIntegratorTest extends BubbleDocsServiceTest {
	
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
		
		RenewPasswordIntegrator service = new RenewPasswordIntegrator(userToken);
		
		new Expectations() {
			{
				idRemote.renewPassword(username);
			}
		};
		service.execute();
		
		assertNull("Password was not invalidated", bd.getUserByUsername(bd.getUsernameByToken(userToken)).getPassword());
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		RenewPasswordIntegrator service = new RenewPasswordIntegrator(notInSessionToken);
		service.execute();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void invalidToken() {
		RenewPasswordIntegrator service = new RenewPasswordIntegrator("");
		service.execute();
	}
	
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
	
	@Test(expected = LoginBubbleDocsException.class)
	public void loginFailure() {
		RenewPasswordIntegrator service = new RenewPasswordIntegrator(userToken);
		
		new Expectations() {
			{
				idRemote.renewPassword(withNotNull());
				result = new LoginBubbleDocsException("invalidUser");
			}
		};
		service.execute();
	}
}// End RenewPasswordIntegratorTest class