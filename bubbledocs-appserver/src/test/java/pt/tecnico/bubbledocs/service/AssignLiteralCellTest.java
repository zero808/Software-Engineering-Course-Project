package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

//import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Literal;
//import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
//import pt.tecnico.bubbledocs.exception.InvalidReferenceException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.BubbleDocsServiceTest;

public class AssignLiteralCellTest extends BubbleDocsServiceTest {

	private String ownerToken;
	private String notOwnerToken;
	private String notInSessionToken = "antonio6";

	public void populate4Test() {
		getBubbleDocs();

		User leo = createUser("lv", "pass", "Leo");
		this.ownerToken = addUserToSession("lv");

		User ze = createUser("zz", "pass", "Jose");
		this.notOwnerToken = addUserToSession("zz");

		Spreadsheet teste = createSpreadSheet(leo, "teste", 10, 10);
		leo.addSpreadsheets(teste);

		leo.givePermissionto(getSpreadSheet("teste"), ze, true);
	}

	@Test
	public void success() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String literal = "3";

		Literal expected_literal = new Literal(Integer.parseInt(literal));

		AssignLiteralCell service = new AssignLiteralCell(ownerToken, sucessTestSpreadsheet.getId(), cell, literal);
		service.execute();

		String result = service.getResult();

		assertEquals(expected_literal.toString(), result);
	}

	@Test(expected = OutofBoundsException.class)
	public void cellIsOutOfBonds() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "11;11"; // Is an invalid position since the spreadsheet is 10x10.
		String literal = "3";

		AssignLiteralCell service = new AssignLiteralCell(ownerToken, testSpreadsheet.getId(), cell, literal);
		service.execute();
	}

	@Test(expected = CellIsProtectedException.class)
	public void cellIsProtected() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String literal = "3";

		protectCell("teste", 1, 1);

		AssignLiteralCell service = new AssignLiteralCell(ownerToken, testSpreadsheet.getId(), cell, literal);
		service.execute();
	}

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() {
		String cell = "1;1";
		String literal = "3";

		AssignLiteralCell service = new AssignLiteralCell(ownerToken, -1, cell, literal); // No spreadsheet should ever have -1 Id.
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String literal = "3";

		AssignLiteralCell service = new AssignLiteralCell(notInSessionToken, testSpreadsheet.getId(), cell, literal); 
		service.execute();
	}

	@Test(expected = InvalidTokenException.class)
	public void invalidToken() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String literal = "3";

		AssignLiteralCell service = new AssignLiteralCell("", testSpreadsheet.getId(), cell, literal); 
		service.execute();
	}

	@Test(expected = InvalidPermissionException.class)
	public void userNotOwner() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String literal = "3";
		User leoUser = getUserFromUsername("lv");
		User zeUser = getUserFromUsername("zz");

		leoUser.removePermissionfrom(testSpreadsheet, zeUser);

		AssignLiteralCell service = new AssignLiteralCell(notOwnerToken, testSpreadsheet.getId(), cell, literal);
		service.execute();
	}

	@Test(expected = InvalidPermissionException.class)
	public void userWithoutPermission() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		User leoUser = getUserFromUsername("lv");
		User zeUser = getUserFromUsername("zz");
		String cell = "1;1";
		String literal = "3";

		leoUser.removePermissionfrom(testSpreadsheet, zeUser);

		AssignLiteralCell service = new AssignLiteralCell(notOwnerToken, testSpreadsheet.getId(), cell, literal);
		service.execute();
	}
}// End AssignLiteralCellTest class