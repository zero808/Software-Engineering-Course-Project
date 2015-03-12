package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class CellAlreadyExistsException extends BubbleDocsException {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private int _row;
	private int _collumn;

	public CellAlreadyExistsException(int row, int collumn) {
		_row = row;
		_collumn = collumn;
	}
	
	public int getRow() {
		return _row;
	}
	
	public int getCollumn() {
		return _collumn;
	}
}