package pt.tecnico.bubbledocs.service;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Root;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;
import pt.tecnico.bubbledocs.exception.UserDoesNotExistException;

public abstract class BubbleDocsServiceTest {

	@Before
	public void setUp() throws Exception {

		try {
			FenixFramework.getTransactionManager().begin(false);
			populate4Test();
		} catch (WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		try {
			FenixFramework.getTransactionManager().rollback();
		} catch (IllegalStateException | SecurityException | SystemException e) {
			e.printStackTrace();
		}
	}
	
	//Each test suite overrides this depending on what it needs.
	public abstract void populate4Test();
	
	// Auxiliary methods that access the domain layer and are needed in the test classes.
	// For defining the initial state and checking that the service has the expected behavior.
	protected static BubbleDocs getBubbleDocs() {
		//return FenixFramework.getDomainRoot().getBubbledocs();
		return BubbleDocs.getInstance();
	}
	
	protected User createUser(String username, String password, String name) throws UserAlreadyExistsException, InvalidArgumentsException {
		Root r = Root.getInstance();
		User user = new User(username, name, password);
		
		r.addUser(user);
		
		return user;
	}

	protected Spreadsheet createSpreadSheet(User user, String name, int row, int column) {
		Spreadsheet spreadsheet = new Spreadsheet(name, new DateTime(), row, column);
		
		user.addSpreadsheets(spreadsheet);
		
		return spreadsheet;
	}

	// returns a spreadsheet whose name is equal to name
	protected Spreadsheet getSpreadSheet(String name) throws SpreadsheetDoesNotExistException {
		BubbleDocs bd = getBubbleDocs();
		Spreadsheet s = bd.getSpreadsheetByName(name);
		
		if(s == null) {
			throw new SpreadsheetDoesNotExistException();
		}
		
		return s;
	}
	
	// returns a cell from the spreadsheet with the given coordinates.
	protected Cell getCellByCoords(String spreadsheetName, int row, int collumn) throws SpreadsheetDoesNotExistException, OutofBoundsException {
		BubbleDocs bd = getBubbleDocs();
		Spreadsheet s = bd.getSpreadsheetByName(spreadsheetName);
		
		if(s == null) {
			throw new SpreadsheetDoesNotExistException();
		}
		
		Cell c = s.getCellByCoords(row, collumn);
		
		if(c == null) {
			throw new OutofBoundsException(row, collumn);
		}
		
		return c;
	}
	
	// protects a cell from being overwritten.
	protected void protectCell(String spreadsheetName, int row, int collumn) throws SpreadsheetDoesNotExistException {
		BubbleDocs bd = getBubbleDocs();
		Spreadsheet s = bd.getSpreadsheetByName(spreadsheetName);

		if(s == null) {
			throw new SpreadsheetDoesNotExistException();
		}

		s.getCellByCoords(row, collumn).setWProtected(true);
	}

	// returns the user registered in the application whose username is equal to username
	//TODO Change to return null and not throw exception? -> Requires changes in tests
	protected User getUserFromUsername(String username) throws UserDoesNotExistException {
		BubbleDocs bd = getBubbleDocs();
		User u = bd.getUserByUsername(username);
		
		if(u == null) {
			throw new UserDoesNotExistException();
		}
		
		return u;
	}

	// put a user into session and returns the token associated to it
	protected String addUserToSession(String username) {
		BubbleDocs bd = getBubbleDocs();
		return bd.login(username, bd.getUserByUsername(username).getPassword());
	}

	// remove a user from session given its token
	protected void removeUserFromSession(String token) {
		BubbleDocs bd = getBubbleDocs();
		bd.removeUserFromSession(token);
	}

	// return the user registered in session whose token is equal to token
	protected User getUserFromSession(String token) {
		BubbleDocs bd = getBubbleDocs();
		return bd.getUserByUsername(bd.getUsernameByToken(token));
	}
}// End BubbleDocsServiceTest class.