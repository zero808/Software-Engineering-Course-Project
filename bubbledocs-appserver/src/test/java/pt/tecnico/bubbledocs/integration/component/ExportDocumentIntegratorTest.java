package pt.tecnico.bubbledocs.integration.component;

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

import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exception.ExportDocumentException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.component.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.service.integration.ExportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

/**
 * Class that contains the test suite for the
 * ExportDocumentIntegrator.
 */

public class ExportDocumentIntegratorTest extends BubbleDocsServiceTest {

	@Mocked
	private StoreRemoteServices storeRemote;
	
	private String ownerToken;
	private String notOwnerToken;
	private String notInSessionToken = "antonio6";
	private byte[] expectationsByte;
	
	/**
	 * Method that populates the DB with all
	 * the objects the test suite needs to execute.
	 */

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
	
	/**
	 * Test Case #1 - Success
	 * 
	 * Tests a normal invocation of the service
	 * where nothing goes wrong.
	 * 
	 * Result - SUCCESS
	 */

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
	
	/**
	 * Test Case #2 - NotOwner
	 * 
	 * Tests what happens when the user trying to export isn't the owner.
	 * 
	 * Result - FAILURE - InvalidPermissionException
	 */
	
	@Test(expected = InvalidPermissionException.class)
	public void notOwner() {
		
		Spreadsheet spreadsheetSucessTest = getSpreadSheet("teste");
		
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
		service.execute();
	}
	
	/**
	 * Test Case #3 - SpreadsheetDoesNotExist
	 * 
	 * Tests what happens when the spreadsheet doesn't exist.
	 * 
	 * Result - FAILURE - SpreadsheetDoesNotExistException
	 */

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() {
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(ownerToken, -1); //No spreadsheet should ever have -1 Id.
		service.execute();
	}
	
	/**
	 * Test Case #4 - UserNotInSession
	 * 
	 * Tests what happens when the user calling the service isn't in session.
	 * 
	 * Result - FAILURE - UserNotInSessionException
	 */
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		Spreadsheet spreadsheetTest = getSpreadSheet("teste");
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator(notInSessionToken, spreadsheetTest.getId());
		service.execute();
	}
	
	/**
	 * Test Case #5 - InvalidToken
	 * 
	 * Tests what happens when the user gives an invalid token.
	 * 
	 * Result - FAILURE - InvalidTokenException
	 */
	
	@Test(expected = InvalidTokenException.class)
	public void invalidToken() {
		Spreadsheet spreadsheetTest = getSpreadSheet("teste");
		
		ExportDocumentIntegrator service = new ExportDocumentIntegrator("", spreadsheetTest.getId());
		service.execute();
	}
	
	/**
	 * Test Case #6 - RemoteInvocationFailure
	 * 
	 * Tests what happens when the remote service is down.
	 * 
	 * Result - FAILURE - UnavailableServiceException
	 */
	
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
		service.execute();
	}
	
	/**
	 * Test Case #7 - StoreFailure
	 * 
	 * Tests what happens when the remote service can't store the spreadsheet.
	 * 
	 * Result - FAILURE - CannotStoreDocumentException
	 */
	
	@Test(expected = CannotStoreDocumentException.class)
	public void storeFailure() {
		User luisUser = getUserFromUsername("lff");
		Spreadsheet spreadsheetTest = createSpreadSheet(luisUser, "Store failure", 10, 10);

		ExportDocumentIntegrator service = new ExportDocumentIntegrator(ownerToken, spreadsheetTest.getId());

		new Expectations() {
			{
				storeRemote.storeDocument(withNotNull(), withNotNull(), withNotNull());
				result = new CannotStoreDocumentException();
			}
		};
		service.execute();
	}
}// End ExportDocumentIntegratorTest class
