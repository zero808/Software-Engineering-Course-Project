package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.service.CreateUserService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserIntegrator extends BubbleDocsIntegrator {

	private String userToken;
	private String newUsername;
	private String email;
	private String name;

	private IDRemoteServices idRemoteService;

	public CreateUserIntegrator(String userToken, String newUsername,
			String email, String name) {
		this.userToken = userToken;
		this.newUsername = newUsername;
		this.email = email;
		this.name = name;
		this.idRemoteService = new IDRemoteServices();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		try {
			idRemoteService.createUser(newUsername, email, name);
			
			CreateUserService createUserService = new CreateUserService(userToken, newUsername, email, name);
			createUserService.execute();
			
		} catch (RemoteInvocationException e) {
			
			CreateUserService createUserService = new CreateUserService(userToken, newUsername, email, name);
			createUserService.execute();
		}
	}

	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemoteService = idRemote;
	}
}// End CreateUserIntegrator class