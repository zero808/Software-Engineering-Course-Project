package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;

/**
 * Class that describes the service that is 
 * responsible for the users login.
 */

public class LoginUserService extends BubbleDocsService {

	private String username;
	private String password;
	private BubbleDocs bd;
	private boolean integratorStatus;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {LoginUserService}
	 * 
	 * @param {String} username The user's username.
	 * @param {String} password The user's password.
	 * @param {Boolean} integratorStatus If the remote service call was successful or not.
	 */

	public LoginUserService(String username, String password, boolean integratorStatus) {
		/** @private */
		this.username = username;
		/** @private */
		this.password = password;
		/** @private */
		this.integratorStatus = integratorStatus;
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

		bd.userExists(username);

		User user = bd.getUserByUsername(username);

		String pass = user.getPassword();

		if(integratorStatus) {
			if(pass == null || !(pass.equals(this.password))) {
				user.setPassword(password);
			}
			token = bd.login(this.username, this.password);
		} else {
			if (pass == null || !(pass.equals(this.password))) {
				throw new UnavailableServiceException();
			} else {
				token = bd.login(this.username, this.password);
			}
		}
	}
	
	/**
	 * Method that returns the token after a successful login.
	 * 
	 * @return {String} The user's new token.
	 */
	
	public String getUserToken() {
		return token;
	}
	
	/**
	 * This particular service doesn't require any specific verification
	 * to execute, so it simply returns.
	 */
	
	@Override
	protected void validateAndAuthorize(String token) {
		return;
	}
	
	/**
	 * This particular service doesn't require any specific permission
	 * to execute, so it simply returns.
	 */

	@Override
	protected void checkAccess() {
		return;
	}
}// End LoginUserService class
