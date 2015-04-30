package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.ExportDocumentService;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ExportDocumentIntegrator extends BubbleDocsIntegrator {
	
	private byte[] docXML;
	private String userToken;
	private int docId;
	
	private String id;
	private String username;

	private StoreRemoteServices storeRemote;
	
	public ExportDocumentIntegrator(String userToken, int docId) {
		this.userToken = userToken;
		this.docId = docId;
		this.storeRemote = new StoreRemoteServices();
	}

	@Override
	public void dispatch() throws BubbleDocsException {
		
		ExportDocumentService exportDocumentService = new ExportDocumentService(userToken, docId);
		exportDocumentService.execute();
		
		username = exportDocumentService.getUsername();
		id = Integer.toString(exportDocumentService.getDocId());
		docXML = exportDocumentService.getDocXML();
		
		try {
			storeRemote.storeDocument(username, id, docXML);
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
	}
	
	public byte[] getDocXML() {
		return docXML;
	}
}// End of ExportDocumentIntegrator class