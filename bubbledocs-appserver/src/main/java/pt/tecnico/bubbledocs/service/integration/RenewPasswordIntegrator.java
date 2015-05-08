package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.service.RenewPasswordService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;

/**
 * Class that describes the service that is 
 * responsible for renewing a users password.
 * 
 * Since this particular service does require
 * a remote call, it does that in the dispatch method
 * before calling the local service.
 */

public class RenewPasswordIntegrator extends BubbleDocsIntegrator {
	
	private String token;
	private GetUsername4TokenService getUsername4TokenService;
	private IDRemoteServices idRemoteService;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {RenewPasswordIntegrator}
	 * 
	 * @param {String} userToken The token of the user that will have his password invalidated.
	 */
	
	public RenewPasswordIntegrator(String userToken) {
		/** @private */
		this.token = userToken;
		/** @private */
		this.getUsername4TokenService = new GetUsername4TokenService(userToken);
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
		getUsername4TokenService.execute();
		String username = getUsername4TokenService.getUserUsername();
		
		try {
			idRemoteService.renewPassword(username);
			
			RenewPasswordService renewPasswordService = new RenewPasswordService(token, true);
			renewPasswordService.execute();
			
		} catch (RemoteInvocationException e) {
			
			RenewPasswordService renewPasswordService = new RenewPasswordService(token, false);
			renewPasswordService.execute();
		}
	}	
}// End RenewPasswordIntegrator class
