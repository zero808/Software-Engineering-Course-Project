package pt.tecnico.bubbledocs.service.remote;

import pt.tecnico.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;

/**
 * Class that describes the remote service StoreRemoteServices.
 * In this project in particular this object will be mocked.
 * Because of this, all of the methods here need to be empty.
 */

public class StoreRemoteServices {
	
	/**
	 * Method used to store a spreadsheet remotely.
	 * 
	 * @param {String} username The user's username.
	 * @param {String} docName String representation of the spreadsheet's id.
	 * @param {Array Bytes} document The bytes of the spreadsheet to be stored.
	 * @throws CannotStoreDocumentException, RemoteInvocationException
	 */

	public void storeDocument(String username, String docName, byte[] document) throws CannotStoreDocumentException, RemoteInvocationException {
		//Needs to be empty.
	}
	
	/**
	 * Method used to load a spreadsheet from the remote service.
	 * 
	 * @param {String} username The user's username.
	 * @param {String} docName String representation of the spreadsheet's id.
	 * @throws CannotLoadDocumentException, RemoteInvocationException
	 * @return {Array Bytes} The bytes of the spreadsheet requested.
	 */
	
	public byte[] loadDocument(String username, String docName) throws CannotLoadDocumentException, RemoteInvocationException {
		//Needs to be empty.
		return null;
	}
}// End StoreRemoteServices class
