package pt.tecnico.bubbledocs.service;

import java.io.UnsupportedEncodingException;

import org.jdom2.output.XMLOutputter;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.ExportDocumentException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;

/**
 * Class that describes the service that is 
 * responsible for exporting a spreadsheet to XML.
 */

public class ExportDocumentService extends AccessService {
	
	private byte[] docXML;
	private int docId;
	private String username;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {ExportDocumentService}
	 * 
	 * @param {String} userToken The token of the user that called the service.
	 * @param {number} docId The spreadsheet's id.
	 */
	
	public ExportDocumentService(String userToken, int docId) {
		/** @private */
		this.token = userToken;
		/** @private */
		this.docId = docId;
		
		BubbleDocs bd = getBubbleDocs();
		/** @private */
		this.username = bd.getUsernameByToken(token);
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
		
		org.jdom2.Document jdomDoc = new org.jdom2.Document();
		
		Spreadsheet spreadsheet = getSpreadsheet(docId);
		
		jdomDoc.setRootElement(spreadsheet.exportToXML());
		
		jdomDoc.getRootElement().setAttribute("owner", username);

		XMLOutputter xml = new XMLOutputter();
		
		try {
			this.docXML = xml.outputString(jdomDoc).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ExportDocumentException(username);
		}
	}
	
	/**
	 * In order to export a spreadsheet one must be
	 * its owner.
	 */

	@Override
	public void checkAccess() {
		Spreadsheet s = getSpreadsheet(docId);
		
		if(s.getUser().getUsername() != username) {
			throw new InvalidPermissionException(username);
		}
	}
	
	/**
	 * Method that returns the bytes that result
	 * from the export.
	 * 
	 * @return {Array Bytes} The spreadsheet's bytes.
	 */
	
	public byte[] getDocXML() {
		return docXML;
	}
	
	/**
	 * Method that returns the username of the
	 * user calling the service.
	 * 
	 * @return {String} The user's username.
	 */
	
	public String getUsername() {
		return username;
	}
	
	/**
	 * Method that returns the id of the spreadsheet
	 * to export.
	 * 
	 * @return {number} The spreadsheet's id.
	 */
	
	public int getDocId() {
		return docId;
	}
}// End of ExportDocumentService class
