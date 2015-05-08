package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the higher level exception that 
 * is thrown when RemoteInvocationException is caught, in order
 * to hide how the remote calls are implemented.
 */

public class UnavailableServiceException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The default constructor.
	 * 
	 * @constructor
	 * @this {UnavailableServiceException}
	 */

	public UnavailableServiceException() {
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
		return "Could not fulfil request sent to remote service.\n";
	}
}// End UnavailableServiceException class
