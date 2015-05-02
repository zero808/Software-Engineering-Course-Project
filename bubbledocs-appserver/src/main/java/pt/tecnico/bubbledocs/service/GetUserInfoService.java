package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class GetUserInfoService extends BubbleDocsService {

	private String username;
	private String name;
	private String email;

	public GetUserInfoService(String username) {
		this.username = username;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		bd.userExists(username);
		User user = bd.getUserByUsername(username);
		this.name = user.getName();
		this.email = user.getEmail();
	}

	@Override
	protected void checkAccess() {
		return;
	}

	public String getUserUsername() {
		return this.username;
	}

	public String getUserName() {
		return this.name;
	}

	public String getUserEmail() {
		return this.email;
	}
}// End GetUserInfoService class