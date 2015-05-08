package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the service that is 
 * responsible for assigning a reference to 
 * a cell belonging to a specific spreadsheet.
 */

public class AssignReferenceCellService extends AccessService {
	
	private String result;
	private int cell_row;
	private int cell_collumn;
	private int reference_row;
	private int reference_collumn;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {AssignReferenceCellService}
	 * 
	 * @param {String} tokenUser The token of the user that called the service.
	 * @param {number} docId The spreadsheet's id.
	 * @param {String} cellId The cell's identifier (row;column).
	 * @param {String} reference The string representation of a reference (row;column).
	 */

	public AssignReferenceCellService(String tokenUser, int docId, String cellId, String reference) {
		String cell_parts[] = cellId.split(";");
		String cell_row = cell_parts[0];
		String cell_collumn = cell_parts[1];
		int cell_row_int = Integer.parseInt(cell_row);
		int cell_collumn_int = Integer.parseInt(cell_collumn);
		
		String reference_parts[] = reference.split(";");
		String reference_row = reference_parts[0];
		String reference_collumn = reference_parts[1];
		int reference_row_int = Integer.parseInt(reference_row);
		int reference_collumn_int = Integer.parseInt(reference_collumn);
		
		/** @private */
		this.docId = docId;
		/** @private */
		this.token = tokenUser;
		/** @private */
		this.cell_row = cell_row_int;
		/** @private */
		this.cell_collumn = cell_collumn_int;
		/** @private */
		this.reference_row = reference_row_int;
		/** @private */
		this.reference_collumn = reference_collumn_int;
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
		Spreadsheet spreadsheet = getSpreadsheet(docId);
		
		Cell referenced_cell = new Cell(reference_row, reference_collumn, false);
		Reference reference = new Reference(referenced_cell);
		
		user.addReferencetoCell(reference, spreadsheet, cell_row, cell_collumn);

		result = getCellByCoords(spreadsheet, cell_row, cell_collumn).getContent().toString();
	}
	
	/**
	 * Method that returns the result of the service execution.
	 * 
	 * @return {String} String representation of the reference.
	 */

	public final String getResult() {
		return result;
	}
}// End AssignReferenceCellService class
