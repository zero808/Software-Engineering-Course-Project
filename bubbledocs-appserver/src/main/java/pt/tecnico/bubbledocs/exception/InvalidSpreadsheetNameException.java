package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class InvalidSpreadsheetNameException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	public InvalidSpreadsheetNameException() {
		//Just needs to exist.
	}
	
	@Override
	public String toString() {
		return "Invalid Spreadsheet name passed";
	}
}// End InvalidSpreadsheetNameException class
