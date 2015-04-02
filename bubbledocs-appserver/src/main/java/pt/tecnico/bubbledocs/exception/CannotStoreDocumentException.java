package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class CannotStoreDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	public CannotStoreDocumentException() {
		//Just needs to exist.
	}
	
	@Override
	public String toString() {
		return "Failed to store document in remote service (possibly out of space).\n";
	}
}// End CannotStoreDocumentException class
