package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.service.LoginUserService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

/**
 * Class that describes the service that is 
 * responsible for the users login.
 * 
 * Since this particular service does require
 * a remote call, it does that in the dispatch method
 * before calling the local service.
 */

public class LoginUserIntegrator extends BubbleDocsIntegrator {

	private String username;
	private String password;
	private String token;
	
	private IDRemoteServices idRemoteService;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {LoginUserIntegrator}
	 * 
	 * @param {String} username The user's username.
	 * @param {String} password The user's password.
	 */

	public LoginUserIntegrator(String username, String password) {
		/** @private */
		this.username = username;
		/** @private */
		this.password = password;
		/** @private */
		this.idRemoteService = new IDRemoteServices();
	}
	
	/**
	 * This is where the service executes what it
	 * is supposed to do.
	 * 
	 * @throws BubbleDocsException
	 */

	@Override
	protected void dispatch() throws BubbleDocsException {

		try {
			idRemoteService.loginUser(username, password);
			
			LoginUserService loginUserService = new LoginUserService(username, password, true);
			loginUserService.execute();
			token = loginUserService.getUserToken();
			
		} catch (RemoteInvocationException e) {
			
			LoginUserService loginUserService = new LoginUserService(username, password, false);
			loginUserService.execute();
			token = loginUserService.getUserToken();
		}
	}
	
	/**
	 * Method that returns the user's token after a successful login.
	 * 
	 * @return {String} The user's token.
	 */
	
	public String getUserToken() {
		return token;
	}
}// End LoginUserIntegrator class
