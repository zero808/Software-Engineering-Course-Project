package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that abstracts all of the integration services.
 * The integration service layer exists in order to act as a
 * bridge that connects the local service layer and the two remote
 * services, IDRemoteServices and StoreRemoteServices.
 */

public abstract class BubbleDocsIntegrator {

	/**
	 * The execute method that is part of the 
	 * Command design pattern, which is used to implement
	 * the integration service layer.
	 * 
	 * @throws BubbleDocsException
	 */
	
	public void execute() throws BubbleDocsException {
		dispatch();
	}
	
	/**
	 * The dispatch method that is part of the 
	 * Command design pattern, which is used to implement
	 * the integration service layer.
	 * 
	 * This is where each service does what it is supposed to do.
	 * 
	 * @throws BubbleDocsException
	 */

	protected abstract void dispatch() throws BubbleDocsException;
}// End BubbleDocsIntegrator class
