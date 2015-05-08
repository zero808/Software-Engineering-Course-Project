package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;

/**
 * Class that describes the service that is 
 * responsible for getting username to which the
 * given token belongs to.
 */

public class GetUsername4TokenService extends BubbleDocsService {

	private BubbleDocs bd;
	private String username;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {GetUsername4TokenService}
	 * 
	 * @param {String} token The user's token.
	 */

	public GetUsername4TokenService(String token) {
		/** @private */
		this.token = token;
		/** @private */
		this.bd = BubbleDocs.getInstance();
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
		this.username = bd.getUsernameByToken(this.token);
		
		if (this.username == null) {
			throw new InvalidTokenException();
		}
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
}// End GetUsername4TokenService class
