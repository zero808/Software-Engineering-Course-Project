package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class GetSpreadSheetContentServiceTest extends BubbleDocsServiceTest {
	
	private static final String USERNAME = "james";
	private static final String EMAIL = "jamesjoyce@fluntern.ch";
	private static final String NAME = "James Joyce";
	private static final String SPREADSHEET = "ulysses";
	private String ownerToken;
	private String notOwnerToken;
	private Spreadsheet teste;

	@Override
	public void populate4Test() {
		getBubbleDocs();
		User james = createUser(USERNAME, EMAIL, NAME);
		this.ownerToken = addUserToSession(USERNAME);
		teste = createSpreadSheet(james, SPREADSHEET, 10, 10);
		Literal l = new Literal(5);
		teste.getCellByCoords(1, 1).setContent(l);
		james.addSpreadsheets(teste);

	}
	
	@Test
	public void success() {
		GetSpreadSheetContentService service = new GetSpreadSheetContentService(ownerToken, SPREADSHEET);
		service.execute();

		assertEquals("5", service.getMatrix()[1][1]);
		assertEquals("", service.getMatrix()[2][2]);

	}
	
	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() {
		GetSpreadSheetContentService service = new GetSpreadSheetContentService(ownerToken, "teste");
		service.execute();
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		removeUserFromSession(ownerToken);
		GetSpreadSheetContentService service = new GetSpreadSheetContentService(ownerToken, SPREADSHEET);
		service.execute();
	}
	
	@Test(expected = InvalidPermissionException.class)
	public void userNotOwner() {
		createUser("oscar", "oscarwilde@bagneux.fr", "Oscar Wilde");
		notOwnerToken = addUserToSession("oscar");
		GetSpreadSheetContentService service = new GetSpreadSheetContentService(notOwnerToken, SPREADSHEET);
		service.execute();
		
	}

}
