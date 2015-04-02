package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;

public class LoginUser extends BubbleDocsService {

	private String username;
	private String password;
	private String userToken;

	public LoginUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	protected void dispatch() throws LoginBubbleDocsException, InvalidTokenException {
		
		BubbleDocs bd = getBubbleDocs();
		User user = bd.getUserByUsername(this.username);
		
		if (user == null) {
			throw new LoginBubbleDocsException("username");
		}
		
		String pass = user.getPassword();
		if (pass == null || !(pass.equals(this.password))) {
			throw new LoginBubbleDocsException("password");
		}
		
		this.userToken = bd.login(this.username, this.password);
		if (this.userToken == null || this.userToken.equals("")) {
			throw new InvalidTokenException();
		}
	}

	public final String getUserToken() {
		return this.userToken;
	}
}// End LoginUser class