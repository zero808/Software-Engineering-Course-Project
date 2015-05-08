package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * the requested spreadsheet does not exist in the application.
 */

public class SpreadsheetDoesNotExistException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The default constructor.
	 * 
	 * @constructor
	 * @this {SpreadsheetDoesNotExistException}
	 */

	public SpreadsheetDoesNotExistException() {
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
		return "The spreadsheet requested does not exist.\n";
	}
}// End SpreadsheetDoesNotExistException class
