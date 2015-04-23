package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Root;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;

public class CreateUserService extends BubbleDocsService {
	
	private String newUsername;
	private String email;
	private String name;
	private User user;
	
	private IDRemoteServices idRemoteService = new IDRemoteServices();

	public CreateUserService(String userToken, String newUsername, String email, String name) {
		this.token = userToken;
		this.newUsername = newUsername;
		this.email = email;
		this.name = name;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		try {
			idRemoteService.createUser(this.newUsername, this.email, this.name);
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
		
		user = new User(this.newUsername, this.name, this.email);
		
		Root r = Root.getInstance();
		r.addUser(user);		
	}
	
	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemoteService = idRemote;
	}

	@Override
	protected void checkAccess() {
		BubbleDocs bd = getBubbleDocs();

		User user = bd.getUserByUsername(bd.getUsernameByToken(token));

		if(!(user.isRoot())) {
			throw new InvalidPermissionException(user.getUsername());
		}		
	}
}// End CreateUser class