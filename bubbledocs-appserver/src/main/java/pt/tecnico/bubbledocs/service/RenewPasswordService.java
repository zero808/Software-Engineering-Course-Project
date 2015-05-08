package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;

/**
 * Class that describes the service that is 
 * responsible for renewing a user's password.
 */

public class RenewPasswordService extends BubbleDocsService {
	
	private BubbleDocs bd;
	private boolean integratorStatus;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {RenewPasswordService}
	 * 
	 * @param {String} userToken The token of the user that will have his password invalidated.
	 * @param {Boolean} integratorStatus If the remote service call was successful or not.
	 */
	
	public RenewPasswordService(String userToken, boolean integratorStatus) {
		/** @private */
		this.token = userToken;
		/** @private */
		this.integratorStatus = integratorStatus;
		/** @private */
		this.bd = getBubbleDocs();
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
		String username = bd.getUsernameByToken(token);
		
		if(integratorStatus) {
			bd.invalidateUserPassword(username);
		} else {
			throw new UnavailableServiceException();
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
}// End of RenewPasswordService class
