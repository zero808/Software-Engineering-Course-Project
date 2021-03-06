package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import mockit.Expectations;
import mockit.Mocked;

import org.jdom2.output.XMLOutputter;
import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exception.ExportDocumentException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.component.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.service.integration.ImportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

/**
 * Class that contains the test suite for the
 * ImportDocumentIntegrator.
 */

public class ImportDocumentIntegratorTest extends BubbleDocsServiceTest {

	@Mocked
	private StoreRemoteServices storeRemote;
	
	private String ownerToken;
	private String notOwnerToken;
	private String notInSessionToken = "antonio6";
	
	private byte[] docXML;
	
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
			docXML = xml.outputString(jdomDoc).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ExportDocumentException(spreadsheetSucessTest.getUser().getUsername());
		}
		
		ImportDocumentIntegrator service = new ImportDocumentIntegrator(ownerToken, spreadsheetSucessTest.getId(), docXML);
		
		new Expectations() {
			{
				storeRemote.loadDocument(username, docId);
				result = docXML;
			}
		};
		service.execute();
		
		Spreadsheet serviceSpreadsheet = service.getSpreadsheet();
		
		for(Cell originCell : serviceSpreadsheet.getCellsSet()) {
			if(originCell.getContent() != null) {
				assertEquals(spreadsheetSucessTest.getCellByCoords(originCell.getRow(), originCell.getCollumn()).getContent().getValue(), serviceSpreadsheet.getCellByCoords(originCell.getRow(), originCell.getCollumn()).getContent().getValue());
			}	
		}
		
		assertEquals(spreadsheetSucessTest.getName(), serviceSpreadsheet.getName());
		assertEquals(spreadsheetSucessTest.getDate(), serviceSpreadsheet.getDate());
		assertEquals(spreadsheetSucessTest.getUser().getUsername(), serviceSpreadsheet.getUser().getUsername());
		assertEquals(spreadsheetSucessTest.getNRows(), serviceSpreadsheet.getNRows());
		assertEquals(spreadsheetSucessTest.getNCols(), serviceSpreadsheet.getNCols());
	}
	
	/**
	 * Test Case #2 - NotOwner
	 * 
	 * Tests what happens when the user trying to import isn't the owner.
	 * 
	 * Result - FAILURE - InvalidPermissionException
	 */
	
	@Test(expected = InvalidPermissionException.class)
	public void notOwner() {
		Spreadsheet spreadsheetTest = getSpreadSheet("teste");
	
		org.jdom2.Document jdomDoc = new org.jdom2.Document();

		jdomDoc.setRootElement(spreadsheetTest.exportToXML());

		XMLOutputter xml = new XMLOutputter();

		try {
			docXML = xml.outputString(jdomDoc).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ExportDocumentException(spreadsheetTest.getUser().getUsername());
		}

		ImportDocumentIntegrator service = new ImportDocumentIntegrator(notOwnerToken, spreadsheetTest.getId(), docXML);
		service.execute();
	}
	
	/**
	 * Test Case #3 - UserNotInSession
	 * 
	 * Tests what happens when the user calling the service isn't in session.
	 * 
	 * Result - FAILURE - UserNotInSessionException
	 */
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		Spreadsheet spreadsheetTest = getSpreadSheet("teste");
		
		org.jdom2.Document jdomDoc = new org.jdom2.Document();

		jdomDoc.setRootElement(spreadsheetTest.exportToXML());

		XMLOutputter xml = new XMLOutputter();

		try {
			docXML = xml.outputString(jdomDoc).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ExportDocumentException(spreadsheetTest.getUser().getUsername());
		}
		
		ImportDocumentIntegrator service = new ImportDocumentIntegrator(notInSessionToken, spreadsheetTest.getId(), docXML);
		service.execute();
	}
	
	/**
	 * Test Case #4 - InvalidToken
	 * 
	 * Tests what happens when the user gives an invalid token.
	 * 
	 * Result - FAILURE - InvalidTokenException
	 */
	
	@Test(expected = InvalidTokenException.class)
	public void invalidToken() {
		Spreadsheet spreadsheetTest = getSpreadSheet("teste");
		
		org.jdom2.Document jdomDoc = new org.jdom2.Document();

		jdomDoc.setRootElement(spreadsheetTest.exportToXML());

		XMLOutputter xml = new XMLOutputter();

		try {
			docXML = xml.outputString(jdomDoc).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ExportDocumentException(spreadsheetTest.getUser().getUsername());
		}
		
		ImportDocumentIntegrator service = new ImportDocumentIntegrator("", spreadsheetTest.getId(), docXML);
		service.execute();
	}
	
	/**
	 * Test Case #5 - RemoteInvocationFailure
	 * 
	 * Tests what happens when the remote service is down.
	 * 
	 * Result - FAILURE - UnavailableServiceException
	 */
	
	@Test(expected = UnavailableServiceException.class)
	public void remoteInvocationFailure() {
		User luisUser = getUserFromUsername("lff");
		Spreadsheet spreadsheetTest = createSpreadSheet(luisUser, "Remote invocation failure", 10, 10);
		
		org.jdom2.Document jdomDoc = new org.jdom2.Document();

		jdomDoc.setRootElement(spreadsheetTest.exportToXML());

		XMLOutputter xml = new XMLOutputter();

		try {
			docXML = xml.outputString(jdomDoc).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ExportDocumentException(spreadsheetTest.getUser().getUsername());
		}
		
		ImportDocumentIntegrator service = new ImportDocumentIntegrator(ownerToken, spreadsheetTest.getId(), docXML);
		
		new Expectations() {
			{
				storeRemote.loadDocument(withNotNull(), withNotNull());
				result = new RemoteInvocationException();
			}
		};
		service.execute();
	}
	
	/**
	 * Test Case #6 - LoadFailure
	 * 
	 * Tests what happens when the remote service can't load the spreadsheet.
	 * 
	 * Result - FAILURE - CannotLoadDocumentException
	 */
	
	@Test(expected = CannotLoadDocumentException.class)
	public void loadFailure() {
		User luisUser = getUserFromUsername("lff");
		Spreadsheet spreadsheetTest = createSpreadSheet(luisUser, "Load failure", 10, 10);
		
		org.jdom2.Document jdomDoc = new org.jdom2.Document();

		jdomDoc.setRootElement(spreadsheetTest.exportToXML());

		XMLOutputter xml = new XMLOutputter();

		try {
			docXML = xml.outputString(jdomDoc).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ExportDocumentException(spreadsheetTest.getUser().getUsername());
		}

		ImportDocumentIntegrator service = new ImportDocumentIntegrator(ownerToken, spreadsheetTest.getId(), docXML);

		new Expectations() {
			{
				storeRemote.loadDocument(withNotNull(), withNotNull());
				result = new CannotLoadDocumentException();
			}
		};
		service.execute();
	}
}// End ImportDocumentIntegratorTest class
