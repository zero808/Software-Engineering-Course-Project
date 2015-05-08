package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidBoundsException;
import pt.tecnico.bubbledocs.exception.InvalidSpreadsheetNameException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.CreateSpreadSheetService;

/**
 * Class that contains the test suite for the
 * CreateSpreadSheetService.
 */

public class CreateSpreadSheetServiceTest extends BubbleDocsServiceTest {
	
	private String userNoSpredToken;
	private String userWithSpredToken;
	private static final String notInSessionToken = "antonio6";
	private static final String legalCharTestName = "Az1 9_+-";
	
	/**
	 * Method that populates the DB with all
	 * the objects the test suite needs to execute.
	 */
	
	@Override
	public void populate4Test() {
		getBubbleDocs();
		
		//This user has no previous spreadsheet.
		User luis = createUser("lff", "woot", "Luis");
		this.userNoSpredToken = addUserToSession("lff");
		
		//This user already has spreadsheet and wants another.
		User ze = createUser("zzz", "pass", "Jose");
		this.userWithSpredToken = addUserToSession("zzz");
		
		Spreadsheet teste = createSpreadSheet(ze, "teste", 10, 10);
		luis.addSpreadsheets(teste);
		
		luis.givePermissionto(getSpreadSheet("teste"), ze, true);
	}
	
	/**
	 * Test Case #1 - Success
	 * 
	 * Tests a normal invocation of the service
	 * where nothing goes wrong and the user has no spreadsheets..
	 * 
	 * Result - SUCCESS
	 */
	
	@Test
	public void success() {
		CreateSpreadSheetService service = new CreateSpreadSheetService(userNoSpredToken,"teste",10,10);
		service.execute();
		
		int expected_id = 1;

		Spreadsheet s = service.getSheet();
		
		assertEquals(expected_id, s.getId());
		assertEquals("teste", s.getName());
		assertEquals(getUserFromSession(userNoSpredToken).getUsername(), s.getUser().getUsername());
		assertEquals(10, s.getNRows());
		assertEquals(10, s.getNCols());
	}
	
	/**
	 * Test Case #2 - SuccessNonEmptySpredlist
	 * 
	 * Tests a normal invocation of the service
	 * where nothing goes wrong and the user already has spreadsheets.
	 * 
	 * Result - SUCCESS
	 */
	
	@Test
	public void successNonEmptySpredlist() {
		CreateSpreadSheetService service = new CreateSpreadSheetService(userWithSpredToken,legalCharTestName,10,10);
		service.execute();
		
		int expected_id = 1;
		
		Spreadsheet s = service.getSheet();
		
		assertEquals(expected_id, s.getId());
		assertEquals(legalCharTestName, s.getName());
		assertEquals(getUserFromSession(userWithSpredToken).getUsername(), s.getUser().getUsername());
		assertEquals(10, s.getNRows());
		assertEquals(10, s.getNCols());
	}
	
	/**
	 * Test Case #3 - InvalidName
	 * 
	 * Tests what happens when an invalid name is given.
	 * 
	 * Result - FAILURE - InvalidSpreadsheetNameException
	 */
	
	@Test(expected = InvalidSpreadsheetNameException.class)
	public void invalidName() {
		
		CreateSpreadSheetService service = new CreateSpreadSheetService(userNoSpredToken,"%&/()ççç",10,10);
		service.execute();
	}
	
	/**
	 * Test Case #4 - EmptyName
	 * 
	 * Tests what happens when an empty name is given.
	 * 
	 * Result - FAILURE - InvalidSpreadsheetNameException
	 */
	
	@Test(expected = InvalidSpreadsheetNameException.class)
	public void emptyName() {
		
		CreateSpreadSheetService service = new CreateSpreadSheetService(userNoSpredToken,"",10,10);
		service.execute();
	}
	
	/**
	 * Test Case #4 - InvalidSpreadsheetRows
	 * 
	 * Tests what happens when the spreadsheet's rows are out of bounds.
	 * 
	 * Result - FAILURE - InvalidBoundsException
	 */
	
	@Test(expected = InvalidBoundsException.class)
	public void invalidSpreadsheetRows() {
		
		CreateSpreadSheetService service = new CreateSpreadSheetService(userWithSpredToken,"teste",-10,10);
		service.execute();
	}
	
	/**
	 * Test Case #5 - InvalidSpreadsheetRows
	 * 
	 * Tests what happens when the spreadsheet's columns are out of bounds.
	 * 
	 * Result - FAILURE - InvalidBoundsException
	 */
	
	@Test(expected = InvalidBoundsException.class)
	public void invalidSpreadsheetCollumns() {
		CreateSpreadSheetService service = new CreateSpreadSheetService(userWithSpredToken,"teste",10,-10);
		service.execute();
	}
	
	/**
	 * Test Case #6 - InvalidArguments
	 * 
	 * Tests what happens when the spreadsheet's columns are out of bounds.
	 * 
	 * Result - FAILURE - InvalidBoundsException
	 */
	
	@Test(expected = InvalidBoundsException.class)
	public void invalidArguments() {
		
		CreateSpreadSheetService service = new CreateSpreadSheetService(userWithSpredToken,"teste",0,0);
		service.execute();
	}
	
	/**
	 * Test Case #7 - UserNotInSession
	 * 
	 * Tests what happens when the user doesn't have a valid session.
	 * 
	 * Result - FAILURE - UserNotInSessionException
	 */
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		
		CreateSpreadSheetService service = new CreateSpreadSheetService(notInSessionToken,"teste",10,10);
		service.execute();
	}
}// End CreateSpreadSheetServiceTest class
