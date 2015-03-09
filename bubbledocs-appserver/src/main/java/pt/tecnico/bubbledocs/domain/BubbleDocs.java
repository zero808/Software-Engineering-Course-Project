package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.InvalidPasswordException;
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
    	setIdGlobal(1); //root id = 0
    }
    
    private User getUserByName(String name) {
    	for(User user : getUsersSet()) {
    		if(user.getName().equals(name)) {
    			return user;
    		}
    	}
    	return null;
    }
    
    public void login(String username, String password) throws UsernameDoesNotExistException, InvalidPasswordException {
    	
    	User u = getUserByName(username);
     	String pass = u.getPassword();
    	
    	if(u.equals(null)) {
    		throw new UsernameDoesNotExistException(username);
    	}
    	
    	if(pass.equals(null) || !(pass.equals(password))) {
    		throw new InvalidPasswordException();
    	}
    }
    
    public void printUsers() {
    	for (User user : getUsersSet()) {
	    	user.toString();
	    }
    }
    
    public void printSpreadsheets() {
    	for (User user : getUsersSet()) {
	    	for(Spreadsheet spreadsheet : user.getSpreadsheetsSet()) {
	    		spreadsheet.toString();
	    	}
	    }
    }
    
}//End BubbleDocs Class
