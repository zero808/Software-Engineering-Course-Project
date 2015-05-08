package pt.tecnico.bubbledocs.domain;

import java.util.concurrent.ConcurrentHashMap;

import java.util.Random;

import org.jdom2.Element;
import org.joda.time.LocalTime;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

/**
 * Class that describes the singleton BubbleDocs.
 * 
 * It's the application's main class, responsible
 * for storing all the users and the spreadsheets 
 * in the application.
 */

public class BubbleDocs extends BubbleDocs_Base {
	
	/**
	 * HashMaps that store all the information regarding
	 * the user logins and tokens.
	 */
	
	private ConcurrentHashMap<String, LocalTime> _tokenTimeMap = new ConcurrentHashMap<String, LocalTime>();
	private ConcurrentHashMap<String, String> _tokenUsernameMap = new ConcurrentHashMap<String, String>();
	
	/**
	 * Since this is a singleton, this method
	 * is used to return the only instance of BubbleDocs.
	 * 
	 * @return {BubbleDocs} The single instance of BubbleDocs.
	 */
	
	public static BubbleDocs getInstance() {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		if (bd == null)
			bd = new BubbleDocs();
		return bd;
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * Since this is the main class of the application,
	 * because of the projects's architecture, it must be
	 * connected to the root class of FenixFramework.
	 * 
	 * @constructor
	 * @this {BubbleDocs}
	 */
	
	private BubbleDocs() {
		FenixFramework.getDomainRoot().setBubbledocs(this);
		/** @private */
		setIdGlobal(0);
	}
	
	/**
	 * Export BubbleDocs to a XML document.
	 * 
	 * @return {XML Element} The element describing BubbleDocs.
	 */
	
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
	
	/**
	 * Import BubbleDocs from a XML document.
	 * 
	 * @param {XML Element} bubbledocsElement The element that has BubbleDocs' data.
	 */
	
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
	
	/**
	 * Method that checks if a user already exists in BubbleDocs.
	 * This is required because the usernames must all be unique.
	 * 
	 * @param {String} username The username to check if exists.
	 * @throws LoginBubbleDocsException
	 */
	
	public void userExists(String username) throws LoginBubbleDocsException {
		if(null == getUserByUsername(username))
			throw new LoginBubbleDocsException("username");
	}
	
	/**
	 * Method that returns the user with the given username,
	 * if it exists. Otherwise, returns null.
	 * 
	 * @param {String} username The username to search for the user.
	 * @return {User} The user that has the given username.
	 */
	
	public User getUserByUsername(String username) {
		return getUsersSet().stream().filter(u -> u.getUsername().equals(username)).findAny().orElse(null);
	}
	
	/**
	 * Method that returns the username of the user which the
	 * token given belongs to.
	 * 
	 * @param {String} userToken A token of a user currently in session.
	 * @return {String} The username of the user to which the token belongs to.
	 */
	
	public String getUsernameByToken(String userToken) {
		return _tokenUsernameMap.get(userToken);
	}
	
	/**
	 * Method that returns the token that belongs to the user with
	 * the username given.
	 * If the particular user isn't currently in session, returns null.
	 * 
	 * @param {String} username The username to search for the token.
	 * @return {String} The token that belongs to the user with that username.
	 */
	
	public String getTokenByUsername(String username) {
		for (String userToken : _tokenUsernameMap.keySet()) {
			if (_tokenUsernameMap.get(userToken).equals(username)) {
				return userToken;
			}
		}
		return null;
	}
	
	/**
	 * Method that checks if the given token belongs to root user.
	 * 
	 * @param {String} userToken The token to be checked.
	 * @return {Boolean} True if it belongs to root, false otherwise.
	 */
	
	public boolean isRoot(String userToken) {
		if (_tokenUsernameMap.containsKey(userToken)) {
			return _tokenUsernameMap.get(userToken).equals("root");
		}
		else {
			return false;
		}
	}
	
	/**
	 * Method that returns the spreadsheet that has the given name.
	 * 
	 * @param {String} name The spreadsheet's name to search.
	 * @throws SpreadsheetDoesNotExistException
	 * @return {Spreadsheet} The spreadsheet that has that name.
	 */
	
	public Spreadsheet getSpreadsheetByName(String name) throws SpreadsheetDoesNotExistException {
		for (Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if (spreadsheet.getName().equals(name)) {
				return spreadsheet;
			}
		}
		throw new SpreadsheetDoesNotExistException();
	}
	
	/**
	 * Method that returns the spreadsheet that has the given id, null otherwise.
	 * 
	 * @param {number} id The spreadsheet's id to search.
	 * @return {Spreadsheet} The spreadsheet that has that id.
	 */
	
	public Spreadsheet getSpreadsheetById(int id) {
		for (Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if (spreadsheet.getId() == id) {
				return spreadsheet;
			}
		}
		return null;
	}
	
	/**
	 * Method that invalidates a users password.
	 * This happens when the remote service IDRemoteServices,
	 * updates the users password, and, because of that, the local
	 * copy must be invalidated.
	 * 
	 * @param {String} username The username of the user to invalidate the password.
	 */
	
	public void invalidateUserPassword(String username) {
		getUserByUsername(username).setPassword(null);
	}
	
	/**
	 * Method used to login users in the BubbleDocs application.
	 * 
	 * @param {String} username The username of the user trying to login.
	 * @param {String} password The password of the user trying to login.
	 * @return {String} The session token that belongs to that user.
	 */
	
	public String login(String username, String password) {
		//Creates Actual Date.
		LocalTime currentTime = new LocalTime();
		//Creates Expiration Date (2 hours ahead).
		LocalTime expirationDate = currentTime.plusHours(2);	
		String oldToken = "";
		
		//If user already in session, removes token (user) from session to create a new one.
		if (!(_tokenTimeMap.isEmpty()) && !(_tokenUsernameMap.isEmpty()) && _tokenUsernameMap.containsValue(username)) {
			oldToken = getTokenByUsername(username);
			removeUserFromSession(oldToken);
		}
		
		//Creates new token and puts user in session.
		Random rand = new Random();
		String newToken = username + rand.nextInt(10);
		while (newToken.equals(oldToken)) {
			Random newRand = new Random();
			newToken = username + newRand.nextInt(10);
		}
		putUserInSession(newToken, username, expirationDate);
		
		//Removes expired date users from session.
		for (String token : _tokenTimeMap.keySet()) {
			if (_tokenTimeMap.get(token).isBefore(currentTime)) {
				removeUserFromSession(token);
			}
		}
		return newToken;
	}
	
	/**
	 * Method that renews a users session.
	 * 
	 * @param {String} userToken The token that belongs to that user.
	 * @param {String} username The users username.
	 * @param {LocalTime} expirationDate The expiration time.
	 */
	
	private void putUserInSession(String userToken, String username, LocalTime expirationDate) {
		_tokenUsernameMap.put(userToken, username);
		_tokenTimeMap.put(userToken, expirationDate);
	}
	
	/**
	 * Method that ends a users current session.
	 * 
	 * @param {String} userToken The token that belongs to the user.
	 */
	
	public void removeUserFromSession(String userToken) {
		_tokenUsernameMap.remove(userToken);
		_tokenTimeMap.remove(userToken);
	}
	
	/**
	 * Method that checks if a particular token is valid(currently in session).
	 * 
	 * @param {String} userToken The token that belongs a particular user.
	 * @return {Boolean} Returns true if the token is in session, false otherwise.
	 */
	
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
	
	/**
	 * Method that returns the time when the session began.
	 * 
	 * @param {String} userToken The token that belongs to a particular user.
	 * @return {LocalTime} The time when the session began.
	 */
	
	public LocalTime getLastAccessTimeInSession(String userToken) {
		return _tokenTimeMap.get(userToken).minusHours(2);
	}
	
	/**
	 * Method that changes the expiration time on a token.
	 * 
	 * @param {String} userToken The token that belongs to a particular user.
	 * @param {LocalTime} newExpirationDate The new expiration time.
	 */
	
	public void changeUserTokenExpirationDate(String userToken, LocalTime newExpirationDate) {
		_tokenTimeMap.replace(userToken, newExpirationDate);		
	}
	
	/**
	 * Method that removes the user that has the
	 * given username.
	 * 
	 * @param {String} userToRemove The username of the user to be removed.
	 */
	
	@Override
	public void removeUsers(User userToRemove) {
		super.removeUsers(userToRemove);
		userToRemove.delete();
	}
	
	/**
	 * Method that deletes BubbleDocs.
	 * 
	 * To delete BubbleDocs, given the architecture of
	 * the application, first its required to sever all the
	 * connections BubbleDocs has.
	 * More specifically, to the root class of FenixFramework,
	 * and to all the users in the application.
	 * After doing that, then the object is deleted.
	 */
	
	public void delete() {
		for(User u : getUsersSet()) {
			u.delete();
		}
		
		FenixFramework.getDomainRoot().setBubbledocs(null);
		deleteDomainObject();
	}
}// End BubbleDocs class
