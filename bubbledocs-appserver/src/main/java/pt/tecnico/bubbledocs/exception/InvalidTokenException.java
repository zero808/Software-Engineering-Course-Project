package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * an invalid token is passed when trying to execute a
 * particular service.
 * 
 * A token must follow a specific structure and not be empty
 * in order to be valid.
 */

public class InvalidTokenException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The default constructor.
	 * 
	 * @constructor
	 * @this {InvalidTokenException}
	 */

	public InvalidTokenException() {
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
		return "Token given is invalid.\n";
	}
}// End InvalidTokenException class
