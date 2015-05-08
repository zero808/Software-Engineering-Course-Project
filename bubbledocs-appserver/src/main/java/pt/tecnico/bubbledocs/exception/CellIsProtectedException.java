package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * trying to change the value of a cell, while it is 
 * protected.
 */

public class CellIsProtectedException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private int _row;
	private int _collumn;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {CellIsProtectedException}
	 * 
	 * @param {number} row The cell's row value.
	 * @param {number} collumn The cell's column value.
	 */

	public CellIsProtectedException(int row, int collumn) {
		/** @private */
		_row = row;
		/** @private */
		_collumn = collumn;
	}
	
	/**
	 * Method that returns the cell's row value.
	 * 
	 * @return {number} The cell's row value.
	 */
	
	public int getRow() {
		return _row;
	}
	
	/**
	 * Method that returns the cell's column value.
	 * 
	 * @return {number} The cell's column value.
	 */
	
	public int getCollumn() {
		return _collumn;
	}
	
	/**
	 * The string representation of the exception.
	 * 
	 * @return {String} The string that describes what happened
	 * for this exception to be thrown.
	 */
	
	@Override
	public String toString() {
		return "Cell at " + "(" + getRow() + ", " + getCollumn() + ")" + "is protected.\n"; 
	}
}// End CellIsProtectedException class
