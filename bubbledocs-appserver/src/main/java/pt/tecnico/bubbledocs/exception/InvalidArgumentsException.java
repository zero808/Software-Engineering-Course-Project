package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class InvalidArgumentsException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	public InvalidArgumentsException() {
		//Just needs to exist.
	}
	
	@Override
	public String toString() {
		return "Invalid arguments passed.\n";
	}
}// End InvalidArgumentsException class
