package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * trying to create/edit a user with an invalid username.
 * 
 * A username must have between 3 and 8 in length and not 
 * be null in order to be valid.
 */

public class InvalidUsernameException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String _desc;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {InvalidUsernameException}
	 * 
	 * @param {number} desc The description of which of 
	 * the rules was broken.
	 */

	public InvalidUsernameException(String desc) {
		/** @private */
		_desc = desc;
	}
	
	/**
	 * Method that returns the description of the rule that 
	 * was broken.
	 * 
	 * @return {String} The description of the rule that 
	 * was broken.
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
		return "Invalid Username because " + getDesc() + "\n";
	}
}// End InvalidUsernameException class
