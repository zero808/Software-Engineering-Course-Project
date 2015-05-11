package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

/**
 * Class that describes the service that is 
 * responsible for creating a matrix representation of
 * the contents of a spreadsheet.
 */

public class GetSpreadSheetContentService extends AccessService {
	
	private int ssId;
	private String[][] matrix;
	
	/**
	 * The specific constructor needed for this
	 * application in particular.
	 * 
	 * @constructor
	 * @this {GetSpreadSheetContentService}
	 * 
	 * @param {String} userToken The token of the user that called the service.
	 * @param {number} spreadSheetId The spreadsheet id.
	 */

	public GetSpreadSheetContentService(String userToken, int spreadSheetId) {
		this.token = userToken;
		this.ssId = spreadSheetId;
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
		Spreadsheet s = getSpreadsheet(ssId);
		final int cols = s.getNCols();
		final int rows = s.getNRows();
		matrix = new String[rows][cols];
		
		for(int rowsIterator = 1, ri = 0; rowsIterator <= rows; ++rowsIterator, ++ri) {
			for(int colsIterator = 1, ci = 0; colsIterator <= cols; ++colsIterator, ++ci) {
				String ret = "";
				if(s.getCellByCoords(rowsIterator, colsIterator) != null) {
					if(s.getCellByCoords(rowsIterator, colsIterator).getContent() != null) {
						ret = s.getCellByCoords(rowsIterator, colsIterator).getContent().toString();
						matrix[ri][ci] = ret;
					}
					else
						matrix[ri][ci] = ret;
				}
				else
					matrix[ri][ci] = ret;
			}
		}
	}
	
	/**
	 * Method that returns the id of the spreadsheet.
	 * 
	 * @return {number} The spreadsheet's id.
	 */
	
	public int getSpreadSheetId() {
		return ssId;
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
}// End GetSpreadSheetContentService class
