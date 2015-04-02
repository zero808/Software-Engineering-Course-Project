package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class DuplicateEmailException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String _email;

	public DuplicateEmailException(String email) {
		_email = email;
	}
	
	public String getEmail() {
		return _email;
	}
	
	@Override
	public String toString() {
		return "The email " + getEmail() + " already belongs to another user in BubbleDocs.\n";
	}
}// End DuplicateEmailException class