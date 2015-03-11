package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;
import pt.tecnico.bubbledocs.exception.UserDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserDoesNotHavePermissionException;

public class Root extends Root_Base {

	private static Root instance = null;

	private Root() {
		setBubbledocs(FenixFramework.getDomainRoot().getBubbledocs());
		setId(0);
		setUsername("root");
		setName("Super User");
		setPassword("rootroot"); // So that it doesn't have null.
	}

	public static Root getInstance() {
		if (instance == null) {
			instance = new Root();
		}
		return instance;
	}

	private Spreadsheet getSpreadsheetByName(String spreadsheetName) {
		for(Spreadsheet s :getBubbledocs().getSpreadsheetsSet()) {
			if(s.getName().equals(spreadsheetName)) {
				return s;
			}
		}
		return null;
	}

	private User getUserByName(String name) {
		BubbleDocs bd = getBubbledocs();
		for (User usr : bd.getUsersSet()) {
			if (usr.getName().equals(name)) {
				return usr;
			}
		}
		return null;
	}

	public void removeUser(String username) throws UserDoesNotExistException {
		BubbleDocs bd = getBubbledocs();
		User toRemove = getUserByName(username);

		if (toRemove == null)
			throw new UserDoesNotExistException(username);

		bd.removeUsers(toRemove);
		for(Spreadsheet s : toRemove.getSpreadsheetsSet()) {
			for(Permission p : s.getPermissionsSet()) {
				p.delete();
			}
		}
		toRemove.delete();
	}

	@Override
	public void addUser(String username, String name, String pass) throws UserAlreadyExistsException {
		BubbleDocs bd = getBubbledocs();

		User usr = getUserByName(username);
		if (usr != null)
			throw new UserAlreadyExistsException(username);

		bd.addUsers(new User(username, name, pass));
	}

	@Override
	public void removeSpreadsheets(String spreadsheetName) throws SpreadsheetDoesNotExistException {
		Spreadsheet toRemove = getSpreadsheetByName(spreadsheetName);

		if (toRemove == null)
			throw new SpreadsheetDoesNotExistException(spreadsheetName);

		super.removeSpreadsheets(toRemove);
		toRemove.deleteSpreadsheetContent();

	}
	
	@Override
	public void removePermissionfrom(Spreadsheet s, User u) {
			if(s.getPermissionOfUser(u) == null) {
				throw new UserDoesNotHavePermissionException(u.getName());
			} else {
				s.getPermissionOfUser(u).setRw(false);
			}
	}
	
	@Override
	public void givePermissionto(Spreadsheet s, User u) {
		new Permission(s, u, true);
	}
	
	@Override
	public boolean isRoot() {
		return true;
	}
	
}// End of Root Class
