package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPassword extends BubbleDocsService {
	
	private String userToken;
	
	private IDRemoteServices idRemote = new IDRemoteServices();
	
	public RenewPassword(String userToken) {
		this.userToken = userToken;
	}

	@Override
	protected void dispatch() throws InvalidTokenException, UserNotInSessionException, UnavailableServiceException {
		BubbleDocs bd = getBubbleDocs();

		String username = bd.getUsernameByToken(userToken);
		
		if(userToken.equals("")) {
			throw new InvalidTokenException();
		}
		
		if(!(bd.isInSession(userToken))) {
			throw new UserNotInSessionException(username);
		}
		
		//New functionality for 3ª Delivery

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
}// End of RenewPassword class