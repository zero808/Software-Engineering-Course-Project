package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Add;
import pt.tecnico.bubbledocs.domain.Avg;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Range;
import pt.tecnico.bubbledocs.domain.Reference;
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
	private String[][] matrix;

	@Override
	public void populate4Test() {
		BubbleDocs bd = getBubbleDocs();
		
		User james = createUser(USERNAME, EMAIL, NAME);
		this.ownerToken = addUserToSession(USERNAME);
		
		teste = createSpreadSheet(james, "ulysses", 5, 5);
		
		Literal l = new Literal(5);
		Cell c = teste.getCellByCoords(1, 1);
		c.setContent(l);
		Reference r = new Reference(c);
		Add binaryFunction = new Add(l, r);
		Range range = new Range(c, teste.getCellByCoords(2, 1));
		Avg unaryFunction = new Avg(range);
		teste.getCellByCoords(2, 1).setContent(r);
		teste.getCellByCoords(3, 3).setContent(binaryFunction);
		teste.getCellByCoords(1, 4).setContent(unaryFunction);
		
		james.addSpreadsheets(teste);
		
		docId = bd.getSpreadsheetByName("ulysses").getId();
	}
	
	@Test
	public void success() {
		GetSpreadSheetContentService service = new GetSpreadSheetContentService(ownerToken, docId);
		service.execute();
		matrix = service.getMatrix();
		
		int i = 1;
		int j = 1;
		
		while(i < teste.getNRows()) {
			System.out.print("|");
			while(j < teste.getNCols()) {
				System.out.printf("%16s", matrix[i][j]);
				System.out.printf("%4s", "");
				j++;
			}
			System.out.printf("%4s", "|");
			System.out.println();
			j = 1;
			i++;
		}

		assertEquals("5", service.getMatrix()[1][1]);
		assertEquals("5", service.getMatrix()[2][1]);
		assertEquals("ADD(5,5)", service.getMatrix()[3][3]);
		assertEquals("AVG((1,1)(2,1))", service.getMatrix()[1][4]);
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
