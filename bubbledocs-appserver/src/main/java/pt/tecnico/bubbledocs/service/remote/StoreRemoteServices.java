package pt.tecnico.bubbledocs.service.remote;

import pt.tecnico.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;

public class StoreRemoteServices {

	public void storeDocument(String username, String docName, byte[] document) throws CannotStoreDocumentException, RemoteInvocationException {
		//Needs to be empty.
	}
	
	public byte[] loadDocument(String username, String docName) throws CannotLoadDocumentException, RemoteInvocationException {
		//Needs to be empty.
		return null;
	}
}// End StoreRemoteServices class
