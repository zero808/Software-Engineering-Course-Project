package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class SpreadsheetDoesNotExistException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	public SpreadsheetDoesNotExistException() {
		//Just needs to exist.
	}
	
	@Override
	public String toString() {
		return "The spreadsheet requested does not exist.\n";
	}
}// End SpreadsheetDoesNotExistException class
