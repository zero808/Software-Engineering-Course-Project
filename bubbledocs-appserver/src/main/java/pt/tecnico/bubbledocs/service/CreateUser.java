package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class CreateUser extends BubbleDocsService {
	
	String userToken;
	String newUsername;
	String password;
	String name;
	BubbleDocs bd;
	User user;

	public CreateUser(String userToken, String newUsername, String password, String name) {
		this.userToken = userToken;
		this.newUsername = newUsername;
		this.password = password;
		this.name = name;
		this.bd = BubbleDocs.getInstance();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		//the user is not logged in
		if(!bd.isInSession(this.userToken))
			throw new UserNotInSessionException(this.newUsername);
		
		//the username is already being used
		if(this.bd.getUserByUsername(this.newUsername) != null)
			throw new UserAlreadyExistsException(this.newUsername);
		
		//only root can create new users
		if(!bd.isRoot(this.userToken))
			throw new InvalidPermissionException(this.newUsername);
		
		//make sure the username is not the empty string
		if(this.newUsername.equals(""))
			throw new InvalidUsernameException(this.newUsername);
		
		user = new User(this.newUsername, this.name, this.password);
		this.bd.addUsers(user);
	}
}// End CreateUser class