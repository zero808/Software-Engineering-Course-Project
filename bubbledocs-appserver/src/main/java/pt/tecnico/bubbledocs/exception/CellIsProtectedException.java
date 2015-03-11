package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class CellIsProtectedException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String _spreadsheetName;

	public CellIsProtectedException(String spreadsheetName) {
		_spreadsheetName = spreadsheetName;
	}
	
	public String getSpreadsheetName() {
		return _spreadsheetName;
	}
}
