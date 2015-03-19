package pt.tecnico.bubbledocs.exception;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class ExportDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String _desc;

	public ExportDocumentException(String desc) {
		_desc = desc;
	}
	
	public String getDesc() {
		return _desc;
	}
	
	@Override
	public String toString() {
		return "Error exporting document of " + getDesc() + ".\n"; 
	}
}//End ExportDocumentException class.