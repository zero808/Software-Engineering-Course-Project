package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.ExportDocumentService;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

/**
 * Class that describes the service that is 
 * responsible for exporting a spreadsheet to XML.
 * 
 * Since this particular service does require
 * a remote call, it does that in the dispatch method
 * after calling the local service.
 */

public class ExportDocumentIntegrator extends BubbleDocsIntegrator {
	
	private byte[] docXML;
	private String userToken;
	private int docId;
	private String id;
	private String username;

	private StoreRemoteServices storeRemote;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {ExportDocumentIntegrator}
	 * 
	 * @param {String} userToken The token of the user that called the service.
	 * @param {number} docId The spreadsheet's id.
	 */
	
	public ExportDocumentIntegrator(String userToken, int docId) {
		/** @private */
		this.userToken = userToken;
		/** @private */
		this.docId = docId;
		/** @private */
		this.storeRemote = new StoreRemoteServices();
	}
	
	/**
	 * This is where the service executes what it
	 * is supposed to do.
	 * 
	 * @throws BubbleDocsException
	 */

	@Override
	public void dispatch() throws BubbleDocsException {
		
		GetUsername4TokenService usernameService = new GetUsername4TokenService(userToken);
		usernameService.execute();
		
		username = usernameService.getUserUsername();
		
		ExportDocumentService exportDocumentService = new ExportDocumentService(userToken, docId);
		exportDocumentService.execute();
		
		id = Integer.toString(exportDocumentService.getDocId());
		docXML = exportDocumentService.getDocXML();
		
		try {
			storeRemote.storeDocument(username, id, docXML);
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
	}
	
	/**
	 * Method that returns the spreadsheet's bytes
	 * that result from the export.
	 * 
	 * @return {Array Bytes} The spreadsheet's bytes.
	 */
	
	public byte[] getDocXML() {
		return docXML;
	}
}// End ExportDocumentIntegrator class
