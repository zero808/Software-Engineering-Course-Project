package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.LoginUserService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LoginUserIntegrator extends BubbleDocsIntegrator {

	private String username;
	private String password;
	private IDRemoteServices idRemoteService;

	public LoginUserIntegrator(String username, String password) {
		this.username = username;
		this.password = password;
		this.idRemoteService = new IDRemoteServices();
	}

	@Override
	protected void dispatch() throws LoginBubbleDocsException, UnavailableServiceException {
		LoginUserService loginUserService = new LoginUserService(username, password);
		loginUserService.execute();
		
		try {
			idRemoteService.loginUser(username, password);
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
	}

	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemoteService = idRemote;
	}
}// End LoginUserIntegrator class