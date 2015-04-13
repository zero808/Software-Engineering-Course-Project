package pt.tecnico.bubbledocs.service;

import java.io.UnsupportedEncodingException;

import org.jdom2.output.XMLOutputter;

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
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ExportDocument extends BubbleDocsService {
	
	private byte[] docXML;
	private String userToken;
	private int docId;
	
	private StoreRemoteServices storeRemote = new StoreRemoteServices();
	
	public ExportDocument(String userToken, int docId) {
		this.userToken = userToken;
		this.docId = docId;
	}

	@Override
	protected void dispatch() throws ExportDocumentException, UnavailableServiceException, SpreadsheetDoesNotExistException, InvalidPermissionException, UserNotInSessionException, InvalidTokenException {
		BubbleDocs bd = getBubbleDocs();
		org.jdom2.Document jdomDoc = new org.jdom2.Document();
		
		Spreadsheet spreadsheet = getSpreadsheet(docId);
		
		String spreadsheetName = spreadsheet.getName();

		String username = bd.getUsernameByToken(userToken);
		
		if(userToken.equals("")) {
			throw new InvalidTokenException();
		}
		
		if(!(bd.isInSession(userToken))) {
			throw new UserNotInSessionException(username);
		}
		
		User user = bd.getUserByUsername(username); //Not my responsibility to test if its null, it shouldn't.
		
		if(user.hasOwnerPermission(spreadsheet)) {
			
			jdomDoc.setRootElement(spreadsheet.exportToXML());

			XMLOutputter xml = new XMLOutputter();
			try {
				this.docXML = xml.outputString(jdomDoc).getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new ExportDocumentException(spreadsheet.getName());
			}
			
			//New functionality for 3ª Delivery

			try {
				storeRemote.storeDocument(username, spreadsheetName, docXML);
			} catch (RemoteInvocationException e) {
				throw new UnavailableServiceException();
			}
		}else {
			if(user.hasPermission(spreadsheet)) {
				jdomDoc.setRootElement(spreadsheet.exportToXML());

				XMLOutputter xml = new XMLOutputter();
				try {
					this.docXML = xml.outputString(jdomDoc).getBytes("UTF-8");				
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					throw new ExportDocumentException(spreadsheet.getName());
				}
				
				//New functionality for 3ª Delivery
				
				try {
					storeRemote.storeDocument(username, spreadsheetName, docXML);
				} catch (RemoteInvocationException e) {
					throw new UnavailableServiceException();
				}
			}else {
				throw new InvalidPermissionException(username);
			}
		}
		
	}

	public void setStoreRemoteService(StoreRemoteServices storeRemote) {
		this.storeRemote = storeRemote;
	}
	
	public byte[] getDocXML() {
		return docXML;
	}
}// End of ExportDocument class