package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * trying to create a user with an email that already
 * belongs to another user in the application.
 */

public class DuplicateEmailException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String _email;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {DuplicateEmailException}
	 * 
	 * @param {String} email The user's email.
	 */

	public DuplicateEmailException(String email) {
		/** @private */
		_email = email;
	}
	
	/**
	 * Method that returns the user's email.
	 * 
	 * @return {String} The user's email.
	 */
	
	public String getEmail() {
		return _email;
	}
	
	/**
	 * The string representation of the exception.
	 * 
	 * @return {String} The string that describes what happened
	 * for this exception to be thrown.
	 */
	
	@Override
	public String toString() {
		return "The email " + getEmail() + " already belongs to another user in BubbleDocs.\n";
	}
}// End DuplicateEmailException class
