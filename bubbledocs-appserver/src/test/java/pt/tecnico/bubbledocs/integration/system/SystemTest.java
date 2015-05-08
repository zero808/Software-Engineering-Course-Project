package pt.tecnico.bubbledocs.integration.system;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;

/**
 * Class that abstracts all of the test suites for
 * each of the system test suites.
 */

public abstract class SystemTest {
	
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
	 * Since these are system tests, to clean the DB one cannot simply
	 * rollback. So the BubbleDocs has to be deleted.
	 */

	@After
	public void tearDown() {
		FenixFramework.getDomainRoot().getBubbledocs().delete();
		try {
			FenixFramework.getTransactionManager().commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e2) {
			e2.printStackTrace();
		}
	}
	
	/**
	 * Abstract method that each test suite implements,
	 * depending on what it needs to perform the tests.
	 */
	
	public abstract void populate4Test();
}// End SystemTest class
