package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.RenewPasswordService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordIntegrator extends BubbleDocsIntegrator {
	
	private String userToken;
	
	private IDRemoteServices idRemote;
	
	public RenewPasswordIntegrator(String userToken) {
		this.userToken = userToken;
		this.idRemote = new IDRemoteServices();
	}

	@Override
	protected void dispatch() throws InvalidTokenException, UserNotInSessionException, UnavailableServiceException {
		RenewPasswordService renewPasswordService = new RenewPasswordService(userToken);
		renewPasswordService.execute();
	}
	
	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemote = idRemote;
	}
}// End of RenewPasswordIntegrator class