package pt.tecnico.bubbledocs.service;

import java.io.UnsupportedEncodingException;

import org.jdom2.output.XMLOutputter;

import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.ExportDocumentException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

public class ExportDocument extends BubbleDocsService {
	
	private byte[] docXML;
	//private String userToken;
	private int docId;
	
	public ExportDocument(String userToken, int docId) {
		//this.userToken = userToken;
		this.docId = docId;
	}

	@Override
	protected void dispatch() throws ExportDocumentException, SpreadsheetDoesNotExistException {
	    org.jdom2.Document jdomDoc = new org.jdom2.Document();
        Spreadsheet spreadsheet = getSpreadsheet(this.docId);
        
        //TODO Check for permissions and session with token.

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
}// End of ExportDocument class.