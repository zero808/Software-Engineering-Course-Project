package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class LoginBubbleDocsException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private String _desc;

	public LoginBubbleDocsException(String desc) {
		_desc = desc;
	}
	
	public String getDesc() {
		return _desc;
	}
	
	@Override
	public String toString() {
		return "The " + getDesc() + " given is invalid.\n";
	}
}// End LoginBubbleDocsException class