package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.InvalidPasswordException;
import pt.tecnico.bubbledocs.exception.UsernameDoesNotExistException;

public class BubbleDocs extends BubbleDocs_Base {

	public static BubbleDocs getInstance() {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		if (bd == null)
			bd = new BubbleDocs();
		return bd;
	}

	private BubbleDocs() {
		FenixFramework.getDomainRoot().setBubbledocs(this);
		setIdGlobal(1); // root id = 0
	}

	public User getUserByName(String name) throws UsernameDoesNotExistException {
		for (User user : getUsersSet()) {
			if (user.getName().equals(name)) {
				return user;
			}
		}
		throw new UsernameDoesNotExistException(name);
	}

	public void login(String username, String password) throws InvalidPasswordException {

		User u = getUserByName(username); // If user doesn't exist the exception is thrown by getUserByName.
		String pass = u.getPassword();

		if (pass == null || !(pass.equals(password))) {
			throw new InvalidPasswordException();
		}
	}

	public void printUsers() {
		for (User user : getUsersSet()) {
			if (!(user.isRoot())) { // Only print the actual users without root.
				user.toString();
			}
		}
	}

	public void printSpreadsheets() {
		for (User user : getUsersSet()) {
			for (Spreadsheet spreadsheet : user.getSpreadsheetsSet()) {
				spreadsheet.toString();
			}
		}
	}

}// End BubbleDocs Class