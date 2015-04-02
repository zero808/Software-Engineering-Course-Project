package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class InvalidEmailException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	public InvalidEmailException() {
		//Just needs to exist.
	}
	
	@Override
	public String toString() {
		return "Email given is invalid.\n";
	}
}// End InvalidEmailException class