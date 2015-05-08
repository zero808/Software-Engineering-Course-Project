package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the service that is 
 * responsible for assigning a literal to 
 * a cell belonging to a specific spreadsheet.
 */

public class AssignLiteralCellService extends AccessService {
	
	private String result;
	private int docId;
	private String cellId;
	private int literal;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {AssignLiteralCellService}
	 * 
	 * @param {String} tokenUser The token of the user that called the service.
	 * @param {number} docId The spreadsheet's id.
	 * @param {String} cellId The cell's identifier (row;column).
	 * @param {String} literal The string representation of a literal.
	 */
	
	public AssignLiteralCellService(String tokenUser, int docId, String cellId, String literal) {
		/** @private */
		token = tokenUser;
		/** @private */
		this.docId = docId;
		/** @private */
		this.literal = Integer.parseInt(literal);
		/** @private */
		this.cellId = cellId;	
	}
	
	/**
	 * This is where the service executes what it
	 * is supposed to do.
	 * 
	 * It's a local service, so it only does local
	 * invocations to the domain layer underneath.
	 * 
	 * @throws BubbleDocsException 
	 */

	@Override
	protected void dispatch() throws BubbleDocsException {
		Spreadsheet s = getSpreadsheet(docId);
		
		String cell_parts[] = cellId.split(";");
		int cellRow = Integer.parseInt(cell_parts[0]);
		int cellCol = Integer.parseInt(cell_parts[1]);
		
		Literal l = new Literal(literal);
		
		user.addLiteraltoCell(l, s, cellRow, cellCol);

		result = getCellByCoords(s, cellRow, cellCol).getContent().toString();
	}
	
	/**
	 * Method that returns the result of the service execution.
	 * 
	 * @return {String} String representation of the literal.
	 */

	public String getResult() {
		return result;
	}
}// End AssignLiteralCellService class
