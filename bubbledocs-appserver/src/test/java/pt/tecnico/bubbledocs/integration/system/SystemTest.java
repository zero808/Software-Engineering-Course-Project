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

public abstract class SystemTest {
	
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
		FenixFramework.getDomainRoot().getBubbledocs().delete();
		try {
			FenixFramework.getTransactionManager().commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e2) {
			e2.printStackTrace();
		}
	}
	
	public abstract void populate4Test();
}// End SystemTest class
