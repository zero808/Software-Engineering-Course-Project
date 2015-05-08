package pt.tecnico.bubbledocs.service;

import org.joda.time.DateTime;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the service that is 
 * responsible for creating a spreadsheet.
 */

public class CreateSpreadSheetService extends BubbleDocsService {
	
	private int sheetId;
	private String name;
	private int nrow;
	private int ncol;
	private Spreadsheet s;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {CreateSpreadSheetService}
	 * 
	 * @param {String} userToken The token of the user that called the service.
	 * @param {String} name The spreadsheet's id.
	 * @param {number} rows The spreadsheet's max rows.
	 * @param {number} columns The spreadsheet's max columns.
	 */
	
	public CreateSpreadSheetService(String userToken, String name, int rows, int columns) {
		/** @private */
		this.token = userToken;
		/** @private */ 
		this.name = name;
		/** @private */ 
		nrow = rows;
		/** @private */ 
		ncol = columns;
	}
	
	/**
	 * This is where the service executes what it
	 * is supposed to do.
	 * 
	 * It's a local service, so it only does local
	 * invocations to the domain layer underneath.
	 * 
	 * @throws BubbleDocsException
	 */

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = getBubbleDocs();
		DateTime date = new DateTime();

		String username = bd.getUsernameByToken(token);
		
		User user = bd.getUserByUsername(username);	
		
		Spreadsheet s = new Spreadsheet(name, date, nrow, ncol);
		
		user.addSpreadsheets(s);
		this.s = s;
	}
	
	/**
	 * Method that returns the id of the spreadsheet.
	 * 
	 * @return {number} The spreadsheet's id.
	 */
	
	public int getSheetId() {
		return sheetId;
	}
	
	/**
	 * Method that returns the spreadsheet created.
	 * 
	 * @return {Spreadsheet} The spreadsheet that was created.
	 */
	
	public Spreadsheet getSheet(){
		return s;
	}
	
	/**
	 * This particular service doesn't require any specific permission
	 * to execute, so it simply returns.
	 */

	@Override
	protected void checkAccess() {
		return;
	}
}// End CreateSpreadSheetService class
