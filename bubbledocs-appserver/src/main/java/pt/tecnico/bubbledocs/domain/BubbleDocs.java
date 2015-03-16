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

		//Element root = users.getChild("root");
		Root r = Root.getInstance();
		//r.importFromXML(root);

		for (Element user : users.getChildren("user")) {
			if(!(user.getAttribute("username").getValue().equals("root"))) {
				if(getUserByUsername(user.getAttribute("username").getValue()) == null) {
					User u = new User();
					u.importFromXML(user);
					r.addUser(u);	
				} else {
					Element spreadsheets = user.getChild("spreadsheets");
					for (Element spreadsheetElement : spreadsheets.getChildren("spreadsheet")) {
						try{
							getSpreadsheetByName(spreadsheetElement.getAttribute("name").getValue());
						}catch (SpreadsheetDoesNotExistException ex){
							Spreadsheet s = new Spreadsheet();
							s.importFromXML(spreadsheetElement);
							addSpreadsheets(s);
						}	
					}
				}

			}
		}
	}


	public User getUserByUsername(String username) {
		for(User user : getUsersSet()) {
			if(user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}
	
	public Spreadsheet getSpreadsheetByName(String name) throws SpreadsheetDoesNotExistException {
		for (Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if (spreadsheet.getName().equals(name)) {
				return spreadsheet;
			}
		}
		throw new SpreadsheetDoesNotExistException(name);
	}

	public void login(String username, String password) throws InvalidPasswordException, UsernameDoesNotExistException {
		
		if(getUserByUsername(username) == null) {
			throw new UsernameDoesNotExistException(username);
		}
		
		User u = getUserByUsername(username);
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