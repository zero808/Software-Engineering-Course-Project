package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.InvalidPasswordException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
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
	
	public Element exportToXML() {
		
		Element element = new Element("bubbledocs");
		
		Element rootElement = new Element("root");
		element.addContent(rootElement);
		
		Root root = Root.getInstance();
		rootElement.addContent(root.exportToXML());
		
		Element userElement = new Element("users");
		element.addContent(userElement);
		
		for (User user : getUsersSet()) {
			if(!(user.getUsername().equals("root"))) {
				userElement.addContent(user.exportToXML());
			}
		}
	
		return element;
	}
	
	public void importFromXML(Element bubbledocsElement) {
		
		Element users = bubbledocsElement.getChild("users");
		
		Element root = users.getChild("root");
		Root r = Root.getInstance();
		r.importFromXML(root);
		
		for (Element user : users.getChildren("user")) {
			if(!(user.getName().equals("root"))) { //Not sure if its getName()
				User u = new User();
			    u.importFromXML(user);
			    r.addUser(u.getUsername(), u.getName(), u.getPassword());
			}
		}
	}

	public User getUserByName(String name) throws UsernameDoesNotExistException {
		for (User user : getUsersSet()) {
			if (user.getName().equals(name)) {
				return user;
			}
		}
		throw new UsernameDoesNotExistException(name);
	}
	
	public Spreadsheet getSpreadsheetByName(String name) throws SpreadsheetDoesNotExistException {
		for (Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if (spreadsheet.getName().equals(name)) {
				return spreadsheet;
			}
		}
		throw new SpreadsheetDoesNotExistException(name);
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