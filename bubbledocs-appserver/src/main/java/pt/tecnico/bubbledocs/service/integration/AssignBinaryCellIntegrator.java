package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.AssignBinaryCellService;

/**
 * Class that describes the service that is 
 * responsible for assigning a binary function to 
 * a cell belonging to a specific spreadsheet.
 * 
 * Since this particular service doesn't require
 * any remote calls, it simply calls the local service
 * below.
 */

public class AssignBinaryCellIntegrator extends BubbleDocsIntegrator {
	
	private String tokenUser;
	private int docId;
	private String cellId;
	private String func;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {AssignBinaryCellIntegrator}
	 * 
	 * @param {String} tokenUser The token of the user that called the service.
	 * @param {number} docId The spreadsheet's id.
	 * @param {String} cellId The cell's identifier (row;column).
	 * @param {String} func The function's identifier (=FunctionName(Argument 1, Argument 2)).
	 */
	
	public AssignBinaryCellIntegrator(String tokenUser, int docId, String cellId, String func) {
		/** @private */
		this.tokenUser = tokenUser;
		/** @private */
		this.docId = docId;
		/** @private */
		this.func = func;
		/** @private */
		this.cellId = cellId;	
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
		AssignBinaryCellService assignBinaryCellService = new AssignBinaryCellService(tokenUser, docId, cellId, func);
		assignBinaryCellService.execute();
	}
}// End AssignBinaryCellIntegrator class
