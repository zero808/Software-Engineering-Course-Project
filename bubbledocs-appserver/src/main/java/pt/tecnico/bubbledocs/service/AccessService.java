package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;

public abstract class AccessService extends BubbleDocsService {

	protected int docId;
	protected User user;
	protected boolean writePermission;

	@Override
	protected void checkAccess() throws BubbleDocsException {
		BubbleDocs bd = getBubbleDocs();

		user = bd.getUserByUsername(bd.getUsernameByToken(token));
		
		try {
			user.hasOwnerPermission(getSpreadsheet(docId));
		} catch (InvalidPermissionException e) {
			user.hasPermission(writePermission, getSpreadsheet(docId));
		}	
	}
}// End BubbleDocsService class