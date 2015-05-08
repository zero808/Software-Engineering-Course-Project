package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the service that is 
 * responsible for getting all of a user's info.
 */

public class GetUserInfoService extends BubbleDocsService {

	private String username;
	private String name;
	private String email;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {GetUserInfoService}
	 * 
	 * @param {String} username The user's username.
	 */

	public GetUserInfoService(String username) {
		/** @private */
		this.username = username;
	}
	
	/**
	 * This is where the service executes what it
	 * is supposed to do.
	 * 
	 * It's a local service, so it only does local
	 * invocations to the domain layer underneath.
	 * 
	 * @throws BubbleDocsException
	 */

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		bd.userExists(username);
		User user = bd.getUserByUsername(username);
		this.name = user.getName();
		this.email = user.getEmail();
	}
	
	/**
	 * This particular service doesn't require any specific permission
	 * to execute, so it simply returns.
	 */

	@Override
	protected void checkAccess() {
		return;
	}
	
	/**
	 * Method that returns the user's username.
	 * 
	 * @return {String} The user's username.
	 */

	public String getUserUsername() {
		return this.username;
	}
	
	/**
	 * Method that returns the user's name.
	 * 
	 * @return {String} The user's name.
	 */

	public String getUserName() {
		return this.name;
	}
	
	/**
	 * Method that returns the user's email.
	 * 
	 * @return {String} The user's email.
	 */

	public String getUserEmail() {
		return this.email;
	}
}// End GetUserInfoService class
