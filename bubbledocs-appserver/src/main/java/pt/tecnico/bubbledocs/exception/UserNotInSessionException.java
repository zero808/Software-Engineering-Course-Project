package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * a user tries to perform a certain action with a token
 * that has expired.
 */

public class UserNotInSessionException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String _username;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {UserNotInSessionException}
	 * 
	 * @param {String} username The user's username.
	 */

	public UserNotInSessionException(String username) {
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
		return "The user " + getUsername() + " does not currently have a valid session.\n";
	}
}// End UserNotInSessionException class
