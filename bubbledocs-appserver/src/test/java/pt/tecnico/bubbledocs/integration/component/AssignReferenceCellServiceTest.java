package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidReferenceException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.component.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.service.AssignReferenceCellService;

/**
 * Class that contains the test suite for the
 * AssignReferenceCellService.
 */

public class AssignReferenceCellServiceTest extends BubbleDocsServiceTest {

	private String ownerToken;
	private String notOwnerToken;
	private String notInSessionToken = "antonio6";
	
	/**
	 * Method that populates the DB with all
	 * the objects the test suite needs to execute.
	 */

	public void populate4Test() {
		getBubbleDocs();
		
		User luis = createUser("lff", "woot", "Luis");
		this.ownerToken = addUserToSession("lff");
		
		User ze = createUser("zzz", "pass", "Jose");
		this.notOwnerToken = addUserToSession("zzz");
		
		Spreadsheet teste = createSpreadSheet(luis, "teste", 10, 10);
		luis.addSpreadsheets(teste);
		
		luis.givePermissionto(getSpreadSheet("teste"), ze, true);
	}
	
	/**
	 * Test Case #1 - Success
	 * 
	 * Tests the normal invocation of the service, where
	 * nothing goes wrong.
	 * 
	 * Result - SUCCESS
	 */

	@Test
	public void success() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		
		Cell expected_cell = new Cell(1, 1, false);
		Reference expected_reference = new Reference(expected_cell);
		
		AssignReferenceCellService service = new AssignReferenceCellService(ownerToken, sucessTestSpreadsheet.getId(), cell, reference);
		service.execute();
		
		String result = service.getResult();
		
		assertEquals(expected_reference.toString(), result);
	}
	
	/**
	 * Test Case #2 - SuccessNotOwner
	 * 
	 * Tests a normal invocation of the service of 
	 * a user that doesn't have owner permission, but has 
	 * write permissions.
	 * 
	 * Result - SUCCESS
	 */
	
	@Test
	public void successNotOwner() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		
		Cell expected_cell = new Cell(1, 1, false);
		Reference expected_reference = new Reference(expected_cell);
		
		AssignReferenceCellService service = new AssignReferenceCellService(notOwnerToken, sucessTestSpreadsheet.getId(), cell, reference);
		service.execute();
		
		String result = service.getResult();
		
		assertEquals(expected_reference.toString(), result);
	}
	
	/**
	 * Test Case #3 - InvalidCellRowArgument
	 * 
	 * Tests what happens when the cell's row is invalid.
	 * 
	 * Result - FAILURE - InvalidArgumentsException
	 */
	
	@Test(expected = InvalidArgumentsException.class)
	public void invalidCellRowArgument() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "-1;1";
		String reference = "1;1";
		
		AssignReferenceCellService service = new AssignReferenceCellService(notOwnerToken, sucessTestSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	/**
	 * Test Case #4 - InvalidCellCollumnArgument
	 * 
	 * Tests what happens when the cell's column is invalid.
	 * 
	 * Result - FAILURE - InvalidArgumentsException
	 */
	
	@Test(expected = InvalidArgumentsException.class)
	public void invalidCellCollumnArgument() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "1;-1";
		String reference = "1;1";
		
		AssignReferenceCellService service = new AssignReferenceCellService(notOwnerToken, sucessTestSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	/**
	 * Test Case #5 - InvalidReferenceRowArgument
	 * 
	 * Tests what happens when the reference's row is invalid.
	 * 
	 * Result - FAILURE - InvalidArgumentsException
	 */
	
	@Test(expected = InvalidArgumentsException.class)
	public void invalidReferenceRowArgument() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "-1;1";
		
		AssignReferenceCellService service = new AssignReferenceCellService(notOwnerToken, sucessTestSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	/**
	 * Test Case #6 - InvalidReferenceCollumnArgument
	 * 
	 * Tests what happens when the reference's column is invalid.
	 * 
	 * Result - FAILURE - InvalidArgumentsException
	 */
	
	@Test(expected = InvalidArgumentsException.class)
	public void invalidReferenceCollumnArgument() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;-1";
		
		AssignReferenceCellService service = new AssignReferenceCellService(notOwnerToken, sucessTestSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	/**
	 * Test Case #7 - ReferenceRowIsInvalid
	 * 
	 * Tests what happens when the reference's row is out of bounds.
	 * 
	 * Result - FAILURE - InvalidReferenceException
	 */
	
	@Test(expected = InvalidReferenceException.class)
	public void referenceRowIsInvalid() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "11;1"; //Pointing to an invalid position since the spreadsheet is 10x10.
		
		AssignReferenceCellService service = new AssignReferenceCellService(ownerToken, testSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	/**
	 * Test Case #8 - ReferenceCollumnIsInvalid
	 * 
	 * Tests what happens when the reference's column is out of bounds.
	 * 
	 * Result - FAILURE - InvalidReferenceException
	 */
	
	@Test(expected = InvalidReferenceException.class)
	public void referenceCollumnIsInvalid() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;11"; //Pointing to an invalid position since the spreadsheet is 10x10.
		
		AssignReferenceCellService service = new AssignReferenceCellService(ownerToken, testSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	/**
	 * Test Case #9 - CellRowIsOutOfBounds
	 * 
	 * Tests what happens when the cell's row is out of bounds.
	 * 
	 * Result - FAILURE - OutofBoundsException
	 */
	
	@Test(expected = OutofBoundsException.class)
	public void cellRowIsOutOfBounds() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "11;1"; //Is an invalid position since the spreadsheet is 10x10.
		String reference = "1;2";
		
		AssignReferenceCellService service = new AssignReferenceCellService(ownerToken, testSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	/**
	 * Test Case #10 - CellCollumnIsOutOfBounds
	 * 
	 * Tests what happens when the cell's column is out of bounds.
	 * 
	 * Result - FAILURE - OutofBoundsException
	 */
	
	@Test(expected = OutofBoundsException.class)
	public void cellCollumnIsOutOfBounds() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;11"; //Is an invalid position since the spreadsheet is 10x10.
		String reference = "1;2";
		
		AssignReferenceCellService service = new AssignReferenceCellService(ownerToken, testSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	/**
	 * Test Case #11 - CellIsProtected
	 * 
	 * Tests what happens when a user tries to change the 
	 * value of a protected cell.
	 * 
	 * Result - FAILURE - CellIsProtectedException
	 */
	
	@Test(expected = CellIsProtectedException.class)
	public void cellIsProtected() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		
		protectCell("teste", 1, 1);
		
		AssignReferenceCellService service = new AssignReferenceCellService(ownerToken, testSpreadsheet.getId(), cell, reference);
		service.execute();	
	}
	
	/**
	 * Test Case #12 - SpreadsheetDoesNotExist
	 * 
	 * Tests what happens when the spreadsheet doesn't exist.
	 * 
	 * Result - FAILURE - SpreadsheetDoesNotExistException
	 */

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() {
		String cell = "1;1";
		String reference = "1;2";
		
		AssignReferenceCellService service = new AssignReferenceCellService(ownerToken, -1, cell, reference); //No spreadsheet should ever have -1 Id.
		service.execute();	
	}
	
	/**
	 * Test Case #13 - UserNotInSession
	 * 
	 * Tests what happens when the user doesn't have an active session.
	 * 
	 * Result - FAILURE - UserNotInSessionException
	 */
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		
		AssignReferenceCellService service = new AssignReferenceCellService(notInSessionToken, testSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	/**
	 * Test Case #14 - InvalidToken
	 * 
	 * Tests what happens when the user gives an invalid token.
	 * 
	 * Result - FAILURE - InvalidTokenException
	 */
	
	@Test(expected = InvalidTokenException.class)
	public void invalidToken() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		
		AssignReferenceCellService service = new AssignReferenceCellService("", testSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	/**
	 * Test Case #15 - UserNotOwner
	 * 
	 * Tests what happens when the user doesn't have
	 * the necessary permissions to perform this action.
	 * 
	 * Result - FAILURE - InvalidPermissionException
	 */
	
	@Test(expected = InvalidPermissionException.class)
	public void userNotOwner() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		User luisUser = getUserFromUsername("lff");
		User zeUser = getUserFromUsername("zzz");
		
		luisUser.removePermissionfrom(testSpreadsheet, zeUser);
		
		AssignReferenceCellService service = new AssignReferenceCellService(notOwnerToken, testSpreadsheet.getId(), cell, reference);
		service.execute();
	}
}// End AssignReferenceCellServiceTest class
