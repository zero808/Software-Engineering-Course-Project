package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;
import pt.tecnico.bubbledocs.service.ImportDocumentService;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ImportDocumentIntegrator extends BubbleDocsIntegrator {
	
	private String userToken;
	private int docId;
	private byte[] docXML;
	
	private String id;
	private String username;
	
	private Spreadsheet spreadsheet;

	private StoreRemoteServices storeRemote;
	
	public ImportDocumentIntegrator(String userToken, int docId, byte[] docXML) {
		this.userToken = userToken;
		this.docId = docId;
		this.docXML = docXML;
		this.storeRemote = new StoreRemoteServices();
	}

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
	
	public byte[] getDocXML() {
		return docXML;
	}
	
	public Spreadsheet getSpreadsheet() {
		return spreadsheet;
	}
}// End of ImportDocumentIntegrator class