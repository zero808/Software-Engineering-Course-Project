package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * the remote service StoreRemoteServices cannot load
 * a spreadsheet properly.
 */

public class CannotLoadDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The default constructor.
	 * 
	 * @constructor
	 * @this {CannotLoadDocumentException}
	 */

	public CannotLoadDocumentException() {
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
		return "Failed to load document in remote service.\n";
	}
}// End CannotLoadDocumentException class
