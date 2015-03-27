package pt.tecnico.bubbledocs.service;

import java.io.UnsupportedEncodingException;

import org.jdom2.output.XMLOutputter;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.ExportDocumentException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class ExportDocument extends BubbleDocsService {
	
	private byte[] docXML;
	private String userToken;
	private int docId;
	
	public ExportDocument(String userToken, int docId) {
		this.userToken = userToken;
		this.docId = docId;
	}

	@Override
	protected void dispatch() throws ExportDocumentException, SpreadsheetDoesNotExistException, InvalidPermissionException, UserNotInSessionException, InvalidTokenException, UserDoesNotExistException {
		BubbleDocs bd = getBubbleDocs();
		org.jdom2.Document jdomDoc = new org.jdom2.Document();

		Spreadsheet spreadsheet = getSpreadsheet(docId);

		String username = bd.getUsernameByToken(userToken);
		
		if(userToken.equals("")) {
			throw new InvalidTokenException();
		}
		
		if(!(bd.isInSession(userToken))) {
			throw new UserNotInSessionException(username);
		}
		
		User user = bd.getUserByUsername(username);

		if(user == null) {
			throw new UserDoesNotExistException();
		}

		if(!(user.hasOwnerPermission(spreadsheet)) || !(user.hasPermission(spreadsheet))) {
			throw new InvalidPermissionException(username);
		}

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