package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LoginUserService extends BubbleDocsService {

	private String username;
	private String password;
	private IDRemoteServices idRemoteService;

	public LoginUserService(String username, String password) {
		this.username = username;
		this.password = password;
		this.idRemoteService = new IDRemoteServices();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		BubbleDocs bd = getBubbleDocs();
		
		boolean found = false;
		
		for(User u : bd.getUsersSet()) {
			if(u.getUsername() == username) {
				found = true;
			}
		}
		
		if(!(found)) {
			throw new LoginBubbleDocsException(username);
		}
		
		User user = bd.getUserByUsername(username);
		
		String pass = user.getPassword();
		
		try {
			this.idRemoteService.loginUser(this.username, this.password);

			if (pass != null) { // Means that user already has a password.
				if (!(pass.equals(this.password))) { // If the password is different then the one stored locally.
					user.setPassword(this.password);
				}
			} 
			else {
				user.setPassword(this.password); // Means this is the first time the user logs in, so he gets a new password.
			}
		} catch (RemoteInvocationException e) { // Means the service is down.
			if (pass == null || !(pass.equals(this.password))) { // If the password is null or it doesn't match the local copy.
				throw new UnavailableServiceException();
			}
		}
		
		token = bd.login(this.username, this.password);	
	}

	public final String getUserToken() {
		return token;
	}
	
	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemoteService = idRemote;
	}
	
	@Override
	protected void validateAndAuthorize(String token) {
		
	}

	@Override
	protected void checkAccess() {
		return;
	}
}// End LoginUser class