package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.CellIsProtectedException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.component.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.service.AssignLiteralCellService;

public class AssignLiteralCellServiceTest extends BubbleDocsServiceTest {

	private String ownerToken;
	private String notOwnerToken;
	private String notInSessionToken = "antonio6";

	public void populate4Test() {
		getBubbleDocs();

		User leo = createUser("leoval", "leo@email.com", "Leo");
		this.ownerToken = addUserToSession("leoval");

		User ze = createUser("zeze", "ze@email.com", "Jose");
		this.notOwnerToken = addUserToSession("zeze");

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

		AssignLiteralCellService service = new AssignLiteralCellService(ownerToken, sucessTestSpreadsheet.getId(), cell, literal);
		service.execute();

		String result = service.getResult();

		assertEquals(expected_literal.toString(), result);
	}
	
	@Test
	public void successNotOwner() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String literal = "3";
		
		Literal expected_literal = new Literal(Integer.parseInt(literal));

		AssignLiteralCellService service = new AssignLiteralCellService(notOwnerToken, sucessTestSpreadsheet.getId(), cell, literal);
		service.execute();

		String result = service.getResult();

		assertEquals(expected_literal.toString(), result);
	}

	@Test(expected = OutofBoundsException.class)
	public void cellRowIsOutOfBonds() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "11;1"; // Is an invalid position since the spreadsheet is 10x10.
		String literal = "3";

		AssignLiteralCellService service = new AssignLiteralCellService(ownerToken, testSpreadsheet.getId(), cell, literal);
		service.execute();
	}
	
	@Test(expected = OutofBoundsException.class)
	public void cellCollumnIsOutOfBonds() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;11"; // Is an invalid position since the spreadsheet is 10x10.
		String literal = "3";

		AssignLiteralCellService service = new AssignLiteralCellService(ownerToken, testSpreadsheet.getId(), cell, literal);
		service.execute();
	}
	
	@Test(expected = InvalidArgumentsException.class)
	public void cellRowIsInvalid() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "-1;1"; // Is an invalid position since the spreadsheet is 10x10.
		String literal = "3";

		AssignLiteralCellService service = new AssignLiteralCellService(ownerToken, testSpreadsheet.getId(), cell, literal);
		service.execute();
	}
	
	@Test(expected = InvalidArgumentsException.class)
	public void cellCollumnIsInvalid() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;-1"; // Is an invalid position since the spreadsheet is 10x10.
		String literal = "3";

		AssignLiteralCellService service = new AssignLiteralCellService(ownerToken, testSpreadsheet.getId(), cell, literal);
		service.execute();
	}

	@Test(expected = CellIsProtectedException.class)
	public void cellIsProtected() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String literal = "3";

		protectCell("teste", 1, 1);

		AssignLiteralCellService service = new AssignLiteralCellService(ownerToken, testSpreadsheet.getId(), cell, literal);
		service.execute();
	}

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() {
		String cell = "1;1";
		String literal = "3";

		AssignLiteralCellService service = new AssignLiteralCellService(ownerToken, -1, cell, literal); // No spreadsheet should ever have -1 Id.
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String literal = "3";

		AssignLiteralCellService service = new AssignLiteralCellService(notInSessionToken, testSpreadsheet.getId(), cell, literal); 
		service.execute();
	}

	@Test(expected = InvalidTokenException.class)
	public void invalidToken() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String literal = "3";

		AssignLiteralCellService service = new AssignLiteralCellService("", testSpreadsheet.getId(), cell, literal); 
		service.execute();
	}

	@Test(expected = InvalidPermissionException.class)
	public void userNotOwner() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String literal = "3";
		User leoUser = getUserFromUsername("leoval");
		User zeUser = getUserFromUsername("zeze");

		leoUser.removePermissionfrom(testSpreadsheet, zeUser);

		AssignLiteralCellService service = new AssignLiteralCellService(notOwnerToken, testSpreadsheet.getId(), cell, literal);
		service.execute();
	}
}// End AssignLiteralCellTest class