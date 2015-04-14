package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
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
		User userToDelete = bd.getUserByUsername(toDeleteUsername);

		// user does not exist
		if (userToDelete == null)
			throw new LoginBubbleDocsException(toDeleteUsername);
		
		userToDelete.delete(userToken, toDeleteUsername);

		// toDeleteUser is in session,so we need to remove it.
		String tok = bd.getTokenByUsername(toDeleteUsername);
		if (tok != null) {
			bd.removeUserFromSession(tok);
		}
	}

	public void setIDRemoteService(IDRemoteServices idRemote) {
		this.idRemoteService = idRemote;
	}
}// End DeleteUser Class