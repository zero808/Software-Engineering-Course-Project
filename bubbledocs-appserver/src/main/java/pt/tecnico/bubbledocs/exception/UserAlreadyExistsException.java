package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class UserAlreadyExistsException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String _username;

	public UserAlreadyExistsException(String username) {
		_username = username;
	}
	
	public String getUsername() {
		return _username;
	}
	
	@Override
	public String toString() {
		return "The username " + getUsername() + " already exists in BubbleDocs.\n";
	}
}// End UserAlreadyExistsException class
