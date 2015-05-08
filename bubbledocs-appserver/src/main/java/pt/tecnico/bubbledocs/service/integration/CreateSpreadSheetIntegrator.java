package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.CreateSpreadSheetService;

/**
 * Class that describes the service that is 
 * responsible for creating a spreadsheet.
 * 
 * Since this particular service doesn't require
 * any remote calls, it simply calls the local service
 * below.
 */

public class CreateSpreadSheetIntegrator extends BubbleDocsIntegrator {
	
	private String userToken;
	private String name;
	private int rows;
	private int columns;
	private int docId;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {CreateSpreadSheetIntegrator}
	 * 
	 * @param {String} userToken The token of the user that called the service.
	 * @param {String} name The spreadsheet's id.
	 * @param {number} rows The spreadsheet's max rows.
	 * @param {number} columns The spreadsheet's max columns.
	 */
	
	public CreateSpreadSheetIntegrator(String userToken, String name, int rows, int columns) {
		/** @private */
		this.userToken = userToken;
		/** @private */
		this.name = name;
		/** @private */
		this.rows = rows;
		/** @private */
		this.columns = columns;
	}
	
	/**
	 * This is where the service executes what it
	 * is supposed to do.
	 * 
	 * Since it doesn't require any remote calls
	 * it simply calls the local service.
	 * 
	 * @throws BubbleDocsException
	 */

	@Override
	protected void dispatch() throws BubbleDocsException {
		CreateSpreadSheetService createSpreadSheetService = new CreateSpreadSheetService(userToken, name, rows, columns);
		createSpreadSheetService.execute();
		
		docId = createSpreadSheetService.getSheetId();
	}
	
	/**
	 * Method that returns the id of the spreadsheet that was created.
	 * 
	 * @return {number} The spreadsheet's id.
	 */
	
	public int getDocId() {
		return docId;
	}
}// End CreateSpreadSheetIntegrator class
