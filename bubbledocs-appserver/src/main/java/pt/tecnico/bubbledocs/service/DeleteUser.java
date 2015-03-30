package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.UserDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class DeleteUser extends BubbleDocsService {

	private String userToken;
	private String toDeleteUsername;

	public DeleteUser(String userToken, String toDeleteUsername) {
		this.userToken = userToken;
		this.toDeleteUsername = toDeleteUsername;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = getBubbleDocs();

		// root is not in session
		if (!bd.isInSession(userToken))
			throw new UserNotInSessionException(bd.getUsernameByToken(userToken));

		// user (root) does not exist
		if (bd.getUserByUsername(bd.getUsernameByToken(userToken)) == null)
			throw new UserDoesNotExistException();

		// Only root can delete users
		if (!bd.isRoot(userToken))
			throw new InvalidPermissionException(bd.getUsernameByToken(userToken));

		// user does not exist
		if (bd.getUserByUsername(toDeleteUsername) == null)
			throw new UserDoesNotExistException();

		User userToDelete = bd.getUserByUsername(toDeleteUsername);

		userToDelete.delete();
		
		// toDeleteUser is in session,so we need to remove it.
		String tok = bd.getTokenByUsername(toDeleteUsername);
		if (tok != null) {
			bd.removeUserFromSession(tok);
		}
	}
}// End DeleteUser Class