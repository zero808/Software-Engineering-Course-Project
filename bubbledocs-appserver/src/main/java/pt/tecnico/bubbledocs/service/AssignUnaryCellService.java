package pt.tecnico.bubbledocs.service;

import java.util.regex.Pattern;

import pt.tecnico.bubbledocs.domain.Avg;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Prd;
import pt.tecnico.bubbledocs.domain.Range;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.Unary;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

/**
 * Class that describes the service that is 
 * responsible for assigning a unary function to 
 * a cell belonging to a specific spreadsheet.
 */

public class AssignUnaryCellService extends AccessService {

	private String result;
	private int docId;
	private String cellId;
	private String func;

	final private String POSITIVE = "(?!0)\\d+";
	final private String CELL = POSITIVE + ";" + POSITIVE;
	final private String RANGE = CELL + ":" + CELL;
	final private String RANGE_OPERATOR = "(AVG|PRD)";
	final private String RANGE_FUNCTION = "=" + RANGE_OPERATOR + "\\(" + RANGE + "\\)";
	final private String PARSE_RANGE_FUNCTION = "[=(:)]";
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {AssignUnaryCellService}
	 * 
	 * @param {String} tokenUser The token of the user that called the service.
	 * @param {number} docId The spreadsheet's id.
	 * @param {String} cellId The cell's identifier (row;column).
	 * @param {String} function The function's identifier (=FunctionName(row;column:row;column)).
	 */

	public AssignUnaryCellService(String tokenUser, int docId, String cellId, String function) {
		/** @private */
		token = tokenUser;
		/** @private */
		this.docId = docId;
		/** @private */
		this.cellId = cellId;
		/** @private */
		this.func = function;
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

		Unary b = generateFunc(func);

		user.addFunctiontoCell(b, s, cellRow, cellCol);

		try {
			result = Integer.toString(getCellByCoords(s, cellRow, cellCol).getContent().getValue());
		} catch (BubbleDocsException e) {
			result = "#VALUE";
		}
	}
	
	/**
	 * Auxiliary method used by the service to create the right
	 * binary function, depending on the argument given.
	 * 
	 * @param {String} func The function identifier.
	 * @return {Unary Function} The unary function requested.
	 */

	private Unary generateFunc(String func) {

		if (!Pattern.matches(RANGE_FUNCTION, func))
			throw new InvalidArgumentsException();

		//[0]Operator, [1]arg1, [2]arg2.
		String func_parts[] = func.replaceFirst("=", "").split(PARSE_RANGE_FUNCTION);

		return parseUnary(func_parts[0], parseRange(func_parts[1], func_parts[2]));
	}
	
	/**
	 * Auxiliary method used by the service to create the range of
	 * cells that the function is supposed to use.
	 * 
	 * @param {String} start The range's starting cell, in string form.
	 * @param {String} end The range's ending cell, in string form.
	 * @return {Range} The requested range.
	 */

	private Range parseRange(String start, String end) {
		Range a = null;
		Spreadsheet spreadsheet = getSpreadsheet(docId);
		Cell startCell, endCell;
		
		if (Pattern.matches(CELL, start) && Pattern.matches(CELL, end)) {
			String start_cell_parts[] = start.split(";");
			int startCellRow = Integer.parseInt(start_cell_parts[0]);
			int startCellCol = Integer.parseInt(start_cell_parts[1]);
			startCell = getCellByCoords(spreadsheet, startCellRow, startCellCol);
			
			String end_cell_parts[] = end.split(";");
			int endCellRow = Integer.parseInt(end_cell_parts[0]);
			int endCellCol = Integer.parseInt(end_cell_parts[1]);
			endCell = getCellByCoords(spreadsheet, endCellRow, endCellCol);
			
			a = new Range(startCell, endCell);
		} else
			throw new InvalidArgumentsException();

		return a;
	}
	
	/**
	 * Auxiliary method that parses the various components that 
	 * make up the function identifier.
	 * 
	 * @param {String} string The name of the unary function.
	 * @param {Range} r The unary function's only argument.
	 * @return {Unary Function} The unary function requested.
	 */

	private Unary parseUnary(String string, Range r) {

		if (string.equals("AVG"))
			return new Avg(r);
		if (string.equals("PRD"))
			return new Prd(r);

		return null;
	}
	
	/**
	 * Method that returns the result of the service execution.
	 * 
	 * @return {String} String representation of the function's return value,
	 * #VALUE if invalid arguments are passed.
	 */

	public String getResult() {
		return result;
	}
}// End AssignUnaryCellService class
