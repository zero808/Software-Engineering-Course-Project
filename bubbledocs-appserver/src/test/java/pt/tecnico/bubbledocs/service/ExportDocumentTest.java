package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.jdom2.output.XMLOutputter;
import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserDoesNotHavePermissionException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class ExportDocumentTest extends BubbleDocsServiceTest {

	private String userToken;
	//private String rootToken;

	@Override
	public void populate4Test() {
		User luis = createUser("lf", "woot", "Luis");
		Spreadsheet teste = createSpreadSheet(luis, "teste", 10, 10);
		org.jdom2.Document DocTest = new org.jdom2.Document();

		DocTest.setRootElement(teste.exportToXML());
		//TODO Add root and user to session.
	}

	@Test
	public void success() {
		Spreadsheet sucessTeste = getSpreadSheet("teste");
		
		ExportDocument service = new ExportDocument(userToken, sucessTeste.getId());
		service.execute();
		
		byte[] sucessDocXML = service.getDocXML();
		
		org.jdom2.Document docTest = new org.jdom2.Document();
		byte[] docTestBytes = null;

		docTest.setRootElement(sucessTeste.exportToXML());
		
		XMLOutputter xml = new XMLOutputter();
		
		try {
			docTestBytes = xml.outputString(docTest).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		assertEquals(docTestBytes, sucessDocXML);
	}

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() {
		ExportDocument service = new ExportDocument(userToken, -1); //No spreadsheet should ever have -1 Id.
		service.execute();
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		//TODO Login needs to be implemented.
	}
	
	@Test(expected = UserDoesNotHavePermissionException.class)
	public void userWithoutPermission() {
		//TODO
	}
}// End ExportDocumentTest class.