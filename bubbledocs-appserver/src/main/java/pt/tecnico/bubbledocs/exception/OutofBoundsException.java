package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * trying to add a particular content to a cell that 
 * is outside of the spreadsheet bounds.
 */

public class OutofBoundsException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private int _rows;
	private int _collumns;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {OutofBoundsException}
	 * 
	 * @param {number} rows The spreadsheet's max rows.
	 * @param {number} collumns The spreadsheet's max columns.
	 */

	public OutofBoundsException(int rows, int collumns) {
		/** @private */
		_rows = rows;
		/** @private */
		_collumns = collumns;
	}
	
	/**
	 * Method that returns the spreadsheet's max rows value.
	 * 
	 * @return {number} The spreadsheet's max rows value.
	 */
	
	public int getRows() {
		return _rows;
	}
	
	/**
	 * Method that returns the spreadsheet's max columns value.
	 * 
	 * @return {number} The spreadsheet's max columns value.
	 */
	
	public int getCollumns() {
		return _collumns;
	}
	
	/**
	 * The string representation of the exception.
	 * 
	 * @return {String} The string that describes what happened
	 * for this exception to be thrown.
	 */
	
	@Override
	public String toString() {
		return "Out of spreadsheet bonds -> " + "(" + getRows() + ", " + getCollumns() + ")";
	}
}// End OutofBoundsException class
