package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
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
import pt.tecnico.bubbledocs.service.BubbleDocsServiceTest;

public class AssignReferenceCellTest extends BubbleDocsServiceTest {

	private String ownerToken;
	private String notOwnerToken;
	private String notInSessionToken = "antonio6";

	public void populate4Test() {
		BubbleDocs.getInstance();
		
		User luis = createUser("lf", "woot", "Luis");
		this.ownerToken = addUserToSession("lf");
		
		User ze = createUser("zz", "pass", "Jose");
		this.notOwnerToken = addUserToSession("zz");
		
		Spreadsheet teste = createSpreadSheet(luis, "teste", 10, 10);
		luis.addSpreadsheets(teste);
		
		luis.givePermissionto(getSpreadSheet("teste"), ze, true);
	}

	@Test
	public void success() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		
		Cell expected_cell = new Cell(1, 1, false);
		Reference expected_reference = new Reference(expected_cell);
		
		AssignReferenceCell service = new AssignReferenceCell(ownerToken, sucessTestSpreadsheet.getId(), cell, reference);
		service.execute();
		
		String result = service.getResult();
		
		assertEquals(expected_reference.toString(), result);
	}
	
	@Test
	public void successNotOwner() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		
		Cell expected_cell = new Cell(1, 1, false);
		Reference expected_reference = new Reference(expected_cell);
		
		AssignReferenceCell service = new AssignReferenceCell(notOwnerToken, sucessTestSpreadsheet.getId(), cell, reference);
		service.execute();
		
		String result = service.getResult();
		
		assertEquals(expected_reference.toString(), result);
	}
	
	@Test(expected = InvalidArgumentsException.class)
	public void invalidArguments() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "-1;-1";
		String reference = "-1;-1";
		
		AssignReferenceCell service = new AssignReferenceCell(notOwnerToken, sucessTestSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	@Test(expected = InvalidReferenceException.class)
	public void referenceIsInvalid() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "11;11"; //Pointing to an invalid position since the spreadsheet is 10x10.
		
		AssignReferenceCell service = new AssignReferenceCell(ownerToken, testSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	@Test(expected = OutofBoundsException.class)
	public void cellIsOutOfBonds() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "11;11"; //Is an invalid position since the spreadsheet is 10x10.
		String reference = "1;2";
		
		AssignReferenceCell service = new AssignReferenceCell(ownerToken, testSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	@Test(expected = CellIsProtectedException.class)
	public void cellIsProtected() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		
		protectCell("teste", 1, 1);
		
		AssignReferenceCell service = new AssignReferenceCell(ownerToken, testSpreadsheet.getId(), cell, reference);
		service.execute();	
	}

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() {
		String cell = "1;1";
		String reference = "1;2";
		
		AssignReferenceCell service = new AssignReferenceCell(ownerToken, -1, cell, reference); //No spreadsheet should ever have -1 Id.
		service.execute();	
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		
		AssignReferenceCell service = new AssignReferenceCell(notInSessionToken, testSpreadsheet.getId(), cell, reference); //No spreadsheet should ever have -1 Id.
		service.execute();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void invalidToken() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		
		AssignReferenceCell service = new AssignReferenceCell("", testSpreadsheet.getId(), cell, reference); //No spreadsheet should ever have -1 Id.
		service.execute();
	}
	
	@Test(expected = InvalidPermissionException.class)
	public void userNotOwner() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		User luisUser = getUserFromUsername("lf");
		User zeUser = getUserFromUsername("zz");
		
		luisUser.removePermissionfrom(testSpreadsheet, zeUser);
		
		AssignReferenceCell service = new AssignReferenceCell(notOwnerToken, testSpreadsheet.getId(), cell, reference); //No spreadsheet should ever have -1 Id.
		service.execute();
	}
	
	@Test(expected = InvalidPermissionException.class)
	public void userWithoutPermission() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		User luisUser = getUserFromUsername("lf");
		User zeUser = getUserFromUsername("zz");
		String cell = "1;1";
		String reference = "1;2";

		luisUser.removePermissionfrom(testSpreadsheet, zeUser);
		
		AssignReferenceCell service = new AssignReferenceCell(notOwnerToken, testSpreadsheet.getId(), cell, reference); //No spreadsheet should ever have -1 Id.
		service.execute();
	}
}// End AssignReferenceCellTest class.