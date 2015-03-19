package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

public abstract class BubbleDocsService {

	@Atomic
	public final void execute() throws BubbleDocsException {
		dispatch();
	}
	
	static BubbleDocs getBubbleDocs() {
		return FenixFramework.getDomainRoot().getBubbledocs();
	}
	
	static Spreadsheet getSpreadsheet(int docId) {
		BubbleDocs bd = getBubbleDocs();
		Spreadsheet s = bd.getSpreadsheetById(docId);
		
		if(s == null) {
			throw new SpreadsheetDoesNotExistException();
		}
		
		return s;
	}

	protected abstract void dispatch() throws BubbleDocsException;
}