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

/**
 * Class that abstracts all of the local services.
 * The service layer exists in order to act as a wall
 * to protect the access to the domain.
 * It also serves as a bridge for the transaction calls
 * made by the remote services used in BubbleDocs.
 * 
 * All local services that don't require a specific read/write
 * permission to execute must extend this class.
 */

public abstract class BubbleDocsService {
	
	protected String token;
	
	/**
	 * The execute method that is part of the 
	 * Command design pattern, which is used to implement
	 * the local service layer.
	 * 
	 * @throws BubbleDocsException
	 */

	@Atomic
	public final void execute() throws BubbleDocsException {

		if(token != null) {
			validateAndAuthorize(token);
		} 
		dispatch();
	}
	
	/**
	 * Auxiliary method used by the services to
	 * request the single instance of BubbleDocs.
	 * 
	 * @return {BubbleDocs} The single instance of BubbleDocs.
	 */
	
	protected static BubbleDocs getBubbleDocs() {
		return BubbleDocs.getInstance();
	}
	
	/**
	 * Auxiliary method used by the services to
	 * get the spreadsheet with the id given.
	 * 
	 * @param {number} docId The spreadsheet's id.
	 * @return {Spreadsheet} The spreadsheet that has the id given.
	 * @throws SpreadsheetDoesNotExistException
	 */
	
	protected static Spreadsheet getSpreadsheet(int docId) throws SpreadsheetDoesNotExistException {
		BubbleDocs bd = getBubbleDocs();
		Spreadsheet s = bd.getSpreadsheetById(docId);
		
		if(s == null) {
			throw new SpreadsheetDoesNotExistException();
		}
		
		return s;
	}
	
	/**
	 * Auxiliary method that is called before each service executes.
	 * It checks if the token is valid and if the user is in session.
	 * 
	 * @param {String} token The token of the user that called the service.
	 * @throws InvalidTokenException, UserNotInSessionException
	 */
	
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
	
	/**
	 * Abstract method that checks for the permissions of the user
	 * that called the service.
	 * Depending on the service that was called, each required specific
	 * permissions from the user, and, because of that, each implements it
	 * according to what they need to check.
	 * 
	 * Some services don't require any specific permissions, so the override
	 * implementation simply returns.
	 */
	
	protected abstract void checkAccess();
	
	/**
	 * Auxiliary method used by the services to get a particular cell
	 * that has the coordinates given.
	 * 
	 * @param {Spreadsheet} s The spreadsheet to which the cell belongs.
	 * @param {number} row The cell's row.
	 * @param {number} collumn The cell's column.
	 * @return {Cell} The cell with the coordinates given.
	 * @throws OutofBoundsException, InvalidArgumentsException
	 */

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
	
	/**
	 * The dispatch method that is part of the 
	 * Command design pattern, which is used to implement
	 * the local service layer.
	 * 
	 * This is where each service does what it is supposed to do.
	 * 
	 * @throws BubbleDocsException
	 */

	protected abstract void dispatch() throws BubbleDocsException;
}// End BubbleDocsService class
