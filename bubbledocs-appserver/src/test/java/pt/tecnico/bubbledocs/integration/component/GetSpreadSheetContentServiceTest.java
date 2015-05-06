package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.GetSpreadSheetContentService;

public class GetSpreadSheetContentServiceTest extends BubbleDocsServiceTest {
	
	private static final String USERNAME = "james";
	private static final String EMAIL = "jamesjoyce@fluntern.ch";
	private static final String NAME = "James Joyce";
	private String ownerToken;
	private String notOwnerToken;
	private Spreadsheet teste;
	private int docId;

	@Override
	public void populate4Test() {
		BubbleDocs bd = getBubbleDocs();
		
		User james = createUser(USERNAME, EMAIL, NAME);
		this.ownerToken = addUserToSession(USERNAME);
		
		teste = createSpreadSheet(james, "ulysses", 10, 10);
		Literal l = new Literal(5);
		teste.getCellByCoords(1, 1).setContent(l);
		
		james.addSpreadsheets(teste);
		
		docId = bd.getSpreadsheetByName("ulysses").getId();
	}
	
	@Test
	public void success() {
		GetSpreadSheetContentService service = new GetSpreadSheetContentService(ownerToken, docId);
		service.execute();

		assertEquals("5", service.getMatrix()[1][1]);
		assertEquals("#VALUE", service.getMatrix()[2][2]);
	}
	
	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() {
		GetSpreadSheetContentService service = new GetSpreadSheetContentService(ownerToken, -1);
		service.execute();
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		removeUserFromSession(ownerToken);
		GetSpreadSheetContentService service = new GetSpreadSheetContentService(ownerToken, docId);
		service.execute();
	}
	
	@Test(expected = InvalidPermissionException.class)
	public void userNotOwner() {
		createUser("oscar", "oscarwilde@bagneux.fr", "Oscar Wilde");
		notOwnerToken = addUserToSession("oscar");
		GetSpreadSheetContentService service = new GetSpreadSheetContentService(notOwnerToken, docId);
		service.execute();	
	}
}// End GetSpreadSheetContentServiceTest class
