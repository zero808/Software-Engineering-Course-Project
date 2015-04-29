package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.LoginUserService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LoginUserIntegrator extends BubbleDocsIntegrator {

	private String username;
	private String password;
	private String userPass;
	private String token;
	
	private IDRemoteServices idRemoteService;

	public LoginUserIntegrator(String username, String password) {
		this.username = username;
		this.password = password;
		this.idRemoteService = new IDRemoteServices();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		LoginUserService loginUserService = new LoginUserService(username, password);
		loginUserService.checkUser();
		userPass = loginUserService.getUserPass();

		if (userPass != null) { 
			if (userPass.equals(this.password)) {
				loginUserService.execute();

				userPass = loginUserService.getUserPass();
				token = loginUserService.getUserToken();

				try {
					this.idRemoteService.loginUser(this.username, this.password);
				} catch (RemoteInvocationException e) {
					return; //If the service is down, since the pass is correct the login stays.
				}

			} else {
				try {
					this.idRemoteService.loginUser(this.username, this.password);
					
					loginUserService.execute();

					userPass = loginUserService.getUserPass();
					token = loginUserService.getUserToken();
				} catch (RemoteInvocationException e) {
					throw new UnavailableServiceException();
				}
			}
		} else {
			try {
				this.idRemoteService.loginUser(this.username, this.password);
				
				loginUserService.execute();

				userPass = loginUserService.getUserPass();
				token = loginUserService.getUserToken();
			} catch (RemoteInvocationException e) {
				throw new UnavailableServiceException();
			}
		}
	}
	
	public String getUserToken() {
		return token;
	}

	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemoteService = idRemote;
	}
}// End LoginUserIntegrator class