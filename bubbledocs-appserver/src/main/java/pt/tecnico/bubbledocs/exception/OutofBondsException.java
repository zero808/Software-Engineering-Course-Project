package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class OutofBondsException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private String _spreadsheetName;

	public OutofBondsException(String spreadsheetname) {
		_spreadsheetName = spreadsheetname;
	}
	
	public String getSpreadsheetName() {
		return _spreadsheetName;
	}
}
