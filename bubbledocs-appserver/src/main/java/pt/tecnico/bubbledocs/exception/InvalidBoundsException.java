package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class InvalidBoundsException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private int row;
	private int col;

	public InvalidBoundsException(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	@Override
	public String toString() {
		return "Invalid bounds passed. Must be between 1 and MAX_INT. Received Row: " + row + " Col: " + col + "\n";
	}
}// End InvalidBoundsException class
