package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * the remote service StoreRemoteServices cannot store
 * a spreadsheet properly.
 */

public class CannotStoreDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The default constructor.
	 * 
	 * @constructor
	 * @this {CannotStoreDocumentException}
	 */

	public CannotStoreDocumentException() {
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
		return "Failed to store document in remote service (possibly out of space).\n";
	}
}// End CannotStoreDocumentException class
