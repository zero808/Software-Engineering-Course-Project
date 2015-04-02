package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class InvalidUsernameException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private String _desc;

	public InvalidUsernameException(String desc) {
		_desc = desc;
	}
	
	public String getDesc() {
		return _desc;
	}
	
	@Override
	public String toString() {
		return "Invalid Username because " + getDesc() + "\n";
	}
}// End InvalidUsernameException class
