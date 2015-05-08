package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.AssignLiteralCellService;

/**
 * Class that describes the service that is 
 * responsible for assigning a literal to 
 * a cell belonging to a specific spreadsheet.
 * 
 * Since this particular service doesn't require
 * any remote calls, it simply calls the local service
 * below.
 */

public class AssignLiteralCellIntegrator extends BubbleDocsIntegrator {
	
	private String tokenUser;
	private int docId;
	private String cellId;
	private String literal;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {AssignLiteralCellIntegrator}
	 * 
	 * @param {String} tokenUser The token of the user that called the service.
	 * @param {number} docId The spreadsheet's id.
	 * @param {String} cellId The cell's identifier (row;column).
	 * @param {String} literal The string representation of a literal.
	 */
	
	public AssignLiteralCellIntegrator(String tokenUser, int docId, String cellId, String literal) {
		/** @private */
		this.tokenUser = tokenUser;
		/** @private */
		this.docId = docId;
		/** @private */
		this.literal = literal;
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
		AssignLiteralCellService assignLiteralCellService = new AssignLiteralCellService(tokenUser, docId, cellId, literal);
		assignLiteralCellService.execute();
	}
}// End AssignLiteralCellIntegrator class
