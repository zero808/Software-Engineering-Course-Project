package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class OutofBondsException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private int _rows;
	private int _collumns;

	public OutofBondsException(int rows, int collumns) {
		_rows = rows;
		_collumns = collumns;
	}
	
	public int getRows() {
		return _rows;
	}
	
	public int getCollumns() {
		return _collumns;
	}
	
	@Override
	public String toString() {
		return "Out of spreadsheet bonds -> " + "(" + getRows() + ", " + getCollumns() + ")";
	}
}// End OutofBondsException class.
