package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;

public class GetUsername4TokenService extends BubbleDocsService {

	private BubbleDocs bd;
	private String username;

	public GetUsername4TokenService(String token) {
		this.token = token;
		this.bd = BubbleDocs.getInstance();
	}

	@Override
	protected void dispatch() throws InvalidTokenException {
		this.username = bd.getUsernameByToken(this.token);
		if (this.username == null) {
			throw new InvalidTokenException();
		}
	}
	
	@Override
	protected void checkAccess() {
		return;
	}
	
	public String getUserUsername() {
		return this.username;
	}
}// End GetUsername4TokenService class