package pt.tecnico.bubbledocs.service.remote;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;

public class IDRemoteServices {
	
	private ConcurrentHashMap<String, String> _passwordMap = new ConcurrentHashMap<String, String>();
	
	public void createUser(String username, String email) throws InvalidUsernameException, DuplicateUsernameException, DuplicateEmailException, InvalidEmailException, RemoteInvocationException {
		
		if(username.equals("invalidUsername")) {
			throw new InvalidUsernameException(username); //For testing purposes.
		}
		
		if(username.equals("duplicateUsername")) {
			throw new DuplicateUsernameException(username); //For testing purposes.
		}
		
		if(email.equals("duplicateEmail")) {
			throw new DuplicateEmailException(email); //For testing purposes.
		}
		
		if(email.equals("invalidEmail")) {
			throw new InvalidEmailException(); //For testing purposes.
		}
		
		if(username.equals("Remote invocation failure")) {
			throw new RemoteInvocationException(); //For testing purposes.
		}	
	}
	
	public void loginUser(String username, String password) throws LoginBubbleDocsException, RemoteInvocationException {
		
		if(username.equals("invalidUser") || password.equals("invalidPassword")) {
			throw new LoginBubbleDocsException(username); //For testing purposes.
		}
		
		if(username.equals("Remote invocation failure")) {
			throw new RemoteInvocationException(); //For testing purposes.
		}
		
		SecureRandom random = new SecureRandom();
		String newPass = new BigInteger(130, random).toString(32);
		
		if(_passwordMap.contains(username)) {
			_passwordMap.replace(username, newPass);
		} else {
			random = new SecureRandom();
			newPass = new BigInteger(130, random).toString(32);
			_passwordMap.put(username, newPass);
		}
	}

	public void removeUser(String username) throws LoginBubbleDocsException, RemoteInvocationException {
		
		if(username.equals("invalidUser")) {
			throw new LoginBubbleDocsException(username); //For testing purposes.
		}
		
		if(username.equals("Remote invocation failure")) {
			throw new RemoteInvocationException(); //For testing purposes.
		}	
	}
	
	public void renewPassword(String username) throws LoginBubbleDocsException, RemoteInvocationException {
		
		if(username.equals("invalidUser")) {
			throw new LoginBubbleDocsException(username); //For testing purposes.
		}
		
		if(username.equals("Remote invocation failure")) {
			throw new RemoteInvocationException(); //For testing purposes.
		}
		
		SecureRandom random = new SecureRandom();
		String newPass = new BigInteger(130, random).toString(32);
		
		_passwordMap.replace(username, newPass);
	}
}// End IDRemoteServices class
