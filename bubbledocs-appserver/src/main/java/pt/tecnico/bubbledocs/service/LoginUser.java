package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LoginUser extends BubbleDocsService {

	private String username;
	private String password;
	private String userToken;
	private IDRemoteServices idRemoteService;

	public LoginUser(String username, String password) {
		this.username = username;
		this.password = password;
		this.idRemoteService = new IDRemoteServices();
	}

	@Override
	protected void dispatch() throws LoginBubbleDocsException, UnavailableServiceException {
		
		BubbleDocs bd = getBubbleDocs();
		
		User user = bd.getUserByUsername(this.username);
		if (user == null) {
			throw new LoginBubbleDocsException("username");
		}
		
		String pass = user.getPassword();
		
		try {
			
			this.idRemoteService.loginUser(this.username, this.password);
			
			if (pass != null) {
				if (!(pass.equals(this.password))) {
					user.setPassword(this.password);
				}
			} 
			else {
				user.setPassword(this.password);
			}
			
			
		} catch (RemoteInvocationException e) {
			
			if (pass == null || !(pass.equals(this.password))) {
				throw new UnavailableServiceException();
			}
			
		}
		
		this.userToken = bd.login(this.username, this.password);
		
	}

	public final String getUserToken() {
		return this.userToken;
	}
	
	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemoteService = idRemote;
	}
}// End LoginUser class