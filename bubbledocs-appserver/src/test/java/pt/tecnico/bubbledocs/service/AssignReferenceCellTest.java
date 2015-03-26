package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidReferenceException;
import pt.tecnico.bubbledocs.exception.OutofBondsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.BubbleDocsServiceTest;

public class AssignReferenceCellTest extends BubbleDocsServiceTest {

	private String userToken;
	//private String rootToken;

	public void populate4Test() {
		BubbleDocs.getInstance();
		User luis = createUser("lf", "woot", "Luis");
		Spreadsheet teste = createSpreadSheet(luis, "teste", 10, 10);
		luis.addSpreadsheets(teste);
	
		//TODO Add root and user to session.
	}

	@Test
	public void success() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		
		Cell expected_cell = new Cell(1, 1, false);
		Reference expected_reference = new Reference(expected_cell);
		
		AssignReferenceCell service = new AssignReferenceCell(userToken, sucessTestSpreadsheet.getId(), cell, reference);
		service.execute();
		
		String result = service.getResult();
		
		assertEquals(expected_reference.toString(), result);
	}
	
	@Test(expected = InvalidReferenceException.class)
	public void referenceIsInvalid() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "11;11"; //Pointing to an invalid position since the spreadsheet is 10x10.
		
		AssignReferenceCell service = new AssignReferenceCell(userToken, testSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	@Test(expected = OutofBondsException.class)
	public void cellIsOutOfBonds() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "11;11"; //Is an invalid position since the spreadsheet is 10x10.
		String reference = "1;2";
		
		AssignReferenceCell service = new AssignReferenceCell(userToken, testSpreadsheet.getId(), cell, reference);
		service.execute();
	}
	
	@Test(expected = CellIsProtectedException.class)
	public void cellIsProtected() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String reference = "1;2";
		
		protectCell("teste", 1, 1);
		
		AssignReferenceCell service = new AssignReferenceCell(userToken, testSpreadsheet.getId(), cell, reference);
		service.execute();	
	}

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() {
		String cell = "1;1";
		String reference = "1;2";
		
		AssignReferenceCell service = new AssignReferenceCell(userToken, -1, cell, reference); //No spreadsheet should ever have -1 Id.
		service.execute();	
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		//TODO Login needs to be implemented.
		throw new UserNotInSessionException("TODO");
	}
	
	@Test(expected = InvalidPermissionException.class)
	public void userWithoutPermission() {
		//TODO
		throw new InvalidPermissionException("TODO");
	}
}// End AssignReferenceCellTest class.