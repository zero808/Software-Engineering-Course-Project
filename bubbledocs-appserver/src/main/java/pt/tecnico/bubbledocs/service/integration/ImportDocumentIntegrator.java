package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;
import pt.tecnico.bubbledocs.service.ImportDocumentService;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

/**
 * Class that describes the service that is 
 * responsible for importing a spreadsheet from XML.
 * 
 * Since this particular service does require
 * a remote call, it does that in the dispatch method
 * before calling the local service.
 */

public class ImportDocumentIntegrator extends BubbleDocsIntegrator {
	
	private String userToken;
	private int docId;
	private byte[] docXML;
	private String id;
	private String username;
	private Spreadsheet spreadsheet;

	private StoreRemoteServices storeRemote;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {ImportDocumentIntegrator}
	 * 
	 * @param {String} userToken The token of the user that called the service.
	 * @param {number} docId The spreadsheet's id.
	 * @param {Array Bytes} docXML The spreadsheet's bytes.
	 */
	
	public ImportDocumentIntegrator(String userToken, int docId, byte[] docXML) {
		/** @private */
		this.userToken = userToken;
		/** @private */
		this.docId = docId;
		/** @private */
		this.docXML = docXML;
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
		
		id = Integer.toString(docId);
	
		try {
			
			storeRemote.loadDocument(username, id);	
			
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
		
		ImportDocumentService importDocumentService = new ImportDocumentService(userToken, docId, docXML);
		importDocumentService.execute();
		
		spreadsheet = importDocumentService.getSpreadsheet();
	}
	
	/**
	 * Method that returns the spreadsheet's bytes
	 * that are used in the import.
	 * 
	 * @return {Array Bytes} The spreadsheet's bytes.
	 */
	
	public byte[] getDocXML() {
		return docXML;
	}
	
	/**
	 * Method that returns the spreadsheet resulted from
	 * the import.
	 * 
	 * @return {Spreadsheet} The spreadsheet that resulted from the import.
	 */
	
	public Spreadsheet getSpreadsheet() {
		return spreadsheet;
	}
}// End ImportDocumentIntegrator class
