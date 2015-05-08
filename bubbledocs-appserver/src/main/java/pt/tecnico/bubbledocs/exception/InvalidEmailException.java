package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * trying to create/edit a user with an invalid email.
 * 
 * Emails must follow a specific structure in order to be
 * valid.
 */

public class InvalidEmailException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The default constructor.
	 * 
	 * @constructor
	 * @this {InvalidEmailException}
	 */

	public InvalidEmailException() {
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
		return "Email given is invalid.\n";
	}
}// End InvalidEmailException class
