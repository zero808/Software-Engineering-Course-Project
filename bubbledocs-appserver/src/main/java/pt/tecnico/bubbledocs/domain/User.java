package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;

import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.OutofBondsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

public class User extends User_Base {
	
	protected User() {
		super(); //Protected so that only Root can use it
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
	
	private Spreadsheet getSpreadsheetByName(String name) {
		for(Spreadsheet spreadsheet : getSpreadsheetsSet()) {
			if(spreadsheet.getName().equals(name)) {
				return spreadsheet;
			}
		}
		return null;
	}
	
	public void addUser(String username, String name, String pass) throws InvalidPermissionException{
		throw new InvalidPermissionException(getUsername());
	}
	
	@Override
	 public void addSpreadsheets(Spreadsheet spreadsheetToBeAdded) {
		 super.addSpreadsheets(spreadsheetToBeAdded);
	    }
	
	@Override
	public void setBubbledocs(BubbleDocs b) {
		if(b == null) {
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
	    	toRemove.delete();
	    }
	 
	 public List<Spreadsheet> listarfolhas(String str) {
	    	List<Spreadsheet> matchingSpreadsheets = new ArrayList<Spreadsheet>();
		
	    	for(Spreadsheet spreadsheet : getSpreadsheetsSet()) {
	    		if(spreadsheet.getName().contains(str) && spreadsheet.getUser().getName().equals(getUsername())) {
	    			matchingSpreadsheets.add(spreadsheet);
	    		}
	    	}

	    	return matchingSpreadsheets;
	    }
	 
	    public void delete() {
	    	for(Spreadsheet s : getSpreadsheetsSet()) {
				s.delete();
			}

			setBubbledocs(null);
			deleteDomainObject();
	    }
	    
	    public String toString() {
	    	return "Id:" + getId() + "Nome:" + getName() + "Username:" + getUsername();
	    }
	    
	    public void addLiteraltoCell(Literal l, Spreadsheet s, int row, int collumn) throws OutofBondsException {
	    	
	    	if(row > s.getNRows() || collumn > s.getNCols())
	    		throw new OutofBondsException(s.getName());
	    	
	    	if(!hasPermission(s))
	    		throw new InvalidPermissionException(getUsername());
	    	
	    	for(Cell cell : s.getCellsSet()) {
	    		if(cell.getRow() == row && cell.getCollumn() == collumn) {
	    				cell.setContent(l);
	    			}
	    	}
	    }
	    
	    public void addReferencetoCell(Literal l, Spreadsheet s) {
	    	//TODO
	    }
	    
	    public void addFunctiontoCell(Literal l, Spreadsheet s) {
	    	//TODO
	    }
	    
	    public void givePermissionto(Spreadsheet s, User u) {
	    	//TODO
	    	
	    }
	    
	    public void removePermissionfrom(Spreadsheet s, User u) {
	    	//TODO
	    }
	    
	    public boolean hasPermission(Spreadsheet s) {
	    	//TODO
			return false;
	    }
    
}//End User Class
