package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class LoginUser extends BubbleDocsService {

	private BubbleDocs bd;
	private String username;
	private String password;
	private String userToken;

	public LoginUser(String username, String password) {
		this.bd = BubbleDocs.getInstance();
		this.username = username;
		this.password = password;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		bd.login(username, password);
		userToken = bd.getTokenByUsername(username);
	}

	public final String getUserToken() {
		return userToken;
	}
}