package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

public abstract class BubbleDocsService {

	@Atomic
	public final void execute() throws BubbleDocsException {
		dispatch();
	}
	
	protected static BubbleDocs getBubbleDocs() {
		return BubbleDocs.getInstance();
	}
	
	protected static Spreadsheet getSpreadsheet(int docId) throws SpreadsheetDoesNotExistException {
		BubbleDocs bd = getBubbleDocs();
		Spreadsheet s = bd.getSpreadsheetById(docId);
		
		if(s == null) {
			throw new SpreadsheetDoesNotExistException();
		}
		
		return s;
	}
	
	protected static Cell getCellByCoords(Spreadsheet s, int row, int collumn) throws OutofBoundsException {
		Cell c = s.getCellByCoords(row, collumn);
		if(c == null) {
			throw new OutofBoundsException(row, collumn);
		}
		
		return c;
	}

	protected abstract void dispatch() throws BubbleDocsException;
}// End BubbleDocsService class