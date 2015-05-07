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

public class User extends User_Base {

	protected User() {
		super(); // Root needs this.
	}

	public User(String username, String name, String email) {
		setUsername(username);
		setName(name);
		setPassword(null);
		setEmail(email);
	}

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

	private Spreadsheet getSpreadsheetByName(String name) {
		for (Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if (spreadsheet.getName().equals(name)) {
				return spreadsheet;
			}
		}
		return null;
	}

	public void addUser(User u) throws InvalidPermissionException {
		throw new InvalidPermissionException(getUsername());
	}

	@Override
	public void setBubbledocs(BubbleDocs b) {
		if (b == null) {
			super.setBubbledocs(null);
			return;
		}
		b.addUsers(this);
	}

	public void removeSpreadsheets(String spreadsheetName) throws SpreadsheetDoesNotExistException, InvalidPermissionException {
		Spreadsheet toRemove = getSpreadsheetByName(spreadsheetName);
		if (toRemove == null)
			throw new SpreadsheetDoesNotExistException();

		if (!(toRemove.getUser().getUsername().equals(getUsername())))
			throw new InvalidPermissionException(getUsername());

		super.removeSpreadsheets(toRemove);
		toRemove.delete();
	}

	public List<Spreadsheet> listSpreadsheetsContains(String str) {
		List<Spreadsheet> _matchingSpreadsheets = new ArrayList<Spreadsheet>();

		for (Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if (spreadsheet.getName().contains(str) && spreadsheet.getUser().getName().equals(getUsername())) {
				_matchingSpreadsheets.add(spreadsheet);
			}
		}

		return _matchingSpreadsheets;
	}

	public boolean isRoot() {
		return false;
	}

	public void delete() {

		for (Spreadsheet s : getSpreadsheetsSet()) {
			s.delete();
		}

		setBubbledocs(null);
		setPermission(null);
		deleteDomainObject();
	}

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

	public void givePermissionto(Spreadsheet s, User u, boolean b) {
		hasOwnerPermission(s);
		new Permission(s, u, b);
	}

	public void removePermissionfrom(Spreadsheet s, User u) throws InvalidPermissionException {
		hasOwnerPermission(s);
		hasPermission(false, s);
		if (s.getPermissionOfUser(u) == null) {
			throw new InvalidPermissionException(u.getUsername());
		} else {
			s.getPermissionOfUser(u).setRw(false);
		}
	}

	public void hasOwnerPermission(Spreadsheet s) throws InvalidPermissionException {
		
		if (!(s.getUser().getUsername().equals(getUsername()))) {
			throw new InvalidPermissionException(getUsername());
		}	
	}

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

	@Override
	public String toString() {
		return "Nome: " + getName() + " " + "Username: " + getUsername() + " " + "Password: " + getPassword() + " " + "Email: " + getEmail() + "\n";
	}
}// End User class
