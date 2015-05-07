package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.service.LoginUserService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LoginUserIntegrator extends BubbleDocsIntegrator {

	private String username;
	private String password;
	private String token;
	
	private IDRemoteServices idRemoteService;

	public LoginUserIntegrator(String username, String password) {
		this.username = username;
		this.password = password;
		this.idRemoteService = new IDRemoteServices();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		try {
			idRemoteService.loginUser(username, password);
			
			LoginUserService loginUserService = new LoginUserService(username, password, true);
			loginUserService.execute();
			token = loginUserService.getUserToken();
			
		} catch (RemoteInvocationException e) {
			
			LoginUserService loginUserService = new LoginUserService(username, password, false);
			loginUserService.execute();
			token = loginUserService.getUserToken();
		}
	}
	
	public String getUserToken() {
		return token;
	}
}// End LoginUserIntegrator class
