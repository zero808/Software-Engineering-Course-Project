package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;

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

		BubbleDocs bd = getBubbledocs();
		_idnext = bd.getIdGlobal();
		bd.setIdGlobal(_idnext++);

		setId(_idnext);
		setUsername(username);
		setName(name);
		setPassword(pass);
	}
	
	@Override
	public void setName(String name) {
		if(name == null) {
			throw new InvalidUsernameException("Null passed as username");
		}
			
		if(name.equals("")) {
			throw new InvalidUsernameException("Username cannot be empty");
		}
			
		if(!(name.equals(getName()))) { //If its a new name, we need to check it doesn't already belong to another user.
			BubbleDocs bd = getBubbledocs();
			for(User u : bd.getUsersSet()) {
				if(u.getUsername().equals(name)) {
					throw new UserAlreadyExistsException(name);
				}
			}
			super.setName(name);
		} 
		
		super.setName(name); //If its exactly the same, its allowed.	
	}

	private Spreadsheet getSpreadsheetByName(String name) {
		for (Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if (spreadsheet.getName().equals(name)) {
				return spreadsheet;
			}
		}
		return null;
	}

	public void addUser(String username, String name, String pass) throws InvalidPermissionException {
		throw new InvalidPermissionException(getUsername());
	}

	@Override
	public void addSpreadsheets(Spreadsheet spreadsheetToBeAdded) {
		super.addSpreadsheets(spreadsheetToBeAdded);
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

	public String toString() {
		return "Id:" + getId() + "Nome:" + getName() + "Username:" + getUsername() + "Password:" + getPassword();
	}

	public void addLiteraltoCell(Literal l, Spreadsheet s, int row, int collumn) throws OutofBondsException, InvalidPermissionException, CellIsProtectedException {
		if (row > s.getNRows() || collumn > s.getNCols() || row < 1 || collumn < 1)
			throw new OutofBondsException(s.getName());

		if (!hasPermission(s))
			throw new InvalidPermissionException(getUsername());

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

		if (r.getCell().getRow() > s.getNRows() || r.getCell().getCollumn() > s.getNCols() || r.getCell().getContent() == null)
			throw new InvalidReferenceException(s.getName());

		if (!hasPermission(s))
			throw new InvalidPermissionException(getUsername());

		for (Cell cell : s.getCellsSet()) {
			if (cell.getRow() == row && cell.getCollumn() == collumn) {
				if(!(cell.getWProtected())) {
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

		if (!hasPermission(s))
			throw new InvalidPermissionException(getUsername());

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

}// End User Class