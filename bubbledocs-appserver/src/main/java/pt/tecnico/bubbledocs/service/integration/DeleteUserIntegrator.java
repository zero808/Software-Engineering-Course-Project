package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.CreateUserService;
import pt.tecnico.bubbledocs.service.DeleteUserService;
import pt.tecnico.bubbledocs.service.GetUserInfoService;
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
		
		
		GetUserInfoService getUserInfoService = new GetUserInfoService(toDeleteUsername);
		getUserInfoService.execute();
		
		String [] backupUser = {toDeleteUsername, getUserInfoService.getUserEmail(), getUserInfoService.getUserName()};
		
		
		//Local execution
		DeleteUserService deleteUserService = new DeleteUserService(userToken,toDeleteUsername);
		deleteUserService.execute();
		
		try {
			idRemoteService.removeUser(toDeleteUsername);
		} catch (RemoteInvocationException e) {
			//"Rollback"
			CreateUserService createUserService = new CreateUserService(userToken, backupUser[0], backupUser[1], backupUser[2]);
			createUserService.execute();
			throw new UnavailableServiceException();
		}

	}

	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemoteService = idRemote;
	}
}// End DeleteUserIntegrator Class