package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;

/**
 * Class that abstracts all of the local services that 
 * require specific read/write permissions from the user.
 * 
 * All local services that need this must extend this class.
 */

public abstract class AccessService extends BubbleDocsService {

	protected int docId;
	protected User user;
	protected boolean writePermission;
	
	/**
	 * Method that checks if the user has the correct permissions
	 * in order to perform a particular service.
	 * @throws BubbleDocsException
	 */

	@Override
	protected void checkAccess() throws BubbleDocsException {
		BubbleDocs bd = getBubbleDocs();

		user = bd.getUserByUsername(bd.getUsernameByToken(token));
		
		try {
			user.hasOwnerPermission(getSpreadsheet(docId));
		} catch (InvalidPermissionException e) {
			//Check for write permission.
			writePermission = true;
			user.hasPermission(writePermission, getSpreadsheet(docId));
			//Check for read only permission.
			writePermission = false;
			user.hasPermission(writePermission, getSpreadsheet(docId));
		}	
	}
}// End AccessService class
