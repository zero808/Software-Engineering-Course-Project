package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class InvalidTokenException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	public InvalidTokenException() {
		//Just needs to exist.
	}
	
	@Override
	public String toString() {
		return "Token given is invalid.\n";
	}
}// End InvalidTokenException class
