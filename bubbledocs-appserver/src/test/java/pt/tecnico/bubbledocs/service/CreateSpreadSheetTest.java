package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidBoundsException;
import pt.tecnico.bubbledocs.exception.InvalidSpreadsheetNameException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class CreateSpreadSheetTest extends BubbleDocsServiceTest {
	
	private String userNoSpredToken;
	private String userWithSpredToken;
	private static final String notInSessionToken = "antonio6";
	private static final String legalCharTestName = "Az1 9_+-";
	
	@Override
	public void populate4Test() {
		getBubbleDocs();
		
		//this user has no previous spreadsheet
		User luis = createUser("lff", "woot", "Luis");
		this.userNoSpredToken = addUserToSession("lff");
		
		//this user already has spreadsheet and wants another
		User ze = createUser("zzz", "pass", "Jose");
		this.userWithSpredToken = addUserToSession("zzz");
		
		Spreadsheet teste = createSpreadSheet(ze, "teste", 10, 10);
		luis.addSpreadsheets(teste);
		
		luis.givePermissionto(getSpreadSheet("teste"), ze, true);
	}
	
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
	
	@Test(expected = InvalidSpreadsheetNameException.class)
	public void invalidName() {
		
		CreateSpreadSheetService service = new CreateSpreadSheetService(userNoSpredToken,"%&/()ççç",10,10);
		service.execute();
	}
	
	@Test(expected = InvalidSpreadsheetNameException.class)
	public void emptyName() {
		
		CreateSpreadSheetService service = new CreateSpreadSheetService(userNoSpredToken,"",10,10);
		service.execute();
	}
	
	@Test(expected = InvalidBoundsException.class)
	public void invalidSpreadsheetRows() {
		
		CreateSpreadSheetService service = new CreateSpreadSheetService(userWithSpredToken,"teste",-10,10);
		service.execute();
	}
	
	@Test(expected = InvalidBoundsException.class)
	public void invalidSpreadsheetCollumns() {
		CreateSpreadSheetService service = new CreateSpreadSheetService(userWithSpredToken,"teste",10,-10);
		service.execute();
	}
	
	@Test(expected = InvalidBoundsException.class)
	public void invalidArguments() {
		
		CreateSpreadSheetService service = new CreateSpreadSheetService(userWithSpredToken,"teste",0,0);
		service.execute();
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		
		CreateSpreadSheetService service = new CreateSpreadSheetService(notInSessionToken,"teste",10,10);
		service.execute();
	}
}// End CreateSpreadSheetTest class