package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;

public class CreateUser extends BubbleDocsService {
	
	private String userToken;
	private String newUsername;
	private String email;
	private String name;
	private BubbleDocs bd;
	private User user;
	
	private IDRemoteServices idRemoteService = new IDRemoteServices();

	public CreateUser(String userToken, String newUsername, String email, String name) {
		this.userToken = userToken;
		this.newUsername = newUsername;
		this.email = email;
		this.name = name;
		this.bd = getBubbleDocs();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		try {
			idRemoteService.createUser(this.newUsername, this.email, this.name);
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
		
		//the user is not logged in
		if(!bd.isInSession(this.userToken))
			throw new UserNotInSessionException(this.newUsername);
		
		//the username is already being used
		if(this.bd.getUserByUsername(this.newUsername) != null)
			throw new UserAlreadyExistsException(this.newUsername);
		
		//only root can create new users
		if(!bd.isRoot(this.userToken))
			throw new InvalidPermissionException(this.newUsername);
		
		//make sure the username length is between 3 and 8 characters
		if(this.newUsername.length()<3||this.newUsername.length()>8)
			throw new InvalidUsernameException(this.newUsername);
		
		user = new User(this.newUsername, this.name, this.email);
		this.bd.addUsers(user);
	}
	
	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemoteService = idRemote;
	}
}// End CreateUser class