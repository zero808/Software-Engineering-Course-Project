package pt.tecnico.bubbledocs.integration.component;

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
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.InvalidBoundsException;
import pt.tecnico.bubbledocs.exception.InvalidSpreadsheetNameException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

/**
 * Class that abstracts all of the test suites for
 * each of the services of this application.
 */

public abstract class BubbleDocsServiceTest {
	
	/**
	 * Set up method that is executed before each test.
	 * It populates the DB with what the test requires.
	 * 
	 * @throws Exception
	 */

	@Before
	public void setUp() throws Exception {

		try {
			FenixFramework.getTransactionManager().begin(false);
			populate4Test();
		} catch (WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Tear down method that is executed after each test.
	 * It rollbacks the transaction of the service so that the 
	 * DB is clean for the next test.
	 */

	@After
	public void tearDown() {
		try {
			FenixFramework.getTransactionManager().rollback();
		} catch (IllegalStateException | SecurityException | SystemException e2) {
			e2.printStackTrace();
		}
	}
	
	/**
	 * Abstract method that each test suite implements,
	 * depending on what it needs to perform the tests.
	 */
	
	public abstract void populate4Test();
	
	/**
	 * Auxiliary method used in the test suites to get the single instance
	 * of BubbleDocs.
	 * 
	 * @return {BubbleDocs} The single instance of BubbleDocs.
	 */
	
	protected static BubbleDocs getBubbleDocs() {
		return BubbleDocs.getInstance();
	}
	
	/**
	 * Auxiliary method used in the test suites to create a user.
	 * 
	 * @param {String} username The user's username. 
	 * @param {String} email The user's email.
	 * @param {String} name The user's name.
	 * @throws DuplicateUsernameException, InvalidArgumentsException, InvalidUsernameException
	 * @return {User} The user that was created with the given arguments.
	 */
	
	protected User createUser(String username, String email, String name) throws DuplicateUsernameException, InvalidArgumentsException, InvalidUsernameException {
		Root r = Root.getInstance();
		User user = new User(username, name, email);
		
		r.addUser(user);
		
		return user;
	}
	
	/**
	 * Auxiliary method used in the test suites to create a spreadsheet.
	 * 
	 * @param {User} user The owner of the spreadsheet.
	 * @param {String} name The spreadsheet's name.
	 * @param {number} row The spreadsheet's max rows.
	 * @param {number} column The spreadsheet's max columns.
	 * @throws InvalidSpreadsheetNameException, InvalidBoundsException
	 * @return {Spreadsheet} The spreadsheet that was created with the given arguments.
	 */

	protected Spreadsheet createSpreadSheet(User user, String name, int row, int column) throws InvalidSpreadsheetNameException, InvalidBoundsException {
		Spreadsheet spreadsheet = new Spreadsheet(name, new DateTime(), row, column);
		
		user.addSpreadsheets(spreadsheet);
		
		return spreadsheet;
	}

	/**
	 * Auxiliary method that returns a spreadsheet with the given name.
	 * 
	 * @param {String} name The spreadsheet's name.
	 * @throws SpreadsheetDoesNotExistException
	 * @return {Spreadsheet} The spreadsheet whose name is equal to the name given.
	 */
	
	protected Spreadsheet getSpreadSheet(String name) throws SpreadsheetDoesNotExistException {
		BubbleDocs bd = getBubbleDocs();
		Spreadsheet s = bd.getSpreadsheetByName(name);
		
		if(s == null) {
			throw new SpreadsheetDoesNotExistException();
		}
		
		return s;
	}
	
	/**
	 * Auxiliary method that returns the cell from the spreadsheet given
	 * with the given coordinates.
	 * 
	 * @param {String} spreadsheetName The spreadsheet's name.
	 * @param {number} row The cell's row value.
	 * @param {number} collumn The cell's column value.
	 * @throws SpreadsheetDoesNotExistException, OutofBoundsException 
	 * @return {Cell} The cell in the given coordinates.
	 */
	
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
	
	/**
	 * Auxiliary method that protects a cell from having its content overwritten.
	 * 
	 * @param {String} spreadsheetName The name of the spreadsheet which has the cell.
	 * @param {number} row The cell's row value.
	 * @param {number} collumn The cell's column value.
	 * @throws SpreadsheetDoesNotExistException
	 */
	
	protected void protectCell(String spreadsheetName, int row, int collumn) throws SpreadsheetDoesNotExistException {
		BubbleDocs bd = getBubbleDocs();
		Spreadsheet s = bd.getSpreadsheetByName(spreadsheetName);

		if(s == null) {
			throw new SpreadsheetDoesNotExistException();
		}

		s.getCellByCoords(row, collumn).setWProtected(true);
	}
	
	/**
	 * Auxiliary method that returns the user with the given username.
	 * 
	 * @param {String} username The user's username.
	 * @throws LoginBubbleDocsException
	 * @return {User} The user with the given username.
	 */
	
	protected User getUserFromUsername(String username) throws LoginBubbleDocsException {
		BubbleDocs bd = getBubbleDocs();
		User u = bd.getUserByUsername(username);
		
		if(u == null) {
			throw new LoginBubbleDocsException("username");
		}
		
		return u;
	}
	
	/**
	 * Auxiliary method that puts a user in session and returns his token.
	 * 
	 * @param {String} username The user's username.
	 * @return {String} The user's token.
	 */

	protected String addUserToSession(String username) {
		BubbleDocs bd = getBubbleDocs();
		return bd.login(username, bd.getUserByUsername(username).getPassword());
	}
	
	/**
	 * Auxiliary method that removes a user from session.
	 * 
	 * @param {String} token The user's token.
	 */
	
	protected void removeUserFromSession(String token) {
		BubbleDocs bd = getBubbleDocs();
		bd.removeUserFromSession(token);
	}
	
	/**
	 * Auxiliary method that returns the user who has the given token.
	 * 
	 * @param {String} token The user's token.
	 * @return {User} The user to which the token given belongs to.
	 */

	protected User getUserFromSession(String token) {
		BubbleDocs bd = getBubbleDocs();
		return bd.getUserByUsername(bd.getUsernameByToken(token));
	}
}// End BubbleDocsServiceTest class
