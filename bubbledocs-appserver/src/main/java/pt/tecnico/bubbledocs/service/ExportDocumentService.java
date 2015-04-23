package pt.tecnico.bubbledocs.service;

import java.io.UnsupportedEncodingException;

import org.jdom2.output.XMLOutputter;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.ExportDocumentException;

public class ExportDocumentService extends AccessService {
	
	private byte[] docXML;
	private int docId;
	
	private String username;
	
	public ExportDocumentService(String userToken, int docId) {
		this.token = userToken;
		this.docId = docId;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = getBubbleDocs();
		
		org.jdom2.Document jdomDoc = new org.jdom2.Document();
		
		Spreadsheet spreadsheet = getSpreadsheet(docId);

		username = bd.getUsernameByToken(token);
		
		jdomDoc.setRootElement(spreadsheet.exportToXML());

		XMLOutputter xml = new XMLOutputter();
		
		try {
			this.docXML = xml.outputString(jdomDoc).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ExportDocumentException(spreadsheet.getName());
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
}// End of ExportDocument class