package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class CannotLoadDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	public CannotLoadDocumentException() {
		//Just needs to exist.
	}
	
	@Override
	public String toString() {
		return "Failed to load document in remote service.\n";
	}
}// End CannotLoadDocumentException class