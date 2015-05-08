package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Root;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;

/**
 * Class that describes the service that is 
 * responsible for deleting a user.
 */

public class DeleteUserService extends BubbleDocsService {

	private String toDeleteUsername;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {DeleteUserService}
	 * 
	 * @param {String} userToken The token of the user that called the service.
	 * @param {String} toDeleteUsername The user's username to delete..
	 */

	public DeleteUserService(String userToken, String toDeleteUsername) {
		/** @private */
		this.token = userToken;
		/** @private */
		this.toDeleteUsername = toDeleteUsername;
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
		BubbleDocs bd = getBubbleDocs();
				
		//After checking that it is root that is calling the service.
		Root r = Root.getInstance();
		//Root checks if user exists and then tells BubbleDocs to remove it.
		r.removeUser(toDeleteUsername); 
		// If the user that was removed was in a session then we remove that as well.
		String tok = bd.getTokenByUsername(toDeleteUsername);
		if (tok != null) {
			bd.removeUserFromSession(tok);
		}
	}
	
	/**
	 * In order to delete a user, one must have root
	 * privileges.
	 */

	@Override
	protected void checkAccess() {
		BubbleDocs bd = getBubbleDocs();
		
		User user = bd.getUserByUsername(bd.getUsernameByToken(token));
		
		if(!(user.isRoot())) {
			throw new InvalidPermissionException(user.getUsername());
		}	
	}
}// End DeleteUserService class
