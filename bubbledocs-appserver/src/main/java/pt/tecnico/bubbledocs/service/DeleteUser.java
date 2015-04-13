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

	private IDRemoteServices idRemoteService = new IDRemoteServices();

	public DeleteUser(String userToken, String toDeleteUsername) {
		this.userToken = userToken;
		this.toDeleteUsername = toDeleteUsername;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = getBubbleDocs();

		try {
			idRemoteService.removeUser(toDeleteUsername);
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}

		// root is not in session
		if (!bd.isInSession(userToken))
			throw new UserNotInSessionException(
					bd.getUsernameByToken(userToken));

		// user (root) does not exist
		if (bd.getUserByUsername(bd.getUsernameByToken(userToken)) == null)
			throw new LoginBubbleDocsException("username");

		// Only root can delete users
		if (!bd.isRoot(userToken))
			throw new InvalidPermissionException(
					bd.getUsernameByToken(userToken));

		// user does not exist
		if (bd.getUserByUsername(toDeleteUsername) == null)
			throw new LoginBubbleDocsException("username");

		User userToDelete = bd.getUserByUsername(toDeleteUsername);

		userToDelete.delete();

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