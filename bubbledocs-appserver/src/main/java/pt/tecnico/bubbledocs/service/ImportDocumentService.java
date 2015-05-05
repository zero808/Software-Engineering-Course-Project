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

public class ImportDocumentService extends AccessService {
	
	private byte[] docXML;
	private int docId;
	
	private Spreadsheet spreadsheet;
	
	private String username;
	
	public ImportDocumentService(String userToken, int docId, byte[] docXML) {		
		this.token = userToken;
		this.docId = docId;
		this.docXML = docXML;
		
		BubbleDocs bd = getBubbleDocs();
		this.username = bd.getUsernameByToken(userToken);
	}

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
	
	public Spreadsheet getSpreadsheet() {
		return spreadsheet;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getDocId() {
		return docId;
	}
	
	@Override
	public void checkAccess() {
		BubbleDocs bd = getBubbleDocs();
		
		if(bd.getSpreadsheetById(docId).getUser().getUsername() != username) {
			throw new InvalidPermissionException(username);
		}
	}
}// End of ImportDocumentService class