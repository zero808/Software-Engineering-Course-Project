package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Root;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;

/**
 * Class that describes the service that is 
 * responsible for creating a user.
 */

public class CreateUserService extends BubbleDocsService {
	
	private String newUsername;
	private String email;
	private String name;
	private User user;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {CreateUserService}
	 * 
	 * @param {String} userToken The token of the user that called the service.
	 * @param {String} newUsername The user's username.
	 * @param {String} email The user's email.
	 * @param {String} name The user's name.
	 */
	
	public CreateUserService(String userToken, String newUsername, String email, String name) {
		/** @private */
		this.token = userToken;
		/** @private */
		this.newUsername = newUsername;
		/** @private */
		this.email = email;
		/** @private */
		this.name = name;
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
		
		user = new User(this.newUsername, this.name, this.email);
		
		Root r = Root.getInstance();
		r.addUser(user);		
	}
	
	/**
	 * In order to create a user, one must have root
	 * privileges.
	 */

	@Override
	protected void checkAccess() {
		BubbleDocs bd = getBubbleDocs();

		User user = bd.getUserByUsername(bd.getUsernameByToken(token));

		if(!user.isRoot()) {
			throw new InvalidPermissionException(user.getUsername());
		}		
	}
}// End CreateUserService class
