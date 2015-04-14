package pt.tecnico.bubbledocs.service.remote;

import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;

public class IDRemoteServices {
	
	public void createUser(String username, String email, String name) throws InvalidUsernameException, DuplicateUsernameException, DuplicateEmailException, InvalidEmailException, RemoteInvocationException {
		//Needs to be empty.
	}
	
	public void loginUser(String username, String password) throws LoginBubbleDocsException, RemoteInvocationException {
		//Needs to be empty.
	}

	public void removeUser(String username) throws LoginBubbleDocsException, RemoteInvocationException {
		//Needs to be empty.
	}
	
	public void renewPassword(String username) throws LoginBubbleDocsException, RemoteInvocationException {
		//Needs to be empty.
	}
}// End IDRemoteServices class
