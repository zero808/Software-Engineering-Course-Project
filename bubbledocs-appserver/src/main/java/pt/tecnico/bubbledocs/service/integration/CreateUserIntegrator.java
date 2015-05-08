package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.CreateUserService;
import pt.tecnico.bubbledocs.service.DeleteUserService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

/**
 * Class that describes the service that is 
 * responsible for creating a user.
 * 
 * Since this particular service does require
 * a remote call, it does that in the dispatch method
 * after calling the local service.
 */

public class CreateUserIntegrator extends BubbleDocsIntegrator {

	private String userToken;
	private String newUsername;
	private String email;
	private String name;

	private IDRemoteServices idRemoteService;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {CreateUserIntegrator}
	 * 
	 * @param {String} userToken The token of the user that called the service.
	 * @param {String} newUsername The user's username.
	 * @param {String} email The user's email.
	 * @param {String} name The user's name.
	 */

	public CreateUserIntegrator(String userToken, String newUsername, String email, String name) {
		/** @private */
		this.userToken = userToken;
		/** @private */
		this.newUsername = newUsername;
		/** @private */
		this.email = email;
		/** @private */
		this.name = name;
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
		CreateUserService createUserService = new CreateUserService(userToken, newUsername, email, name);
		createUserService.execute();
		
		try {
			idRemoteService.createUser(newUsername, email, name);	
		} catch (RemoteInvocationException e) {
			DeleteUserService deleteUserService = new DeleteUserService(userToken, newUsername);
			deleteUserService.execute();
			
			throw new UnavailableServiceException();
		}
	}
}// End CreateUserIntegrator class
