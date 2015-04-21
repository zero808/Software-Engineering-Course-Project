package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.RenewPassword;
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
		RenewPassword renewPasswordService = new RenewPassword(userToken);
		renewPasswordService.execute();
	}
	
	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemote = idRemote;
	}
}// End of RenewPasswordIntegrator class