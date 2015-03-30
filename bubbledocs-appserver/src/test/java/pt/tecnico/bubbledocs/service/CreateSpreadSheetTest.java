package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
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
		BubbleDocs.getInstance();
		
		//this user has no previous spreadsheet
		User luis = createUser("lf", "woot", "Luis");
		this.userNoSpredToken = addUserToSession("lf");
		
		//this user already has spreadsheet and want's another
		User ze = createUser("zz", "pass", "Jose");
		this.userWithSpredToken = addUserToSession("zz");
		
		//isto parece mau. Estou a fazer uma coisa que vou testar ;_;
		Spreadsheet teste = createSpreadSheet(ze, "teste", 10, 10);
		luis.addSpreadsheets(teste);
		
		luis.givePermissionto(getSpreadSheet("teste"), ze, true);
		
	}
	
	@Test
	public void success() {
		CreateSpreadSheet service = new CreateSpreadSheet(userNoSpredToken,"teste",10,10);
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
	public void success2() {
		CreateSpreadSheet service = new CreateSpreadSheet(userWithSpredToken,legalCharTestName,10,10);
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
		
		CreateSpreadSheet service = new CreateSpreadSheet(userNoSpredToken,"%&/()ççç",10,10);
		service.execute();
	}
	
	@Test(expected = InvalidSpreadsheetNameException.class)
	public void invalidName2() {
		
		CreateSpreadSheet service = new CreateSpreadSheet(userNoSpredToken,"",10,10);
		service.execute();
	}
	
	@Test(expected = InvalidBoundsException.class)
	public void outofBonds1() {
		
		CreateSpreadSheet service = new CreateSpreadSheet(userWithSpredToken,"teste",-10,10);
		service.execute();
	}
	
	@Test(expected = InvalidBoundsException.class)
	public void outofBonds2() {
		CreateSpreadSheet service = new CreateSpreadSheet(userWithSpredToken,"teste",10,-10);
		service.execute();
	}
	
	@Test(expected = InvalidBoundsException.class)
	public void outofBonds3() {
		
		CreateSpreadSheet service = new CreateSpreadSheet(userWithSpredToken,"teste",0,0);
		service.execute();
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		
		CreateSpreadSheet service = new CreateSpreadSheet(notInSessionToken,"teste",10,10);
		service.execute();
	}

}