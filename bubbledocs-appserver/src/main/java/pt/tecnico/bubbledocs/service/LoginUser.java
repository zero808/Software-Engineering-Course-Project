package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidPasswordException;
import pt.tecnico.bubbledocs.exception.UserDoesNotExistException;

public class LoginUser extends BubbleDocsService {

	private BubbleDocs bd;
	private String username;
	private String password;
	private String userToken;

	public LoginUser(String username, String password) {
		this.bd = getBubbleDocs();
		this.username = username;
		this.password = password;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		if (bd.getUserByUsername(username) == null) {
			throw new UserDoesNotExistException();
		}
		
		String pass = bd.getUserByUsername(username).getPassword();
		if (pass == null || !(pass.equals(password))) {
			throw new InvalidPasswordException();
		}
		
		userToken = bd.login(username, password);
	}

	public final String getUserToken() {
		return userToken;
	}
}// End LoginUser class