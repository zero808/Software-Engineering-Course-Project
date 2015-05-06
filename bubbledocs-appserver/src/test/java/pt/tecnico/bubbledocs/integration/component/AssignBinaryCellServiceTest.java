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
import pt.tecnico.bubbledocs.service.AssignBinaryCellService;

public class AssignBinaryCellServiceTest extends BubbleDocsServiceTest {

	private String ownerToken;
	private String notOwnerToken;
	private String notInSessionToken = "antonio6";
	private String validInputRefs = "=ADD(2;1,2;2)";
	private String validInputLits = "=SUB(4,2)";
	private String malformedArguments = "=MUL(-1;-1,11;12)";

	
	public void populate4Test() {
		getBubbleDocs();

		User leo = createUser("leoval", "leo@email.com", "Leo");
		this.ownerToken = addUserToSession("leoval");

		User ze = createUser("zeze", "ze@email.com", "Jose");
		this.notOwnerToken = addUserToSession("zeze");

		Spreadsheet teste = createSpreadSheet(leo, "teste", 10, 10);
		leo.addSpreadsheets(teste);
		
		teste.getCellByCoords(2, 1).setContent(new Literal(2));
		teste.getCellByCoords(2, 2).setContent(new Literal(2));

		leo.givePermissionto(getSpreadSheet("teste"), ze, true);
	}

	@Test
	public void success() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String expected_result = "4";

		AssignBinaryCellService service = new AssignBinaryCellService(ownerToken, sucessTestSpreadsheet.getId(), cell, validInputRefs);
		service.execute();
		
		String result = service.getResult();

		assertEquals(expected_result, result);
	}
	
	@Test
	public void successNotOwner() {
		Spreadsheet sucessTestSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		String expected_result = "2";

		AssignBinaryCellService service = new AssignBinaryCellService(notOwnerToken, sucessTestSpreadsheet.getId(), cell, validInputLits);
		service.execute();

		String result = service.getResult();
		assertEquals(expected_result, result);
	}
	
	@Test(expected = OutofBoundsException.class)
	public void cellRowIsOutOfBonds() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "11;1"; // Is an invalid position since the spreadsheet is 10x10.

		AssignBinaryCellService service = new AssignBinaryCellService(notOwnerToken, testSpreadsheet.getId(), cell, validInputLits);
		service.execute();
	}
	
	@Test(expected = OutofBoundsException.class)
	public void cellCollumnIsOutOfBonds() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;11"; // Is an invalid position since the spreadsheet is 10x10.

		AssignBinaryCellService service = new AssignBinaryCellService(notOwnerToken, testSpreadsheet.getId(), cell, validInputLits);
		service.execute();
	}
	
	@Test(expected = InvalidArgumentsException.class)
	public void cellRowIsInvalid() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "-1;1"; // Is an invalid position since the spreadsheet is 10x10.

		AssignBinaryCellService service = new AssignBinaryCellService(notOwnerToken, testSpreadsheet.getId(), cell, validInputLits);
		service.execute();
	}
	
	@Test(expected = InvalidArgumentsException.class)
	public void cellCollumnIsInvalid() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;-1"; // Is an invalid position since the spreadsheet is 10x10.

		AssignBinaryCellService service = new AssignBinaryCellService(notOwnerToken, testSpreadsheet.getId(), cell, validInputLits);
		service.execute();
	}
	
	@Test(expected = CellIsProtectedException.class)
	public void cellIsProtected() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";

		protectCell("teste", 1, 1);

		AssignBinaryCellService service = new AssignBinaryCellService(notOwnerToken, testSpreadsheet.getId(), cell, validInputLits);
		service.execute();
	}
	
	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() {
		String cell = "1;1";

		AssignBinaryCellService service = new AssignBinaryCellService(notOwnerToken, -1, cell, validInputLits); // No spreadsheet should ever have -1 Id.
		service.execute();
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";

		AssignBinaryCellService service = new AssignBinaryCellService(notInSessionToken, testSpreadsheet.getId(), cell, validInputLits);
		service.execute();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void invalidToken() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";

		AssignBinaryCellService service = new AssignBinaryCellService("", testSpreadsheet.getId(), cell, validInputLits);
		service.execute();
	}

	@Test(expected = InvalidPermissionException.class)
	public void userNotOwner() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";
		User leoUser = getUserFromUsername("leoval");
		User zeUser = getUserFromUsername("zeze");

		leoUser.removePermissionfrom(testSpreadsheet, zeUser);

		AssignBinaryCellService service = new AssignBinaryCellService(notOwnerToken, testSpreadsheet.getId(), cell, validInputLits);
		service.execute();
	}
	
	@Test(expected = InvalidArgumentsException.class)
	public void malformedArguments() {
		Spreadsheet testSpreadsheet = getSpreadSheet("teste");
		String cell = "1;1";

		AssignBinaryCellService service = new AssignBinaryCellService(ownerToken, testSpreadsheet.getId(), cell, malformedArguments);
		service.execute();
	}
	
}// End AssignBinaryCellTest class