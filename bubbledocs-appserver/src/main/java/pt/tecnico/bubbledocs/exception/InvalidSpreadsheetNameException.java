package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * trying to create a spreadsheet with an invalid name.
 * 
 * Spreadsheet names must only contain regular ASCII 
 * characters.
 */

public class InvalidSpreadsheetNameException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The default constructor.
	 * 
	 * @constructor
	 * @this {InvalidSpreadsheetNameException}
	 */

	public InvalidSpreadsheetNameException() {
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
		return "Invalid Spreadsheet name passed.\n";
	}
}// End InvalidSpreadsheetNameException class
