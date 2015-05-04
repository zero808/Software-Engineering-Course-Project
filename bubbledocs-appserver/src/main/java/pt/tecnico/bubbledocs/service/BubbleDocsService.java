package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.bubbledocs.exception.OutofBoundsException;
import pt.tecnico.bubbledocs.exception.SpreadsheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public abstract class BubbleDocsService {
	
	protected String token;

	@Atomic
	public final void execute() throws BubbleDocsException {
		
		/*
		 * There are some services that don't have tokens, 
		 * such as GetUserInfoService
		 *  
		 */
		
		if(token!=null) validateAndAuthorize(token);
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
	
	protected void validateAndAuthorize(String token) throws InvalidTokenException, UserNotInSessionException {
		BubbleDocs bd = getBubbleDocs();
		
		if(token.equals("")) {
			throw new InvalidTokenException();
		}
		
		if(!(bd.isInSession(token))) {
			throw new UserNotInSessionException(token);
		}	
		checkAccess();
	}
	
	protected abstract void checkAccess();

	protected static Cell getCellByCoords(Spreadsheet s, int row, int collumn) throws OutofBoundsException, InvalidArgumentsException {
		Cell c = s.getCellByCoords(row, collumn);
		
		if(row < 1 || collumn < 1) {
			throw new InvalidArgumentsException();
		}
		
		if(c == null) {
			throw new OutofBoundsException(row, collumn);
		}
		
		return c;
	}

	protected abstract void dispatch() throws BubbleDocsException;
}// End BubbleDocsService class