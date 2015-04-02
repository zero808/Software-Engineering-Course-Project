package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class ImportDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String _desc;

	public ImportDocumentException(String desc) {
		_desc = desc;
	}
	
	public String getDesc() {
		return _desc;
	}
	
	@Override
	public String toString() {
		return "Error importing document of " + getDesc() + ".\n"; 
	}
}// End ImportDocumentException class
