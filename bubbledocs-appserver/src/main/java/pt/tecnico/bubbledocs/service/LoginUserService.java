package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class LoginUserService extends BubbleDocsService {

	private String username;
	private String password;
	private BubbleDocs bd;

	public LoginUserService(String username, String password) {
		this.username = username;
		this.password = password;
		this.bd = BubbleDocs.getInstance();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		checkUser();
		
		token = bd.login(this.username, this.password);	
	}
	
	public String getUserToken() {
		return token;
	}
	
	public String getUserPass() {
		
		User user = bd.getUserByUsername(username);
		
		return user.getPassword();
	}
	
	public void setUserPass(String newPass) {
		
		User user = bd.getUserByUsername(username);
		
		user.setPassword(newPass);	
	}
	
	public void removeUserFromSession() {
		
		String tok = bd.getTokenByUsername(this.username);
		
		if (tok != null) {
			bd.removeUserFromSession(tok);
		}
	}
	
	public void checkUser() {
		bd.userExists(username);
	}
	
	@Override
	protected void validateAndAuthorize(String token) {
		return;
	}

	@Override
	protected void checkAccess() {
		return;
	}
}// End LoginUserService class