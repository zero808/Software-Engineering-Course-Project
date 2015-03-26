package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.OutofBondsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;

public abstract class BubbleDocsService {

	@Atomic
	public final void execute() throws BubbleDocsException {
		dispatch();
	}
	
	static BubbleDocs getBubbleDocs() {
		return FenixFramework.getDomainRoot().getBubbledocs();
	}
	
	static Spreadsheet getSpreadsheet(int docId) throws SpreadsheetDoesNotExistException {
		BubbleDocs bd = getBubbleDocs();
		Spreadsheet s = bd.getSpreadsheetById(docId);
		
		if(s == null) {
			throw new SpreadsheetDoesNotExistException();
		}
		
		return s;
	}
	
	static Cell getCellByCoords(Spreadsheet s, int row, int collumn) throws OutofBondsException {
		Cell c = s.getCellByCoords(row, collumn);
		if(c == null) {
			throw new OutofBondsException(row, collumn);
		}
		
		return c;
	}

	protected abstract void dispatch() throws BubbleDocsException;
}