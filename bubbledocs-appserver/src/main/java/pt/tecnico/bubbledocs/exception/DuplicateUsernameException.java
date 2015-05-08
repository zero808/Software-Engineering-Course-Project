package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * trying to create a user with an username that already
 * belongs to another user in the application.
 */

public class DuplicateUsernameException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String _username;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {DuplicateUsernameException}
	 * 
	 * @param {String} username The user's username.
	 */

	public DuplicateUsernameException(String username) {
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
		return "The username " + getUsername() + " already exists in BubbleDocs.\n";
	}
}// End DuplicateUsernameException class
