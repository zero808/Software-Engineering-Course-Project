package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.AssignReferenceCellService;

/**
 * Class that describes the service that is 
 * responsible for assigning a reference to 
 * a cell belonging to a specific spreadsheet.
 * 
 * Since this particular service doesn't require
 * any remote calls, it simply calls the local service
 * below.
 */

public class AssignReferenceCellIntegrator extends BubbleDocsIntegrator {
	
	private String cellId;
	private String reference;
	private int docId;
	private String tokenUser;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {AssignReferenceCellIntegrator}
	 * 
	 * @param {String} tokenUser The token of the user that called the service.
	 * @param {number} docId The spreadsheet's id.
	 * @param {String} cellId The cell's identifier (row;column).
	 * @param {String} reference The string representation of a reference (row;column).
	 */

	public AssignReferenceCellIntegrator(String tokenUser, int docId, String cellId, String reference) {
		/** @private */
		this.docId = docId;
		/** @private */
		this.tokenUser = tokenUser;
		/** @private */
		this.cellId = cellId;
		/** @private */
		this.reference = reference;
	}
	
	/**
	 * This is where the service executes what it
	 * is supposed to do.
	 * 
	 * Since it doesn't require any remote calls
	 * it simply calls the local service.
	 * 
	 * @throws BubbleDocsException
	 */

	@Override
	protected void dispatch() throws BubbleDocsException {
		AssignReferenceCellService assignReferenceCellService = new AssignReferenceCellService(tokenUser, docId, cellId, reference);
		assignReferenceCellService.execute();
	}
}// End AssignReferenceCellIntegrator class
