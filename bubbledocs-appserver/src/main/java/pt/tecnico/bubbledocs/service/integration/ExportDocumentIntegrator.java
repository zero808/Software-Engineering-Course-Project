package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.ExportDocumentException;
import pt.tecnico.bubbledocs.exception.InvalidPermissionException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.ExportDocument;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ExportDocumentIntegrator extends BubbleDocsIntegrator {
	
	private String userToken;
	private int docId;
	
	private StoreRemoteServices storeRemote;
	
	public ExportDocumentIntegrator(String userToken, int docId) {
		this.userToken = userToken;
		this.docId = docId;
		this.storeRemote = new StoreRemoteServices();
	}

	@Override
	protected void dispatch() throws ExportDocumentException, UnavailableServiceException, SpreadsheetDoesNotExistException, InvalidPermissionException, UserNotInSessionException, InvalidTokenException {
		ExportDocument exportDocumentService = new ExportDocument(userToken, docId);
		exportDocumentService.execute();
	}

	public void setStoreRemoteService(StoreRemoteServices storeRemote) {
		this.storeRemote = storeRemote;
	}
	
}// End of ExportDocumentIntegrator class