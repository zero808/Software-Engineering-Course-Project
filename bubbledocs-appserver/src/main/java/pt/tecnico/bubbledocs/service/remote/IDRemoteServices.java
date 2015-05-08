package pt.tecnico.bubbledocs.service.remote;

import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;

/**
 * Class that describes the remote service IDRemoteServices.
 * In this project in particular this object will be mocked.
 * Because of this, all of the methods here need to be empty.
 */

public class IDRemoteServices {
	
	/**
	 * Method used to create a new user remotely.
	 * 
	 * @param {String} username The user's username.
	 * @param {String} email The user's email.
	 * @param {String} name The user's name.
	 * @throws InvalidUsernameException, DuplicateUsernameException, DuplicateEmailException, InvalidEmailException, RemoteInvocationException
	 */
	
	public void createUser(String username, String email, String name) throws InvalidUsernameException, DuplicateUsernameException, DuplicateEmailException, InvalidEmailException, RemoteInvocationException {
		//Needs to be empty.
	}
	
	/**
	 * Method that performs the user's login remotely.
	 * 
	 * @param {String} username The user's username.
	 * @param {String} password The user's password.
	 * @throws LoginBubbleDocsException, RemoteInvocationException
	 */
	
	public void loginUser(String username, String password) throws LoginBubbleDocsException, RemoteInvocationException {
		//Needs to be empty.
	}
	
	/**
	 * Method that removes a user remotely.
	 * 
	 * @param {String} username The user's username.
	 * @throws LoginBubbleDocsException, RemoteInvocationException
	 */

	public void removeUser(String username) throws LoginBubbleDocsException, RemoteInvocationException {
		//Needs to be empty.
	}
	
	/**
	 * Method that renews a user's password remotely.
	 * 
	 * @param {String} username The user's username.
	 * @throws LoginBubbleDocsException, RemoteInvocationException
	 */
	
	public void renewPassword(String username) throws LoginBubbleDocsException, RemoteInvocationException {
		//Needs to be empty.
	}
}// End IDRemoteServices class
