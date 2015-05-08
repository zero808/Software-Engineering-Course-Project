package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * a range is passed as an argument to a unary function
 * that exceeds the maximum limits of the spreadsheet
 * where the function is.
 */

public class InvalidRangeException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The default constructor.
	 * 
	 * @constructor
	 * @this {InvalidRangeException}
	 */

	public InvalidRangeException() {
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
		return "Range is out of bounds.\n";
	}
}// End InvalidRangeException class
