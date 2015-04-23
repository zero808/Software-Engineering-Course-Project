package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import mockit.Expectations;
import mockit.Mocked;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.ExportDocumentException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.service.integration.ExportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ExportDocumentIntegratorTest extends BubbleDocsServiceTest {

	@Mocked
	private StoreRemoteServices storeRemote;
	
	private String ownerToken;
	private String notOwnerToken;
	private String notInSessionToken = "antonio6";
	private byte[] expectationsByte;

	@Override
	public void populate4Test() {
		getBubbleDocs();
		
		User luis = createUser("lff", "woot", "Luis");
		this.ownerToken = addUserToSession("lff");
		
		User ze = createUser("zzz", "pass", "Jose");
		this.notOwnerToken = addUserToSession("zzz");
		
		Spreadsheet teste = createSpreadSheet(luis, "teste", 10, 10);
		luis.addSpreadsheets(teste);
		
		luis.givePermissionto(getSpreadSheet("teste"), ze, true);
	}

	@Test
	public void success() {
		Spreadsheet spreadsheetSucessTest = getSpreadSheet("teste");
		
		String docId = Integer.toString(spreadsheetSucessTest.getId());
		String username = spreadsheetSucessTest.getUser().getUsername();
		
		org.jdom2.Document jdomDoc = new org.jdom2.Document();
		
		jdomDoc.setRootElement(spreadsheetSucessTest.exportToXML());
		
		XMLOutputter xml = new XMLOutputter();	
		try {
			this.expectationsByte = xml.outputString(jdomDoc).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ExportDocumentException(spreadsheetSucessTest.getName());
		}
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(ownerToken, spreadsheetSucessTest.getId());
		
		new Expectations() {
			{
				storeRemote.storeDocument(username, docId, expectationsByte);
			}
		};
		service.setStoreRemoteService(storeRemote);
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

		docTest.setRootElement(serviceSpreadsheet.exportToXML());
		
		Element testRootElement = docTest.getRootElement();
		Spreadsheet testSpreadsheet = new Spreadsheet();
		testSpreadsheet.importFromXML(testRootElement);
		
		assertEquals(testSpreadsheet.getName(), serviceSpreadsheet.getName());
		assertEquals(testSpreadsheet.getDate(), serviceSpreadsheet.getDate());
		assertEquals(testSpreadsheet.getUser().getUsername(), serviceSpreadsheet.getUser().getUsername());
		assertEquals(testSpreadsheet.getNRows(), serviceSpreadsheet.getNRows());
		assertEquals(testSpreadsheet.getNCols(), serviceSpreadsheet.getNCols());
	}
	
	@Test
	public void successNotOwner() {
		BubbleDocs bd = getBubbleDocs();
		
		Spreadsheet spreadsheetSucessTest = getSpreadSheet("teste");
		String notOwnerUsername = bd.getUsernameByToken(notOwnerToken);
		
		String docId = Integer.toString(spreadsheetSucessTest.getId());
		
		org.jdom2.Document jdomDoc = new org.jdom2.Document();
		
		jdomDoc.setRootElement(spreadsheetSucessTest.exportToXML());
		
		XMLOutputter xml = new XMLOutputter();	
		try {
			this.expectationsByte = xml.outputString(jdomDoc).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ExportDocumentException(spreadsheetSucessTest.getName());
		}
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(notOwnerToken, spreadsheetSucessTest.getId());
		
		new Expectations() {
			{
				storeRemote.storeDocument(notOwnerUsername, docId, expectationsByte);
			}
		};
		service.setStoreRemoteService(storeRemote);
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

		docTest.setRootElement(serviceSpreadsheet.exportToXML());

		Element testRootElement = docTest.getRootElement();
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
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(ownerToken, -1); //No spreadsheet should ever have -1 Id.
		service.setStoreRemoteService(storeRemote);
		service.execute();
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		Spreadsheet spreadsheetTest = getSpreadSheet("teste");
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(notInSessionToken, spreadsheetTest.getId());
		service.setStoreRemoteService(storeRemote);
		service.execute();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void invalidToken() {
		Spreadsheet spreadsheetTest = getSpreadSheet("teste");
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator("", spreadsheetTest.getId());
		service.setStoreRemoteService(storeRemote);
		service.execute();
	}
	
	@Test(expected = InvalidPermissionException.class)
	public void userNotOwner() {
		Spreadsheet spreadsheetTest = getSpreadSheet("teste");
		User luisUser = getUserFromUsername("lff");
		User zeUser = getUserFromUsername("zzz");

		luisUser.removePermissionfrom(spreadsheetTest, zeUser);
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(notOwnerToken, spreadsheetTest.getId());
		service.setStoreRemoteService(storeRemote);
		service.execute();
	}
	
	@Test(expected = InvalidPermissionException.class)
	public void userWithoutPermission() {
		Spreadsheet spreadsheetTest = getSpreadSheet("teste");
		User luisUser = getUserFromUsername("lff");
		User zeUser = getUserFromUsername("zzz");

		luisUser.removePermissionfrom(spreadsheetTest, zeUser);

		ExportDocumentIntegrator service = new ExportDocumentIntegrator(notOwnerToken, spreadsheetTest.getId());
		service.setStoreRemoteService(storeRemote);
		service.execute();
	}
	
	@Test(expected = UnavailableServiceException.class)
	public void remoteInvocationFailure() {
		User luisUser = getUserFromUsername("lff");
		Spreadsheet spreadsheetTest = createSpreadSheet(luisUser, "Remote invocation failure", 10, 10);
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(ownerToken, spreadsheetTest.getId());
		
		new Expectations() {
			{
				storeRemote.storeDocument(withNotNull(), withNotNull(), withNotNull());
				result = new RemoteInvocationException();
			}
		};
		service.setStoreRemoteService(storeRemote);
		service.execute();
	}
}// End ExportDocumentTest class