package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * a user tries to perform an action that he has no permission
 * to do so.
 */

public class InvalidPermissionException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String _username;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {InvalidPermissionException}
	 * 
	 * @param {String} username The user's username.
	 */

	public InvalidPermissionException(String username) {
		/** @private */
		_username = username;
	}
	
	/**
	 * Method that returns the user's username.
	 * 
	 * @return {String} The user's username.
	 */
	
	public String getUsername() {
		return _username;
	}
	
	/**
	 * The string representation of the exception.
	 * 
	 * @return {String} The string that describes what happened
	 * for this exception to be thrown.
	 */
	
	@Override
	public String toString() {
		return "This username " + getUsername() + " does not have permissions to perform this action.\n";
	}
}// End InvalidPermissionException class
