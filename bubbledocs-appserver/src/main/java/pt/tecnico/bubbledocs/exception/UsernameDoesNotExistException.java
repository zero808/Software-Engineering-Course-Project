package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class UsernameDoesNotExistException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String _username;

	public UsernameDoesNotExistException(String username) {
		_username = username;
	}
	
	public String getUsername() {
		return _username;
	}
}
