package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * trying to invoke a remote service and it is unavailable.
 */

public class RemoteInvocationException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The default constructor.
	 * 
	 * @constructor
	 * @this {RemoteInvocationException}
	 */

	public RemoteInvocationException() {
		//Just needs to exist.
	}
	
	/**
	 * The string representation of the exception.
	 * 
	 * @return {String} The string that describes what happened
	 * for this exception to be thrown.
	 */
	
	@Override
	public String toString() {
		return "Remote call failed.\n";
	}
}// End RemoteInvocationException class
