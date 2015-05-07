package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

public class Permission extends Permission_Base {
	
	public Permission() {
		super();
	}

	public Permission(Spreadsheet spred, User user, boolean b) throws SpreadsheetDoesNotExistException, LoginBubbleDocsException {

		if (spred == null)
			throw new SpreadsheetDoesNotExistException();
		if (user == null)
			throw new LoginBubbleDocsException("username");

		setRw(b);
		setUser(user);
		setSpreadsheet(spred);

		spred.addPermissions(this);
	}

	public void edit(boolean b) {
		setRw(b);
	}

	public void delete() {
		setUser(null);
		setSpreadsheet(null);
		deleteDomainObject();
	}
}// End Permission class
