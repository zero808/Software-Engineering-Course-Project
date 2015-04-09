package pt.tecnico.bubbledocs.service.remote;

import pt.tecnico.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;

public class StoreRemoteServices {

	public void storeDocument(String username, String docName, byte[] document) throws CannotStoreDocumentException, RemoteInvocationException {
		
		if(docName.equals("Store failure")) {
			throw new CannotStoreDocumentException(); //For testing purposes.
		}
		
		if(docName.equals("Remote invocation failure")) {
			throw new RemoteInvocationException(); //For testing purposes.
		}
	}
	
	public byte[] loadDocument(String username, String docName) throws CannotLoadDocumentException, RemoteInvocationException {
		
		if(docName.equals("Load failure")) {
			throw new CannotLoadDocumentException(); //For testing purposes.
		}
		
		if(docName.equals("Remote invocation failure")) {
			throw new RemoteInvocationException(); //For testing purposes.
		}
		
		return null;
	}
}// End StoreRemoteServices class
