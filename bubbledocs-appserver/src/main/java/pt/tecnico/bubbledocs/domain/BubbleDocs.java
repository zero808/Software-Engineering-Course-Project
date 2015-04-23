package pt.tecnico.bubbledocs.domain;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

import org.jdom2.Element;
import org.joda.time.LocalTime;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

public class BubbleDocs extends BubbleDocs_Base {
	
	private ConcurrentHashMap<String, LocalTime> _tokenTimeMap = new ConcurrentHashMap<String, LocalTime>();
	private ConcurrentHashMap<String, String> _tokenUsernameMap = new ConcurrentHashMap<String, String>();
	
	public static BubbleDocs getInstance() {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		if (bd == null)
			bd = new BubbleDocs();
		return bd;
	}
	
	private BubbleDocs() {
		FenixFramework.getDomainRoot().setBubbledocs(this);
		setIdGlobal(0);
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
		Root r = Root.getInstance();

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
	
	public String getUsernameByToken(String userToken) {
		return _tokenUsernameMap.get(userToken);
	}
	
	public String getTokenByUsername(String username) {
		for (String userToken : _tokenUsernameMap.keySet()) {
			if (_tokenUsernameMap.get(userToken).equals(username)) {
				return userToken;
			}
		}
		return null;
	}
	
	public boolean isRoot(String userToken) {
		if (_tokenUsernameMap.containsKey(userToken)) {
			return _tokenUsernameMap.get(userToken).equals("root");
		}
		else {
			return false;
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
	
	public Spreadsheet getSpreadsheetByNameNoException(String name) {
		for (Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if (spreadsheet.getName().equals(name)) {
				return spreadsheet;
			}
		}
		return null;
	}
	
	public Spreadsheet getSpreadsheetById(int id) {
		for (Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if (spreadsheet.getId() == id) {
				return spreadsheet;
			}
		}
		return null;
	}
	
	public void invalidateUserPassword(String username) {
		//IDRemoteServices invalidates current user password.
		//When the user tries to login, it has to use IDRemoteServices, which, if the password is different then the local copy, it should update it.
		getUserByUsername(username).setPassword(null);
	}
	
	public String login(String username, String password) {
		
		LocalTime currentTime = new LocalTime();				//Creates Actual Date
		LocalTime expirationDate = currentTime.plusHours(2);	//Creates Expiration Date (2 hours ahead)
		String oldToken = "";
		
		//If user already in session, removes token (user) from session to create a new one
		if (!(_tokenTimeMap.isEmpty()) && !(_tokenUsernameMap.isEmpty()) && _tokenUsernameMap.containsValue(username)) {
			oldToken = getTokenByUsername(username);
			removeUserFromSession(oldToken);
		}
		
		//Creates new token and puts user in session
		Random rand = new Random();
		String newToken = username + rand.nextInt(10);
		while (newToken.equals(oldToken)) {
			Random newRand = new Random();
			newToken = username + newRand.nextInt(10);
		}
		putUserInSession(newToken, username, expirationDate);
		
		//Removes expired date users from session
		for (String token : _tokenTimeMap.keySet()) {
			if (_tokenTimeMap.get(token).isBefore(currentTime)) {
				removeUserFromSession(token);
			}
		}
		return newToken;
	}
	
	private void putUserInSession(String userToken, String username, LocalTime expirationDate) {
		_tokenUsernameMap.put(userToken, username);
		_tokenTimeMap.put(userToken, expirationDate);
	}
	
	public void removeUserFromSession(String userToken) {
		_tokenUsernameMap.remove(userToken);
		_tokenTimeMap.remove(userToken);
	}
	
	public boolean isInSession(String userToken) {
		if (_tokenUsernameMap.containsKey(userToken) && _tokenTimeMap.containsKey(userToken)) {
			LocalTime currentTime = new LocalTime();
			if (_tokenTimeMap.get(userToken).isBefore(currentTime)) {
				removeUserFromSession(userToken);
				return false;
			}
			else {
				LocalTime newExpirationDate = currentTime.plusHours(2);
				_tokenTimeMap.replace(userToken, newExpirationDate);
				return true;
			}
		}
		else {
			return false;
		}
	}
	
	public LocalTime getLastAccessTimeInSession(String userToken) {
		return _tokenTimeMap.get(userToken).minusHours(2);
	}
	
	public void changeUserTokenExpirationDate(String userToken, LocalTime newExpirationDate) {
		_tokenTimeMap.replace(userToken, newExpirationDate);		
	}
	
	@Override
	public void removeUsers(User userToRemove) {
		super.removeUsers(userToRemove);
		userToRemove.delete();
	}
}// End BubbleDocs class