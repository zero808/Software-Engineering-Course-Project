package pt.tecnico.bubbledocs.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.ImportDocumentException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;

/**
 * Class that describes the service that is 
 * responsible for importing a spreadsheet from XML.
 */

public class ImportDocumentService extends AccessService {
	
	private byte[] docXML;
	private int docId;
	private Spreadsheet spreadsheet;
	private String username;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {ImportDocumentService}
	 * 
	 * @param {String} userToken The token of the user that called the service.
	 * @param {number} docId The spreadsheet's id.
	 * @param {Array Bytes} docXML The spreadsheet's bytes.
	 */
	
	public ImportDocumentService(String userToken, int docId, byte[] docXML) {
		/** @private */
		this.token = userToken;
		/** @private */
		this.docId = docId;
		/** @private */
		this.docXML = docXML;
		
		BubbleDocs bd = getBubbleDocs();
		/** @private */
		this.username = bd.getUsernameByToken(userToken);
	}

	/**
	 * This is where the service executes what it
	 * is supposed to do.
	 * 
	 * It's a local service, so it only does local
	 * invocations to the domain layer underneath.
	 * 
	 * @throws BubbleDocsException
	 */
	
	@Override
	protected void dispatch() throws BubbleDocsException {

		org.jdom2.Document jdomDoc;

		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		try {
			jdomDoc = builder.build(new ByteArrayInputStream(docXML));
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
			throw new ImportDocumentException(username);
		}

		Element rootElement = jdomDoc.getRootElement();
		spreadsheet = new Spreadsheet();
		spreadsheet.importFromXML(rootElement);
	}
	
	/**
	 * Method that returns the spreadsheet after the import.
	 * 
	 * @return {Spreadsheet} The spreadsheet that resulted from
	 * the import.
	 */
	
	public Spreadsheet getSpreadsheet() {
		return spreadsheet;
	}
	
	/**
	 * Method that returns the username of the user that
	 * called the service.
	 * 
	 * @return {String} The user's username.
	 */
	
	public String getUsername() {
		return username;
	}
	
	/**
	 * Method that returns the spreadsheet's id.
	 * 
	 * @return {number} The spreadsheet's id.
	 */
	
	public int getDocId() {
		return docId;
	}
	
	/**
	 * In order to import a spreadsheet one must be
	 * its owner.
	 */
	
	@Override
	public void checkAccess() {
		BubbleDocs bd = getBubbleDocs();
		
		if(bd.getSpreadsheetById(docId).getUser().getUsername() != username) {
			throw new InvalidPermissionException(username);
		}
	}
}// End of ImportDocumentService class
