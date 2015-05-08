package pt.tecnico.bubbledocs.service;

import java.util.regex.Pattern;

import pt.tecnico.bubbledocs.domain.Add;
import pt.tecnico.bubbledocs.domain.Argument;
import pt.tecnico.bubbledocs.domain.Mul;
import pt.tecnico.bubbledocs.domain.Div;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.Sub;
import pt.tecnico.bubbledocs.domain.Binary;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;

/**
 * Class that describes the service that is 
 * responsible for assigning a binary function to 
 * a cell belonging to a specific spreadsheet.
 */

public class AssignBinaryCellService extends AccessService {

	private String result;
	private int docId;
	private String cellId;
	private String func;

	final private String ZERO = "0";
	final private String NEGATIVE = "-(?!0)\\d+";
	final private String POSITIVE = "(?!0)\\d+";
	final private String LITERAL = "(" + NEGATIVE + "|" + ZERO + "|" + POSITIVE + ")";
	final private String CELL = POSITIVE + ";" + POSITIVE;
	final private String ARGUMENT = "(" + LITERAL + "|" + CELL + ")";
	final private String BINARY_OPERATOR = "(ADD|SUB|MUL|DIV)";
	final private String BINARY_FUNCTION = "=" + BINARY_OPERATOR + "\\(" + ARGUMENT + "," + ARGUMENT + "\\)";
	final String PARSE_BINARY_FUNCTION = "[=(,)]";
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {AssignBinaryCellService}
	 * 
	 * @param {String} tokenUser The token of the user that called the service.
	 * @param {number} docId The spreadsheet's id.
	 * @param {String} cellId The cell's identifier (row;column).
	 * @param {String} function The function's identifier (=FunctionName(Argument 1, Argument 2)).
	 */

	public AssignBinaryCellService(String tokenUser, int docId, String cellId, String function) {
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

		Binary b = generateFunc(func);

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
	 * @return {Binary Function} The binary function requested.
	 */
	
	private Binary generateFunc(String func) {

		if (!Pattern.matches(BINARY_FUNCTION, func))
			throw new InvalidArgumentsException();

		//[0]Operator, [1]arg1, [2]arg2.
		String func_parts[] = func.replaceFirst("=", "").split(PARSE_BINARY_FUNCTION);

		return parseBinary(func_parts[0], parseArgument(func_parts[1]),parseArgument(func_parts[2]));
	}
	
	/**
	 * Auxiliary method that parses the various components that 
	 * make up the function identifier.
	 * 
	 * @param {String} string The name of the binary function.
	 * @param {Argument} arg1 The binary function's first argument.
	 * @param {Argument} arg2 The binary function's second argument.
	 * @return {Binary Function} The binary function requested.
	 */

	private Binary parseBinary(String string, Argument arg1, Argument arg2) {

		if (string.equals("ADD"))
			return new Add(arg1, arg2);
		if (string.equals("MUL"))
			return new Mul(arg1, arg2);
		if (string.equals("DIV"))
			return new Div(arg1, arg2);
		if (string.equals("SUB"))
			return new Sub(arg1, arg2);

		return null;
	}
	
	/**
	 * Auxiliary method used to parse the function's arguments.
	 * 
	 * @param {String} string The identifier of the arguments.
	 * @return {Argument} The argument of the binary function.
	 */

	private Argument parseArgument(String string) {

		Argument a = null;

		if (Pattern.matches(CELL, string)) {
			Spreadsheet spreadsheet = getSpreadsheet(docId);
			String cell_parts[] = string.split(";");
			int cellRow = Integer.parseInt(cell_parts[0]);
			int cellCol = Integer.parseInt(cell_parts[1]);
			a = new Reference(getCellByCoords(spreadsheet, cellRow, cellCol));
		} else
			a = new Literal(Integer.parseInt(string));

		return a;
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
}// End AssignBinaryCellService class
