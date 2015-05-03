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

public class AssignBinaryCellService extends AccessService {

	private String result;
	private int docId;
	private String cellId;
	private String func;

	final private String ZERO = "0";
	final private String NEGATIVE = "-(?!0)\\d+";
	final private String POSITIVE = "(?!0)\\d+";
	final private String LITERAL = "(" + NEGATIVE + "|" + ZERO + "|" + POSITIVE
			+ ")";
	final private String CELL = POSITIVE + ";" + POSITIVE;
	final private String ARGUMENT = "(" + LITERAL + "|" + CELL + ")";
	final private String BINARY_OPERATOR = "(ADD|SUB|MUL|DIV)";
	final private String BINARY_FUNCTION = "=" + BINARY_OPERATOR + "\\("
			+ ARGUMENT + "," + ARGUMENT + "\\)";
	final String PARSE_BINARY_FUNCTION = "[=(,)]";

	public AssignBinaryCellService(String tokenUser, int docId, String cellId,
			String function) {
		token = tokenUser;
		this.docId = docId;
		this.cellId = cellId;
		this.func = function;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		Spreadsheet s = getSpreadsheet(docId);
		String cell_parts[] = cellId.split(";");
		int cellRow = Integer.parseInt(cell_parts[0]);
		int cellCol = Integer.parseInt(cell_parts[1]);

		Binary b = generateFunc(func);

		user.addFunctiontoCell(b, s, cellRow, cellCol);

		try {
			result = Integer.toString(getCellByCoords(s, cellRow, cellCol)
					.getContent().getValue());
		} catch (BubbleDocsException e) {
			result = "#VALUE";
		}

	}

	private Binary generateFunc(String func) {

		if (!Pattern.matches(BINARY_FUNCTION, func))
			throw new InvalidArgumentsException();

		// [0]Operator, [1]arg1, [2]arg2
		String func_parts[] = func.replaceFirst("=", "").split(
				PARSE_BINARY_FUNCTION);

		return parseBinary(func_parts[0], parseArgument(func_parts[1]),
				parseArgument(func_parts[2]));
	}

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

	public String getResult() {
		return result;
	}

}// End AssignBinaryFunctionToCell class
