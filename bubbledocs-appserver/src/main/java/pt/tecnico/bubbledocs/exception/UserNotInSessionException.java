package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class UserNotInSessionException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String _username;

	public UserNotInSessionException(String username) {
		_username = username;
	}

	public String getUsername() {
		return _username;
	}
}