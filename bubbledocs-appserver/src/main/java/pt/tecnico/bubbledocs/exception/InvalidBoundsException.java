package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * trying to create a spreadsheet with invalid bounds.
 */

public class InvalidBoundsException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private int row;
	private int col;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {InvalidBoundsException}
	 * 
	 * @param {number} row The spreadsheet's row value.
	 * @param {number} col The spreadsheet's column value.
	 */

	public InvalidBoundsException(int row, int col) {
		/** @private */
		this.row = row;
		/** @private */
		this.col = col;
	}
	
	/**
	 * Method that returns the spreadsheet's row value.
	 * 
	 * @return {number} The spreadsheet's row value.
	 */
	
	public int getRow() {
		return row;
	}
	
	/**
	 * Method that returns the spreadsheet's column value.
	 * 
	 * @return {number} The spreadsheet's column value.
	 */
	
	public int getColumn() {
		return col;
	}
	
	/**
	 * The string representation of the exception.
	 * 
	 * @return {String} The string that describes what happened
	 * for this exception to be thrown.
	 */
	
	@Override
	public String toString() {
		return "Invalid bounds passed. Must be between 1 and MAX_INT. Received Row: " + getRow() + " Col: " + getColumn() + "\n";
	}
}// End InvalidBoundsException class
