package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class DivByZeroException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	public DivByZeroException() {
		//Just needs to exist.
	}
	
	@Override
	public String toString() {
		return "Cannot divide by 0.\n";
	}
}// End DivByZeroException class
