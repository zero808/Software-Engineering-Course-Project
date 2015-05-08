package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * trying to import a spreadsheet locally.
 */

public class ImportDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String _desc;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {ImportDocumentException}
	 * 
	 * @param {String} desc The spreadsheet's name that
	 * failed to import properly.
	 */

	public ImportDocumentException(String desc) {
		/** @private */
		_desc = desc;
	}
	
	/**
	 * Method that returns the spreadsheet's name.
	 * 
	 * @return {String} The spreadsheet's name.
	 */
	
	public String getDesc() {
		return _desc;
	}
	
	/**
	 * The string representation of the exception.
	 * 
	 * @return {String} The string that describes what happened
	 * for this exception to be thrown.
	 */
	
	@Override
	public String toString() {
		return "Error importing document " + getDesc() + ".\n"; 
	}
}// End ImportDocumentException class
