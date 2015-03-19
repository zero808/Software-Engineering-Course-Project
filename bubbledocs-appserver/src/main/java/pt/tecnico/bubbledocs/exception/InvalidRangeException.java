package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class InvalidRangeException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	public InvalidRangeException() {
		//Just needs to exist
	}
	
	@Override
	public String toString() {
		return "Range is out of bonds.\n";
	}
}// End InvalidRangeException class.
