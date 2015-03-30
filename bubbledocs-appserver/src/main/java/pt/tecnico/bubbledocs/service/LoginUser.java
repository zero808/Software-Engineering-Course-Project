package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidPasswordException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.UserDoesNotExistException;

public class LoginUser extends BubbleDocsService {

	private String username;
	private String password;
	private String userToken;

	public LoginUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	protected void dispatch() throws UserDoesNotExistException, InvalidPasswordException, InvalidTokenException {
		
		BubbleDocs bd = getBubbleDocs();
		User user = bd.getUserByUsername(this.username);
		
		if (user == null) {
			throw new UserDoesNotExistException();
		}
		
		String pass = user.getPassword();
		if (pass == null || !(pass.equals(this.password))) {
			throw new InvalidPasswordException();
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