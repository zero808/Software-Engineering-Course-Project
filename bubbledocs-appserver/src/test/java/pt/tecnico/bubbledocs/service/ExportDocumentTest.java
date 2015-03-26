package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.BubbleDocsServiceTest;

public class ExportDocumentTest extends BubbleDocsServiceTest {

	private String userToken;
	//private String rootToken;

	public void populate4Test() {
		BubbleDocs.getInstance();
		User luis = createUser("lf", "woot", "Luis");
		Spreadsheet teste = createSpreadSheet(luis, "teste", 10, 10);
		luis.addSpreadsheets(teste);
		org.jdom2.Document DocTest = new org.jdom2.Document();

		DocTest.setRootElement(teste.exportToXML());
		//TODO Add root and user to session.
	}

	@Test
	public void success() {
		Spreadsheet spreadsheetSucessTest = getSpreadSheet("teste");
		
		ExportDocument service = new ExportDocument(userToken, spreadsheetSucessTest.getId());
		service.execute();
		
		byte[] serviceDocBytes = service.getDocXML();
		
		org.jdom2.Document serviceDoc = new org.jdom2.Document();
		
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		try {
			serviceDoc = builder.build(new ByteArrayInputStream(serviceDocBytes));
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		
		Element serviceRootElement = serviceDoc.getRootElement();
		Spreadsheet serviceSpreadsheet = new Spreadsheet();
		serviceSpreadsheet.importFromXML(serviceRootElement);

		org.jdom2.Document docTest = new org.jdom2.Document();

		docTest.setRootElement(spreadsheetSucessTest.exportToXML());
		
		Element testRootElement = serviceDoc.getRootElement();
		Spreadsheet testSpreadsheet = new Spreadsheet();
		testSpreadsheet.importFromXML(testRootElement);
		
		assertEquals(testSpreadsheet.getName(), serviceSpreadsheet.getName());
		assertEquals(testSpreadsheet.getDate(), serviceSpreadsheet.getDate());
		assertEquals(testSpreadsheet.getUser().getUsername(), serviceSpreadsheet.getUser().getUsername());
		assertEquals(testSpreadsheet.getNRows(), serviceSpreadsheet.getNRows());
		assertEquals(testSpreadsheet.getNCols(), serviceSpreadsheet.getNCols());
	}

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() {
		ExportDocument service = new ExportDocument(userToken, -1); //No spreadsheet should ever have -1 Id.
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
}// End ExportDocumentTest class.