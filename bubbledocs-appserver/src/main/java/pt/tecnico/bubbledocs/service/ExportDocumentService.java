package pt.tecnico.bubbledocs.service;

import java.io.UnsupportedEncodingException;

import org.jdom2.output.XMLOutputter;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.ExportDocumentException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;

public class ExportDocumentService extends AccessService {
	
	private byte[] docXML;
	private int docId;
	
	private String username;
	
	public ExportDocumentService(String userToken, int docId) {
		this.token = userToken;
		this.docId = docId;
		
		BubbleDocs bd = getBubbleDocs();
		this.username = bd.getUsernameByToken(token);
	}

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

	@Override
	public void checkAccess() {
		Spreadsheet s = getSpreadsheet(docId);
		
		if(s.getUser().getUsername() != username) {
			throw new InvalidPermissionException(username);
		}
	}
	
	public byte[] getDocXML() {
		return docXML;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getDocId() {
		return docId;
	}
}// End of ExportDocumentService class