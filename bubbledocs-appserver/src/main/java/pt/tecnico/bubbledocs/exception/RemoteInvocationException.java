package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class RemoteInvocationException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	public RemoteInvocationException() {
		//Just needs to exist.
	}
	
	@Override
	public String toString() {
		return "Remote call failed.\n";
	}
}// End RemoteInvocationException class