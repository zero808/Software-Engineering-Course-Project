package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.InvalidReferenceException;

/**
 * Class that describes a user.
 * 
 * Users are the life and blood of this 
 * application.
 */

public class User extends User_Base {

	/**
	 * The default constructor provided by
	 * the FenixFramework.
	 * 
	 * @constructor
	 * @this {User}
	 */
	protected User() {
		super();
	}
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {User}
	 * 
	 * @param {String} username The user's username.
	 * @param {String} name The user's name.
	 * @param {String} email The user's email.
	 */

	public User(String username, String name, String email) {
		/** @private */
		setUsername(username);
		/** @private */
		setName(name);
		/** @private */
		//Users start with null password, since it is provided by IDRemoteServices.
		setPassword(null);
		/** @private */
		setEmail(email);
	}
	
	/**
	 * Method to change the username of this particular user.
	 * 
	 * @param {String} username The new username.
	 * @throws InvalidUsernameException
	 */

	@Override
	public void setUsername(String username) throws InvalidUsernameException {

		if (username == null) {
			throw new InvalidUsernameException("Null passed as username.");
		}
		
		if(username.length() >= 3 && username.length() <= 8) {
			super.setUsername(username);
		} else {
			throw new InvalidUsernameException("Username must have between 3 and 8 characters.");
		}
	}
	
	/**
	 * Export this user to a XML document.
	 * 
	 * @return {XML Element} The element describing the user.
	 */

	public Element exportToXML() {
		Element element = new Element("user");

		element.setAttribute("username", getUsername());
		element.setAttribute("name", getName());
		element.setAttribute("pass", getPassword());
		element.setAttribute("email", getEmail());

		Element spreadsheetsElement = new Element("spreadsheets");
		element.addContent(spreadsheetsElement);

		for (Spreadsheet s : getSpreadsheetsSet()) {
			spreadsheetsElement.addContent(s.exportToXML());
		}

		return element;
	}
	
	/**
	 * Import this user from a XML document.
	 * 
	 * @param {XML Element} userElement The element that has the user data.
	 */

	public void importFromXML(Element userElement) {

		setUsername(userElement.getAttribute("username").getValue());
		setName(userElement.getAttribute("name").getValue());
		setPassword(userElement.getAttribute("pass").getValue());
		setEmail(userElement.getAttribute("email").getValue());
		Element spreadsheets = userElement.getChild("spreadsheets");

		for (Element spreadsheetElement : spreadsheets.getChildren("spreadsheet")) {
			if (getSpreadsheetByName(spreadsheetElement.getAttribute("name").getValue()) == null) {
				Spreadsheet s = new Spreadsheet();
				s.importFromXML(spreadsheetElement);
				addSpreadsheets(s);
			}
		}
	}
	
	/**
	 * Method that returns a spreadsheet that has the given name.
	 * 
	 * @param {String} name The spreadsheet's name.
	 * @return {Spreadsheet} The spreadsheet that has that name if it exists,
	 * null otherwise.
	 */

	private Spreadsheet getSpreadsheetByName(String name) {
		for (Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if (spreadsheet.getName().equals(name)) {
				return spreadsheet;
			}
		}
		return null;
	}

	/**
	 * Method that creates a link between this user and BubbleDocs.
	 * 
	 * @param {BubbleDocs} b The single instance of BubbleDocs.
	 */
	
	@Override
	public void setBubbledocs(BubbleDocs b) {
		if (b == null) {
			super.setBubbledocs(null);
			return;
		}
		b.addUsers(this);
	}
	
	/**
	 * Method that removes a spreadsheet from this user.
	 * 
	 * @param {String} spreadsheetName The name of the spreadsheet to remove.
	 * @throws SpreadsheetDoesNotExistException, InvalidPermissionException
	 */

	public void removeSpreadsheets(String spreadsheetName) throws SpreadsheetDoesNotExistException, InvalidPermissionException {
		Spreadsheet toRemove = getSpreadsheetByName(spreadsheetName);
		if (toRemove == null)
			throw new SpreadsheetDoesNotExistException();

		if (!(toRemove.getUser().getUsername().equals(getUsername())))
			throw new InvalidPermissionException(getUsername());

		super.removeSpreadsheets(toRemove);
		toRemove.delete();
	}
	
	/**
	 * Method that returns a list of all the spreadsheets whose names contain
	 * a particular string.
	 * 
	 * @param {String} str The string to match.
	 * @return {List of Spreadsheets} The list of spreadsheets whose names fit the criteria.
	 */

	public List<Spreadsheet> listSpreadsheetsContains(String str) {
		List<Spreadsheet> _matchingSpreadsheets = new ArrayList<Spreadsheet>();

		for (Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if (spreadsheet.getName().contains(str) && spreadsheet.getUser().getName().equals(getUsername())) {
				_matchingSpreadsheets.add(spreadsheet);
			}
		}

		return _matchingSpreadsheets;
	}
	
	/**
	 * Method to check if it is the root user or not.
	 * 
	 * @return {Boolean} True if yes, false otherwise.
	 */

	public boolean isRoot() {
		return false;
	}
	
	/**
	 * Method that deletes a user.
	 * 
	 * To delete a user, given the architecture of
	 * the application, first its required to sever all the
	 * connections this user has.
	 * More specifically, all the spreadsheets of this user
	 * and the permission this user has regarding spreadsheets.
	 * 
	 * The link to BubbleDocs is also severed.
	 * 
	 * If a user is deleted, then all of its spreadsheets are also deleted.
	 * 
	 * After doing that, then the object is deleted.
	 */

	public void delete() {

		for (Spreadsheet s : getSpreadsheetsSet()) {
			s.delete();
		}

		setBubbledocs(null);
		setPermission(null);
		deleteDomainObject();
	}
	
	/**
	 * Method that adds a literal to a particular cell of a particular spreadsheet.
	 * 
	 * @param {Literal} l The literal to be added.
	 * @param {Spreadsheet} s The spreadsheet where the literal is going to.
	 * @param {number} row The row value of the position where the literal is going to.
	 * @param {number} collumn The column value of the position where the literal is going to.
	 * @throws OutofBoundsException, CellIsProtectedException, InvalidArgumentsException
	 */

	public void addLiteraltoCell(Literal l, Spreadsheet s, int row, int collumn) throws OutofBoundsException, CellIsProtectedException, InvalidArgumentsException {
		if (row > s.getNRows() || collumn > s.getNCols())
			throw new OutofBoundsException(s.getNRows(), s.getNCols());
		
		if(row < 1 || collumn < 1)
			throw new InvalidArgumentsException();

		for (Cell cell : s.getCellsSet()) {
			if (cell.getRow() == row && cell.getCollumn() == collumn) {
				if (!(cell.getWProtected())) {
					cell.setContent(l);
					return;
				} else {
					throw new CellIsProtectedException(cell.getRow(), cell.getCollumn());
				}
			}
		}
	}
	
	/**
	 * Method that adds a reference to a particular cell of a particular spreadsheet.
	 * 
	 * @param {Reference} r The reference to be added.
	 * @param {Spreadsheet} s The spreadsheet where the reference is going to.
	 * @param {number} row The row value of the position where the reference is going to.
	 * @param {number} collumn The column value of the position where the reference is going to.
	 * @throws OutofBoundsException, InvalidReferenceException, CellIsProtectedException, InvalidArgumentsException
	 */

	public void addReferencetoCell(Reference r, Spreadsheet s, int row, int collumn) throws OutofBoundsException, InvalidReferenceException, CellIsProtectedException, InvalidArgumentsException {
		if (row > s.getNRows() || collumn > s.getNCols())
			throw new OutofBoundsException(s.getNRows(), s.getNCols());
		
		if(row < 1 || collumn < 1)
			throw new InvalidArgumentsException();
		
		if(r.getReferencedCell().getRow() < 1 || r.getReferencedCell().getCollumn() < 1)
			throw new InvalidArgumentsException();

		if (r.getReferencedCell().getRow() > s.getNRows() || r.getReferencedCell().getCollumn() > s.getNCols())
			throw new InvalidReferenceException(r.getReferencedCell().getRow(), r.getReferencedCell().getCollumn());

		for (Cell cell : s.getCellsSet()) {
			if (cell.getRow() == row && cell.getCollumn() == collumn) {
				if (!(cell.getWProtected())) {
					if (r.getReferencedCell().getContent() == null) {
						cell.setContent(null);
					}
					cell.setContent(r);
					return;
				} else {
					throw new CellIsProtectedException(cell.getRow(), cell.getCollumn());
				}
			}
		}
	}
	
	/**
	 * Method that adds a function to a particular cell of a particular spreadsheet.
	 * 
	 * @param {Function} f The function to be added.
	 * @param {Spreadsheet} s The spreadsheet where the function is going to.
	 * @param {number} row The row value of the position where the function is going to.
	 * @param {number} collumn The column value of the position where the function is going to.
	 * @throws OutofBoundsException, CellIsProtectedException, InvalidArgumentsException
	 */

	public void addFunctiontoCell(Function f, Spreadsheet s, int row, int collumn) throws OutofBoundsException, CellIsProtectedException, InvalidArgumentsException {
		if (row > s.getNRows() || collumn > s.getNCols())
			throw new OutofBoundsException(s.getNRows(), s.getNCols());
		
		if(row < 1 || collumn < 1)
			throw new InvalidArgumentsException();

		for (Cell cell : s.getCellsSet()) {
			if (cell.getRow() == row && cell.getCollumn() == collumn) {
				if (!(cell.getWProtected())) {
					cell.setContent(f);
					return;
				} else {
					throw new CellIsProtectedException(cell.getRow(), cell.getCollumn());
				}
			}
		}
	}
	
	/**
	 * Method that allows the user to give access to his spreadsheets
	 * to other users.
	 * 
	 * @param {Spreadsheet} s The spreadsheet that the permission refers to.
	 * @param {User} u The user thats getting a brand new permission.
	 * @param {Boolean} b The type of permission.
	 */

	public void givePermissionto(Spreadsheet s, User u, boolean b) {
		hasOwnerPermission(s);
		new Permission(s, u, b);
	}
	
	/**
	 * Method that allows the user to remove access to his spreadsheets
	 * to from users.
	 * 
	 * @param {Spreadsheet} s The spreadsheet that the permission refers to.
	 * @param {User} u The user that is getting his permission revoked.
	 * @throws InvalidPermissionException
	 */

	public void removePermissionfrom(Spreadsheet s, User u) throws InvalidPermissionException {
		hasOwnerPermission(s);
		hasPermission(false, s);
		if (s.getPermissionOfUser(u) == null) {
			throw new InvalidPermissionException(u.getUsername());
		} else {
			s.getPermissionOfUser(u).setRw(false);
		}
	}
	
	/**
	 * Method that checks if this particular user is the owner of the spreadsheet given.
	 * 
	 * @param {Spreadsheet} s The spreadsheet to check.
	 * @throws InvalidPermissionException
	 */

	public void hasOwnerPermission(Spreadsheet s) throws InvalidPermissionException {
		
		if (!(s.getUser().getUsername().equals(getUsername()))) {
			throw new InvalidPermissionException(getUsername());
		}	
	}
	
	/**
	 * Method to check if this user has any permissions regarding the spreadsheet given.
	 * 
	 * @param {Boolean} searchPermissions The type of permission.
	 * @param {Spreadsheet} s The spreadsheet in question.
	 * @throws InvalidPermissionException
	 */

	public void hasPermission(boolean searchPermissions, Spreadsheet s) throws InvalidPermissionException {
		BubbleDocs bd = BubbleDocs.getInstance();
		Permission permission = s.getPermissionOfUser(bd.getUserByUsername(getUsername()));
		
		if(permission == null) {
			throw new InvalidPermissionException(getUsername());
		}	
		
		if(searchPermissions && !(permission.getRw())) {
			throw new InvalidPermissionException(getUsername());
		}
	}
	
	/**
	 * The string representation of a User.
	 * 
	 * @return {String} The string that represents
	 * the user.
	 */

	@Override
	public String toString() {
		return "Nome: " + getName() + " " + "Username: " + getUsername() + " " + "Password: " + getPassword() + " " + "Email: " + getEmail() + "\n";
	}
}// End User class
