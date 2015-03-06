package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class InvalidPermissionException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String _username;

	public InvalidPermissionException(String username) {
		_username = username;
	}
	
	public String getUsername() {
		return _username;
	}
}
