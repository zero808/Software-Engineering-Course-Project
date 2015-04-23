package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Root;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class DeleteUser extends BubbleDocsService {

	private String userToken;
	private String toDeleteUsername;

	private IDRemoteServices idRemoteService;

	public DeleteUser(String userToken, String toDeleteUsername) {
		this.userToken = userToken;
		this.toDeleteUsername = toDeleteUsername;
		this.idRemoteService = new IDRemoteServices();
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = getBubbleDocs();
		
		try {
			idRemoteService.removeUser(toDeleteUsername);
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
		
		//Check if the user invoking the service is in session.
		if (!bd.isInSession(userToken))
			throw new UserNotInSessionException(bd.getUsernameByToken(userToken));
		
		// Check if root is the one invoking the service.
		if (!bd.isRoot(userToken))
			throw new InvalidPermissionException(bd.getUsernameByToken(userToken));
		
		Root r = Root.getInstance(); //After checking that it is root that is calling the service.
		
		r.removeUser(toDeleteUsername); //Root checks if user exists and then tells BubbleDocs to remove it.

		// If the user that was removed was in a session then we remove that as well.
		String tok = bd.getTokenByUsername(toDeleteUsername);
		if (tok != null) {
			bd.removeUserFromSession(tok);
		}
	}

	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemoteService = idRemote;
	}
}// End DeleteUser Class