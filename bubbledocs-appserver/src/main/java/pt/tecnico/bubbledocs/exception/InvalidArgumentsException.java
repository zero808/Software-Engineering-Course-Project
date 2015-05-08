package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * invalid arguments are passed when trying to create a
 * particular object in the application.
 * 
 * Depending on which object is being created, what defines 
 * what an invalid argument is varies.
 */

public class InvalidArgumentsException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The default constructor.
	 * 
	 * @constructor
	 * @this {InvalidArgumentsException}
	 */

	public InvalidArgumentsException() {
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
		return "Invalid arguments passed.\n";
	}
}// End InvalidArgumentsException class
