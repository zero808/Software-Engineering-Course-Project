package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class UnavailableServiceException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	public UnavailableServiceException() {
		//Just needs to exist.
	}
	
	@Override
	public String toString() {
		return "Could not fulfil request sent to remote service.\n";
	}
}// End UnavailableServiceException class