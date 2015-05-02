package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.service.RenewPasswordService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;

public class RenewPasswordIntegrator extends BubbleDocsIntegrator {
	
	private String token;
	private GetUsername4TokenService getUsername4TokenService;
	private IDRemoteServices idRemoteService;
	
	public RenewPasswordIntegrator(String userToken) {
		this.token = userToken;
		this.getUsername4TokenService = new GetUsername4TokenService(userToken);
		this.idRemoteService = new IDRemoteServices();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		getUsername4TokenService.execute();
		String username = getUsername4TokenService.getUserUsername();
		
		try {
			idRemoteService.renewPassword(username);
			
			RenewPasswordService renewPasswordService = new RenewPasswordService(token, true);
			renewPasswordService.execute();
			
		} catch (RemoteInvocationException e) {
			
			RenewPasswordService renewPasswordService = new RenewPasswordService(token, false);
			renewPasswordService.execute();
		}
		
	}
	
}// End of RenewPasswordIntegrator class