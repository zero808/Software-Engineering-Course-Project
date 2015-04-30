package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;

public class LoginUserService extends BubbleDocsService {

	private String username;
	private String password;
	private BubbleDocs bd;
	private boolean integratorStatus;

	public LoginUserService(String username, String password, boolean integratorStatus) {
		this.username = username;
		this.password = password;
		this.integratorStatus = integratorStatus;
		this.bd = BubbleDocs.getInstance();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		bd.userExists(username);

		User user = bd.getUserByUsername(username);

		String pass = user.getPassword();

		if(integratorStatus) {
			if(pass == null || !(pass.equals(this.password))) {
				user.setPassword(password);
			}
			token = bd.login(this.username, this.password);
		} else {
			if (pass == null || !(pass.equals(this.password))) {
				throw new UnavailableServiceException();
			} else {
				token = bd.login(this.username, this.password);
			}
		}
	}
	
	public String getUserToken() {
		return token;
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