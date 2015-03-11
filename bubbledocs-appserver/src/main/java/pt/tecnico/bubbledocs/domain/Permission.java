package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserDoesNotExistException;

public class Permission extends Permission_Base {

	public Permission(Spreadsheet spred, User user, boolean b) throws SpreadsheetDoesNotExistException, UserDoesNotExistException {

		if (spred == null)
			throw new SpreadsheetDoesNotExistException("NULL");
		if (user == null)
			throw new UserDoesNotExistException("NULL");

		setRw(b);
		setUser(user);
		setSpreadsheet(spred);

		spred.addPermissions(this);
		user.addSpreadsheets(spred);

	}

	public void edit(boolean b) {
		setRw(b);
	}

	public void delete() {
		setUser(null);
		setSpreadsheet(null);
		deleteDomainObject();
	}

}// End of Permission Class
