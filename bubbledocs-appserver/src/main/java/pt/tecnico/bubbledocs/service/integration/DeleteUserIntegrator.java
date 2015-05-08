package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.CreateUserService;
import pt.tecnico.bubbledocs.service.DeleteUserService;
import pt.tecnico.bubbledocs.service.GetUserInfoService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

/**
 * Class that describes the service that is 
 * responsible for deleting a user.
 * 
 * Since this particular service does require
 * a remote call, it does that in the dispatch method
 * after calling the local service.
 */

public class DeleteUserIntegrator extends BubbleDocsIntegrator {

	private String userToken;
	private String toDeleteUsername;

	private IDRemoteServices idRemoteService;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {DeleteUserIntegrator}
	 * 
	 * @param {String} userToken The token of the user that called the service.
	 * @param {String} toDeleteUsername The user's username to delete..
	 */

	public DeleteUserIntegrator(String userToken, String toDeleteUsername) {
		/** @private */
		this.userToken = userToken;
		/** @private */
		this.toDeleteUsername = toDeleteUsername;
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
		
		GetUserInfoService getUserInfoService = new GetUserInfoService(toDeleteUsername);
		getUserInfoService.execute();
		
		String [] backupUser = {toDeleteUsername, getUserInfoService.getUserEmail(), getUserInfoService.getUserName()};
		
		DeleteUserService deleteUserService = new DeleteUserService(userToken,toDeleteUsername);
		deleteUserService.execute();
		
		try {
			idRemoteService.removeUser(toDeleteUsername);
		} catch (RemoteInvocationException e) {
			CreateUserService createUserService = new CreateUserService(userToken, backupUser[0], backupUser[1], backupUser[2]);
			createUserService.execute();
			throw new UnavailableServiceException();
		}
	}
}// End DeleteUserIntegrator class
