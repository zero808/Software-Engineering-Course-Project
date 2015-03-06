package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;

import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

public class User extends User_Base {
	
	public User() {
		super(); //TODO Nao pode ser chamado para criar utilizador comum
	}
    
	public User(String username, String name, String pass) {
		super();
		
		int _idnext;
		
		BubbleDocs bd = getBubbledocs();
		_idnext = bd.getIdGlobal();
		_idnext++;
		
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
    
}//End User Class
