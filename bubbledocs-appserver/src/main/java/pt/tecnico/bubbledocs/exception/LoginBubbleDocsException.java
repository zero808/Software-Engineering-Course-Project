package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the exception that occurs when
 * either the username or password passed in the login 
 * service are incorrect/invalid.
 */

public class LoginBubbleDocsException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private String _desc;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {LoginBubbleDocsException}
	 * 
	 * @param {String} desc The description of which
	 * (username or password) was invalid/incorrect.
	 */

	public LoginBubbleDocsException(String desc) {
		/** @private */
		_desc = desc;
	}
	
	/**
	 * Method that returns the description of which
	 * (username or password) was invalid/incorrect.
	 * 
	 * @return {String} The description of which
	 * (username or password) was invalid/incorrect.
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
		return "The " + getDesc() + " given is invalid.\n";
	}
}// End LoginBubbleDocsException class
