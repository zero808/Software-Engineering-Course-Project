package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;

public class Root extends Root_Base {
	
	public static Root getInstance() {
		Root instance = null;
		if (instance == null) {
			instance = new Root();
		}
		return instance;
	}

	private Root() {
		
		super();
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		setBubbledocs(bd);
		setUsername("root");
		setName("Super User");
		setPassword("rootroot"); // So that it doesn't have null.
		setEmail("root@root.pt");
		
		bd.addUsers(this);
	}

	private Spreadsheet getSpreadsheetByName(String spreadsheetName) {
		for(Spreadsheet s :FenixFramework.getDomainRoot().getBubbledocs().getSpreadsheetsSet()) {
			if(s.getName().equals(spreadsheetName)) {
				return s;
			}
		}
		return null;
	}
	
	public void removeUser(String username) throws LoginBubbleDocsException {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		User toRemove = bd.getUserByUsername(username);

		if (toRemove == null)
			throw new LoginBubbleDocsException("username");

		bd.removeUsers(toRemove);
	}


	@Override
	public void addUser(User u) throws UserAlreadyExistsException, InvalidArgumentsException {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		
		if(bd.getUsersSet().isEmpty()) {
			bd.addUsers(u);
		} else {
			
			if(bd.getUserByUsername(u.getUsername()) != null) {
				throw new UserAlreadyExistsException(u.getUsername());
			}
			
			if(u.getName() != null && u.getPassword() != null && u.getUsername() != null) {
				bd.addUsers(u);
			} else {
				throw new InvalidArgumentsException();
			}	
		}	
	}

	@Override
	public void removeSpreadsheets(String spreadsheetName) throws SpreadsheetDoesNotExistException {
		Spreadsheet toRemove = getSpreadsheetByName(spreadsheetName);

		if (toRemove == null)
			throw new SpreadsheetDoesNotExistException();

		super.removeSpreadsheets(toRemove);
		toRemove.delete();
	}
	
	@Override
	public void removePermissionfrom(Spreadsheet s, User u) {
		if(s.getPermissionOfUser(u) == null) {
			throw new InvalidPermissionException(u.getName());
		} else {
			s.getPermissionOfUser(u).setRw(false);
		}
	}
	
	@Override
	public void givePermissionto(Spreadsheet s, User u, boolean b) {
		new Permission(s, u, b);
	}
	
	@Override
	public boolean isRoot() {
		return true;
	}		
}// End Root class