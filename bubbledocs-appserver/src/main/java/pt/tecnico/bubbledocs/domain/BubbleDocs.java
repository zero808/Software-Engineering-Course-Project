package pt.tecnico.bubbledocs.domain;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

import org.jdom2.Element;
import org.joda.time.LocalTime;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class BubbleDocs extends BubbleDocs_Base {
	
	private ConcurrentHashMap<String, LocalTime> _tokenTimeMap;
	private ConcurrentHashMap<String, String> _tokenUsernameMap;
	
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
				} 
				else {
					Element spreadsheets = user.getChild("spreadsheets");
					for (Element spreadsheetElement : spreadsheets.getChildren("spreadsheet")) {
						try {
							getSpreadsheetByName(spreadsheetElement.getAttribute("name").getValue());
						} 
						catch (SpreadsheetDoesNotExistException ex) {
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
	
	public String getUsernameByToken(String userToken) throws InvalidTokenException {
		if (_tokenUsernameMap.containsKey(userToken)) {
			return _tokenUsernameMap.get(userToken);
		}
		else {
			throw new InvalidTokenException();
		}
	}
	
	private String getTokenByUsername(String username) throws UserDoesNotExistException, UserNotInSessionException {
		
		if (getUserByUsername(username) == null) {
			throw new UserDoesNotExistException();
		}
		
		for (String userToken : _tokenUsernameMap.keySet()) {
			if (_tokenUsernameMap.get(userToken).equals(username)) {
				return userToken;
			}
		}
		
		throw new UserNotInSessionException(username);
	}
	
	public boolean isRoot(String userToken) throws InvalidTokenException {
		if (_tokenUsernameMap.containsKey(userToken)) {
			if (_tokenUsernameMap.get(userToken).equals("root")) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			throw new InvalidTokenException();
		}
	}
	
	public Spreadsheet getSpreadsheetByName(String name) throws SpreadsheetDoesNotExistException {
		for (Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if (spreadsheet.getName().equals(name)) {
				return spreadsheet;
			}
		}
		throw new SpreadsheetDoesNotExistException();
	}
	
	public Spreadsheet getSpreadsheetById(int id) {
		for (Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if (spreadsheet.getId() == id) {
				return spreadsheet;
			}
		}
		return null;
	}
	
	public String login(String username, String password) {
		
		LocalTime actualDate = new LocalTime();				//Creates Actual Date
		LocalTime expirationDate = actualDate.plusHours(2);	//Creates Expiration Date (2 hours ahead)
		String userToken;
		
		if (!(_tokenTimeMap.isEmpty()) && !(_tokenUsernameMap.isEmpty())) {
			//If in session, resets user expiration
			if (_tokenUsernameMap.containsValue(username)) {
				_tokenTimeMap.replace(getTokenByUsername(username), expirationDate);
				userToken = getTokenByUsername(username);
			}
			//If not, creates new token and new session
			else {
				Random rand = new Random();
				String token = username + rand.nextInt(10);
				_tokenUsernameMap.put(token, username);
				_tokenTimeMap.put(token, expirationDate);
				userToken = token;
			}
		}
		else {
			Random rand = new Random();
			String token = username + rand.nextInt(10);
			_tokenUsernameMap.put(token, username);
			_tokenTimeMap.put(token, expirationDate);
			userToken = token;
		}
		
		//Removes sessions of expired date users
		for (String uToken : _tokenTimeMap.keySet()) {
			if (_tokenTimeMap.get(uToken).isBefore(actualDate)) {
				_tokenTimeMap.remove(uToken);
				_tokenUsernameMap.remove(uToken);
			}
		}
		return userToken;
	}
	
	public void removeUserFromSession(String uToken) {
		_tokenTimeMap.remove(uToken);
		_tokenUsernameMap.remove(uToken);
	}
	
	public boolean isInSession(String userToken) {
		if (_tokenUsernameMap.containsKey(userToken)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public LocalTime getLastAccessTimeInSession(String userToken) {
		return _tokenTimeMap.get(userToken).minusHours(2);
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