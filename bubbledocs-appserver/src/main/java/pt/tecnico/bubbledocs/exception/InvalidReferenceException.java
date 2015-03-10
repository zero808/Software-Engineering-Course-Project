package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class InvalidReferenceException extends BubbleDocsException {
	
	/**
	 */
	private static final long serialVersionUID = 1L;

	private String _spreadsheetName;

	public InvalidReferenceException(String spreadsheetname) {
		_spreadsheetName = spreadsheetname;
	}
	
	public String getSpreadsheetName() {
		return _spreadsheetName;
	}

}