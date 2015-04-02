package pt.tecnico.bubbledocs.service.remote;

import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;

public class IDRemoteServices {
	
	public void createUser(String username, String email) throws InvalidUsernameException, DuplicateUsernameException, DuplicateEmailException, InvalidEmailException, RemoteInvocationException {
		// TODO : the connection and invocation of the remote service
	}
	
	public void loginUser(String username, String password) throws LoginBubbleDocsException, RemoteInvocationException {
		// TODO : the connection and invocation of the remote service
	}
	
	public void removeUser(String username) throws LoginBubbleDocsException, RemoteInvocationException {
		// TODO : the connection and invocation of the remote service
	}
	
	public void renewPassword(String username) throws LoginBubbleDocsException, RemoteInvocationException {
		// TODO : the connection and invocation of the remote service
	}
}// End IDRemoteServices class
