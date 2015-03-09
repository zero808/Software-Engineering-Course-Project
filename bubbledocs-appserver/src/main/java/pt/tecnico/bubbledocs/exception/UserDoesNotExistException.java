package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class UserDoesNotExistException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String _username;

	public UserDoesNotExistException(String username) {
		_username = username;
	}
	
	public String getUserName() {
		return _username;
	}
}
