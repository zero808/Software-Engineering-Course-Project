package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.GetSpreadSheetContentService;

/**
 * Class that describes the service that is 
 * responsible for creating a matrix representation
 * of a spreadsheet.
 * 
 * Since this particular service doesn't require
 * any remote calls, it simply calls the local service
 * below.
 */

public class GetSpreadSheetContentIntegrator extends BubbleDocsIntegrator {
	
	private String tokenUser;
	private int ssId;
	private String[][] matrix;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {GetSpreadSheetContentIntegrator}
	 * 
	 * @param {String} userToken The token of the user that called the service.
	 * @param {number} spreadSheetId The spreadsheet id.
	 */
	
	public GetSpreadSheetContentIntegrator(String tokenUser, int spreadSheetId) {
		/** @private */
		this.tokenUser = tokenUser;
		/** @private */
		this.ssId = spreadSheetId;	
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
		GetSpreadSheetContentService getSpreadSheetContentService = new GetSpreadSheetContentService(tokenUser, ssId);
		getSpreadSheetContentService.execute();
		
		matrix = getSpreadSheetContentService.getMatrix();
	}
	
	/**
	 * Method that returns the matrix representation 
	 * of the spreadsheet.
	 * 
	 * @return {String Matrix} The matrix representation of the spreadsheet.
	 */
	
	public String[][] getMatrix() {
		return matrix;
	}
}// End GetSpreadSheetContentIntegrator class
