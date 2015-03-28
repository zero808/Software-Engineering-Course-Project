package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.OutofBondsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class CreateSpreadSheetTest extends BubbleDocsServiceTest {
	private String userNoSpredToken;
	private String userWithSpredToken;
	private String notInSessionToken = "antonio6";
	
	@Override
	public void populate4Test() {
		BubbleDocs.getInstance();
		
		//this user has no previous spreadsheet
		User luis = createUser("lf", "woot", "Luis");
		this.userNoSpredToken = addUserToSession("lf");
		
		//this user already has spreadsheet and want's another
		User ze = createUser("zz", "pass", "Jose");
		this.userWithSpredToken = addUserToSession("zz");
		
		Spreadsheet teste = createSpreadSheet(ze, "teste", 10, 10);
		luis.addSpreadsheets(teste);
		
		luis.givePermissionto(getSpreadSheet("teste"), ze, true);
		
	}
	
	@Test
	public void success() {
		CreateSpreadSheet service = new CreateSpreadSheet(userNoSpredToken,"teste",10,10);
		service.execute();
		
		int expected_id = 1;
		
		int result = service.getSheetId();
		
		assertEquals(expected_id, result);
	}
	
	@Test
	public void success2() {
		CreateSpreadSheet service = new CreateSpreadSheet(userWithSpredToken,"teste",10,10);
		service.execute();
		
		int expected_id = 1;
		
		int result = service.getSheetId();
		
		assertEquals(expected_id, result);
	}
	
	@Test(expected = OutofBondsException.class)
	public void outofBonds1() {
		
		CreateSpreadSheet service = new CreateSpreadSheet(userWithSpredToken,"teste",-1,10);
		service.execute();
	}
	
	@Test(expected = OutofBondsException.class)
	public void outofBonds2() {
		CreateSpreadSheet service = new CreateSpreadSheet(userWithSpredToken,"teste",10,-1);
		service.execute();
	}
	
	@Test(expected = OutofBondsException.class)
	public void outofBonds3() {
		
		CreateSpreadSheet service = new CreateSpreadSheet(userWithSpredToken,"teste",-1,-1);
		service.execute();
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		
		CreateSpreadSheet service = new CreateSpreadSheet(notInSessionToken,"teste",10,10);
		service.execute();
	}

}
