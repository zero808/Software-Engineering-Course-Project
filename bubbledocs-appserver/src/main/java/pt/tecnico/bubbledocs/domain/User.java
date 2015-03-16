package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.OutofBondsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.InvalidReferenceException;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;
import pt.tecnico.bubbledocs.exception.UserDoesNotHavePermissionException;

public class User extends User_Base {

	protected User() {
		super(); // Protected so that only Root can use it.
	}

	public User(String username, String name, String pass) {
		super();
		int _idnext;
		
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		_idnext = bd.getIdGlobal();
		setId(_idnext);
		bd.setIdGlobal(++_idnext);

		super.setUsername(username);
		setName(name);
		setPassword(pass);
	}
	
	@Override
	public void setUsername(String username) throws InvalidUsernameException, UserAlreadyExistsException {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		
		if(username == null) {
			throw new InvalidUsernameException("Null passed as username");
		}
		
		if(username.equals("root")) {
			super.setUsername("root");
			return;
		}
			
		if(username.equals("")) {
			throw new InvalidUsernameException("Username cannot be empty");
		}
		
		if(!(username.equals(getName()))) { //If its a new name, we need to check it doesn't already belong to another user.
			if(!(bd.getUsersSet().isEmpty())) {
				for(User u : bd.getUsersSet()) {
					if(u.getUsername().equals(username)) {
						throw new UserAlreadyExistsException(username);
					}
				}
				super.setUsername(username);
			}
		} 
		
		super.setUsername(username); //If its exactly the same, its allowed.	
	}
	
	public Element exportToXML() {
		Element element = new Element("user");

		element.setAttribute("username", getUsername());
		element.setAttribute("name", getName());
		element.setAttribute("pass", getPassword());

		Element spreadsheetsElement = new Element("spreadsheets");
		element.addContent(spreadsheetsElement);
		
		//Maybe missing permissions on this side too.
		for (Spreadsheet s : getSpreadsheetsSet()) {
			spreadsheetsElement.addContent(s.exportToXML());
		}

		return element;
	}
	
	public void importFromXML(Element userElement) {

		setUsername(userElement.getAttribute("username").getValue());
		setName(userElement.getAttribute("name").getValue());
		setPassword(userElement.getAttribute("pass").getValue());
		Element spreadsheets = userElement.getChild("spreadsheets");

		//Maybe missing permissions on this side too.
		for (Element spreadsheetElement : spreadsheets.getChildren("spreadsheet")) {
			if(getSpreadsheetByName(spreadsheetElement.getAttribute("name").getValue()) == null) {
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
	
	@Override
	public void addSpreadsheets(Spreadsheet s) {
		s.setUser(this);
		super.addSpreadsheets(s);
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
			throw new SpreadsheetDoesNotExistException(spreadsheetName);

		if (!(toRemove.getUser().getUsername().equals(getUsername())))
			throw new InvalidPermissionException(getUsername());

		super.removeSpreadsheets(toRemove);
		toRemove.deleteSpreadsheetContent();
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
		deleteDomainObject();
	}

	public void addLiteraltoCell(Literal l, Spreadsheet s, int row, int collumn) throws OutofBondsException, InvalidPermissionException, CellIsProtectedException {
		if (row > s.getNRows() || collumn > s.getNCols() || row < 1 || collumn < 1)
			throw new OutofBondsException(s.getName());

		//if (!hasPermission(s))
			//throw new InvalidPermissionException(getUsername()); //TODO Second Delivery.

		for (Cell cell : s.getCellsSet()) {
			if (cell.getRow() == row && cell.getCollumn() == collumn) {
				if(!(cell.getWProtected())) {
					cell.setContent(l);
					return;
				} else {
					throw new CellIsProtectedException(s.getName());
				}
			}
		}
	}

	public void addReferencetoCell(Reference r, Spreadsheet s, int row, int collumn) throws OutofBondsException, InvalidReferenceException, InvalidPermissionException, CellIsProtectedException {
		if (row > s.getNRows() || collumn > s.getNCols() || row < 1 || collumn < 1)
			throw new OutofBondsException(s.getName());

		if (r.getReferencedCell().getRow() > s.getNRows() || r.getReferencedCell().getCollumn() > s.getNCols())
			throw new InvalidReferenceException(s.getName());

		//if (!hasPermission(s))
			//throw new InvalidPermissionException(getUsername()); //TODO Second Delivery.

		for (Cell cell : s.getCellsSet()) {
			if (cell.getRow() == row && cell.getCollumn() == collumn) {
				if(!(cell.getWProtected())) {
					if(r.getReferencedCell().getContent() == null) {
						cell.setContent(null);
					}
					cell.setContent(r);
					return;
				} else {
					throw new CellIsProtectedException(s.getName());
				}
			}
		}
	}

	public void addFunctiontoCell(Function f, Spreadsheet s, int row, int collumn) throws OutofBondsException, InvalidPermissionException, CellIsProtectedException {
		if (row > s.getNRows() || collumn > s.getNCols() || row < 1 || collumn < 1)
			throw new OutofBondsException(s.getName());

		//if (!hasPermission(s))
			//throw new InvalidPermissionException(getUsername()); //TODO Second Delivery.

		for (Cell cell : s.getCellsSet()) {
			if (cell.getRow() == row && cell.getCollumn() == collumn) {
				if(!(cell.getWProtected())) {
					cell.setContent(f);
					return;
				} else {
					throw new CellIsProtectedException(s.getName());
				}
			}
		}
	}

	public void givePermissionto(Spreadsheet s, User u, boolean b) {
		if (hasOwnerPermission(s)) {
			new Permission(s, u, b);
		}
	}

	public void removePermissionfrom(Spreadsheet s, User u) {
		if (hasOwnerPermission(s) || hasPermission(s)) {
			if(s.getPermissionOfUser(u) == null) {
				throw new UserDoesNotHavePermissionException(u.getName());
			} else {
				s.getPermissionOfUser(u).setRw(false);
			}
		}
	}

	public boolean hasOwnerPermission(Spreadsheet s) {
		if (s.getUser().getName().equals(getName()))
			return true;
		return false;
	}

	public boolean hasPermission(Spreadsheet s) {
		if(getPermission().getSpreadsheet().getName().equals(s.getName()) && getPermission().getRw() == true) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Id: " + getId() + " " + "Nome: " + getName() + " " + "Username: " + getUsername() + " " + "Password: " + getPassword() + "\n";
	}

}// End User Class