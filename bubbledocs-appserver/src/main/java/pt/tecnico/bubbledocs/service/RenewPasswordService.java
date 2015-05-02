package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;

public class RenewPasswordService extends BubbleDocsService {
	
	private BubbleDocs bd;
	private boolean integratorStatus;
	
	public RenewPasswordService(String userToken, boolean integratorStatus) {
		this.token = userToken;
		this.integratorStatus = integratorStatus;
		this.bd = getBubbleDocs();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		String username = bd.getUsernameByToken(token);
		
		if(integratorStatus) {
			bd.invalidateUserPassword(username);
		} else {
			throw new UnavailableServiceException();
		}
	}
	
	@Override
	protected void checkAccess() {
		return;	
	}
}// End of RenewPasswordService class