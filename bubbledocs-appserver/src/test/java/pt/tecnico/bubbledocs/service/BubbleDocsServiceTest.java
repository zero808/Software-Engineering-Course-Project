package pt.tecnico.bubbledocs.service;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Root;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;

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
	User createUser(String username, String password, String name) {
		Root r = Root.getInstance();
		User user = new User(username, name, password);
		
		r.addUser(user);
		
		return user;
	}

	public Spreadsheet createSpreadSheet(User user, String name, int row, int column) {
		Spreadsheet spreadsheet = new Spreadsheet(name, new DateTime(), row, column);
		
		user.addSpreadsheets(spreadsheet);
		
		return spreadsheet;
	}

	// returns a spreadsheet whose name is equal to name
	public Spreadsheet getSpreadSheet(String name) {
		BubbleDocs bd = BubbleDocs.getInstance();
		
		return bd.getSpreadsheetByName(name);
	}

	// returns the user registered in the application whose username is equal to username
	User getUserFromUsername(String username) {
		BubbleDocs bd = BubbleDocs.getInstance();
		
		return bd.getUserByUsername(username);
	}

	// put a user into session and returns the token associated to it
	String addUserToSession(String username) {
		// add code here
		return null;
	}

	// remove a user from session given its token
	void removeUserFromSession(String token) {
		// add code here
	}

	// return the user registered in session whose token is equal to token
	User getUserFromSession(String token) {
		// add code here
		return null;
	}
}// End BubbleDocsServiceTest class.