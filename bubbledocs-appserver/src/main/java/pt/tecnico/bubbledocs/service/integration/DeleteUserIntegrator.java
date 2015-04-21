package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.DeleteUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class DeleteUserIntegrator extends BubbleDocsIntegrator {

	private String userToken;
	private String toDeleteUsername;

	private IDRemoteServices idRemoteService;

	public DeleteUserIntegrator(String userToken, String toDeleteUsername) {
		this.userToken = userToken;
		this.toDeleteUsername = toDeleteUsername;
		this.idRemoteService = new IDRemoteServices();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		DeleteUser deleteUserService = new DeleteUser(userToken,toDeleteUsername);
		deleteUserService.execute();
	}

	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemoteService = idRemote;
	}
}// End DeleteUserIntegrator Class