package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class UserDoesNotExistException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	public UserDoesNotExistException() {
		//Just needs to exist.
	}
	
	@Override
	public String toString() {
		return "The requested user does not exist in BubbleDocs.\n";
	}
}// End UserDoesNotExistException class
