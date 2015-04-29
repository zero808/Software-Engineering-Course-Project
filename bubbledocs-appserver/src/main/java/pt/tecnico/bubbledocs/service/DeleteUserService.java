package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Root;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class DeleteUserService extends BubbleDocsService {

	private String toDeleteUsername;

	private IDRemoteServices idRemoteService;

	public DeleteUserService(String userToken, String toDeleteUsername) {
		this.token = userToken;
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

	@Override
	protected void checkAccess() {
		BubbleDocs bd = getBubbleDocs();
		
		User user = bd.getUserByUsername(bd.getUsernameByToken(token));
		
		if(!(user.isRoot())) {
			throw new InvalidPermissionException(user.getUsername());
		}	
	}
}// End DeleteUserService Class