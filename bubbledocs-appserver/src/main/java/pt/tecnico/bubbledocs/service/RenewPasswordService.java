package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordService extends BubbleDocsService {
	
	private IDRemoteServices idRemote = new IDRemoteServices();
	
	public RenewPasswordService(String userToken) {
		token = userToken;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = getBubbleDocs();

		String username = bd.getUsernameByToken(token);

		try {
			idRemote.renewPassword(username);
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
		
		bd.invalidateUserPassword(username);
	}
	
	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemote = idRemote;
	}

	@Override
	protected void checkAccess() {
		return;	
	}
}// End of RenewPasswordService class